package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import com.vaadin.grails.VaadinUIClass
import com.vaadin.grails.VaadinViewClass

/**
 * @author Stephan Grundner
 */
class DefaultMapping implements Mapping {

    String path
    VaadinUIClass uiClass
    String theme
    String widgetset
    String preservedOnRefresh
    String pageTitle
    String pushMode
    String pushTransport

    Map<String, VaadinViewClass> viewClassByFragment = new HashMap()
    Map<VaadinViewClass, String> fragmentByViewClass = new HashMap()

    @Override
    VaadinUIClass getUIClass() {
        uiClass
    }

    void setUIClass(VaadinUIClass uiClass) {
        this.uiClass = uiClass
    }

    @Override
    Collection<String> getAllFragments() {
        viewClassByFragment.keySet()
    }

    @Override
    boolean containsFragment(String fragment) {
        viewClassByFragment.containsKey(fragment)
    }

    @Override
    String getFragment(String view) {
        def clazz = Vaadin.utils.getVaadinViewClass(view, uiClass.namespace)
        fragmentByViewClass.get(clazz)
    }

    @Override
    VaadinViewClass getViewClass(String fragment) {
        viewClassByFragment.get(fragment)
    }

    void addViewClass(String fragment, VaadinViewClass viewClass) {
        viewClassByFragment.put(fragment, viewClass)
        fragmentByViewClass.put(viewClass, fragment)
    }
}
