package com.vaadin.grails;

import com.vaadin.grails.server.UriMappingsHolder;
import grails.util.Holders;
import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

import java.util.Collection;

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

    private UriMappingsHolder getMappings() {
        return Holders.getApplicationContext().getBean(UriMappingsHolder.class);
    }

    @Override
    public String getNamespace() {
        return getStaticPropertyValue("namespace", String.class);
    }

    @Override
    public String getPath() {
        UriMappingsHolder mappings = getMappings();
        return mappings.getPath(this);
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