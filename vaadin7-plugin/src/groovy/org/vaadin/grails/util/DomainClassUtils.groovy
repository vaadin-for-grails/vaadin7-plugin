package org.vaadin.grails.util

import grails.util.GrailsNameUtils
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder

/**
 * @author Stephan Grundner
 *
 * @see {@link GrailsNameUtils}
 * @since 2.0
 */
final class DomainClassUtils {

    static GrailsDomainClass getDomainClass(String name) {
        def grailsApplication = Holders.grailsApplication
        grailsApplication.getDomainClass(name) as GrailsDomainClass
    }

    static GrailsDomainClass getDomainClass(Class<?> type) {
        getDomainClass(type?.name)
    }

    static String getCaption(Class<?> type, String propertyName, Locale locale) {
//        TODO Handle nested captions
//        if (StringUtils.contains(propertyId, '.'.toCharacter())) {
//            return getNestedCaption(type, propertyId, locale)
//        }
        def typePropertyName = GrailsNameUtils.getPropertyNameRepresentation(type)
        def applicationContext = Holders.applicationContext
        def caption
        try {
            def code = "$typePropertyName.$propertyName"
            caption = applicationContext.getMessage(code, [] as Object[], locale ?: LocaleContextHolder.locale)
        } catch (NoSuchMessageException e) {
            caption = GrailsNameUtils.getNaturalName(propertyName)
        }
        caption
    }

    static String getCaption(Class<?> type, Object propertyName, Locale locale) {
        getCaption(type, (String) propertyName?.toString(), locale)
    }

    static String getCaption(Class<?> type, String propertyName) {
        def locale = ApplicationContextUtils.locale
        getCaption(type, propertyName, locale)
    }

    static String getCaption(Class<?> type, Object propertyId) {
        getCaption(type, (String) propertyId?.toString())
    }

    static String[] getCaptionList(Class<?> type, Object[] propertyIdList, Locale locale) {
        def result = new String[propertyIdList.length]
        propertyIdList.eachWithIndex { propertyId, index ->
            result[index] = getCaption(type, propertyId, locale)
        }
        result
    }

    private DomainClassUtils() { }
}
