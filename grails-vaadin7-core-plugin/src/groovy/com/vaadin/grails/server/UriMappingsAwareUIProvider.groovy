package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import com.vaadin.grails.navigator.UriMappingsAwareViewProvider
import com.vaadin.navigator.Navigator
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.util.UrlPathHelper

/**
 * An {@link com.vaadin.server.UIProvider} implementation that uses mappings
 * provided by a {@link UriMappingsHolder}.
 *
 * @author Stephan Grundner
 */
class UriMappingsAwareUIProvider extends com.vaadin.server.UIProvider {

    private static final def log = Logger.getLogger(UriMappingsAwareUIProvider)

    final def pathHelper = new UrlPathHelper()

    @Autowired
    UriMappingsHolder uriMappings

    UriMappingsAwareUIProvider() {

    }

    protected Navigator createNavigator(UICreateEvent event, UI ui) {
        def path = pathHelper.getPathWithinApplication(event.request)
        def navigator = new Navigator(ui, ui)
        navigator.addProvider(new UriMappingsAwareViewProvider(path))
        navigator
    }

    @Override
    UI createInstance(UICreateEvent event) {
        def utils = Vaadin.utils
        def uiClass = utils.getVaadinUIClass(event.getUIClass())
        UI ui = utils.instantiateVaadinComponentClass(uiClass)
        def path = pathHelper.getPathWithinApplication(event.request)
        def fragments = uriMappings.getAllFragments(path).size()
        if (fragments > 0) {
            ui.navigator = createNavigator(event, ui)
        }
        ui
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        def uiClass = uriMappings.getUIClass(path)
        log.debug("UI class [${uiClass?.fullName}] found for path [${path}]")
        log
        if (uiClass == null) {
//            TODO warn: No UI class found
        }
        uiClass?.clazz
    }

    @Override
    String getTheme(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappingsHolder.THEME_PATH_PROPERTY) ?:
                super.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappingsHolder.WIDGETSET_PATH_PROPERTY) ?:
                super.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        def result = uriMappings.getPathProperty(path, UriMappingsHolder.PRESERVED_ON_REFRESH_PATH_PROPERTY)
        if (result != null) {
            return result
        }
        super.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappingsHolder.PAGE_TITLE_PATH_PROPERTY) ?:
                super.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappingsHolder.PUSH_MODE_PATH_PROPERTY) as PushMode ?:
                super.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappingsHolder.PUSH_TRANSPORT_PATH_PROPERTY) as Transport ?:
                super.getPushTransport(event)
    }
}