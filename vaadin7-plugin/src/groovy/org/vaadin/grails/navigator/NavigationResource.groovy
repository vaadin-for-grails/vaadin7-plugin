package org.vaadin.grails.navigator

import com.vaadin.server.ExternalResource

/**
 * @author Stephan Grundner
 *
 * @see {@link Navigation#navigateTo(java.lang.Object)}
 * @since 2.0
 */
class NavigationResource extends ExternalResource {

    private static final String createUri(String path, String fragment, Map params) {
        Navigation.getCurrent().getUri(path, fragment, params)
    }

    private static final String createUri(Map params) {
        createUri(params?.path as String, params?.fragment as String, params?.params as Map)
    }

    NavigationResource(Map params) {
        super(createUri(params))
    }
}
