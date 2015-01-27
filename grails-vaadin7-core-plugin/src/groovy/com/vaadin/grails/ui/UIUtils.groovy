package com.vaadin.grails.ui

import com.vaadin.grails.server.UIAttributesHolder
import com.vaadin.grails.ui.builders.ComponentBuilder
import com.vaadin.ui.UI

import java.util.concurrent.Future

/**
 * Utilities for UI related operations.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
final class UIUtils {

    static Object build(Closure<?> closure) {
        (new ComponentBuilder()).call(closure)
    }

    /**
     * @see {@link UI#access(java.lang.Runnable)}
     */
    static Future<Void> access(Closure<Void> closure) {
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
    static void accessSynchronously(Closure<Void> closure) {
        UI.current.accessSynchronously(new Runnable() {
            @Override
            void run() {
                closure?.call()
            }
        })
    }

    /**
     * Retrieve a stored value associated with the current UI.
     *
     * @param name The name of the attribute
     * @return The value of the attribute
     */
    static Object getAttribute(String name) {
        UIAttributesHolder.instance.getAttribute(name)
    }

    /**
     * Retrieve a stored value associated with the current UI.
     *
     * @param type The type of the attribute
     * @return The value of the attribute
     */
    static Object getAttribute(Class type) {
        UIAttributesHolder.instance.getAttribute(type)
    }

    /**
     * Store a value associated with the current UI.
     *
     * @param name The name of the attribute
     * @param value The value of the attribute
     */
    static void setAttribute(String name, Object value) {
        UIAttributesHolder.instance.setAttribute(name, value)
    }

    /**
     * Store a value associated with the current UI.
     *
     * @param type The type of the attribute
     * @param value The value of the attribute
     */
    static void setAttribute(Class type, Object value) {
        UIAttributesHolder.instance.setAttribute(type, value)
    }

    private UIUtils() { }
}
