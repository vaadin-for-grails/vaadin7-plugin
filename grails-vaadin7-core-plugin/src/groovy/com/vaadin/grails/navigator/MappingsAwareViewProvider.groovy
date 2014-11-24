package com.vaadin.grails.navigator

import com.vaadin.grails.server.Mapping
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider

/**
 * A {@link com.vaadin.navigator.ViewProvider} implementation that uses mappings
 * defined in the <code>VaadinConfig</code> script.
 *
 * @author Stephan Grundner
 */
class MappingsAwareViewProvider implements ViewProvider {

    final Mapping mapping

    MappingsAwareViewProvider(Mapping mapping) {
        this.mapping = mapping
    }

    @Override
    String getViewName(String fragmentAndParams) {
        String fragment = fragmentAndParams
        while (true) {
            if (mapping.containsFragment(fragment)) {
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
        def viewClass = mapping.getViewClass(fragment)
        if (viewClass) {
            return viewClass.newInstance()
        }
        null
    }
}
