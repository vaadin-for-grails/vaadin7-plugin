package com.vaadin.grails.server

import com.vaadin.grails.VaadinUIClass
import com.vaadin.grails.VaadinViewClass

/**
 * Provides mappings between URIs such as /vaadin/book#!show/id=1 and Vaadin UIs or Views.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
interface MappingsProvider {

    VaadinUIClass getUIClass(String path)
    String getPath(VaadinUIClass uiClass)

    String getTheme(String path)
    String getWidgetset(String path)
    boolean isPreservedOnRefresh(String path)
    String getPageTitle(String path)
    String getPushMode(String path)
    String getPushTransport(String path)

    VaadinViewClass getViewClass(String path, String fragment)
    String getFragment(String path, VaadinViewClass viewClass)

    Collection<String> getAllFragments(String path)
}
