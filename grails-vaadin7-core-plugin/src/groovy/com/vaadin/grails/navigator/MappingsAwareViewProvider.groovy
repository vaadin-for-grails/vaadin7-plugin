package com.vaadin.grails.navigator

import com.vaadin.grails.server.MappingsProvider
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider
import grails.util.Holders

/**
 * A {@link com.vaadin.navigator.ViewProvider} implementation that uses mappings
 * defined with {@link com.vaadin.grails.VaadinMappingsClass} artefacts.
 *
 * @author Stephan Grundner
 */
class MappingsAwareViewProvider implements ViewProvider {

    MappingsProvider mappingsProvider

    MappingsAwareViewProvider() {

    }

    @Override
    String getViewName(String viewAndParameters) {
        String path = "#!$viewAndParameters"
        while (true) {
            if (mappingsProvider.containsMapping(path)) {
                return path.substring(1)
            }
            def i = path.lastIndexOf("/")
            if (i == -1) {
                break
            }
            path = path.substring(0, i)
        }
        viewAndParameters
    }

    @Override
    View getView(String viewName) {
        def path = "#!$viewName"
        def mapping = mappingsProvider.getMapping(path)
        if (mapping) {
            return createInstance(mapping)
        }
        null
    }

    /**
     * Return a new View instance as definied in the specified mapping.
     *
     * @param mapping A view mapping
     * @return A new View instance as definied in the specified mapping
     */
    View createInstance(MappingsProvider.ViewMapping mapping) {
        def applicationContext = Holders.applicationContext
        def viewClass = mapping.clazz

        View view

        def beanNames = applicationContext.getBeanNamesForType(viewClass)
        if (beanNames?.size() == 1) {
            view = applicationContext.getBean(beanNames.first())
        } else {
//            TODO warn: More than one bean found for class
            view = viewClass.newInstance()
        }

        view
    }
}
