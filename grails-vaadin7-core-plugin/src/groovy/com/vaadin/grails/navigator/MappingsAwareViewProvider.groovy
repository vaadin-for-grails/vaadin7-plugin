package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.server.MappingsProvider
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider
import org.apache.log4j.Logger

/**
 * A {@link com.vaadin.navigator.ViewProvider} implementation that uses mappings
 * defined in the <code>VaadinConfig</code> script.
 *
 * @author Stephan Grundner
 */
class MappingsAwareViewProvider implements ViewProvider {

    static final def log = Logger.getLogger(MappingsAwareViewProvider)

    final String path

    final MappingsProvider mappingsProvider

    MappingsAwareViewProvider(String path) {
        this.path = path
        mappingsProvider = Vaadin.applicationContext.getBean(MappingsProvider)
    }

    @Override
    String getViewName(String fragmentAndParams) {
        String fragment = fragmentAndParams
        log.debug("Find view for fragment [${fragment}]")
        while (true) {
            def viewClass = mappingsProvider.getViewClass(path, fragment)

            if (viewClass) {
                log.debug("Found: ${fragment}")
                return fragment
            }
            def i = fragment.lastIndexOf("/")
            if (i == -1) {
                break
            }
            fragment = fragment.substring(0, i)
        }

        log.debug("Not found")
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
