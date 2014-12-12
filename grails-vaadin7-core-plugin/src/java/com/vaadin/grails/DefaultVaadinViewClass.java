package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

/**
 * Default implementation for {@link VaadinViewClass}.
 *
 * @author Stephan Grundner
 */
public class DefaultVaadinViewClass extends AbstractGrailsClass implements VaadinViewClass {

    public DefaultVaadinViewClass(Class<?> clazz) {
        super(clazz, VIEW);
    }

    @Override
    public String getNamespace() {
        return getStaticPropertyValue("namespace", String.class);
    }
}