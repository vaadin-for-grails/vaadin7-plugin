package com.vaadin.grails;

import com.vaadin.grails.server.UriMappingsHolder;
import com.vaadin.grails.ui.VaadinUI;
import grails.util.Holders;
import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

import java.util.Collection;

class AnnotatedVaadinUIClass extends AbstractGrailsClass implements VaadinUIClass {

    public AnnotatedVaadinUIClass(Class<?> clazz) {
        super(clazz, UI);
    }

    private UriMappingsHolder getMappings() {
        return Holders.getApplicationContext().getBean(UriMappingsHolder.class);
    }

    private VaadinUI getAnnotation() {
        return getClazz().getAnnotation(VaadinUI.class);
    }

    @Override
    public String getNamespace() {
        String namespace = getAnnotation().namespace();
        if (namespace != null && namespace.length() == 0) {
            return null;
        }
        return namespace;
    }

    @Override
    public String getPath() {
        UriMappingsHolder mappings = getMappings();
        return mappings.getPath(this);
    }

    @Override
    public Object getPathProperty(String name) {
        UriMappingsHolder mappings = getMappings();
        return mappings.getPathProperty(getPath(), name);
    }

    @Override
    public Collection<String> getAllFragments() {
        UriMappingsHolder mappings = getMappings();
        return mappings.getAllFragments(getPath());
    }

    @Override
    public VaadinViewClass getViewClass(String fragment) {
        UriMappingsHolder mappings = getMappings();
        return mappings.getViewClass(getPath(), fragment);
    }
}
