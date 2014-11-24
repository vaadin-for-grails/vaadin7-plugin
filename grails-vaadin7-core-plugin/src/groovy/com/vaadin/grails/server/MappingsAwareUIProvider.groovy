package com.vaadin.grails.server

import com.vaadin.grails.navigator.MappingsAwareViewProvider
import com.vaadin.navigator.Navigator
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProviderEvent
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import grails.util.Holders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.util.UrlPathHelper

/**
 * An {@link com.vaadin.server.UIProvider} implementation that uses mappings
 * defined in the <code>VaadinConfig</code> script.
 *
 * @author Stephan Grundner
 */
class MappingsAwareUIProvider extends com.vaadin.server.UIProvider {

    final def pathHelper = new UrlPathHelper()

    @Autowired
    MappingsProvider mappingsProvider

    MappingsAwareUIProvider() {

    }

    protected Mapping getMapping(UIProviderEvent event) {
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

        def mapping = getMapping(event)
        if (!mapping.allFragments.empty) {
            applyNavigator(ui, mapping)
        }

        ui
    }

    protected void applyNavigator(UI ui, Mapping mapping) {
        def navigator = new Navigator(ui, ui)
        navigator.addProvider(new MappingsAwareViewProvider(mapping))
        ui.navigator = navigator
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        println mappingsProvider.allMappings.collect {it.path}
        def mapping = getMapping(event)
        if (mapping == null) {
            throw new RuntimeException("No UI mapped for path [${event.request.pathInfo}]")
        }
        mapping.getUIClass().clazz
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
