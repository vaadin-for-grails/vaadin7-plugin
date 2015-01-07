package com.vaadin.grails;

import com.vaadin.grails.ui.VaadinUI;
import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * The Vaadin UI artefact handler.
 *
 * @author Stephan Grundner
 */
public class VaadinUIArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = VaadinUIClass.UI;

    public VaadinUIArtefactHandler() {
        super(TYPE, VaadinUIClass.class, DefaultVaadinUIClass.class, VaadinUIClass.UI);
    }

    @Override
    public boolean isArtefactClass(Class clazz) {
        return !clazz.isAnnotationPresent(VaadinUI.class)
                && super.isArtefactClass(clazz);
    }
}
