package com.vaadin.grails;

import com.vaadin.grails.ui.VaadinComponent;
import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

public class AnnotatedVaadinComponentClass extends AbstractGrailsClass implements VaadinComponentClass {

    public AnnotatedVaadinComponentClass(Class<?> clazz) {
        super(clazz, COMPONENT);
    }

    private VaadinComponent getAnnotation() {
        return getClazz().getAnnotation(VaadinComponent.class);
    }

    @Override
    public String getNamespace() {
        String namespace = getAnnotation().namespace();
        if (namespace != null && namespace.length() == 0) {
            return null;
        }
        return namespace;
    }
}
