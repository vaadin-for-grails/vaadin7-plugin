package org.vaadin.grails.server

import com.vaadin.navigator.Navigator
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProvider
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.HasComponents
import com.vaadin.ui.UI
import grails.util.Holders
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.springframework.web.util.UrlPathHelper
import org.vaadin.grails.navigator.GrailsAwareViewProvider
import org.vaadin.grails.ui.util.ComponentUtils
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * Grails specific implementation of {@link UIProvider}.
 *
 * @author Stephan Grundner
 */
class GrailsAwareUIProvider extends UIProvider {

    private static final def log = Logger.getLogger(GrailsAwareUIProvider)

    final def pathHelper = new UrlPathHelper()

    UriMappings getUriMappings() {
        UriMappingUtils.uriMappings
    }

    protected Navigator createNavigator(UI ui) {
        def navigator = new Navigator(ui, ui)
        navigator.addProvider(new GrailsAwareViewProvider())
        navigator
    }

    @Override
    UI createInstance(UICreateEvent event) {
        UIIdHolder.setCurrent(event.uiId)

        def uiClass = event.getUIClass()
        def ui = ApplicationContextUtils.getBeanOrInstance(uiClass)

        def path = getPathHelper().getPathWithinApplication(event.request)
        def fragments = uriMappings.getAllFragments(path)
        if (fragments?.size() > 0) {
            ui.navigator = createNavigator(ui)
        }

        def config = Holders.config.vaadin as ConfigObject
        if (config.isSet('nullRepresentation')) {
            def nullRepresentation = config.get('nullRepresentation')
            ui.addComponentAttachListener(new HasComponents.ComponentAttachListener() {
                @Override
                void componentAttachedToContainer(HasComponents.ComponentAttachEvent e) {
                    def component = e.attachedComponent
                    if (component instanceof HasComponents.ComponentAttachDetachNotifier) {
                        component.addComponentAttachListener(this)
                    }
                    ComponentUtils.setNullRepresentation(component, nullRepresentation.toString())
                }
            })
        }

        ui
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        def uiClass = uriMappings.getUIClass(path)
        if (uiClass == null) {
            log.warn("No UI class found for path [${path}]")
            return null
        }
        uiClass
    }

    protected ConfigObject getConfig() {
        Holders.config.vaadin
    }

    @Override
    String getTheme(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.THEME_PATH_PROPERTY) ?:
                config.theme ?: super.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.WIDGETSET_PATH_PROPERTY) ?:
                config.widgetset ?: super.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        def result = uriMappings.getPathProperty(path, UriMappings.PRESERVED_ON_REFRESH_PATH_PROPERTY)
        if (result != null) {
            return result
        }
        result = config.getProperty(UriMappings.PRESERVED_ON_REFRESH_PATH_PROPERTY)
        if (Boolean.isAssignableFrom(result.getClass())) {
            return result
        }
        super.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.PAGE_TITLE_PATH_PROPERTY) ?:
                config.pageTitle ?: super.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.PUSH_MODE_PATH_PROPERTY) as PushMode ?:
                config.pushMode ?: super.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.PUSH_TRANSPORT_PATH_PROPERTY) as Transport ?:
                config.pushTransport ?: super.getPushTransport(event)
    }
}
