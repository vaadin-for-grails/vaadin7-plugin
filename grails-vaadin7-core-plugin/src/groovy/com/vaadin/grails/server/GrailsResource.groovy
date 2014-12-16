package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import com.vaadin.server.ExternalResource
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

/**
 * Grails Resource
 *
 * @see {@link LinkGenerator#link(java.util.Map)}
 * @author Stephan Grundner
 */
class GrailsResource extends ExternalResource {

    static String createSourceURL(Map params) {
        def linkGenerator = Vaadin.applicationContext.getBean(LinkGenerator)
        linkGenerator.link(params)
    }

    GrailsResource(Map params) {
        super(createSourceURL(params))
    }

    GrailsResource(Map params, String mimeType) {
        super(createSourceURL(params), mimeType)
    }
}
