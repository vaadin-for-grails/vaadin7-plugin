package com.vaadin.grails

import com.vaadin.grails.navigator.NavigationHelper
import com.vaadin.grails.ui.builders.ComponentBuilder
import grails.util.Holders
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

/**
 * Your best friend when developing Vaadin applications.
 *
 * @since 1.0
 * @author Stephan Grundner
 */
final class Vaadin {

    /**
     * Return the Vaadin application context.
     *
     * @return The Vaadin application context
     */
    static ApplicationContext getApplicationContext() {
        Holders.applicationContext
    }

    static SpringHelper getSpringHelper() {
        applicationContext.getBean(SpringHelper)
    }

    static NavigationHelper getNavigationHelper() {
        applicationContext.getBean(NavigationHelper)
    }

    static <T> T newInstance(Class<? extends T> type, Object ...args) {
        springHelper.newInstance(type, args)
    }

    static <T> T getInstance(Class<? extends T> type) {
        springHelper.getInstance(type)
    }

    static String i18n(String key, Object[] args = null, Locale locale = null, MessageSource messageSource = null) {
        springHelper.i18n(key, args, locale, messageSource)
    }

    static void enter(Map params) {
        navigationHelper.enter(params)
    }

    static Object build(Closure closure) {
        (new ComponentBuilder()).call(closure)
    }

    private Vaadin() { }
}
