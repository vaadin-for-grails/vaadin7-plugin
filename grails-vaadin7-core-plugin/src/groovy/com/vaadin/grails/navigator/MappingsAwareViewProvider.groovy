package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.server.UriMappingsHolder
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
    final UriMappingsHolder mappingsProvider

    MappingsAwareViewProvider(String path) {
        this.path = path
        mappingsProvider = Vaadin.applicationContext.getBean(UriMappingsHolder)
    }

    String getDefaultFragment() {
        mappingsProvider.getPathProperty(path, UriMappingsHolder.DEFAULT_FRAGMENT)
    }

    @Override
    String getViewName(String fragmentAndParams) {
        String fragment = fragmentAndParams

        def assignmentIndex = fragmentAndParams.indexOf("=")
        if (assignmentIndex != -1) {
            def delimiterIndex = fragmentAndParams.lastIndexOf("/", assignmentIndex)
            if (delimiterIndex == -1) {
                throw new RuntimeException("Malformed URI fragment [${fragmentAndParams}]")
//                delimiterIndex = 0
            }
            fragment = fragmentAndParams.substring(0, delimiterIndex)
        }

        if (fragment == "" && mappingsProvider.getViewClass(path, defaultFragment)) {
            return ""
        }

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
        if (fragment == "") {
            fragment = defaultFragment
        }

        def viewClass = mappingsProvider.getViewClass(path, fragment)
        if (viewClass) {
            return Vaadin.utils.instantiateVaadinComponentClass(viewClass)
        }

        null
    }
}
