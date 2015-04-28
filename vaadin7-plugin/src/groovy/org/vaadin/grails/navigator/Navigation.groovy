package org.vaadin.grails.navigator

import com.vaadin.server.VaadinSession
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * Navigating between UIs and Views as defined in <code>VaadinConfig.groovy</code>.
 *
 * @see {@link org.vaadin.grails.server.UriMappings}
 * @author Stephan Grundner
 *
 * @since 2.0
 */
abstract class Navigation {

    static Navigation getCurrent() {
        def session = VaadinSession.current
        def navigation = session.getAttribute(Navigation)
        if (navigation == null) {
            navigation = ApplicationContextUtils.getBeanOrInstance(Navigation)
            session.setAttribute(Navigation, navigation)
        }
        navigation
    }

    /**
     * Navigate to a different UI or View.
     * This method may be overwritten for special behaviour!
     *
     * @see {@link #navigateTo(java.lang.String, java.lang.String, java.util.Map)}
     * @param args
     */
    static void navigateTo(def args) {
        def path = args.get('path') as String
        def fragment = args.get('fragment') as String
        def params = args.get('params') as Map
        current.navigateTo(path, fragment, params)
    }

    /**
     * Get the path mapped to the current UI.
     *
     * @return The path mapped to the current UI
     */
    abstract String getPath()

    /**
     * Get the current parameter map.
     *
     * @return The current parameter map
     */
    abstract Map getParams()

    /**
     * Convert a parameter map into a String like <code>/key1=foo/key2=bar</code>.
     *
     * @param params
     * @return
     */
    abstract String toParamsString(Map params)

    /**
     * Convert a String like <code>/key1=foo/key2=bar</code> into a parameter map.
     *
     * @param paramsString
     * @return
     */
    abstract Map fromParamsString(String paramsString)

    /**
     * Get the uri for the specified path, fragment and a paramter map.
     *
     * @param path The path mapped to a {@link com.vaadin.ui.UI}
     * @param fragment The fragment mapped to a {@link com.vaadin.navigator.View}
     * @param params A parameter map
     * @return The uri for the specified path, fragment and a paramter map
     */
    abstract String getUri(String path, String fragment, Map params)

    /**
     * Navigate to a different UI or View.
     *
     * @see {@link com.vaadin.navigator.Navigator#navigateTo(java.lang.String)}
     *
     * @param path The path mapped to a {@link com.vaadin.ui.UI}
     * @param fragment The fragment mapped to a {@link com.vaadin.navigator.View}
     * @param params A parameter map
     */
    abstract void navigateTo(String path, String fragment, Map params)
}
