package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.GrailsClass;

/**
 * Vaadin artefact class.
 *
 * @author Stephan Grundner
 */
public interface VaadinClass extends GrailsClass {

    public String getNamespace();
}
