package com.vaadin.grails.navigator

import com.vaadin.server.Page
import com.vaadin.ui.UI
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.context.request.WebRequest
import org.springframework.web.util.UrlPathHelper

/**
 * Helper for ease navigation between Views and UIs.
 *
 * @since 1.0
 * @author Stephan Grundner
 */
class NavigationHelper {

    static final def log = Logger.getLogger(NavigationHelper)

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

    String getCurrentPath() {
        String url
        def page = Page.current
        if (page == null) {
            url = currentWebRequest.parameterMap["v-loc"]
        } else {
            url = page.location.toString()
        }
        def path = url.substring(currentWebRequest.baseUrl.length())
        def indexOfFragment = path.indexOf("#")
        if (indexOfFragment > -1) {
            path = path.substring(0, indexOfFragment)
        }
        path
    }

    void enter(Map args) {
        def path = args.remove('path')
        def fragment = args.remove('fragment')
        def params = args.remove('params')

        if (path && !path.equals(currentPath)) {
            def helper = new UrlPathHelper()
            def contextPath = helper.getContextPath(currentWebRequest.nativeRequest)
            if (contextPath.endsWith("/")) {
                contextPath.substring(0, contextPath.length() - 1)
            }

            def uri = contextPath + path

            if (fragment) {
                uri += "#!${fragment}"
            }

            if (params) {
                if (fragment == null) {
                    uri += "#!"
                }
                uri += "/${encodeParams(params)}"
            }

            Page.current.setLocation(uri)
        } else {
            if (params) {
                UI.current.navigator.navigateTo("$fragment/${encodeParams(params)}")
            } else {
                UI.current.navigator.navigateTo(fragment ?: '')
            }
        }
    }

}
