package org.vaadin.grails.navigator

import com.vaadin.server.Page
import com.vaadin.ui.UI
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.util.UrlPathHelper

import javax.servlet.http.HttpServletRequest

/**
 * A default implementation of {@link Navigation}.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
class DefaultNavigation extends Navigation {

    private static final UIDL_SUFFIX = '/UIDL/'

    String getPath() {
        String path

        def pathHelper = new UrlPathHelper()
        HttpServletRequest currentRequest =
                GrailsWebRequest.lookup().nativeRequest

        path = pathHelper.getPathWithinApplication(currentRequest)

        if (path.endsWith(UIDL_SUFFIX)) {
            path = StringUtils.removeEnd(path, UIDL_SUFFIX)
        }

        path
    }

    protected int indexOfParameters(String fragment) {
        int i = fragment?.indexOf("=") ?: -1
        if (i != -1) {
            return fragment.lastIndexOf("/", i)
        }
        i
    }

    Map getParams() {
        def page = Page.current
        def fragment = page.location.fragment

        int i = indexOfParameters(fragment)
        if (i != -1) {
            def paramsString = fragment.substring(i)
            return fromParamsString(paramsString)
        }

        Collections.EMPTY_MAP
    }

    String toParamsString(Map params) {
        def encoded = WebUtils.toQueryString(params)
        encoded = encoded.replace("&", "/")
        if (encoded.startsWith("?")) {
            encoded = encoded.substring(1, encoded.length())
        }
        encoded
    }

    Map fromParamsString(String paramsString) {
        WebUtils.fromQueryString(paramsString.replace("/", "&"))
    }

    String getUri(String path, String fragment, Map params) {
        String uri

        HttpServletRequest currentRequest =
                GrailsWebRequest.lookup().nativeRequest

        def helper = new UrlPathHelper()
        def contextPath = helper.getContextPath(currentRequest)
        if (contextPath.endsWith("/")) {
            contextPath.substring(0, contextPath.length() - 1)
        }

        if (path == null) {
            path = this.path
        }

        uri = contextPath + path

        if (fragment) {
            uri += "#!${fragment}"
        }

        if (params) {
            if (fragment == null) {
                uri += "#!"
            }
            uri += "/${toParamsString(params)}"
        }

        uri
    }

    void navigateTo(String path, String fragment, Map params) {
        def currentPath = this.path

        if (path == null) {
            path = currentPath
        }

        if (path?.equals(currentPath)) {
            String uri
            if (params) {
                uri = "$fragment/${toParamsString(params)}"
            } else {
                uri = fragment ?: ''
            }
            UI.current.navigator.navigateTo(uri)
        } else {
            def uri = getUri(path, fragment, params)
            Page.current.setLocation(uri)
        }
    }
}
