package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.server.UriMappings
import com.vaadin.grails.server.UriMappingsHolder
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider
import com.vaadin.server.VaadinSession
import org.apache.log4j.Logger

/**
 * A {@link com.vaadin.navigator.ViewProvider} implementation that uses mappings
 * defined in the <code>VaadinConfig</code> script.
 *
 * @since 1.0
 * @author Stephan Grundner
 */
class UriMappingsAwareViewProvider implements ViewProvider {

    private  static final def log = Logger.getLogger(UriMappingsAwareViewProvider)

    protected final Map<String, View> cache = new HashMap()

    String getPath() {
        Vaadin.getUIHelper().getAttribute("path")
    }

    UriMappings getUriMappings() {
        Vaadin.getInstance(UriMappingsHolder)
    }

    String getDefaultFragment(String path) {
        uriMappings.getPathProperty(path, UriMappingsHolder.DEFAULT_FRAGMENT)
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
        if (fragment == "") {
            fragment = getDefaultFragment(path)
        }

        def viewClass = uriMappings.getViewClass(path, fragment)
        if (viewClass) {
            log.debug("View class [${viewClass?.name}] found for path [${path}] and fragment [${fragment}]")
            def view = cache.get(fragment)
            if (view == null) {
                view = Vaadin.newInstance(viewClass)
                cache.put(fragment, view)
            }
            VaadinSession.current.setAttribute(View, view)
            return view
        }

        log.debug("No View class found for path [${path}] and fragment [${fragment}]")
        null
    }
}
