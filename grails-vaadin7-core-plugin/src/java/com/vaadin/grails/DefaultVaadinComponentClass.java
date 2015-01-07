package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

/**
 * Default implementation for {@link com.vaadin.grails.VaadinComponentClass}.
 *
 * @author Stephan Grundner
 */
public class DefaultVaadinComponentClass extends AbstractGrailsClass implements VaadinComponentClass {

    public DefaultVaadinComponentClass(Class<?> clazz) {
        super(clazz, VaadinComponentClass.COMPONENT);
    }

    @Override
    public String getNamespace() {
        return getStaticPropertyValue("namespace", String.class);
    }
}
