import com.vaadin.navigator.View
import com.vaadin.ui.UI
import grails.util.Environment
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.vaadin.grails.navigator.DefaultNavigation
import org.vaadin.grails.server.DefaultLazyInitializer
import org.vaadin.grails.server.DefaultUriMappings
import org.vaadin.grails.server.UriMappings
import org.vaadin.grails.ui.DefaultUI
import org.vaadin.grails.ui.declarative.Design

class Vaadin7GrailsPlugin {

    def version = "2.0-SNAPSHOT"
    def grailsVersion = "2.4 > *"

    def group = "com.github.vaadin-for-grails"
    def title = "Vaadin Core Plugin"
    def author = "Stephan Grundner"
    def authorEmail = "stephan.grundner@gmail.com"
    def description = '''\
Plugin for integrating Vaadin7 into Grails.
'''
    def documentation = "https://github.com/vaadin-for-grails/vaadin7-plugin"

    def license = "APACHE"
    def organization = [ name: "Vaadin for Grails", url: "https://github.com/vaadin-for-grails" ]
    def developers = [ [ name: "Stephan Grundner", email: "stephan.grundner@gmail.com" ]]

    def scm = [ url: "https://github.com/vaadin-for-grails/vaadin7-plugin.git" ]

    def artefacts = []

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
        def config = loadConfig(application)
        application.config.merge(config)

        'uriMappings'(DefaultUriMappings)
        'navigation'(DefaultNavigation)
        'lazyInitializer'(DefaultLazyInitializer)
        'design'(Design)
    }

    def doWithApplicationContext = { ctx ->
        def uriMappings = ctx.getBean(UriMappings)

        uriMappings.clear()

        def mappingsConfig = Holders.config.vaadin.mappings
        mappingsConfig.each { String path, ConfigObject pathConfig ->

            def ui = pathConfig.get("ui")

            uriMappings.putPathProperty(path, UriMappings.DEFAULT_FRAGMENT_PATH_PROPERTY, pathConfig.get(UriMappings.DEFAULT_FRAGMENT_PATH_PROPERTY) ?: "index")
            uriMappings.putPathProperty(path, UriMappings.THEME_PATH_PROPERTY, pathConfig.get(UriMappings.THEME_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.WIDGETSET_PATH_PROPERTY, pathConfig.get(UriMappings.WIDGETSET_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.PRESERVED_ON_REFRESH_PATH_PROPERTY, pathConfig.get(UriMappings.PRESERVED_ON_REFRESH_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.PAGE_TITLE_PATH_PROPERTY, pathConfig.get(UriMappings.PAGE_TITLE_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.PUSH_MODE_PATH_PROPERTY, pathConfig.get(UriMappings.PUSH_MODE_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.PUSH_TRANSPORT_PATH_PROPERTY, pathConfig.get(UriMappings.PUSH_TRANSPORT_PATH_PROPERTY))

            Class<? extends UI> uiClass

            if (ui instanceof String) {
                def classLoader = application.classLoader
                uiClass = classLoader.loadClass(ui)
            } else {
                uiClass = ui ?: DefaultUI
            }

            if (uiClass == null) {
                throw new RuntimeException("No class found for [${path}]")
            }
            log.debug("Register UI [${uiClass.name}] for path [${path}]")
            uriMappings.setUIClass(path, uiClass)


            def fragments = pathConfig.fragments
            fragments.each { String fragment, ConfigObject fragmentConfig ->

                def view = fragmentConfig.get("view")

                Class<? extends View> viewClass

                if (view instanceof String) {
                    def classLoader = application.classLoader
                    viewClass = classLoader.loadClass(view)
                } else {
                    viewClass = view
                }

                if (viewClass == null) {
                    throw new RuntimeException("No class found for view [${view}]")
                }
                log.debug("Register View [${viewClass.name}] for path [${path}#!${fragment}]")
                uriMappings.setViewClass(path, fragment, viewClass)
            }
        }
    }

    def doWithWebDescriptor = { xml ->
        def config = loadConfig(application)?.vaadin

        if (!config) {
            return
        }

        boolean productionMode = Environment.current == grails.util.Environment.PRODUCTION
        if (config.containsKey("productionMode")) {
            productionMode = config.productionMode
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

        def servletClass = config.servletClass ?: "com.vaadin.server.VaadinServlet"

        def servlets = xml."servlet"
        mappings.eachWithIndex { mapping, i ->
            def uiProviderClass = mapping.value['uiProvider'] ?:
                    "org.vaadin.grails.server.UriMappingsAwareUIProvider"

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
