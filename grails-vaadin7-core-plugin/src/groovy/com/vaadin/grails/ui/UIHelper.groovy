package com.vaadin.grails.ui

import com.vaadin.grails.server.UIAttributesHolder
import com.vaadin.grails.ui.builders.ComponentBuilder
import com.vaadin.ui.UI

import java.util.concurrent.Future

/**
 * Helper for ease access to UI related operations.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class UIHelper {

    Object build(Closure<?> closure) {
//        TODO Keep a single instance in the UI scope!
        (new ComponentBuilder()).call(closure)
    }

    /**
     * @see {@link UI#access(java.lang.Runnable)}
     */
    Future<Void> access(Closure<Void> closure) {
        UI.current.access(new Runnable() {
            @Override
            void run() {
                closure?.call()
            }
        })
    }

    /**
     * @see {@link UI#accessSynchronously(java.lang.Runnable)}
     */
    void accessSynchronously(Closure<Void> closure) {
        UI.current.accessSynchronously(new Runnable() {
            @Override
            void run() {
                closure?.call()
            }
        })
    }

    private final getAttributesHolder() {
        UIAttributesHolder.getInstance()
    }

    /**
     * Retrieve a stored value associated with the current UI.
     *
     * @param name The name of the attribute
     * @return The value of the attribute
     */
    Object getAttribute(String name) {
        attributesHolder.getAttribute(name)
    }

    /**
     * Retrieve a stored value associated with the current UI.
     *
     * @param type The type of the attribute
     * @return The value of the attribute
     */
    Object getAttribute(Class type) {
        attributesHolder.getAttribute(type)
    }

    /**
     * Store a value associated with the current UI.
     *
     * @param name The name of the attribute
     * @param value The value of the attribute
     */
    void setAttribute(String name, Object value) {
        attributesHolder.setAttribute(name, value)
    }

    /**
     * Store a value associated with the current UI.
     *
     * @param type The type of the attribute
     * @param value The value of the attribute
     */
    void setAttribute(Class type, Object value) {
        attributesHolder.setAttribute(type, value)
    }

}
