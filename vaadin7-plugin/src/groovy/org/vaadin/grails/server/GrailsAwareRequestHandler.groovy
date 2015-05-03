package org.vaadin.grails.server

import com.vaadin.server.RequestHandler
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinResponse
import com.vaadin.server.VaadinSession
import org.apache.commons.lang.LocaleUtils

/**
 * Default implementation for {@link RequestHandler}.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
class GrailsAwareRequestHandler implements RequestHandler {

    @Override
    boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {

        def langParameter = request.getParameter('lang')
        def localeParameter = request.getParameter('locale') ?: langParameter
        if (localeParameter) {
            def locale = LocaleUtils.toLocale(localeParameter)
            session.locale = locale
        }

        false
    }
}
