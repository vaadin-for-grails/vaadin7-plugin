package com.vaadin.grails.server

import com.vaadin.server.VaadinSession
import com.vaadin.ui.UI

import java.util.concurrent.ConcurrentHashMap

/**
 * Store and retrieve attributes by name,
 * associated with UIs.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class UIAttributesHolder {

    static UIAttributesHolder getInstance() {
        def session = VaadinSession.current
        def instance = session.getAttribute(UIAttributesHolder)
        if (instance == null) {
            instance = new UIAttributesHolder()
            session.setAttribute(UIAttributesHolder, instance)
        }
        instance
    }

    protected final Map<String, Object> attributes

    protected UIAttributesHolder() {
        attributes = createAttributes()
    }

    protected Map<String, Object> createAttributes() {
        new ConcurrentHashMap<String, Object>()
    }

    /**
     * Retrieve a stored value associated with the specified UI.
     *
     * @param name The name of the attribute
     * @return The value of the attribute
     */
    Object getAttribute(UI ui, String name) {
        def id = ui.getUIId()
        attributes.get("$id#$name")
    }

    Object getAttribute(UI ui, Class type) {
        getAttribute(ui, type.name)
    }

    /**
     * Retrieve a stored value associated with the current UI.
     *
     * @param name The name of the attribute
     * @return The value of the attribute
     */
    Object getAttribute(String name) {
        getAttribute(UI.current, name)
    }

    Object getAttribute(Class type) {
        getAttribute(type.name)
    }

    /**
     * Store a value associated with the specified UI.
     *
     * @param name The name of the attribute
     * @param value The value of the attribute
     */
    void setAttribute(UI ui, String name, Object value) {
        def id = ui.getUIId()
        attributes.put("$id#$name", value)
    }

    void setAttribute(UI ui, Class type, Object value) {
        setAttribute(ui, type.name, value)
    }

    /**
     * Store a value associated with the current UI.
     *
     * @param name The name of the attribute
     * @param value The value of the attribute
     */
    void setAttribute(String name, Object value) {
        setAttribute(UI.current, name, value)
    }

    void setAttribute(Class type, Object value) {
        setAttribute(type.name, value)
    }
}
