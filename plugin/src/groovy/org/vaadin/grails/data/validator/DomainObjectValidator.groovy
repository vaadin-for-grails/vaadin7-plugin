package org.vaadin.grails.data.validator

import com.vaadin.data.Validator
import groovy.transform.CompileStatic
import org.springframework.validation.BeanPropertyBindingResult
import org.vaadin.grails.data.util.DomainObjectProvider
import org.vaadin.grails.util.ApplicationContextUtils
import org.vaadin.grails.util.GrailsUtils

/**
 * Vaadin {@link Validator} for validating Grails domain objects.
 *
 * @author Stephan Grundner
 * @since 2.2
 */
@CompileStatic
class DomainObjectValidator<T> implements Validator {

    final DomainObjectProvider<T> objectProvider
    final String propertyName

    Locale locale

    DomainObjectValidator(DomainObjectProvider<T> objectProvider, Object propertyName) {
        this.objectProvider = objectProvider
        this.propertyName = propertyName
    }

    @Override
    void validate(Object value) throws Validator.InvalidValueException {
        def object = objectProvider.object
        def type = object.getClass()

        def constrainedProperty = GrailsUtils.getConstrainedProperty(type, propertyName)
        def errors = new BeanPropertyBindingResult(object, object.getClass().name)

        constrainedProperty.validate(object, value, errors)

        if (errors.hasFieldErrors(propertyName)) {
            def exceptions = []
            errors.getFieldErrors(propertyName)?.each { error ->
                def locale = locale ?: ApplicationContextUtils.locale
                def applicationContext = ApplicationContextUtils.applicationContext
                def message = applicationContext.getMessage(error, locale)
                exceptions.add new Validator.InvalidValueException(message)
            }
            throw new Validator.InvalidValueException(null, exceptions as Validator.InvalidValueException[])
        }
    }
}
