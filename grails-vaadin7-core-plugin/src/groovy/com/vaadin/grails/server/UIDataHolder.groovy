package com.vaadin.grails.server

import com.vaadin.ui.UI

/**
 * @author Stephan Grundner
 */
public interface UIDataHolder {

    Object getData(UI ui, String key)
    void setData(UI ui, String key, Object data)
}