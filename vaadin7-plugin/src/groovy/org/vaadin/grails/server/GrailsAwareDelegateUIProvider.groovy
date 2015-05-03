package org.vaadin.grails.server

import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProvider
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * Delegate UI provider.
 *
 * @author Stephan Grundner
 */
final class GrailsAwareDelegateUIProvider extends UIProvider {

    private final UIProvider provider

    GrailsAwareDelegateUIProvider() {
        provider = ApplicationContextUtils
                .getBeanOrInstance(UIProvider, UriMappingsAwareUIProvider)
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        provider.getUIClass(event)
    }

    @Override
    UI createInstance(UICreateEvent event) {
        provider.createInstance(event)
    }

    @Override
    String getTheme(UICreateEvent event) {
        provider.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        provider.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        provider.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        provider.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        provider.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        provider.getPushTransport(event)
    }
}
