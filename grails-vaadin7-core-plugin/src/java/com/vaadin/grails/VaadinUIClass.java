package com.vaadin.grails;

import java.util.Collection;

/**
 * A Vaadin UI artefact class.
 *
 * @author Stephan Grundner
 */
public interface VaadinUIClass extends VaadinComponentClass {

    public String getPath();
    public Object getPathProperty(String name);
    public Collection<String> getAllFragments();
    public VaadinViewClass getViewClass(String fragment);
}
