package org.vaadin.grails.navigator

import com.vaadin.server.ExternalResource

/**
 * Navigation resource class.
 *
 * @author Stephan Grundner
 *
 * @see {@link com.vaadin.server.Resource}
 * @see {@link Navigation}
 * @see {@link Navigation#navigateTo(java.util.Map)}
 * @since 2.0
 */
class NavigationResource extends ExternalResource {

    private static final String createUri(String path, String fragment, Map params) {
        Navigation.getUri(path, fragment, params)
    }

    private static final String createUri(Map params) {
        createUri(params?.path as String, params?.fragment as String, params?.params as Map)
    }

    NavigationResource(Map params) {
        super(createUri(params))
    }
}
