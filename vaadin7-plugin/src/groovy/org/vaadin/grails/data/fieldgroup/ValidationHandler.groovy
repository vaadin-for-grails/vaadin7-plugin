package org.vaadin.grails.data.fieldgroup

import org.springframework.validation.Errors

/**
 * @author Stephan Grundner
 *
 * @since 2.0
 */
public interface ValidationHandler {

    boolean beforeValidate(DomainFieldGroup<?> fieldGroup)
    void afterValidate(DomainFieldGroup<?> fieldGroup, Errors errors)
}