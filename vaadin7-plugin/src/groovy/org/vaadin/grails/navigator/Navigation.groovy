package org.vaadin.grails.navigator

import com.vaadin.server.Page
import com.vaadin.server.VaadinService
import com.vaadin.ui.UI
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.util.UrlPathHelper
import org.vaadin.grails.server.UriMappingsUtils

/**
 * Navigating between UIs and Views as defined in {@link org.vaadin.grails.server.UriMappings}.
 *
 * @see {@link org.vaadin.grails.server.UriMappings}
 * @author Stephan Grundner
 * @since 2.0
 */
abstract class Navigation {

    /**
     * Get the current path.
     *
     * @return The current path
     */
    static String getCurrentPath() {
        def pathHelper = new UrlPathHelper()
        def currentRequest = VaadinService.currentRequest
        def path = pathHelper.getPathWithinApplication(currentRequest)
        StringUtils.removeEnd(path, '/UIDL/')
    }

    /**
     * Get the current mapped fragment.
     *
     * @return The current mapped fragment
     */
    static String getCurrentFragment() {
        def path = currentPath
        def uri = Page.current.location
        def fragmentAndParameters = StringUtils.removeStart(uri.fragment, "!")
        UriMappingsUtils.lookupFragment(path, fragmentAndParameters)
    }

    /**
     * Get the current parameters as a map.
     *
     * @return The current parameters as a map
     */
    static Map getCurrentParams() {
        def fragment = currentFragment
        if (fragment) {
            def uri = Page.current.location
            def fragmentAndParameters = StringUtils.removeStart(uri.fragment, "!")
            def paramsString = StringUtils.removeStart(fragmentAndParameters, fragment)
            return fromParamsString(paramsString)
        }
        Collections.EMPTY_MAP
    }

    /**
     * Convert a parameter map into a String like <code>/key1=foo/key2=bar</code>.
     *
     * @param params
     * @return
     */
    static String toParamsString(Map params) {
        def encoded = WebUtils.toQueryString(params)
        encoded = encoded.replace("&", "/")
        if (encoded.startsWith("?")) {
            encoded = encoded.substring(1, encoded.length())
        }
        encoded
    }

    /**
     * Convert a String like <code>/key1=foo/key2=bar</code> into a parameter map.
     *
     * @param paramsString
     * @return
     */
    static Map fromParamsString(String paramsString) {
        WebUtils.fromQueryString(paramsString.replace("/", "&"))
    }

    /**
     * Get the uri for the specified path, fragment and a paramter map.
     *
     * @param path The path mapped to a {@link com.vaadin.ui.UI}
     * @param fragment The fragment mapped to a {@link com.vaadin.navigator.View}
     * @param params A parameter map
     * @return The uri for the specified path, fragment and a paramter map
     */
    static String getUri(String path, String fragment, Map params) {
        String uri

        def helper = new UrlPathHelper()
        def currentRequest = VaadinService.currentRequest
        def contextPath = helper.getContextPath(currentRequest)
        if (contextPath.endsWith("/")) {
            contextPath.substring(0, contextPath.length() - 1)
        }

        if (path == null) {
            path = currentPath
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

    /**
     * Navigate to a different UI or View.
     *
     * @see {@link com.vaadin.navigator.Navigator#navigateTo(java.lang.String)}
     *
     * @param path The path mapped to a {@link com.vaadin.ui.UI}
     * @param fragment The fragment mapped to a {@link com.vaadin.navigator.View}
     * @param params A parameter map
     */
    static void navigateTo(String path, String fragment, Map params) {
        def currentPath = currentPath

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

    /**
     * Navigate to a different UI or View.
     *
     * @see {@link #navigateTo(java.lang.String, java.lang.String, java.util.Map)}
     * @param args
     */
    static void navigateTo(Map args) {
        def path = args.get('path') as String
        def fragment = args.get('fragment') as String
        def params = args.get('params') as Map
        navigateTo(path, fragment, params)
    }
}
