import com.vaadin.grails.ui.DefaultUI

vaadin {

//    defaultFragment = "index"
//    productionMode = false

    mappings {

        "/vaadin" {
            ui = DefaultUI
            theme = "valo"
            pageTitle = "Vaadin"

            fragments {
                "index" {
                    view = demo1.IndexView
                }
                "two" {
                    view = demo1.SecondView
                }
            }
        }

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