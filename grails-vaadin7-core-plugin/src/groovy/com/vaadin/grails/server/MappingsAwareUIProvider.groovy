package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import com.vaadin.grails.navigator.MappingsAwareViewProvider
import com.vaadin.grails.server.MappingsProvider.Mapping
import com.vaadin.navigator.Navigator
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProviderEvent
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import grails.util.Holders
import org.springframework.web.util.UrlPathHelper

/**
 * An {@link com.vaadin.server.UIProvider} implementation that uses mappings
 * defined with {@link com.vaadin.grails.VaadinMappingsClass} artefacts.
 *
 * @author Stephan Grundner
 */
class MappingsAwareUIProvider extends com.vaadin.server.UIProvider {

    final def pathHelper = new UrlPathHelper()

    MappingsProvider mappingsProvider

    MappingsAwareUIProvider() {

    }

    protected MappingsProvider.Mapping getMapping(UIProviderEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
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

        applyNavigator(ui, getMapping(event))

        ui
    }

    protected void applyNavigator(UI ui, Mapping mapping) {
        if (!mapping.viewMappings.isEmpty()) {
            def navigator = new Navigator(ui, ui)
            navigator.addProvider(new MappingsAwareViewProvider(mapping))
            ui.navigator = navigator
        }
    }

    protected Class<? extends UI> resolveUIClass(String name, String namespace = null) {
        def found = Vaadin.vaadinUtils.getVaadinUIClass(name, namespace)
        if (found == null) {
            def message = "Unable to resolve Vaadin UI for name [${name}]"
            if (namespace) {
                message += " and namespace [${namespace}]"
            }
            throw new RuntimeException(message)
        }
        found?.clazz
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        def mapping = getMapping(event)

        if (mapping == null) {
            throw new RuntimeException("No UI mapped for path [${event.request.pathInfo}]")
        }

        resolveUIClass(mapping.getUI(), mapping.getNamespace())
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
