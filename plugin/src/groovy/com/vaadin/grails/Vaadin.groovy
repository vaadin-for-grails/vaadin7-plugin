package com.vaadin.grails

import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.vaadin.grails.navigator.Navigation
import org.vaadin.grails.ui.builders.ComponentBuilder
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * @deprecated There's no replacement for this class.
 */
@Deprecated
final class Vaadin {

    /**
     * Return the Vaadin application context.
     *
     * @deprecated Replaced by {@link ApplicationContextUtils#getApplicationContext()}
     *
     * @return The Vaadin application context
     */
    @Deprecated
    static ApplicationContext getApplicationContext() {
        ApplicationContextUtils.applicationContext
    }


    /**
     * Create a new instance of the specified type.
     * If there is a prototype bean registered,
     * a new instance of the bean gets returned.
     *
     * @deprecated Replaced by {@link ApplicationContextUtils#getPrototypeBean(java.lang.String, java.lang.Object[])}
     *
     * @param type The required type
     * @param args Constructor arguments
     * @return A new instance of the specified type
     */
    @Deprecated
    static <T> T newInstance(Class<? extends T> type, Object ...args) {
        ApplicationContextUtils.getPrototypeBean(type, args)
    }

    /**
     * Returns a singleton instance of the specified type.
     * If there is a (singleton) bean registered,
     * the instance of the bean gets returned.
     *
     * @deprecated Replaced by {@link ApplicationContextUtils#getSingletonBean(java.lang.Class)}
     *
     * @param type The required type
     * @return An instance of the specified type
     */
    @Deprecated
    static <T> T getInstance(Class<? extends T> type) {
        ApplicationContextUtils.getSingletonBean(type)
    }

    /**
     * Returns a localized message for the specified property key.
     *
     * @deprecated Replaced by {@link ApplicationContextUtils#getMessage(java.lang.String, java.lang.Object[], java.util.Locale, org.springframework.context.MessageSource)}
     */
    @Deprecated
    static String i18n(String key, Object[] args = null, Locale locale = null, MessageSource messageSource = null) {
        ApplicationContextUtils.getMessage(key, args, locale, messageSource)
    }

    /**
     * Enter a different UI and/or View.
     *
     * Example:
     * <code>
     *     Vaadin.enter(path: "/book", fragment: "edit", params: [id: 23455])
     * </code>
     *
     * @deprecated Replaced by {@link Navigation#navigateTo(java.util.Map)}
     *
     * @param path The path mapped to a UI (must start with an slash "/")
     * @param fragment The fragment mapped to a View
     * @params params Additional parameters as a map
     *
     * @see VaadinConfig.groovy
     */
    @Deprecated
    static void enter(Map params) {
        Navigation.navigateTo(params)
    }

    /**
     * Build Vaadin components using the Groovy Builder syntax.
     *
     * @see {@link ComponentBuilder#call(groovy.lang.Closure)}
     *
     * @deprecated Replaced by {@link ComponentBuilder#build(groovy.lang.Closure)}
     *
     * @param closure The builder closure
     * @return A Vaadin component
     */
    @Deprecated
    static Object build(Closure closure) {
        ComponentBuilder.build(closure)
    }

    private Vaadin() { }
}
