package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

public class DefaultVaadinUIClass extends AbstractGrailsClass implements VaadinUIClass {

    public static final String UI = "UI";

    public DefaultVaadinUIClass(Class<?> clazz) {
        super(clazz, UI);
    }
}