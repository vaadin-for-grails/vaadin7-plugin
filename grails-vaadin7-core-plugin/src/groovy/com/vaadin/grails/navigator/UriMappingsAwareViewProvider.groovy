package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.server.UriMappingsHolder
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider
import org.apache.log4j.Logger

/**
 * A {@link com.vaadin.navigator.ViewProvider} implementation that uses mappings
 * defined in the <code>VaadinConfig</code> script.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
class UriMappingsAwareViewProvider implements ViewProvider {

    private  static final def log = Logger.getLogger(UriMappingsAwareViewProvider)

    final String path
    final UriMappingsHolder uriMappings

    UriMappingsAwareViewProvider(String path) {
        this.path = path
        uriMappings = Vaadin.applicationContext.getBean(UriMappingsHolder)
    }

    String getDefaultFragment() {
        uriMappings.getPathProperty(path, UriMappingsHolder.DEFAULT_FRAGMENT)
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

        if (fragment == "" && uriMappings.getViewClass(path, defaultFragment)) {
            return ""
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

    @Override
    View getView(String fragment) {
        if (fragment == "") {
            fragment = defaultFragment
        }

        def viewClass = uriMappings.getViewClass(path, fragment)
        if (viewClass) {
            log.debug("View class [${viewClass?.fullName}] found for path [${path}] and fragment [${fragment}]")
            return Vaadin.utils.instantiateVaadinComponentClass(viewClass)
        }

        log.debug("No View class found for path [${path}] and fragment [${fragment}]")
        null
    }
}
