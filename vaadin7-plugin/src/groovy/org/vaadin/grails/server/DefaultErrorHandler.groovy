package org.vaadin.grails.server

import com.vaadin.server.ErrorEvent
import com.vaadin.server.ErrorHandler
import com.vaadin.server.Page
import com.vaadin.ui.Notification
import org.codehaus.groovy.runtime.StackTraceUtils

class DefaultErrorHandler implements ErrorHandler {

    @Override
    void error(ErrorEvent event) {
        def throwable = event.throwable
        def page = Page.current
        if (page) {
            def n = new Notification(null, null, Notification.Type.ERROR_MESSAGE)
            def cause = StackTraceUtils.extractRootCause(throwable)
            n.description = cause.toString()
            n.show(Page.current)
        }
        throwable.printStackTrace()
    }
}
