package com.vaadin.grails.server

import com.vaadin.grails.VaadinUIClass
import com.vaadin.grails.VaadinViewClass

/**
 * @author Stephan Grundner
 */
public interface UriMappings extends UriMappingsHolder {

    void addUIClass(String path, VaadinUIClass uiClass)
    Object setPathProperty(String path, String name, Object value)

    void addViewClass(String path, String fragment, VaadinViewClass viewClass)
    Object setFragmentProperty(String path, String fragment, String name, Object value)
}