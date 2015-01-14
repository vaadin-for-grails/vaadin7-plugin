package com.vaadin.grails

import com.vaadin.navigator.View
import com.vaadin.ui.UI
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

    static <T> T newInstance(Class<? extends T> type, Object ...args) {
        utils.newInstance(type, args)
    }

    static <T> T getInstance(Class<? extends T> type) {
        utils.getInstance(type)
    }

    static void enter(Class<?> uiOrViewClass, Map params = null) {
        if (UI.isAssignableFrom(uiOrViewClass)) {
            enter(uiOrViewClass, null, params)
        } else {
            enter(null, uiOrViewClass, params)
        }
    }

    static void enter(Class<? extends UI> uiClass, Class<? extends View> viewClass, Map params = null) {
        utils.navigationUtils.enter(uiClass, viewClass, params)
    }

    private Vaadin() { }
}
