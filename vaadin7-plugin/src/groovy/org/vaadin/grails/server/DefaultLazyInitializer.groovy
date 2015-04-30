package org.vaadin.grails.server

import com.vaadin.data.util.converter.ConverterFactory
import com.vaadin.server.ErrorHandler
import com.vaadin.server.VaadinSession
import com.vaadin.ui.HasComponents
import com.vaadin.ui.UI
import grails.util.Holders
import org.vaadin.grails.data.util.converter.DefaultConverterFactory
import org.vaadin.grails.ui.util.ComponentUtils
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * @author Stephan Grundner
 * @since 2.0
 */
class DefaultLazyInitializer extends LazyInitializer {

    @Override
    protected void initialize(VaadinSession session) {
        def converterFactory = ApplicationContextUtils.getBeanOrInstance(ConverterFactory, DefaultConverterFactory)
        session.setConverterFactory(converterFactory)

        def errorHandler = ApplicationContextUtils.getBeanOrInstance(ErrorHandler, DefaultErrorHandler)
        session.setErrorHandler(errorHandler)
    }

    @Override
    protected void initialize(UI ui) {
        def nullRepresentation = Holders.config.vaadin.nullRepresentation
        if (nullRepresentation != null) {
            ui.addComponentAttachListener(new HasComponents.ComponentAttachListener() {
                @Override
                void componentAttachedToContainer(HasComponents.ComponentAttachEvent e) {
                    def component = e.attachedComponent
                    if (component instanceof HasComponents.ComponentAttachDetachNotifier) {
                        component.addComponentAttachListener(this)
                    }
                    ComponentUtils.setNullRepresentation(component, nullRepresentation.toString())
                }
            })
        }
    }
}
