package com.vaadin.grails.server

import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.VaadinSession
import com.vaadin.ui.UI

class DefaultUIProvider extends com.vaadin.server.UIProvider {

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {

        VaadinSession.current.getU
        return null
    }
}
