package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * The Vaadin mappings artefact handler.
 *
 * @author Stephan Grundner
 */
public class VaadinMappingsArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "VaadinMappings";

    public VaadinMappingsArtefactHandler() {
        super(TYPE, VaadinMappingsClass.class, DefaultVaadinMappingClass.class, DefaultVaadinMappingClass.VAADIN_MAPPINGS);
    }
}
