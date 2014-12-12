package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * The Vaadin UI artefact handler.
 *
 * @author Stephan Grundner
 */
public class VaadinViewArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = VaadinViewClass.VIEW;

    public VaadinViewArtefactHandler() {
        super(TYPE, VaadinViewClass.class, DefaultVaadinViewClass.class, VaadinViewClass.VIEW);
    }

    @Override
    public boolean isArtefactClass(Class clazz) {
        return super.isArtefactClass(clazz);
    }
}
