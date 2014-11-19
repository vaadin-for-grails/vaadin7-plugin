class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

//        "/"(redirect: "/vaadin/")
//        "500"(view:'/error')
	}
}
