package com.vaadin.grails;

import com.vaadin.grails.navigator.VaadinView;
import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

public class AnnotatedVaadinViewClass extends AbstractGrailsClass implements VaadinViewClass {

    public AnnotatedVaadinViewClass(Class<?> clazz) {
        super(clazz, VIEW);
    }

    private VaadinView getAnnotation() {
        return getClazz().getAnnotation(VaadinView.class);
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
