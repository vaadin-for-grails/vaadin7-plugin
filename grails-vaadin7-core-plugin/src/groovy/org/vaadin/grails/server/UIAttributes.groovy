package org.vaadin.grails.server

import com.vaadin.server.*
import com.vaadin.ui.UI

/**
 * @author Stephan Grundner
 */
class UIAttributes implements ClientConnector.DetachListener {

    static UIAttributes getCurrent() {
        def session = VaadinSession.current
        def uiAttributes = session.getAttribute(UIAttributes)
        if (uiAttributes == null) {
            uiAttributes = new UIAttributes()
            session.setAttribute(UIAttributes, uiAttributes)
            VaadinService.current.addSessionDestroyListener(new SessionDestroyListener() {
                @Override
                void sessionDestroy(SessionDestroyEvent event) {
                    uiAttributes.attributesMapByUI.clear()
                    event.session.setAttribute(UIAttributes, null)
                }
            })
        }
        uiAttributes
    }

    protected final Map<UI, Map<String, Object>> attributesMapByUI = new IdentityHashMap()

    protected Map<String, Object> createAttributesMap() {
        new HashMap<String, Object>()
    }

    protected Map<String, Object> getAttributes(UI ui) {
        def attributes = attributesMapByUI.get(ui)
        if (attributes == null) {
            attributes = createAttributesMap()
            attributesMapByUI.put(ui, attributes)
        }
        attributes
    }

    Object getAttribute(UI ui, String name) {
        getAttributes(ui).get(name)
    }

    void setAttribute(UI ui, String name, Object value) {
        def attributes = getAttributes(ui)
        if (value) {
            ui.addDetachListener(this)
            attributes.put(name, value)
        } else {
            attributes.remove(name)
        }
    }

    @Override
    void detach(ClientConnector.DetachEvent event) {
        def ui = event.connector.getUI()
        if (attributesMapByUI.containsKey(ui)) {
            attributesMapByUI.remove(ui)
        }
    }
}
