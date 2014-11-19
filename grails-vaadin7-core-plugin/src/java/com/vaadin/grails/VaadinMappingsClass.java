package com.vaadin.grails;

import groovy.lang.Closure;
import org.codehaus.groovy.grails.commons.GrailsClass;

/**
 * @author Stephan Grundner
 */
public interface VaadinMappingsClass extends GrailsClass {

    public Closure getMappingsClosure();
}
