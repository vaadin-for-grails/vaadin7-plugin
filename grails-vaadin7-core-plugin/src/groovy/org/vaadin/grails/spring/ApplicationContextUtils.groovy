package org.vaadin.grails.spring

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
 * @author Stephan Grundner
 */
final class ApplicationContextUtils {

    static ApplicationContext getApplicationContext() {
        Holders.applicationContext
    }

    static Locale getLocale() {
        UI.current?.locale ?: VaadinSession.current?.locale ?: LocaleContextHolder.locale
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
     *
     * @param type
     * @return
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
     *
     * @param type
     * @param args
     * @return
     * @throws BeanInstantiationException
     * @throws NoUniqueBeanDefinitionException
     */
    static def <T> T instantiateType(Class<T> type, Object... args) throws BeanInstantiationException, NoUniqueBeanDefinitionException {
        T instance
        def beanName = getUniqueBeanNameForType(type)
        if (beanName) {
            if (args?.length > 0) {
                instance = applicationContext.getBean(beanName, args)
            } else {
                instance = applicationContext.getBean(beanName)
            }
        } else {
            instance = type.newInstance(args as Object[])
        }
        instance
    }

    /**
     *
     * @param type
     * @param fallbackType
     * @param args
     * @return
     * @throws BeanInstantiationException
     * @throws NoUniqueBeanDefinitionException
     */
    static def <T> T instantiateType(Class<T> type, Class<? extends T> fallbackType, Object... args) throws BeanInstantiationException, NoUniqueBeanDefinitionException {
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

    private ApplicationContextUtils() { }
}
