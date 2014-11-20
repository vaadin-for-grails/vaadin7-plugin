package com.vaadin.grails.navigator

import com.vaadin.grails.MappingsProvider
import com.vaadin.grails.MappingsProvider.Mapping
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider
import grails.util.Holders

import javax.annotation.PostConstruct

class MappingsAwareViewProvider implements ViewProvider {

    MappingsProvider mappingsProvider

    Map<String, MappingsProvider.Mapping> viewMappings

    MappingsAwareViewProvider() {

    }

    @PostConstruct
    void init() {
        viewMappings = mappingsProvider.viewMappings
    }

    @Override
    String getViewName(String viewAndParameters) {
        String path = "#$viewAndParameters"
        while (true) {
            if (viewMappings.containsKey(path)) {
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
        def path = "#$viewName"
        def mapping = viewMappings[path]
        if (mapping) {
            return createInstance(mapping)
        }
        null
    }

    View createInstance(Mapping mapping) {
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
