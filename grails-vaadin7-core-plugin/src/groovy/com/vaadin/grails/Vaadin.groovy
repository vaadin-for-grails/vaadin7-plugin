package com.vaadin.grails

import com.vaadin.grails.navigator.NavigationHelper
import com.vaadin.grails.spring.BeanHelper
import com.vaadin.grails.ui.UIHelper
import com.vaadin.grails.ui.builders.ComponentBuilder
import grails.util.Holders
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.vaadin.grails.navigator.Navigation
import org.vaadin.grails.spring.ApplicationContextUtils

/**
 * Your best friend when developing Vaadin applications.
 *
 * @deprecated
 * @since 1.0
 * @author Stephan Grundner
 */
@Deprecated
final class Vaadin {

    /**
     * Return the Vaadin application context.
     *
     * @deprecated
     * @return The Vaadin application context
     */
    @Deprecated
    static ApplicationContext getApplicationContext() {
        Holders.applicationContext
    }

    @Deprecated
    static BeanHelper getBeanHelper() {
//        applicationContext.getBean(com.vaadin.grails.spring.BeanHelper)
        ApplicationContextUtils.instantiateType(com.vaadin.grails.spring.BeanHelper)
    }

    @Deprecated
    static NavigationHelper getNavigationHelper() {
//        applicationContext.getBean(com.vaadin.grails.navigator.NavigationHelper)
        ApplicationContextUtils.instantiateType(com.vaadin.grails.navigator.NavigationHelper)
    }

    @Deprecated
    static UIHelper getUIHelper() {
//        applicationContext.getBean(com.vaadin.grails.ui.UIHelper)
        ApplicationContextUtils.instantiateType(com.vaadin.grails.ui.UIHelper)
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
    @Deprecated
    static <T> T newInstance(Class<? extends T> type, Object ...args) {
//        getBeanHelper().newInstance(type, args)
        ApplicationContextUtils.instantiateType(type, args)
    }

    /**
     * Returns a singleton instance of the specified type.
     * If there is a (singleton) bean registered,
     * the instance of the bean gets returned.
     *
     * @param type The required type
     * @return An instance of the specified type
     */
    @Deprecated
    static <T> T getInstance(Class<? extends T> type) {
//        getBeanHelper().getInstance(type)
        Holders.applicationContext.getBean(type)
    }

    /**
     * Returns a localized message for the specified property key.
     *
     * @see {@link BeanHelper#getMessage(java.lang.String, java.lang.Object[], java.util.Locale, org.springframework.context.MessageSource)}
     */
    @Deprecated
    static String i18n(String key, Object[] args = null, Locale locale = null, MessageSource messageSource = null) {
        getBeanHelper().getMessage(key, args, locale, messageSource)
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
     * @deprecated
     */
    @Deprecated
    static void enter(Map params) {
//        getNavigationHelper().enter(params)
        Navigation.enter(params)
    }

    /**
     * Build Vaadin components using the Groovy Builder syntax.
     *
     * @see {@link ComponentBuilder#call(groovy.lang.Closure)}
     * @see {@link UIHelper#build(groovy.lang.Closure)}
     *
     * @deprecated
     * @param closure The builder closure
     * @return A Vaadin component
     */
    @Deprecated
    static Object build(Closure closure) {
        getUIHelper().build(closure)
    }

    private Vaadin() { }
}
