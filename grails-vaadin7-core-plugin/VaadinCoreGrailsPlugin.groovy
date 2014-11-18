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

    def doWithWebDescriptor = { xml ->
        def config = application.config.vaadin

        if (!config) {
            return
        }

        def productionMode = config.productionMode
        def mapping = config.mapping
        if (mapping.isEmpty()) {
            return
        }

        def contextParams = xml."context-param"
        contextParams[contextParams.size() - 1] + {
            "context-param" {
                "description"("Vaadin production mode")
                "param-name"("productionMode")
                "param-value"(productionMode)
            }
        }

        def servletName = "VaadinServlet "
        def servlets = xml."servlet"
        def lastServletDefinition = servlets[servlets.size() - 1]

        mapping.eachWithIndex() { obj, i ->

            lastServletDefinition + {
                "servlet" {
                    "servlet-name"(servletName + i)
                    "servlet-class"("com.vaadin.server.VaadinServlet")

                    if (usingUIProvider) {
                        "init-param" {
                            "description"("Vaadin UI provider")
                            "param-name"("UIProvider")
                            "param-value"(obj.value)
                        }
                    } else {
                        "init-param" {
                            "description"("Vaadin UI class")
                            "param-name"("UI")
                            "param-value"(obj.value)
                        }
                    }

                    "load-on-startup"("1")
                }
            }

        }

        def servletMappings = xml."servlet-mapping"
        def lastServletMapping = servletMappings[servletMappings.size() - 1]

        mapping.eachWithIndex() { obj, i ->
            lastServletMapping + {
                "servlet-mapping" {
                    "servlet-name"(servletName + i)
                    "url-pattern"(obj.key)
                }
            }
        }

        servletMappings[servletMappings.size() - 1] + {
            "servlet-mapping" {
                "servlet-name"(servletName + 0)
                "url-pattern"("/VAADIN/*")
            }
        }
    }

}
