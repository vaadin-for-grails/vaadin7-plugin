package org.vaadin.grails.server

import com.vaadin.data.util.converter.ConverterFactory
import com.vaadin.server.ErrorHandler
import com.vaadin.server.RequestHandler
import com.vaadin.server.ServiceException
import com.vaadin.server.SessionInitEvent
import com.vaadin.server.SessionInitListener
import org.vaadin.grails.data.util.converter.GrailsAwareConverterFactory
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * Grails sepecific implementation for {@link SessionInitListener}.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
class GrailsAwareSessionInitListener implements SessionInitListener {

    @Override
    void sessionInit(SessionInitEvent event) throws ServiceException {
        def session = event.session

        def converterFactory = ApplicationContextUtils.getBeanOrInstance(ConverterFactory, GrailsAwareConverterFactory)
        session.setConverterFactory(converterFactory)

        def requestHandler = ApplicationContextUtils.getBeanOrInstance(RequestHandler, GrailsAwareRequestHandler)
        session.addRequestHandler(requestHandler)

        def errorHandler = ApplicationContextUtils.getBeanOrInstance(ErrorHandler, DefaultErrorHandler)
        session.setErrorHandler(errorHandler)
    }
}
