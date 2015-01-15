package com.vaadin.grails.server

/**
 * Provides mappings between URIs such as /vaadin/book#!show/id=1 and Vaadin UIs or Views.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
interface UriMappingsHolder {

    static final DEFAULT_FRAGMENT = "defaultFragment"
    static final THEME_PATH_PROPERTY = "theme"
    static final WIDGETSET_PATH_PROPERTY = "widgetset"
    static final PRESERVED_ON_REFRESH_PATH_PROPERTY = "preservedOnRefresh"
    static final PAGE_TITLE_PATH_PROPERTY = "pageTitle"
    static final PUSH_MODE_PATH_PROPERTY = "pushMode"
    static final PUSH_TRANSPORT_PATH_PROPERTY = "pushTransport"

    Class<? extends com.vaadin.ui.UI> getUIClass(String path)
    Object getPathProperty(String path, String name)

    Collection<String> getAllPaths()

    Class<? extends com.vaadin.navigator.View> getViewClass(String path, String fragment)
    Object getFragmentProperty(String path, String fragment, String name)

    Collection<String> getAllFragments(String path)
}
