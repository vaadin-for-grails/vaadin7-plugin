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


//        def openSessionInViewFilter = config.openSessionInViewFilter
//        if (openSessionInViewFilter) {
//            def contextParam = xml.'context-param'
//            contextParam[contextParam.size() - 1] + {
//                'filter' {
//                    'filter-name'('openSessionInView')
//                    'filter-class'(openSessionInViewFilter)
//                }
//            }
//
//            def filter = xml.'filter'
//            filter[filter.size() - 1] + {
//                'filter-mapping' {
//                    'filter-name'('openSessionInView')
//                    'url-pattern'('/*')
//                }
//            }
//        }



        def contextParams = xml."context-param"
        contextParams[contextParams.size() - 1] + {
            "context-param" {
                "description"("Vaadin production mode")
                "param-name"("productionMode")
                "param-value"(productionMode)
            }
        }


        if (mapping.isEmpty()) {
            def uiProvider = config.uiProvider ?: "com.vaadin.grails.server.DefaultUIProvider"
            mapping = ["/vaadin/*": uiProvider]
            usingUIProvider = true
        }

        def applicationServlet = config.servletClass ?: "com.vaadin.server.VaadinServlet"
        def servletName = "VaadinServlet "
        def widgetset = config.widgetset
        def asyncSupported = config.asyncSupported
        Map initParams = config.initParams

        def servlets = xml."servlet"
        def lastServletDefinition = servlets[servlets.size() - 1]

        mapping.eachWithIndex() { obj, i ->

            lastServletDefinition + {
                "servlet" {
                    "servlet-name"(servletName + i)
                    "servlet-class"(applicationServlet)

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

                    if (widgetset) {
                        "init-param" {
                            "description"("Application widgetset")
                            "param-name"("widgetset")
                            "param-value"(widgetset)
                        }
                    }
                    for (def name : initParams?.keySet()) {
                        "init-param" {
                            "param-name"(name)
                            "param-value"(initParams.get(name))
                        }
                    }
                    "load-on-startup"("1")

                    if (asyncSupported) {
                        "async-supported"("true")
                    }
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
