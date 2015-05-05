package org.vaadin.grails.server

import org.apache.commons.lang.StringUtils
import org.vaadin.grails.util.ApplicationContextUtils

/**
 *
 * @author Stephan Grundner
 * @since 2.0
 */
final class UriMappingsUtils {

    static UriMappings getUriMappings() {
        ApplicationContextUtils.getSingletonBean(UriMappings)
    }

    /**
     * Lookup a fragment (view name) in {@link UriMappings}.
     *
     * @param path The path
     * @param fragmentAndParameters The fragment of an uri with parameters
     * @return
     */
    static String lookupFragment(String path, String fragmentAndParameters) {
        def fragment = fragmentAndParameters
        if (fragment == null) {
            return fragment
        }

        def defaultFragment = uriMappings.getPathProperty(path,
                UriMappings.DEFAULT_FRAGMENT_PATH_PROPERTY)

        if (StringUtils.isEmpty(fragment) && uriMappings.getViewClass(path, defaultFragment)) {
            return fragment
        }

        while (true) {
            def viewClass = uriMappings.getViewClass(path, fragment)
            if (viewClass) {
                return fragment
            }

            def i = fragment.lastIndexOf("/")
            if (i == -1) {
                break
            }
            fragment = fragment.substring(0, i)
        }

        null
    }

    private UriMappingsUtils() { }
}
