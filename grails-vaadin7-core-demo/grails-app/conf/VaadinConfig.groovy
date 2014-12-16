vaadin {

//    defaultFragment = "index"
//    productionMode = false

    mappings {

        "/demo1" {
            ui = "demo"
            namespace = "ns1"
            theme = "valo"
            pageTitle = "Demo Nr 1"

            fragments {
                "index" {
                    view = "index"
                    namespace = "ns1"
                }
                "two" {
                    view = "second"
                }
            }

        }

        "/demo2" {
            ui = "demo"
            namespace = "ns2"
            theme = "demo"
            pageTitle = "Demo Nr 2"

        }

        "/demo3" {
            ui = "demo3"
            theme = "demo"
            pageTitle = "Demo Nr 3"

            fragments {
                "index" {
                    view = "root"
                }
            }
        }

        "/demo4" {
            ui = "demo4"
            theme = "demo"
        }
    }

}