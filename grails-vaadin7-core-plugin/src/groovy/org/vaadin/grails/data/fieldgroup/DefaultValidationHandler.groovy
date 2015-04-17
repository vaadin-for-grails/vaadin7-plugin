package org.vaadin.grails.data.fieldgroup

import com.vaadin.server.Page
import com.vaadin.server.UserError
import com.vaadin.ui.AbstractField
import com.vaadin.ui.Notification
import org.springframework.validation.Errors
import org.vaadin.grails.spring.ApplicationContextUtils
import org.vaadin.grails.ui.ComponentUtils

class DefaultValidationHandler implements ValidationHandler {

    @Override
    boolean beforeValidate(DomainFieldGroup<?> fieldGroup) {
        fieldGroup.fields.each { field ->
            if (field instanceof AbstractField) {
                field.componentError = null
            }
        }
        true
    }

    @Override
    void afterValidate(DomainFieldGroup<?> fieldGroup, Errors errors) {
        def applicationContext = ApplicationContextUtils.applicationContext
        def message = new StringBuilder()
        errors.globalErrors.each { globalError ->
            def errorMessage = ApplicationContextUtils.getMessage(globalError)
            message.append(errorMessage).append("\n")
        }
        errors.fieldErrors.each { fieldError ->
            def field = fieldGroup.getField(fieldError.field)
            if (field instanceof AbstractField) {
                def locale = ComponentUtils.getLocale(field)
                def errorMessage = applicationContext.getMessage(fieldError, locale)
                field.componentError = new UserError(errorMessage)
            } else {
                def errorMessage = ApplicationContextUtils.getMessage(fieldError)
                message.append(errorMessage).append("\n")
            }
        }
        if (message.length() > 0) {
            def n = new Notification(null, Notification.Type.ERROR_MESSAGE)
            n.htmlContentAllowed = false
            n.description = message.toString()
            n.show(Page.current)
        }
    }
}
