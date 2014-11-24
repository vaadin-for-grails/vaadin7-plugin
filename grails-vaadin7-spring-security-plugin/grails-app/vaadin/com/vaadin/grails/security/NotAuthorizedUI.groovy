package com.vaadin.grails.security

import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import com.vaadin.ui.UI

/**
 * @author Stephan Grundner
 */
class NotAuthorizedUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        content = new Label("Not authorized.")
    }
}
