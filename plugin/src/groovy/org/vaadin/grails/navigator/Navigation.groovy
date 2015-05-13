package org.vaadin.grails.navigator

import com.vaadin.navigator.View
import com.vaadin.server.ExternalResource
import com.vaadin.server.Page
import com.vaadin.server.VaadinService
import com.vaadin.ui.UI
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.util.UrlPathHelper
import org.vaadin.grails.server.UriMappingUtils

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
        UriMappingUtils.lookupFragment(path, fragmentAndParameters)
    }

    /**
     * Get the current parameters as a map.
     *
     * @see {@link #toParamsString(java.util.Map)}
     * @see {@link #fromParamsString(java.lang.String)}
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
     * @param params A parameter map
     * @return A string representation from a parameter map
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
     * @param paramsString A paramsString
     * @return A map representation from a paramsString
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
     * Get the uri for the specified named parameters.
     *
     * @see {@link #getUri(java.lang.String, java.lang.String, java.util.Map)}
     * @param args The named parameters
     * @return The uri for the specified named parameters
     */
    static String getUri(Map args) {
        def path = args.get('path') as String
        def fragment = args.get('fragment') as String
        def params = args.get('params') as Map
        getUri(path, fragment, params)
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
     * Navigate to a different UI or View using the named parameters.
     *
     * @see {@link #navigateTo(java.lang.String, java.lang.String, java.util.Map)}
     * @param args The named parameters
     */
    static void navigateTo(Map args) {
        def path = args.get('path') as String
        def fragment = args.get('fragment') as String
        def params = args.get('params') as Map
        navigateTo(path, fragment, params)
    }

    /**
     * Navigate to a different UI or View by Class using the named parameters.
     *
     * @see org.vaadin.grails.server.UriMappings#getPrimaryPath(java.lang.Class)
     * @see org.vaadin.grails.server.UriMappings#getPrimaryFragment(java.lang.String, java.lang.Class)
     * @param uiOrViewClass A mapped UI or View class
     * @param params The named parameters
     * @since 2.1
     */
    static void navigateTo(Class<?> uiOrViewClass, Map params = null) {
        Class<? extends UI> uiClass
        Class<? extends View> viewClass
        if (UI.isAssignableFrom(uiOrViewClass)) {
            uiClass = uiOrViewClass
        }
        if (View.isAssignableFrom(uiOrViewClass)) {
            viewClass = uiOrViewClass
        }
        navigateTo(uiClass, viewClass, params)
    }

    /**
     * Navigate to a different UI or View by Class using the named parameters.
     *
     * @see org.vaadin.grails.server.UriMappings#getPrimaryPath(java.lang.Class)
     * @see org.vaadin.grails.server.UriMappings#getPrimaryFragment(java.lang.String, java.lang.Class)
     * @param uiClass A {@link UI} mapped to a path
     * @param viewClass A {@link View} mapped to a path & fragment
     * @param params The named parameters
     * @since 2.1
     */
    static void navigateTo(Class<? extends UI> uiClass, Class<? extends View> viewClass, Map params = null) {
        def uriMappings = UriMappingUtils.uriMappings
        def path = uiClass ? uriMappings.getPrimaryPath(uiClass) : currentPath
        def fragment = viewClass ? uriMappings.getPrimaryFragment(path, viewClass) : currentFragment
        navigateTo(path, fragment, params)
    }

    /**
     * Get an {@link ExternalResource} for the specified path, fragment and params.
     *
     * @param path The path mapped to a {@link com.vaadin.ui.UI}
     * @param fragment The fragment mapped to a {@link com.vaadin.navigator.View}
     * @param params A parameter map
     * @return An {@link ExternalResource} for the specified path, fragment and params
     */
    static ExternalResource getResource(String path, String fragment, Map params) {
        new ExternalResource(getUri(path, fragment, params))
    }

    /**
     * Get an {@link ExternalResource} for the specified named parameters.
     *
     * @see {@link #getResource(java.lang.String, java.lang.String, java.util.Map)}
     * @param args The named parameters
     * @return An {@link ExternalResource} for the specified named parameters
     */
    static ExternalResource getResource(Map args) {
        new ExternalResource(getUri(args))
    }
}
