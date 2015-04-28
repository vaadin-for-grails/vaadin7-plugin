class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

//        "/"(view: "index")
        "/"(redirect: "/vaadin")
        "500"(view:'/error')
	}
}
