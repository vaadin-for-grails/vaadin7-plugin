package com.vaadin.grails.ui

import com.vaadin.grails.Vaadin

import java.util.concurrent.Future

/**
 * @deprecated As of release 1.0, replaced by {@link UIHelper}
 */
@Deprecated
final class UIUtils {

    @Deprecated
    static Object build(Closure<?> closure) {
        Vaadin.getUIHelper().build(closure)
    }

    @Deprecated
    static Future<Void> access(Closure<Void> closure) {
        Vaadin.getUIHelper().access(closure)
    }

    @Deprecated
    static void accessSynchronously(Closure<Void> closure) {
        Vaadin.getUIHelper().accessSynchronously(closure)
    }

    @Deprecated
    static Object getAttribute(String name) {
        Vaadin.getUIHelper().getAttribute(name)
    }

    @Deprecated
    static Object getAttribute(Class type) {
        Vaadin.getUIHelper().getAttribute(type)
    }

    @Deprecated
    static void setAttribute(String name, Object value) {
        Vaadin.getUIHelper().setAttribute(name, value)
    }

    @Deprecated
    static void setAttribute(Class type, Object value) {
        Vaadin.getUIHelper().setAttribute(type, value)
    }

    private UIUtils() { }
}
