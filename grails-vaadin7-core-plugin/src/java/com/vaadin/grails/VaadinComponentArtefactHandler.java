package com.vaadin.grails;

import com.vaadin.grails.ui.VaadinComponent;
import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * The Vaadin component artefact handler.
 *
 * @author Stephan Grundner
 */
public class VaadinComponentArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = VaadinComponentClass.COMPONENT;

    public VaadinComponentArtefactHandler() {
        super(TYPE, VaadinComponentClass.class, DefaultVaadinComponentClass.class, VaadinComponentClass.COMPONENT);
    }

    @Override
    public boolean isArtefactClass(Class clazz) {
        return !clazz.isAnnotationPresent(VaadinComponent.class)
                && super.isArtefactClass(clazz);
    }
}
