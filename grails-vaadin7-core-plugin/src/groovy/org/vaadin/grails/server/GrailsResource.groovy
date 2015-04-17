package org.vaadin.grails.server

import com.vaadin.server.ExternalResource
import grails.util.Holders
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

/**
 * Grails Resource
 *
 * @see {@link LinkGenerator#link(java.util.Map)}
 * @author Stephan Grundner
 */
class GrailsResource extends ExternalResource {

    /**
     *
     * Parameters:
     * <ul>
     *    <li>resource - If linking to a REST resource, the name of the resource or resource path to link to. Either 'resource' or 'controller' should be specified, but not both</li>
     *    <li>controller - The name of the controller to use in the link, if not specified the current controller will be linked</li>
     *    <li>action -  The name of the action to use in the link, if not specified the default action will be linked</li>
     *    <li>uri -  relative URI</li>
     *    <li>url -  A map containing the action,controller,id etc.</li>
     *    <li>base -  Sets the prefix to be added to the link target address, typically an absolute server URL. This overrides the behaviour of the absolute property, if both are specified.</li>
     *    <li>absolute -  If set to "true" will prefix the link target address with the value of the grails.serverURL property from Config, or http://localhost:&lt;port&gt; if no value in Config and not running in production.</li>
     *    <li>contextPath - The context path to link to, defaults to the servlet context path</li>
     *    <li>id -  The id to use in the link</li>
     *    <li>fragment -  The link fragment (often called anchor tag) to use</li>
     *    <li>params -  A map containing URL query parameters</li>
     *    <li>mapping -  The named URL mapping to use to rewrite the link</li>
     *    <li>event -  Webflow _eventId parameter</li>
     * </ul>
     *
     * @param params Parameters
     * @return
     */
    static String createSourceURL(Map params) {
        def applicationContext = Holders.applicationContext
        def linkGenerator = applicationContext.getBean(LinkGenerator)
        linkGenerator.link(params)
    }

    /**
     *
     * @see {@link #createSourceURL(java.util.Map)}
     * @param params
     */
    GrailsResource(Map params) {
        super(createSourceURL(params))
    }

    /**
     *
     * @see {@link #createSourceURL(java.util.Map)}
     * @param params
     * @param mimeType
     */
    GrailsResource(Map params, String mimeType) {
        super(createSourceURL(params), mimeType)
    }
}
