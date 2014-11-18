import org.codehaus.groovy.grails.plugins.web.mapping.UrlMappingsGrailsPlugin
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingParser

class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
*DefaultUrlMappingParser
        "/"(view:"/index")
        "500"(view:'/error')
	}
}
