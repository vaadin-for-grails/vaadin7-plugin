package com.vaadin.grails.server

/**
 * @author Stephan Grundner
 */
public interface UriMappings extends UriMappingsHolder {

    void addUIClass(String path, Class<? extends com.vaadin.ui.UI> uiClass)
    Object setPathProperty(String path, String name, Object value)

    void addViewClass(String path, String fragment, Class<? extends com.vaadin.navigator.View> viewClass)
    Object setFragmentProperty(String path, String fragment, String name, Object value)

    void reload()
}