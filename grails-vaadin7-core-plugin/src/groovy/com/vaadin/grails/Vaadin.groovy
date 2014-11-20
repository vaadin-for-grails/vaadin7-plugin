package com.vaadin.grails

import com.vaadin.grails.navigator.NavigationUtils
import com.vaadin.navigator.View
import com.vaadin.ui.UI
import grails.util.Holders
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

/**
 * Your best friend when developing Vaadin applications.
 *
 * @author Stephan Grundner
 */
final class Vaadin {

    /**
     * Get the Vaadin Util singleton.
     *
     * @return The Vaadin Util singleton
     */
    static VaadinUtils getVaadinUtils() {
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
        vaadinUtils.applicationContext
    }

    /**
     * Return the navigation utils singleton.
     *
     * @return The navigation utils
     */
    static NavigationUtils getNavigationUtils() {
        applicationContext.getBean(NavigationUtils)
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
        vaadinUtils.i18n(key, args, locale, messageSource)
    }

    /**
     * Enter a view or an ui.
     *
     * @param targetClass The class of the target
     * @param params The parameter map (optional)
     */
    static void enter(Class<?> targetClass, Map params = null) {
        navigationUtils.enter(targetClass, params)
    }

    /**
     * Enter a view in a different ui.
     *
     * @param uiClass The ui class
     * @param viewClass The view class (optional)
     * @param params The parameter map (optional)
     */
    static void enter(Class<? extends UI> uiClass, Class<? extends View> viewClass, Map params = null) {
        navigationUtils.enter(uiClass, viewClass, params)
    }

    private Vaadin() { }
}
