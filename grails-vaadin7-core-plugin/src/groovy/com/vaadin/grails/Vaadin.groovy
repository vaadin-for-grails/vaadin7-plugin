package com.vaadin.grails

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

    static void enter(Map params) {
        utils.navigationUtils.enter(params)
    }

//    /**
//     * Enter a view or an ui.
//     *
//     * @param targetClass The class of the target
//     * @param params The parameter map (optional)
//     */
//    static void enter(Class<?> targetClass, Map params = null) {
//        def navigationUtils = utils.navigationUtils
//        navigationUtils.enter(targetClass, params)
//    }
//
//    /**
//     * Enter a view in a different ui.
//     *
//     * @param uiClass The ui class
//     * @param viewClass The view class (optional)
//     * @param params The parameter map (optional)
//     */
//    static void enter(Class<? extends UI> uiClass, Class<? extends View> viewClass, Map params = null) {
//        def navigationUtils = utils.navigationUtils
//        navigationUtils.enter(uiClass, viewClass, params)
//    }

    private Vaadin() { }
}
