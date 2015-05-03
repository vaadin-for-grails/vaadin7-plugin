package org.vaadin.grails.data.fieldgroup

import com.vaadin.server.Page
import com.vaadin.server.UserError
import com.vaadin.ui.AbstractField
import com.vaadin.ui.Notification
import org.springframework.validation.Errors
import org.vaadin.grails.ui.util.ComponentUtils
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * @author Stephan Grundner
 *
 * @since 2.0
 */
class DomainFieldGroupValidator {

    protected boolean beforeValidate(DomainFieldGroup<?> fieldGroup) {
        fieldGroup.fields.each { field ->
            if (field instanceof AbstractField) {
                field.componentError = null
            }
        }
        true
    }

    protected void afterValidate(DomainFieldGroup<?> fieldGroup, Errors errors) {
        def applicationContext = ApplicationContextUtils.applicationContext
        def message = new StringBuilder()
        errors.globalErrors.each { globalError ->
            def errorMessage = ApplicationContextUtils.getMessage(globalError)
            message.append(errorMessage).append("\n")
        }
        errors.fieldErrors.each { fieldError ->
            def field = fieldGroup.getField(fieldError.field)
            if (field instanceof AbstractField) {
                def locale = field.locale ?: ApplicationContextUtils.locale
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

    boolean validate(DomainFieldGroup<?> fieldGroup) {
        def itemDataSource = fieldGroup.itemDataSource
        if (beforeValidate(fieldGroup)) {
            def valid = itemDataSource.validate()
            afterValidate(fieldGroup, itemDataSource.errors)
            return valid
        }
    }
}