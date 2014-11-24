package com.vaadin.grails

import grails.util.Holders
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

/**
 * Your best friend when developing Vaadin applications.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
final class Vaadin {

    /**
     * Get the Vaadin Util singleton.
     *
     * @return The Vaadin Util singleton
     */
    static VaadinUtils getUtils() {
        def application = Holders.grailsApplication
        def mainContext = application.mainContext
        mainContext.getBean(VaadinUtils)
    }

    /**
     * Return the Vaadin application context.
     *
     * @return The Vaadin application context
     */
    static ApplicationContext getApplicationContext() {
        utils.applicationContext
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
    static String i18n(String key, Object[] args = null, Locale locale = null, MessageSource messageSource = null) {
        utils.i18n(key, args, locale, messageSource)
    }

    /**
     * Switch between Views and UIs.
     *
     * @see com.vaadin.grails.navigator.NavigationUtils#enter(java.util.Map)
     * @param params Navigation params
     */
    static void enter(Map params) {
        utils.navigationUtils.enter(params)
    }

    private Vaadin() { }
}
