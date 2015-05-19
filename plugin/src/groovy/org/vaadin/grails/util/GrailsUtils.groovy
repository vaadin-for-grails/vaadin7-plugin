package org.vaadin.grails.util

import grails.util.GrailsNameUtils
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsHibernateUtil
import org.codehaus.groovy.grails.validation.ConstrainedProperty
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Convenience methods for working with Grails specific API.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
final class GrailsUtils {

    static boolean isDomainClass(String name) {
        def grailsApplication = Holders.grailsApplication
        def classLoader = grailsApplication.classLoader
        def type = classLoader.loadClass(name)
        grailsApplication.isDomainClass(type)
    }

    static boolean isDomainClass(Class<?> type) {
        def grailsApplication = Holders.grailsApplication
        grailsApplication.isDomainClass(type)
    }

    static GrailsDomainClass getDomainClass(String name) {
        def grailsApplication = Holders.grailsApplication
        grailsApplication.getDomainClass(name) as GrailsDomainClass
    }

    static GrailsDomainClass getDomainClass(Object object) {
        def grailsApplication = Holders.grailsApplication
        object = GrailsHibernateUtil.unwrapIfProxy(object)
        grailsApplication.getDomainClass(object?.getClass()?.name) as GrailsDomainClass
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

    static ConstrainedProperty getConstrainedProperty(Class<?> type, String propertyName) {
        def domainClass = getDomainClass(type)
        def constraints = domainClass.getConstrainedProperties()
        constraints?.get(propertyName)
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

    private GrailsUtils() { }
}
