
import grails.util.Environment
import org.codehaus.groovy.grails.commons.GrailsClassUtils

class VaadinCoreGrailsPlugin {
    def version = "0.1"
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
            com.vaadin.grails.VaadinUIArtefactHandler]

    def doWithSpring = {
        application.getArtefacts("UI").each { uiClass ->
            "${uiClass.propertyName}"(uiClass.clazz) { bean ->
                bean.scope = "prototype"
                bean.autowire = "byName"
            }
        }
        "uiProvider"(com.vaadin.grails.server.MappingsAwareUIProvider) { bean ->
            bean.scope = "prototype"
            bean.autowire = "byName"
        }
        "viewProvider"(com.vaadin.grails.navigator.MappingsAwareViewProvider) { bean ->
            bean.scope = "prototype"
            bean.autowire = "byName"
        }
        "mappingsProvider"(com.vaadin.grails.DefaultMappingsProvider)
    }

    def doWithWebDescriptor = { xml ->
        def config = application.config.vaadin
        if (!config) {
            return
        }

        def mappingsClass = application.classLoader.loadClass("VaadinMappings")
        def base = GrailsClassUtils.getStaticPropertyValue(mappingsClass, "base") as String

        if (base == null) {
            throw new RuntimeException("Base not specified")
        }

        def urlPattern = base
        if (!urlPattern.endsWith("/")) {
            urlPattern += "/"
        }
        urlPattern += "*"

        def contextParams = xml."context-param"
        contextParams[contextParams.size() - 1] + {
            "context-param" {
                "description"("Vaadin production mode")
                "param-name"("productionMode")
                "param-value"(Environment.current == grails.util.Environment.PRODUCTION)
            }
        }

        def servlets = xml."servlet"
        servlets[servlets.size() - 1] + {
            "servlet" {
                "servlet-name"("vaadin")
                "servlet-class"("com.vaadin.server.VaadinServlet")
                "init-param" {
                    "description"("Vaadin UI Provider")
                    "param-name"("UIProvider")
                    "param-value"("com.vaadin.grails.server.DispatcherUIProvider")
                }
                "load-on-startup"("1")
            }
        }

        def servletMappings = xml."servlet-mapping"
        servletMappings[servletMappings.size() - 1] + {
            "servlet-mapping" {
                "servlet-name"("vaadin")
                "url-pattern"(urlPattern)
            }
        }

        servletMappings[servletMappings.size() - 1] + {
            "servlet-mapping" {
                "servlet-name"("vaadin")
                "url-pattern"("/VAADIN/*")
            }
        }
    }

}
