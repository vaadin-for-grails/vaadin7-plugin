package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.server.MappingsProvider
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider

/**
 * A {@link com.vaadin.navigator.ViewProvider} implementation that uses mappings
 * defined with {@link com.vaadin.grails.VaadinMappingsClass} artefacts.
 *
 * @author Stephan Grundner
 */
class MappingsAwareViewProvider implements ViewProvider {

    final MappingsProvider.Mapping mapping

    MappingsAwareViewProvider(MappingsProvider.Mapping mapping) {
        this.mapping = mapping
    }

    @Override
    String getViewName(String pathAndParameters) {

        String path = pathAndParameters
        while (true) {
            if (mapping.viewMappings.containsKey(path)) {
                return path
            }
            def i = path.lastIndexOf("/")
            if (i == -1) {
                break
            }
            path = path.substring(0, i)
        }
        null
    }

    @Override
    View getView(String path) {
        def viewName = mapping.viewMappings.get(path)
        def viewClass = Vaadin.vaadinUtils.getVaadinViewClass(viewName, mapping.namespace)
        if (viewClass) {
            return viewClass.newInstance()
        }
        null
    }
}
