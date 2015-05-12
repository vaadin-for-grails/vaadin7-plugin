package org.vaadin.grails.server

import com.vaadin.server.UICreateEvent
import com.vaadin.util.CurrentInstance

final class UIIDHolder {

    static UIIDHolder getCurrent() {

    }

    static void setCurrent(UIIDHolder uiIdentifier) {
        CurrentInstance.set()
    }

    final Integer uiId

    UIIDHolder(UICreateEvent event) {
        uiId = event.uiId
    }
}
