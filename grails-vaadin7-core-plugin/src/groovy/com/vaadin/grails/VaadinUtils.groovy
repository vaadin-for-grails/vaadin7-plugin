package com.vaadin.grails

import com.vaadin.grails.navigator.NavigationUtils
import com.vaadin.server.VaadinSession
import grails.util.Holders
import org.springframework.beans.BeanInstantiationException
import org.springframework.beans.factory.NoUniqueBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

/**
 * Utils for ease access to Grails within Vaadin applications.
 *
 * @author Stephan Grundner
 */
@Component
class VaadinUtils {

    /**
     * Return the Vaadin application context for the current session.
     *
     * @return The Vaadin application context for the current session
     */
    ApplicationContext getApplicationContext() {
        Holders.applicationContext
    }

    NavigationUtils getNavigationUtils() {
        Vaadin.getInstance(NavigationUtils)
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

    String getUniqueBeanNameForType(Class<?> type) throws NoUniqueBeanDefinitionException {
        def beanNames = applicationContext.getBeanNamesForType(type)
        if (beanNames.size() > 1) {
            throw new NoUniqueBeanDefinitionException(type, beanNames)
        }
        beanNames.length == 1 ? beanNames.first() : null
    }

    def <T> T newInstance(Class<? extends T> type, Object ...args) {
        def beanName = getUniqueBeanNameForType(type)
        if (!applicationContext.isPrototype(beanName)) {
            throw new BeanInstantiationException(type, "[${beanName}] is not a prototype")
        }
        applicationContext.getBean(beanName, args)
    }

    def <T> T getInstance(Class<? extends T> type) {
        def beanName = getUniqueBeanNameForType(type)
//        TODO get singleton!
        applicationContext.getBean(beanName)
    }
}
