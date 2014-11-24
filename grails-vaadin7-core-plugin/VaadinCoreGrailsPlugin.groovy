import grails.util.Environment
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsApplication

class VaadinCoreGrailsPlugin {

    def version = "2.0"
    def grailsVersion = "2.4 > *"
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Grails Vaadin7 Core Plugin" // Headline display name of the plugin
    def author = "Your name"
    def authorEmail = ""
    def description = '''\
Brief summary/description of the plugin.
'''
    def documentation = "http://grails.org/plugin/grails-vaadin7-core-plugin"

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
        application.getArtefacts("UI").each { uiClass ->
            "${uiClass.propertyName}"(uiClass.clazz) { bean ->
                bean.scope = "prototype"
                bean.autowire = "byName"
            }
        }
        "vaadinUtils"(com.vaadin.grails.VaadinUtils)
        "mappingsProvider"(com.vaadin.grails.server.DefaultMappingsProvider)
        "navigationUtils"(com.vaadin.grails.navigator.NavigationUtils)
        "uiProvider"(com.vaadin.grails.server.MappingsAwareUIProvider) { bean ->
            bean.scope = "prototype"
            bean.autowire = "byName"
        }

        def config = loadConfig(application)
        application.config.merge(config)
    }

    def doWithWebDescriptor = { xml ->
        def config = loadConfig(application)?.vaadin
        def mappings = config.mappings as Map

        def pluginManager = Holders.currentPluginManager()
        def openSessionInViewFilter = null
        if (pluginManager.getGrailsPlugin("hibernate3")) {
            openSessionInViewFilter = 'org.springframework.orm.hibernate3.support.OpenSessionInViewFilter'
        } else if (pluginManager.getGrailsPlugin("hibernate4")) {
            openSessionInViewFilter = 'org.springframework.orm.hibernate4.support.OpenSessionInViewFilter'
        }

//        if (openSessionInViewFilter) {
//            def contextParam = xml.'context-param'
//            mappings.eachWithIndex { mapping, i ->
//                contextParam[contextParam.size() - 1] + {
//                    'filter' {
//                        'filter-name'('openSessionInView')
//                        'filter-class'(openSessionInViewFilter)
//                    }
//                }
//            }
//
//            def filter = xml.'filter'
//            mappings.eachWithIndex { mapping, i ->
//                def urlPattern = "" + mapping.key + "/*"
//                filter[filter.size() - 1] + {
//                    'filter-mapping' {
//                        'filter-name'('openSessionInView')
//                        'url-pattern'(urlPattern)
//                    }
//                }
//            }
//        }

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
