import com.vaadin.shared.ui.ui.Transport
import grails.util.Environment
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.vaadin.grails.server.DefaultUriMappings
import org.vaadin.grails.server.UIScope
import org.vaadin.grails.server.UriMappings
import org.vaadin.grails.ui.declarative.Design
import org.vaadin.grails.util.VaadinConfigUtils

/**
 * Vaadin 7 Plugin.
 *
 * @author Stephan Grundner
 */
class Vaadin7GrailsPlugin {

    def version = "2.2"
    def grailsVersion = "2.4 > *"

    def group = "com.github.vaadin-for-grails"
    def title = "Vaadin 7 Plugin"
    def author = "Stephan Grundner"
    def authorEmail = "stephan.grundner@gmail.com"
    def description = '''\
Plugin for integrating Vaadin 7 into Grails.
'''
    def documentation = "https://github.com/vaadin-for-grails/vaadin7-plugin"

    def license = "APACHE"
    def organization = [ name: "Vaadin for Grails", url: "https://github.com/vaadin-for-grails" ]
    def developers = [ [ name: "Stephan Grundner", email: "stephan.grundner@gmail.com" ]]

    def scm = [ url: "https://github.com/vaadin-for-grails/vaadin7-plugin.git" ]

    def artefacts = []

    ConfigObject loadConfig(GrailsApplication application) {
        VaadinConfigUtils.loadConfig(application)

    }

    def doWithSpring = {
        def config = loadConfig(application)
        application.config.merge(config)

        def packages = application.config.grails.spring.bean.packages
        if (packages.isEmpty()) {
            xmlns context:"http://www.springframework.org/schema/context"
            context.'component-scan'('base-package': '*')
        }

        'uiScope'(UIScope)
        'uriMappings'(DefaultUriMappings)
        'design'(Design)
    }

    def doWithApplicationContext = { ctx ->
        def uriMappings = ctx.getBean(UriMappings)
        VaadinConfigUtils.loadUriMappings(application, uriMappings)
    }

    def doWithWebDescriptor = { xml ->
        def config = loadConfig(application)?.vaadin as ConfigObject

        if (!config) {
            return
        }

        boolean productionMode = Environment.current == grails.util.Environment.PRODUCTION
        if (config.isSet("productionMode")) {
            productionMode = true
        }

        def mappings = config.mappings as Map
        if (mappings.isEmpty()) {
            return
        }

        def pluginManager = Holders.currentPluginManager()
        def openSessionInViewFilter = null
        if (pluginManager.getGrailsPlugin("hibernate3")) {
            openSessionInViewFilter = 'org.springframework.orm.hibernate3.support.OpenSessionInViewFilter'
        } else if (pluginManager.getGrailsPlugin("hibernate4")) {
            openSessionInViewFilter = 'org.springframework.orm.hibernate4.support.OpenSessionInViewFilter'
        }

        if (openSessionInViewFilter) {
            def contextParam = xml.'context-param'
                contextParam[contextParam.size() - 1] + {
                'filter' {
                    'filter-name'('openSessionInView')
                    'filter-class'(openSessionInViewFilter)
                }
            }

            def filter = xml.'filter'
            mappings.eachWithIndex { mapping, i ->
                def urlPattern = mapping.key as String
                urlPattern += urlPattern.endsWith('/') ? '*' : '/*'
                filter[filter.size() - 1] + {
                    'filter-mapping' {
                        'filter-name'('openSessionInView')
                        'url-pattern'(urlPattern)
                    }
                }
            }
        }

        def contextParams = xml."context-param"
        contextParams[contextParams.size() - 1] + {
            "context-param" {
                "description"("Vaadin production mode")
                "param-name"("productionMode")
                "param-value"(productionMode)
            }
        }

        def servletClass = config.servletClass ?: "org.vaadin.grails.server.GrailsAwareVaadinServlet"

        def servlets = xml."servlet"
        mappings.eachWithIndex { mapping, i ->
            ConfigObject entry = mapping.value
            def uiProviderClass = entry.get('uiProvider') ?:
                    "org.vaadin.grails.server.GrailsAwareDelegateUIProvider"
            boolean asyncRequired = false
            if (entry.isSet('pushTransport')) {
                def pushTransport = entry.get('pushTransport')
                if (pushTransport != Transport.LONG_POLLING) {
                    log.warn("Unsupported value [${pushTransport}] for option [pushTransport], use it on your own risk!")
                }
                asyncRequired = true
            }

            servlets[servlets.size() - 1] + {
                "servlet" {
                    "servlet-name"("vaadin ${i}")
                    "servlet-class"(servletClass)
                    "init-param" {
                        "description"("Vaadin UI Provider")
                        "param-name"("UIProvider")
                        "param-value"(uiProviderClass)
                    }
                    "load-on-startup"("1")
                    "async-supported"(asyncRequired)
                }
            }
        }

        def servletMappings = xml."servlet-mapping"
        mappings.eachWithIndex { mapping, i ->
            def urlPattern = mapping.key as String
            urlPattern += urlPattern.endsWith('/') ? '*' : '/*'
            servletMappings[servletMappings.size() - 1] + {
                "servlet-mapping" {
                    "servlet-name"("vaadin ${i}")
                    "url-pattern"(urlPattern)
                }
            }
        }

        servletMappings[servletMappings.size() - 1] + {
            "servlet-mapping" {
                "servlet-name"("vaadin 0")
                "url-pattern"("/VAADIN/*")
            }
        }
    }

}
