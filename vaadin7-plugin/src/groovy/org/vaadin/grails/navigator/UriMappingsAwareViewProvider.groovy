package org.vaadin.grails.navigator

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider
import com.vaadin.server.VaadinSession
import grails.util.Holders
import org.apache.log4j.Logger
import org.vaadin.grails.server.UriMappings
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * A {@link com.vaadin.navigator.ViewProvider} implementation that uses uri mappings
 * defined in the <code>VaadinConfig</code> script.
 *
 * @since 1.0
 * @author Stephan Grundner
 */
class UriMappingsAwareViewProvider implements ViewProvider {

    private  static final def log = Logger.getLogger(UriMappingsAwareViewProvider)

    UriMappings getUriMappings() {
        def applicationContext = Holders.applicationContext
        applicationContext.getBean(UriMappings)
    }

    String getDefaultFragment(String path) {
        uriMappings.getPathProperty(path, UriMappings.DEFAULT_FRAGMENT_PATH_PROPERTY)
    }

    @Override
    String getViewName(String fragmentAndParams) {
        String fragment = fragmentAndParams

        if (fragment == null) {
            return null
        }

        def assignmentIndex = fragmentAndParams.indexOf("=")
        if (assignmentIndex != -1) {
            def delimiterIndex = fragmentAndParams.lastIndexOf("/", assignmentIndex)
            if (delimiterIndex == -1) {
                throw new RuntimeException("Malformed URI fragment [${fragmentAndParams}]")
//                delimiterIndex = 0
            }
            fragment = fragmentAndParams.substring(0, delimiterIndex)
        }

        def path = Navigation.current.path
        if (fragment == "" && uriMappings.getViewClass(path, getDefaultFragment(path))) {
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
        def path = Navigation.current.path

        if (fragment == "") {
            fragment = getDefaultFragment(path)
        }

        def viewClass = uriMappings.getViewClass(path, fragment)
        if (viewClass) {
            log.debug("View class [${viewClass?.name}] found for path [${path}] and fragment [${fragment}]")
            def view = ApplicationContextUtils.getBeanOrInstance(viewClass)
            VaadinSession.current.setAttribute(View, view)
            return view
        }

        log.debug("No View class found for path [${path}] and fragment [${fragment}]")
        null
    }
}
