package org.vaadin.grails.util

import grails.util.GrailsNameUtils
import grails.util.Holders
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder

/**
 * @author Stephan Grundner
 *
 * @see {@link GrailsNameUtils}
 * @since 2.0
 */
final class DomainClassUtils {

    static String getCaption(Class<?> type, String propertyId, Locale locale) {
//        TODO Handle nested captions
//        if (StringUtils.contains(propertyId, '.'.toCharacter())) {
//            return getNestedCaption(type, propertyId, locale)
//        }
        def typePropertyName = GrailsNameUtils.getPropertyNameRepresentation(type)
        def applicationContext = Holders.applicationContext
        def caption
        try {
            def code = "$typePropertyName.$propertyId"
            caption = applicationContext.getMessage(code, [] as Object[], locale ?: LocaleContextHolder.locale)
        } catch (NoSuchMessageException e) {
            caption = GrailsNameUtils.getNaturalName(propertyId)
        }
        caption
    }

    static String getCaption(Class<?> type, Object propertyId, Locale locale) {
        getCaption(type, (String) propertyId?.toString(), locale)
    }

    static String getCaption(Class<?> type, String propertyId) {
        def locale = ApplicationContextUtils.locale
        getCaption(type, propertyId, locale)
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
