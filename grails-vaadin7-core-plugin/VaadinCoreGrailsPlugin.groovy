import com.vaadin.grails.VaadinUtils
import com.vaadin.grails.server.DefaultMappingsProvider
import grails.util.Environment
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsClassUtils

class VaadinCoreGrailsPlugin {
    def version = "2.0"
    def grailsVersion = "2.4 > *"
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Grails Vaadin7 Core Plugin Plugin" // Headline display name of the plugin
    def author = "Your name"
    def authorEmail = ""
    def description = '''\
Brief summary/description of the plugin.
'''
    def documentation = "http://grails.org/plugin/grails-vaadin7-core-plugin"

    def artefacts = [
            com.vaadin.grails.VaadinMappingsArtefactHandler,
            com.vaadin.grails.VaadinUIArtefactHandler,
            com.vaadin.grails.VaadinViewArtefactHandler]

    def doWithSpring = {
        application.getArtefacts("UI").each { uiClass ->
            "${uiClass.propertyName}"(uiClass.clazz) { bean ->
                bean.scope = "prototype"
                bean.autowire = "byName"
            }
        }
        "vaadinUtil"(VaadinUtils)
        "navigationUtils"(com.vaadin.grails.navigator.NavigationUtils)
        "mappingsProvider"(com.vaadin.grails.server.DefaultMappingsProvider)
        "uiProvider"(com.vaadin.grails.server.MappingsAwareUIProvider) { bean ->
            bean.scope = "prototype"
            bean.autowire = "byName"
        }
    }

    def doWithWebDescriptor = { xml ->
//        def config = application.config.vaadin

        def mappingsClass
        try {
            mappingsClass = application.classLoader.loadClass("VaadinMappings")
        } catch (ClassNotFoundException e) {
            return
        }

        def mappingsClosure = GrailsClassUtils.getStaticPropertyValue(mappingsClass, "mappings") as Closure
        if (mappingsClosure == null) {
            throw new RuntimeException("Base not specified")
        }

        def mappingsProvider = new DefaultMappingsProvider(mappingsClosure)
        def mappings = mappingsProvider.allMappings


        def pluginManager = Holders.currentPluginManager()
        def openSessionInViewFilter = null
        if (pluginManager.getGrailsPlugin("hibernate3")) {
            openSessionInViewFilter = 'org.springframework.orm.hibernate3.support.OpenSessionInViewFilter'
        } else if (pluginManager.getGrailsPlugin("hibernate4")) {
            openSessionInViewFilter = 'org.springframework.orm.hibernate4.support.OpenSessionInViewFilter'
        }

//        if (openSessionInViewFilter) {
//            def contextParam = xml.'context-param'
//            def lastContextParam = contextParam[contextParam.size() - 1]
//            mappings.eachWithIndex { mapping, i ->
//                lastContextParam + {
//                    'filter' {
//                        'filter-name'('openSessionInView')
//                        'filter-class'(openSessionInViewFilter)
//                    }
//                }
//            }
//
//            def filterDefintions = xml.'filter'
//            def lastFilterDefinition = filterDefintions[filterDefintions.size() - 1]
//            mappings.eachWithIndex { mapping, i ->
//                def urlPattern = mapping.path + "/*"
//                lastFilterDefinition + {
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
//        def lastServlet = servlets[servlets.size() - 1]
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
//        def lastServletMapping = servletMappings[servletMappings.size() - 1]
        mappings.eachWithIndex { mapping, i ->
            def urlPattern = mapping.path + "/*"
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
