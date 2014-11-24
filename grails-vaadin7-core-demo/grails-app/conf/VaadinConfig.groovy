
vaadin {

    mappings {

        "/login2" {
            ui = "login"
        }

        "/demo1" {
            ui = "demo"
            namespace = "ns1"
            theme = "valo"


            views {

//                set the default action (view) to index
                "index" {
                    view = "index"
                    access = ["ROLE_ADMIN"]

                }
            }

        }

        "/demo2" {
            ui = "demo"
            namespace = "ns2"
            theme = "valo"
            access = ["ROLE_ADMIN"]

            views {

//                "#two" {
//                    view = "second"
//                }
            }

        }

    }

}