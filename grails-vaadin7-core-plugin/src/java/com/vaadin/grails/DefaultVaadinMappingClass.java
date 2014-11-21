package com.vaadin.grails;

import groovy.lang.Closure;
import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

/**
 * Default implementation for {@link com.vaadin.grails.VaadinMappingsClass}.
 *
 * @author Stephan Grundner
 */
public class DefaultVaadinMappingClass extends AbstractGrailsClass implements VaadinMappingsClass {

    public static final String VAADIN_MAPPINGS = "VaadinMappings";
    private static final String MAPPINGS_CLOSURE = "mappings";

    public DefaultVaadinMappingClass(Class<?> clazz) {
        super(clazz, VAADIN_MAPPINGS);
    }

    @Override
    public Closure getMappingsClosure() {
        Closure mappingsClosure = getStaticPropertyValue(MAPPINGS_CLOSURE, Closure.class);
        if (mappingsClosure == null) {
            throw new RuntimeException("No mappings closure found for class " + getClazz().getName());
        }
        return mappingsClosure;
    }
}
