import com.vaadin.grails.NamespaceAwareVaadinClass
import com.vaadin.grails.server.DefaultUriMappingsHolder
import com.vaadin.grails.server.UriMappingsAwareUIProvider
import grails.util.Environment
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsApplication

class VaadinCoreGrailsPlugin {

    def version = "2.1"
    def grailsVersion = "2.4 > *"

    def group = "com.github.vaadin-for-grails"
    def title = "Vaadin Core Plugin"
    def author = "Stephan Grundner"
    def authorEmail = "stephan.grundner@gmail.com"
    def description = '''\
Plugin for integrating Vaadin into Grails.
'''
    def documentation = "https://github.com/vaadin-for-grails/grails-vaadin-core-plugin"

    def license = "APACHE"
    def organization = [ name: "Vaadin for Grails", url: "https://github.com/vaadin-for-grails" ]
    def developers = [ [ name: "Stephan Grundner", email: "stephan.grundner@gmail.com" ]]

    def scm = [ url: "https://github.com/vaadin-for-grails/grails-vaadin-core-plugin.git" ]

    def artefacts = [
            com.vaadin.grails.VaadinUIArtefactHandler,
            com.vaadin.grails.VaadinViewArtefactHandler]

    ConfigObject loadConfig(GrailsApplication application) {
        def configScriptClass
        try {
            configScriptClass = application.classLoader.loadClass("VaadinConfig")
        } catch (ClassNotFoundException e) {
            return null
        }

        def parser = new ConfigSlurper(Environment.current.name)
        parser.parse(configScriptClass)
    }

    def doWithSpring = {

        ["UI", "View"].each {
            application.getArtefacts(it).each { NamespaceAwareVaadinClass vaadinClass ->
                def namespace = vaadinClass.namespace
                if (namespace) {
                    "${namespace}.${vaadinClass.propertyName}"(vaadinClass.clazz) { bean ->
                        bean.scope = "prototype"
                        bean.autowire = "byName"
                    }
                } else {
                    "${vaadinClass.propertyName}"(vaadinClass.clazz) { bean ->
                        bean.scope = "prototype"
                        bean.autowire = "byName"
                    }
                }
            }
        }

        def config = loadConfig(application)
        application.config.merge(config)

        "vaadinUtils"(com.vaadin.grails.VaadinUtils)
        "uriMappingsHolder"(DefaultUriMappingsHolder)
        "navigationUtils"(com.vaadin.grails.navigator.NavigationUtils)
        "uiProvider"(UriMappingsAwareUIProvider)
    }

    def doWithWebDescriptor = { xml ->
        def config = loadConfig(application)?.vaadin

        if (!config) {
            return
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
                def urlPattern = mapping.key + "/*"
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
                "param-value"(Environment.current == grails.util.Environment.PRODUCTION)
            }
        }

        def servlets = xml."servlet"
        mappings.eachWithIndex { mapping, i ->
            servlets[servlets.size() - 1] + {
                "servlet" {
                    "servlet-name"("vaadin ${i}")
                    "servlet-class"("com.vaadin.server.VaadinServlet")
                    "init-param" {
                        "description"("Vaadin UI Provider")
                        "param-name"("UIProvider")
                        "param-value"("com.vaadin.grails.server.DispatcherUIProvider")
                    }
                    "load-on-startup"("1")
                }
            }
        }

        def servletMappings = xml."servlet-mapping"
        mappings.eachWithIndex { mapping, i ->
            def urlPattern = mapping.key + "/*"
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
