package com.vaadin.grails.server

import com.vaadin.navigator.Navigator
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProviderEvent
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import grails.util.Holders

/**
 * An {@link com.vaadin.server.UIProvider} implementation that uses mappings
 * defined with {@link com.vaadin.grails.VaadinMappingsClass} artefacts.
 *
 * @author Stephan Grundner
 */
class MappingsAwareUIProvider extends com.vaadin.server.UIProvider {

    MappingsProvider mappingsProvider

    MappingsAwareUIProvider() {

    }

    protected MappingsProvider.UIMapping getMapping(UIProviderEvent event) {
        String path = event.request.pathInfo ?: "/"
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

        applyNavigator(ui)

        ui
    }

    protected void applyNavigator(UI ui) {
        def viewMappings = mappingsProvider.allMappings.findAll { it instanceof MappingsProvider.ViewMapping }
        def viewsFound = viewMappings.find { MappingsProvider.ViewMapping viewMapping ->
            viewMapping.owners.contains(ui.getClass())
        }
        if (viewsFound) {
            def navigator = new Navigator(ui, ui)

            def viewProvider = Holders.applicationContext
                    .getBean("viewProvider")

            navigator.addProvider(viewProvider)
            ui.navigator = navigator
        }
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        def mapping = getMapping(event)

        mapping.clazz
    }

    @Override
    String getTheme(UICreateEvent event) {
        getMapping(event)?.theme ?: super.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        getMapping(event)?.widgetset ?: super.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        getMapping(event)?.preservedOnRefresh ?: super.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        getMapping(event)?.pageTitle ?: super.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        getMapping(event)?.pushMode ?: super.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        getMapping(event)?.pushTransport ?: super.getPushTransport(event)
    }
}
