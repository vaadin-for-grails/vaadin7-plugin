package org.vaadin.grails.data.util.converter

import com.vaadin.data.util.converter.Converter
import org.apache.commons.lang.BooleanUtils
import org.apache.commons.lang.StringUtils
import org.springframework.util.NumberUtils
import org.vaadin.grails.util.GrailsUtils

/**
 * Conversion between Grails Domain Class and {@link String}.
 *
 * @author Stephan Grundner
 * @since 2.2
 */
class StringToDomainObjectConverter<T> implements Converter<String, T> {

    protected final Class<?> modelType

    StringToDomainObjectConverter(Class<?> modelType) {
        this.modelType = modelType
    }

    @Override
    T convertToModel(String value, Class<? extends T> targetType, Locale locale) throws Converter.ConversionException {
        if (StringUtils.isEmpty(value)) {
            return null
        }

        def domainClass = GrailsUtils.getDomainClass(targetType)
        def idProperty = domainClass.getIdentifier()
        def idPropertyType = idProperty.type
        def id

        if (Number.isAssignableFrom(idPropertyType)) {
            try {
                id = NumberUtils.parseNumber(value, idPropertyType)
            } catch (NumberFormatException e) {
                throw new Converter.ConversionException(e)
            }
        } else if (Boolean.isAssignableFrom(idPropertyType)) {
            id = BooleanUtils.toBoolean(value)
        } else if (String.isAssignableFrom(idPropertyType)) {
            id = value
        } else {
            throw new Converter.ConversionException("Unable to convert id of type ${idPropertyType} to type ${idPropertyType}")
        }

        def model = targetType.invokeMethod("get", idPropertyType.cast(id))
        if (model == null) {
            throw new Converter.ConversionException("Unable to convert id ${id} to model of type ${targetType}")
        }

        model
    }

    @Override
    String convertToPresentation(T value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
        if (value != null && value.getClass().isAssignableFrom(modelType)) {
            return value.invokeMethod("getId", null)?.toString()
        }

        null
    }

    @Override
    Class<T> getModelType() {
        modelType
    }

    @Override
    Class<String> getPresentationType() {
        String
    }
}
