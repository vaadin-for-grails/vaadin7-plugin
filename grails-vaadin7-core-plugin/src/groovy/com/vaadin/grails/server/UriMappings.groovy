package com.vaadin.grails.server

/**
 * @author Stephan Grundner
 */
public interface UriMappings extends UriMappingsHolder {

    void putUIClass(String path, Class<? extends com.vaadin.ui.UI> uiClass)
    Object putPathProperty(String path, String name, Object value)

    void putViewClass(String path, String fragment, Class<? extends com.vaadin.navigator.View> viewClass)
    Object putFragmentProperty(String path, String fragment, String name, Object value)

    void reload()
}