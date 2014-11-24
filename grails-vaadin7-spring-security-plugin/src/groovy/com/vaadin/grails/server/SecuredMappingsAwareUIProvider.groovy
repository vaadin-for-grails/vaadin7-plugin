package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import com.vaadin.grails.navigator.SecuredMappingsAwareViewProvider
import com.vaadin.grails.security.LoginUI
import com.vaadin.grails.security.NotAuthorizedUI
import com.vaadin.navigator.Navigator
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.ui.UI
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils

/**
 * @author Stephan Grundner
 */
class SecuredMappingsAwareUIProvider extends MappingsAwareUIProvider {

    boolean requiresAuthentication(SecuredMapping mapping) {
        def securityService = Vaadin.applicationContext.getBean(SpringSecurityService)
        boolean hasViews = mapping.allFragments.size() > 0
        mapping.access && !securityService.isLoggedIn() && !hasViews
    }

    boolean isAuthorized(SecuredMapping mapping) {
        def access = mapping.access
        if (access) {
            return SpringSecurityUtils.ifAllGranted(access.join(","))
        }
        true
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        SecuredMapping mapping = getMapping(event)

        if (requiresAuthentication(mapping)) {
            return LoginUI
        } else {

            if (isAuthorized(mapping)) {
                return super.getUIClass(event)
            }

        }

        NotAuthorizedUI
    }

    @Override
    protected void applyNavigator(UI ui, Mapping mapping) {
        def navigator = new Navigator(ui, ui)
        navigator.addProvider(new SecuredMappingsAwareViewProvider(mapping))
        ui.navigator = navigator
    }
}
