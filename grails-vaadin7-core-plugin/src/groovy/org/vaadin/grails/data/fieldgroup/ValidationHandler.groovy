package org.vaadin.grails.data.fieldgroup

import org.springframework.validation.Errors

/**
 * @author Stephan Grundner
 */
public interface ValidationHandler {

    boolean beforeValidate(DomainFieldGroup<?> fieldGroup)
    void afterValidate(DomainFieldGroup<?> fieldGroup, Errors errors)
}