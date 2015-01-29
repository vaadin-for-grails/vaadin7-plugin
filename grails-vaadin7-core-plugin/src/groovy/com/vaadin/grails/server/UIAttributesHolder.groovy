package com.vaadin.grails.server

import com.vaadin.server.ClientConnector
import com.vaadin.server.VaadinSession
import com.vaadin.ui.UI
import org.apache.log4j.Logger

/**
 * Store and retrieve attributes by name,
 * associated with UIs.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class UIAttributesHolder implements ClientConnector.DetachListener {

    static UIAttributesHolder getInstance() {
        def session = VaadinSession.current
        def instance = session.getAttribute(UIAttributesHolder)
        if (instance == null) {
            instance = new UIAttributesHolder()
            session.setAttribute(UIAttributesHolder, instance)
        }
        instance
    }

    private static final log = Logger.getLogger(UIAttributesHolder)

    protected static class UIAttributes implements Map<String, Object> {

        @Delegate
        final Map<String, Object> delegate

        UIAttributes(Map<String, Object> delegate) {
            this.delegate = delegate
        }

        UIAttributes() {
            this(new HashMap<String, Object>())
        }
    }

    protected final Map<UI, UIAttributes> attributesByUI

    protected UIAttributesHolder() {
        attributesByUI = createAttributesByUIMap()
    }

    protected Map<UI, UIAttributes> createAttributesByUIMap() {
        new IdentityHashMap<String, UIAttributes>()
    }

    protected UIAttributes createAttributesMap() {
        new UIAttributes()
    }

    /**
     * Retrieve a stored value associated with the specified UI.
     *
     * @param name The name of the attribute
     * @return The value of the attribute
     */
    Object getAttribute(UI ui, String name) {
        def attributes = attributesByUI.get(ui, createAttributesMap())
        attributes.get(name)
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
        log.debug("set attribute [$value] with name [$name] on UI [$ui]")
        ui.addDetachListener(this)
        def attributes = attributesByUI.get(ui, createAttributesMap())
        attributes.put(name, value)
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

    @Override
    void detach(ClientConnector.DetachEvent event) {
        def ui = event.connector.getUI()
        if (attributesByUI.containsKey(ui)) {
            attributesByUI.remove(ui)
            log.debug("removed attributes for UI [$ui]")
        }
    }
}
