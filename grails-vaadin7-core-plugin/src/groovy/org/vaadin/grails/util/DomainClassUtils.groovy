package org.vaadin.grails.util

import grails.util.GrailsNameUtils
import grails.util.Holders
import org.springframework.context.NoSuchMessageException
import org.vaadin.grails.spring.ApplicationContextUtils

final class DomainClassUtils {

    static String getCaption(Class<?> type, String propertyId, Locale locale) {
        def typePropertyName = GrailsNameUtils.getPropertyNameRepresentation(type)
        def applicationContext = Holders.applicationContext
        def caption
        try {
            def code = "$typePropertyName.$propertyId"
            caption = applicationContext.getMessage(code, [] as Object[], locale ?: ApplicationContextUtils.locale)
        } catch (NoSuchMessageException e) {
            caption = GrailsNameUtils.getNaturalName(propertyId)
        }
        caption
    }

    private DomainClassUtils() { }
}
