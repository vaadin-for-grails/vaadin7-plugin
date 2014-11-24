package com.vaadin.grails.server

import com.vaadin.grails.VaadinUIClass
import com.vaadin.grails.VaadinViewClass

/**
 * @author Stephan Grundner
 */
public interface Mapping {

    String getPath()
    VaadinUIClass getUIClass()
    String getTheme()
    String getWidgetset()
    String getPreservedOnRefresh()
    String getPageTitle()
    String getPushMode()
    String getPushTransport()

    Collection<String> getAllFragments()
    boolean containsFragment(String fragment)
    String getFragment(String view)

    VaadinViewClass getViewClass(String fragment)
}