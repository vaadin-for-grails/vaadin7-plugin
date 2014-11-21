package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * The Vaadin UI artefact handler.
 *
 * @author Stephan Grundner
 */
public class VaadinViewArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "View";

    public VaadinViewArtefactHandler() {
        super(TYPE, VaadinUIClass.class, DefaultVaadinViewClass.class, DefaultVaadinViewClass.VIEW);
    }

    @Override
    public boolean isArtefactClass(Class clazz) {
        return super.isArtefactClass(clazz);
    }
}
