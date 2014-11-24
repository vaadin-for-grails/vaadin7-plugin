package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.security.LoginView
import com.vaadin.grails.security.NotAuthorizedView
import com.vaadin.grails.server.SecuredMapping
import com.vaadin.navigator.View
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils

/**
 * @author Stephan Grundner
 */
class SecuredMappingsAwareViewProvider extends MappingsAwareViewProvider {

    SecuredMapping mapping

    SecuredMappingsAwareViewProvider(SecuredMapping mapping) {
        super(mapping)
        this.mapping = mapping
    }

    boolean requiresAuthentication(SecuredMapping mapping, String fragment) {
        def securityService = Vaadin.applicationContext.getBean(SpringSecurityService)
        mapping.getFragmentAccess(fragment) && !securityService.isLoggedIn()
    }

    boolean isAuthorized(SecuredMapping mapping, String fragment) {
        def access = mapping.getFragmentAccess(fragment)
        if (access) {
            return SpringSecurityUtils.ifAllGranted(access.join(","))
        }
        true
    }

    @Override
    String getViewName(String fragmentAndParams) {
        println "get view name ${mapping.allFragments}"
        super.getViewName(fragmentAndParams)
    }

    @Override
    View getView(String fragment) {
        def viewClass = mapping.getViewClass(fragment)
        println "get view class: ${viewClass}"
        if (viewClass) {
            if (requiresAuthentication(mapping, fragment)) {
                return LoginView.newInstance()
            } else {
                if (isAuthorized(mapping, fragment)) {
                    return super.getView(fragment)
                } else {
                    return NotAuthorizedView.newInstance()
                }
            }
        }
        null
    }
}
