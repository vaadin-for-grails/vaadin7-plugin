package com.vaadin.grails.server

import com.vaadin.grails.VaadinUIClass
import com.vaadin.grails.VaadinViewClass
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingsHolder

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

    VaadinUIClass getUIClass(String path)
    String getPath(VaadinUIClass uiClass)
    Object getPathProperty(String path, String name)

    Collection<String> getAllPaths()

    VaadinViewClass getViewClass(String path, String fragment)
    String getFragment(String path, VaadinViewClass viewClass)
    Object getFragmentProperty(String path, String fragment, String name)

    Collection<String> getAllFragments(String path)
}
