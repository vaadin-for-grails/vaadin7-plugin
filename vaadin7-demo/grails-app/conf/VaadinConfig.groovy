import demo4.BookListView

vaadin {

    nullRepresentation = ''

    mappings {

        "/vaadin" {
            theme = "valo"
            pageTitle = "Vaadin"

            fragments {
                "index" {
                    view = demo.IndexView
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

        "/demo3" {
            ui = demo3.DemoUI
            theme = "valo"
            pageTitle = "Demo Nr 3"

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
            pageTitle = "Demo Nr 4"
            theme = "valo"

            fragments {
                "index" {
                    view = BookListView
                }
            }
        }

    }

}