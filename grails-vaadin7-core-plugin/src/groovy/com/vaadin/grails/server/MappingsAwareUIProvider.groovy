package com.vaadin.grails.server

import com.vaadin.grails.navigator.MappingsAwareViewProvider
import com.vaadin.navigator.Navigator
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import grails.util.Holders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.util.UrlPathHelper

/**
 * An {@link com.vaadin.server.UIProvider} implementation that uses mappings
 * provided by a {@link MappingsProvider}.
 *
 * @author Stephan Grundner
 */
class MappingsAwareUIProvider extends com.vaadin.server.UIProvider {

    final def pathHelper = new UrlPathHelper()

    @Autowired
    MappingsProvider mappingsProvider

    MappingsAwareUIProvider() {

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

        def path = pathHelper.getPathWithinApplication(event.request)
        if (mappingsProvider.getAllFragments(path).size() > 0) {
            ui.navigator = createNavigator(event, ui)
        }

        ui
    }

    protected Navigator createNavigator(UICreateEvent event, UI ui) {
        def path = pathHelper.getPathWithinApplication(event.request)
        def navigator = new Navigator(ui, ui)
        navigator.addProvider(new MappingsAwareViewProvider(path))
        navigator
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        def uiClass = mappingsProvider.getUIClass(path)
        if (uiClass) {
        }
        uiClass?.clazz
    }

    @Override
    String getTheme(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        mappingsProvider.getTheme(path) ?: super.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        mappingsProvider.getWidgetset(path) ?: super.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        mappingsProvider.isPreservedOnRefresh(path) ?: super.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        mappingsProvider.getPageTitle(path) ?: super.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        mappingsProvider.getPushMode(path) ?: super.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        mappingsProvider.getPushTransport(path) ?: super.getPushTransport(event)
    }
}
