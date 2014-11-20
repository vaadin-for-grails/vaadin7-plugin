package com.vaadin.grails

import com.vaadin.navigator.View
import com.vaadin.ui.UI

public interface MappingsProvider {

    static interface Mapping {

        String getPath()
        String getName()
        Class<?> getClazz()
    }

    static interface UIMapping {
        String getTheme()
        String getWidgetset()
        String getPreservedOnRefresh()
        String getPageTitle()
        String getPushMode()
        String getPushTransport()

        Class<? extends UI> getClazz()
    }

    static interface ViewMapping {

        Collection<Class<? extends UI>> getOwners()
        Class<? extends View> getClazz()
    }

    VaadinMappingsClass getMappingsClass()
    Mapping getMapping(String path)
    Map<String, UIMapping> getUIMappings()
    Map<String, ViewMapping> getViewMappings()

}