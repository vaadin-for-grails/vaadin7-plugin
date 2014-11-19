package com.vaadin.grails

import com.vaadin.navigator.View
import com.vaadin.ui.UI

public interface MappingsProvider {

    static interface Mapping {

        String getPath()
        String getTheme()
        String getWidgetset()
        String getPreservedOnRefresh()
        String getPageTitle()
        String getPushMode()
        String getPushTransport()

        String getUIName()
        Class<? extends UI> getUIClass()

        String getViewName()
        Class<? extends View> getViewClass()
    }

    VaadinMappingsClass getMappingsClass()
    Mapping getMapping(String path)
    Mapping getMapping(Class<? extends UI> uiClass)
    Map<String, Mapping> getUIMappings()
    Map<String, Mapping> getViewMappings()

}