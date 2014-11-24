package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.server.MappingsProvider
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider

/**
 * A {@link com.vaadin.navigator.ViewProvider} implementation that uses mappings
 * defined in the <code>VaadinConfig</code> script.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
class MappingsAwareViewProvider implements ViewProvider {

    final String path
    final MappingsProvider mappingsProvider

    MappingsAwareViewProvider(String path) {
        this.path = path
        mappingsProvider = Vaadin.applicationContext.getBean(MappingsProvider)
    }

    @Override
    String getViewName(String fragmentAndParams) {
        String fragment = fragmentAndParams
        while (true) {
            def viewClass = mappingsProvider.getViewClass(path, fragment)

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

    @Override
    View getView(String fragment) {
        def viewClass = mappingsProvider.getViewClass(path, fragment)
        if (viewClass) {
            return viewClass.newInstance()
        }
        null
    }
}
