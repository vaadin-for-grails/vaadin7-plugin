package org.vaadin.grails.data.fieldgroup.util

import com.vaadin.data.Validator.InvalidValueException
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.fieldgroup.FieldGroup.CommitException
import com.vaadin.ui.Field
import com.vaadin.ui.Notification
import org.apache.commons.lang.StringUtils
import org.springframework.web.util.HtmlUtils

/**
 * Convenience methods for working with {@link FieldGroup}s.
 *
 * @author Stephan Grundner
 * @since 2.2
 */
class FieldGroupUtils {

    static void buildErrorMessage(StringBuilder messageBuilder, Field<?> field,  InvalidValueException exception) {
        def message = exception.htmlMessage
        def visible = !exception.invisible
        if (visible && StringUtils.isNotEmpty(message)) {
            message = HtmlUtils.htmlUnescape(message)
            messageBuilder.append(message).append('\n')
        }
        exception.causes?.each { cause ->
            buildErrorMessage(messageBuilder, field, cause)
        }
    }

    static boolean commit(FieldGroup fieldGroup) {
        try {
            fieldGroup.commit()
        } catch (CommitException commitException) {
            def messageBuilder = new StringBuilder()
            commitException.invalidFields.each { field, invalidValueException ->
                buildErrorMessage(messageBuilder, field, invalidValueException)
            }
            def message = messageBuilder.toString()
            if (StringUtils.isNotEmpty(message)) {
                Notification.show(null, message, Notification.Type.ERROR_MESSAGE)
            }
            return false
        }
        true
    }
}
