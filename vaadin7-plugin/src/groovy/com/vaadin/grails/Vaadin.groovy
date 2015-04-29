package com.vaadin.grails

import org.vaadin.grails.navigator.Navigation
import org.vaadin.grails.ui.builders.ComponentBuilder

final class Vaadin {

    @Deprecated
    static void enter(Map args) {
        Navigation.navigateTo(args)
    }

    @Deprecated
    static Object build(Closure<?> closure) {
        ComponentBuilder.build(closure)
    }

    private Vaadin() { }
}
