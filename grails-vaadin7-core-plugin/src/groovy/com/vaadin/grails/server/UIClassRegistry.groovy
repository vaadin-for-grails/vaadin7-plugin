package com.vaadin.grails.server

import com.vaadin.ui.UI
import org.springframework.stereotype.Component

import java.util.concurrent.ConcurrentHashMap

@Component("uiClassRegistry")
class UIClassRegistry {

    final def uiClasses = new ConcurrentHashMap()

    void setUIClass(String path, Class<? extends UI> uiClass) {
        uiClasses.put(path, uiClass)
    }

    Class<? extends UI> getUIClass(String path) {
        uiClasses.get(path)
    }
}
