package org.vaadin.grails.data.util.converter

import com.vaadin.data.util.converter.Converter

/**
 * A factory for automatically conversions.
 *
 * @see {@link com.vaadin.server.VaadinSession#setConverterFactory(com.vaadin.data.util.converter.ConverterFactory)}
 * @author Stephan Grundner
 * @since 2.0
 */
class DefaultConverterFactory extends com.vaadin.data.util.converter.DefaultConverterFactory {

    @Override
    protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
        if (presentationType == String && modelType == Date) {
            return new StringToDateConverter()
        }
        return super.findConverter(presentationType, modelType)
    }
}
