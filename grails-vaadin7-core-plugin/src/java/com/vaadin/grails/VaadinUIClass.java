package com.vaadin.grails;

import java.util.Collection;

/**
 * A Vaadin UI artefact class.
 *
 * @author Stephan Grundner
 */
public interface VaadinUIClass extends VaadinComponentClass {

    public static final String UI = "UI";

    public String getPath();
    public Object getPathProperty(String name);
    public Collection<String> getAllFragments();
    public VaadinViewClass getViewClass(String fragment);
}
