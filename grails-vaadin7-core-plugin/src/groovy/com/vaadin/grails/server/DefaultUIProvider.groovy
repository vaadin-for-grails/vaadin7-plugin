package com.vaadin.grails.server

import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.ui.UI
import grails.util.Holders

class DefaultUIProvider extends com.vaadin.server.UIProvider {

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        def registry = Holders.applicationContext.getBean(UIClassRegistry)
        registry.getUIClass(event.request.pathInfo)
    }

    @Override
    UI createInstance(UICreateEvent event) {
        Holders.applicationContext.getBean(event.getUIClass())
    }
}
