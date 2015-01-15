vaadin {

//    defaultFragment = "index"
//    productionMode = false

    mappings {

        "/demo1" {
            ui = demo1.DemoUI
            theme = "valo"
            pageTitle = "Demo Nr 1"

            fragments {
                "index" {
                    view = demo1.IndexView
                }
                "two" {
                    view = demo1.SecondView
                }
            }

        }

        "/demo2" {
            ui = demo1.DemoUI
            theme = "valo"
            pageTitle = "Demo Nr 2"

        }

    }

}