package org.vaadin.grails.server

import com.vaadin.navigator.Navigator
import com.vaadin.navigator.ViewProvider
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProvider
import com.vaadin.server.VaadinSession
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.HasComponents
import com.vaadin.ui.UI
import grails.util.Holders
import org.apache.log4j.Logger
import org.springframework.web.util.UrlPathHelper
import org.vaadin.grails.navigator.UriMappingsAwareViewProvider
import org.vaadin.grails.ui.util.ComponentUtils
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * An {@link com.vaadin.server.UIProvider} implementation that uses uri mappings.
 *
 * @author Stephan Grundner
 */
class UriMappingsAwareUIProvider extends UIProvider {

    private static final def log = Logger.getLogger(UriMappingsAwareUIProvider)

    final def pathHelper = new UrlPathHelper()

    UriMappings getUriMappings() {
        def applicationContext = Holders.applicationContext
        applicationContext.getBean(UriMappings)
    }

    protected ViewProvider createViewProvider() {
        new UriMappingsAwareViewProvider()
    }

    protected Navigator createNavigator(UI ui, ViewProvider viewProvider) {
        def navigator = new Navigator(ui, ui)
        navigator.addProvider(viewProvider)
        navigator
    }

    void initUI(UI ui) {
        def nullRepresentation = Holders.config.vaadin.nullRepresentation
        if (nullRepresentation != null) {
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
    }

    @Override
    UI createInstance(UICreateEvent event) {
        def uiClass = event.getUIClass()
        def ui = ApplicationContextUtils.getBeanOrInstance(uiClass)

        initUI(ui)

        def path = getPathHelper().getPathWithinApplication(event.request)
        def fragments = uriMappings.getAllFragments(path)
        if (fragments?.size() > 0) {
            def viewProvider = createViewProvider()
            ui.navigator = createNavigator(ui, viewProvider)
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
        log.debug("UI class [${uiClass?.name}] found for path [${path}]")
        uiClass
    }

    @Override
    String getTheme(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.THEME_PATH_PROPERTY) ?:
                super.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.WIDGETSET_PATH_PROPERTY) ?:
                super.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        def result = uriMappings.getPathProperty(path, UriMappings.PRESERVED_ON_REFRESH_PATH_PROPERTY)
        if (result != null) {
            return result
        }
        super.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.PAGE_TITLE_PATH_PROPERTY) ?:
                super.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.PUSH_MODE_PATH_PROPERTY) as PushMode ?:
                super.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        def path = pathHelper.getPathWithinApplication(event.request)
        uriMappings.getPathProperty(path, UriMappings.PUSH_TRANSPORT_PATH_PROPERTY) as Transport ?:
                super.getPushTransport(event)
    }
}
