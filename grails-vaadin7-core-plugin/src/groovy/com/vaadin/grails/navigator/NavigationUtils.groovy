package com.vaadin.grails.navigator

import com.vaadin.grails.server.UriMappingsHolder
import com.vaadin.navigator.View
import com.vaadin.server.Page
import com.vaadin.ui.UI
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest
import org.springframework.web.util.UrlPathHelper

/**
 * Utils for ease navigation between Views and UIs.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
@Component
@DependsOn("uriMappingsHolder")
class NavigationUtils {

    static final def log = Logger.getLogger(NavigationUtils)

    @Autowired
    UriMappingsHolder uriMappings

    @Autowired
    LinkGenerator linkGenerator

    /**
     * Encode a map to a String.
     *
     * @param params A parameter map
     * @return A string representation of a map
     */
    String encodeParams(Map params) {
        def encoded = WebUtils.toQueryString(params)
        encoded = encoded.replace("&", "/")
        if (encoded.startsWith("?")) {
            encoded = encoded.substring(1, encoded.length())
        }
        encoded
    }

    /**
     * Decode a String to a map.
     *
     * @param encoded An encoded String
     * @return A map representation of an encoded String
     */
    Map decodeParams(String encoded) {
        WebUtils.fromQueryString(encoded.replace("/", "&"))
    }

    /**
     * Return the current {@link WebRequest}.
     *
     * @return The current {@link WebRequest}
     */
    GrailsWebRequest getCurrentWebRequest() {
        GrailsWebRequest.lookup()
    }

    void enter(Class<? extends UI> uiClass, Class<? extends View> viewClass, Map params = null) {
        if (uiClass == null) {
            uiClass = UI.current.class
        }

        def path = uriMappings.getPath(uiClass)

        if (UI.current.class == uiClass) {
            def fragment = uriMappings.getFragment(path, viewClass) ?: ""
            if (params) {
                UI.current.navigator.navigateTo("${fragment}/${encodeParams(params)}")
            } else {
                UI.current.navigator.navigateTo(fragment)
            }
        } else {
            def helper = new UrlPathHelper()
            def contextPath = helper.getContextPath(currentWebRequest.nativeRequest)
            if (contextPath.endsWith("/")) {
                contextPath.substring(0, contextPath.length() - 1)
            }

            def uri = contextPath + path

            if (viewClass) {
                def fragment = uriMappings.getFragment(path, viewClass)
                uri += "#!${fragment}"
            }

            if (params) {
                uri += "/${encodeParams(params)}"
            }

            Page.current.setLocation(uri)
        }

    }
}
