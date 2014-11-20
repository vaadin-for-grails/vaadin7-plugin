package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

/**
 * Default implementation for {@link com.vaadin.grails.VaadinUIClass}.
 *
 * @author Stephan Grundner
 */
public class DefaultVaadinUIClass extends AbstractGrailsClass implements VaadinUIClass {

    public static final String UI = "UI";

    public DefaultVaadinUIClass(Class<?> clazz) {
        super(clazz, UI);
    }
}