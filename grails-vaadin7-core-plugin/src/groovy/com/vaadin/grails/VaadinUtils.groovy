package com.vaadin.grails

import com.vaadin.grails.navigator.NavigationUtils
import com.vaadin.navigator.View
import com.vaadin.server.VaadinSession
import com.vaadin.ui.UI
import grails.util.GrailsNameUtils
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Utils for ease access to Grails within Vaadin applications.
 *
 * @author Stephan Grundner
 */
class VaadinUtils {

    /**
     * Return the Vaadin application context for the current session.
     *
     * @return The Vaadin application context for the current session
     */
    ApplicationContext getApplicationContext() {
        Holders.applicationContext
    }

    /**
     * Returns a localized message for the specified property key.
     *
     * @param key The property key or null
     * @param args The arguments array or null
     * @param locale The locale or null
     * @param messageSource The message source or null
     * @return A localized message for the specified property key
     */
    String i18n(String key, Object[] args = null, Locale locale = null, MessageSource messageSource = null) {
        if (messageSource == null) {
            messageSource = applicationContext.getBean("messageSource")
        }
        String result
        try {
            result = messageSource.getMessage(key, args, locale ?: VaadinSession.current?.locale ?: LocaleContextHolder.locale)
        } catch (NoSuchMessageException e) {
            result = "[${key}]"
        }
        result
    }

    private Collection<VaadinClass> getVaadinClasses(String name, String type, String namespace = null) {
        def found = Holders.grailsApplication.getArtefacts(type).findAll {
            it instanceof VaadinClass &&
                it.logicalPropertyName == name &&
                    it["namespace"] == namespace
        }
        found
    }

    Object newInstance(VaadinClass vaadinClass) {
        def beanName
        if (vaadinClass.namespace) {
            beanName = "${vaadinClass.namespace}.${vaadinClass.propertyName}"
        } else {
            beanName = vaadinClass.propertyName
        }
        applicationContext.getBean(beanName)
    }

    VaadinUIClass getCurrentVaadinUIClass() {
        getVaadinUIClass(UI.current.class)
    }

    /**
     * Return the Vaadin UI Artefact class for the specified UI class.
     *
     * @param nativeUIClass The native UI class
     * @return The Vaadin UI Artefact class for the specified UI class
     */
    VaadinUIClass getVaadinUIClass(Class<? extends UI> nativeUIClass) {
        def logicalPropertyName = GrailsNameUtils.getLogicalPropertyName(nativeUIClass.name, "UI")
        def namespace = GrailsClassUtils.getStaticPropertyValue(nativeUIClass, "namespace")
        def artefacts = Holders.grailsApplication.getArtefacts("UI")
        artefacts.find { VaadinUIClass artefact ->
            artefact.logicalPropertyName == logicalPropertyName &&
                    artefact.namespace == namespace
        }
    }

    /**
     * Return the Vaadin View Artefact class for the specified native View class.
     *
     * @param nativeViewClass The native View class
     * @return The Vaadin View Artefact class for the specified native View class
     */
    VaadinViewClass getVaadinViewClass(Class<? extends View> nativeViewClass) {
        def logicalPropertyName = GrailsNameUtils.getLogicalPropertyName(nativeViewClass.name, "View")
        def namespace = GrailsClassUtils.getStaticPropertyValue(nativeViewClass, "namespace")
        def artefacts = Holders.grailsApplication.getArtefacts("View")
        artefacts.find { VaadinViewClass artefact ->
            artefact.logicalPropertyName == logicalPropertyName &&
                    artefact.namespace == namespace
        }
    }

    VaadinUIClass getVaadinUIClass(String name, String namespace = null) {
        def found = getVaadinClasses(name, "UI", namespace)
        if (found?.size() > 1) {
            def message = "Multiple Vaadin UIs found for name [${name}]"
            if (namespace) {
                message += " and namespace [${namespace}]"
            }
            throw new RuntimeException(message)
        }
        found.empty ? null : (VaadinUIClass) found.first()
    }

    VaadinViewClass getVaadinViewClass(String name, String namespace = null) {
        def found = getVaadinClasses(name, "View", namespace)

        if (found?.size() > 1) {
            def message = "Multiple Vaadin Views found for name [${name}]"
            if (namespace) {
                message += " and namespace [${namespace}]"
            }
            throw new RuntimeException(message)
        }
        found.empty ? null : (VaadinViewClass) found.first()
    }

    NavigationUtils getNavigationUtils() {

        applicationContext.getBean(NavigationUtils)
    }
}
