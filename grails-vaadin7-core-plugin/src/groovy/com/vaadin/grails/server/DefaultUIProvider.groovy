package com.vaadin.grails.server

import com.vaadin.grails.MappingsProvider
import com.vaadin.grails.VaadinUIClass
import com.vaadin.navigator.Navigator
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProviderEvent
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import grails.util.Holders
import org.springframework.web.util.UrlPathHelper

class DefaultUIProvider extends com.vaadin.server.UIProvider {

    private final def urlPathHelper = new UrlPathHelper()

    MappingsProvider mappingsProvider

    DefaultUIProvider() {

    }

    protected MappingsProvider.Mapping getMapping(UIProviderEvent event) {
        String path = urlPathHelper.getPathWithinApplication(event.request)
        mappingsProvider.getMapping(path)
    }

    @Override
    UI createInstance(UICreateEvent event) {
        def applicationContext = Holders.applicationContext
        def uiClass = event.getUIClass()

        UI ui

        def beanNames = applicationContext.getBeanNamesForType(uiClass)
        if (beanNames?.size() == 1) {
            ui = applicationContext.getBean(beanNames.first())
        } else {
//            TODO warn: More than one bean found for class
            ui = super.createInstance(event)
        }

        ui
    }

//    protected void assignViews(UI ui) {
//        ui.navigator = new Navigator(ui, ui)
//        def viewMappings = mappingsProvider.viewMappings
//        viewMappings.each { entry ->
//            def path = entry.key.substring(1)
////            TODO assign to specific UIs
//            def viewClass = entry.value["view"]
//            ui.navigator.addView(path, viewClass)
//        }
//    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        println "available mappings: ${mappingsProvider.getUIMappings()}"
        def mapping = getMapping(event)

        mapping.getUIClass()
    }

    @Override
    String getTheme(UICreateEvent event) {
        getMappingValue(event, "theme") ?: super.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        getMappingValue(event, "widgetset") ?: super.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        getMappingValue(event, "preservedOnRefresh") ?:  super.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        getMappingValue(event, "pageTitle") ?:  super.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        getMappingValue(event, "pushMode") ?:  super.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        getMappingValue(event, "pushTransport") ?:  super.getPushTransport(event)
    }
}
