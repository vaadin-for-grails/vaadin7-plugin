import demo4.BookListView

vaadin {

    nullRepresentation = ''
    theme = "valo"
    pageTitle = "Vaadin 7 Grails Plugin Demos"

    mappings {

        "/vaadin" {

            fragments {
                "index" {
                    view = demo.IndexView
                }
            }
        }

        "/demo1" {
            ui = 'demo1.DemoUI'

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
            ui = 'demo1.DemoUI'

        }

        "/demo3" {
            ui = demo3.DemoUI

            fragments {

                "index" {
                    view = demo3.IndexView
                }

                "different" {
                    view = demo3.DifferentView
                }
            }
        }

        "/demo4" {

            fragments {
                "index" {
                    view = BookListView
                }
            }
        }

    }

}