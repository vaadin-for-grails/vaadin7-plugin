package org.vaadin.grails.server

import com.vaadin.ui.UI
import com.vaadin.util.CurrentInstance

/**
 * Holds the Id {@link UI#uiId} of the current UI.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
final class UIIdHolder {

    static Integer getCurrent() {
        Integer uiId = null
        def ui = UI.current
        if (ui != null) {
            uiId = ui.getUIId()
            if (uiId != null) {
                return uiId
            }
        }
        def uiIdHolder = CurrentInstance.get(UIIdHolder)
        if (uiIdHolder != null) {
            uiId = uiIdHolder.uiId
        }
        uiId
    }

    static void setCurrent(Integer uiId) {
        CurrentInstance.setInheritable(UIIdHolder, new UIIdHolder(uiId))
    }

    final Integer uiId

    UIIdHolder(Integer uiId) {
        this.uiId = uiId
    }
}
