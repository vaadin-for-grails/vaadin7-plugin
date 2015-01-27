package com.vaadin.grails

import com.vaadin.grails.navigator.NavigationHelper
import com.vaadin.grails.spring.BeanHelper
import com.vaadin.grails.ui.UIUtils
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

    static BeanHelper getBeanHelper() {
        applicationContext.getBean(BeanHelper)
    }

    static NavigationHelper getNavigationHelper() {
        applicationContext.getBean(NavigationHelper)
    }

    /**
     * Create a new instance of the specified type.
     * If there is a prototype bean registered,
     * a new instance of the bean gets returned.
     *
     * @param type The required type
     * @param args Constructor arguments
     * @return A new instance of the specified type
     */
    static <T> T newInstance(Class<? extends T> type, Object ...args) {
        beanHelper.newInstance(type, args)
    }

    /**
     * Returns a singleton instance of the specified type.
     * If there is a (singleton) bean registered,
     * the instance of the bean gets returned.
     *
     * @param type The required type
     * @return An instance of the specified type
     */
    static <T> T getInstance(Class<? extends T> type) {
        beanHelper.getInstance(type)
    }

    /**
     * Returns a localized message for the specified property key.
     *
     * @see {@link BeanHelper#getMessage(java.lang.String, java.lang.Object[], java.util.Locale, org.springframework.context.MessageSource)}
     */
    static String i18n(String key, Object[] args = null, Locale locale = null, MessageSource messageSource = null) {
        beanHelper.getMessage(key, args, locale, messageSource)
    }

    /**
     * Enter a different UI and/or View.
     *
     * Example:
     * <code>
     *     Vaadin.enter(path: "/book", fragment: "edit", params: [id: 23455])
     * </code>
     *
     * @param path The path mapped to a UI (must start with an slash "/")
     * @param fragment The fragment mapped to a View
     * @params params Additional parameters as a map
     *
     * @see VaadinConfig.groovy
     * @see {@link NavigationHelper#enter(java.util.Map)}
     */
    static void enter(Map params) {
        navigationHelper.enter(params)
    }

    /**
     * Build Vaadin components using the Groovy Builder syntax.
     *
     * @see {@link ComponentBuilder#call(groovy.lang.Closure)}
     * @see {@link UIUtils#build(groovy.lang.Closure)}
     *
     * @param closure The builder closure
     * @return A Vaadin component
     */
    static Object build(Closure closure) {
        UIUtils.build(closure)
    }

    private Vaadin() { }
}
