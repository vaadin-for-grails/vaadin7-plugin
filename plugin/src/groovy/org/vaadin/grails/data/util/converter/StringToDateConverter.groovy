package org.vaadin.grails.data.util.converter

import com.vaadin.data.util.converter.Converter
import grails.util.Holders
import groovy.transform.CompileStatic
import org.apache.commons.lang.StringUtils
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import org.vaadin.grails.util.ApplicationContextUtils

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Conversion between {@link Date} and {@link String} using
 * localized date formats.
 *
 * @author Stephan Grundner
 * @see {@link com.vaadin.data.util.converter.StringToDateConverter}
 * @since 2.0
 */
@CompileStatic
class StringToDateConverter implements Converter<String, Date> {

    protected final Converter<String, Date> fallbackConverter

    StringToDateConverter(Converter<String, Date> fallbackConverter) {
        this.fallbackConverter = fallbackConverter
    }

    StringToDateConverter() {
        fallbackConverter = new com.vaadin.data.util.converter.StringToDateConverter()
    }

    protected DateFormat getDateFormat(Locale locale) {
        if (locale == null) {
            locale = LocaleContextHolder.locale
        }

        def applicationContext = ApplicationContextUtils.applicationContext
        String dateFormatPattern = null

        try {
            dateFormatPattern = applicationContext.getMessage('date.format', [] as Object[], locale)
        } catch (NoSuchMessageException e) { }

        if (dateFormatPattern == null) {
            try {
                dateFormatPattern = applicationContext.getMessage('default.date.format', [] as Object[], locale)
            } catch (NoSuchMessageException e) { }
        }

        if (dateFormatPattern) {
            return new SimpleDateFormat(dateFormatPattern, locale)
        }

        null
    }

    @Override
    Date convertToModel(String value, Class<? extends Date> targetType, Locale locale) throws Converter.ConversionException {
        if (value == null) {
            return null
        }

        def dateFormat = getDateFormat(locale)
        if (dateFormat && StringUtils.isNotEmpty(value)) {
            return dateFormat.parse(value)
        }

        fallbackConverter.convertToModel(value, targetType, locale)
    }

    @Override
    String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
        def dateFormat = getDateFormat(locale)
        if (dateFormat && value) {
            return dateFormat.format(value)
        }

        fallbackConverter.convertToPresentation(value, targetType, locale)
    }

    @Override
    Class<Date> getModelType() {
        Date
    }

    @Override
    Class<String> getPresentationType() {
        String
    }
}
