package org.vaadin.grails.util

import com.vaadin.server.VaadinSession
import com.vaadin.ui.UI
import grails.util.Holders
import org.springframework.beans.BeanInstantiationException
import org.springframework.beans.factory.NoUniqueBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceResolvable
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Convenience methods for working with the current {@link ApplicationContext}.
 *
 * @see {@link Holders#getGrailsApplication()}
 * @see {@link org.codehaus.groovy.grails.commons.GrailsApplication#getMainContext()}
 *
 * @author Stephan Grundner
 * @since 2.0
 */
final class ApplicationContextUtils {

    static ApplicationContext getApplicationContext() {
        Holders.grailsApplication.mainContext
    }

    static Locale getLocale() {
        UI.current?.locale ?: VaadinSession.current?.locale ?: LocaleContextHolder.locale
    }

    /**
     * Get the localized message for the specified key.
     *
     * @param key The message key
     * @param defaultMessage The defaultMessage
     * @param args The arguments array or null
     * @param locale The locale or null
     * @param messageSource The message source or null
     * @return A localized message for the specified key
     */
    static String getMessage(String key, String defaultMessage, Object[] args = null, Locale locale = null, MessageSource messageSource = null) {
        if (messageSource == null) {
            messageSource = applicationContext.getBean("messageSource")
        }
        messageSource.getMessage(key, args, defaultMessage, locale ?: this.locale)
    }

    /**
     * Get the localized message for the specified key.
     *
     * @param key The message key
     * @param args The arguments array or null
     * @param locale The locale or null
     * @param messageSource The message source or null
     * @return A localized message for the specified key
     */
    static String getMessage(String key, Object[] args = null, Locale locale = null, MessageSource messageSource = null) {
        if (messageSource == null) {
            messageSource = applicationContext.getBean("messageSource")
        }
        String result
        try {
            result = messageSource.getMessage(key, args, locale ?: this.locale)
        } catch (NoSuchMessageException e) {
            result = "[${key}]"
        }
        result
    }

    static String getMessage(MessageSourceResolvable resolvable, Locale locale = null, MessageSource messageSource = null) {
        if (messageSource == null) {
            messageSource = applicationContext.getBean("messageSource")
        }
        String result
        try {
            result = messageSource.getMessage(resolvable, locale ?: this.locale)
        } catch (NoSuchMessageException e) {
            result = "[${resolvable.codes}]"
        }
        result
    }

    /**
     * Get the unique bean name for the specified type.
     *
     * @param type
     * @return The unique bean name for the specified type or null
     * @throws NoUniqueBeanDefinitionException
     */
    static String getUniqueBeanNameForType(Class<?> type) throws NoUniqueBeanDefinitionException {
        def beanNames = applicationContext.getBeanNamesForType(type)
        if (beanNames.size() > 1) {
            throw new NoUniqueBeanDefinitionException(type, beanNames)
        }
        beanNames.length == 1 ? beanNames.first() : null
    }

    /**
     * Get the bean for the specified type, or a new instance of the specified fallback type if no bean is available.
     *
     * @param type
     * @param fallbackType
     * @param args
     * @return The bean for the specified type, or a new instance of the specified fallback type if no bean is available
     * @throws BeanInstantiationException
     * @throws NoUniqueBeanDefinitionException
     */
    static def <T> T getBeanOrInstance(Class<T> type, Class<? extends T> fallbackType, Object... args) throws BeanInstantiationException, NoUniqueBeanDefinitionException {
        T instance
        def beanName = getUniqueBeanNameForType(type)
        if (beanName) {
            if (args?.length > 0) {
                instance = applicationContext.getBean(beanName, args)
            } else {
                instance = applicationContext.getBean(beanName)
            }
        } else {
            instance = fallbackType.newInstance(args as Object[])
        }
        instance
    }

    /**
     * Get the bean for the specified type, or a new instance if no bean is available.
     *
     * @param type
     * @param args
     * @return The bean for the specified type, or a new instance if no bean is available
     * @throws BeanInstantiationException
     * @throws NoUniqueBeanDefinitionException
     */
    static def <T> T getBeanOrInstance(Class<T> type, Object... args) throws BeanInstantiationException, NoUniqueBeanDefinitionException {
        getBeanOrInstance(type, type, args)
    }

    static def Object getSingletonBean(String name) throws BeanInstantiationException {
        if (!applicationContext.isSingleton(name)) {
            def beanClass = applicationContext.getType(name)
            throw new BeanInstantiationException(beanClass, "Singleton bean required")
        }
        applicationContext.getBean(name)
    }

    static def <T> T getSingletonBean(Class<T> type) throws BeanInstantiationException, NoUniqueBeanDefinitionException {
        def beanName = getUniqueBeanNameForType(type)
        (T) getSingletonBean(beanName)
    }

    static def Object getPrototypeBean(String name) throws BeanInstantiationException {
        if (!applicationContext.isPrototype(name)) {
            def beanClass = applicationContext.getType(name)
            throw new BeanInstantiationException(beanClass, "Prototype bean required")
        }
        applicationContext.getBean(name)
    }

    static def <T> T getPrototypeBean(Class<T> type) throws BeanInstantiationException, NoUniqueBeanDefinitionException {
        def beanName = getUniqueBeanNameForType(type)
        (T) getPrototypeBean(beanName)
    }

    private ApplicationContextUtils() { }
}
