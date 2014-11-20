package com.vaadin.grails.server

import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProvider
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import grails.util.Holders

/**
 * Delegates to instances of the bean <code>uiProvider</code>.
 * <p>
 *     This class is used for applying an <code>UIProvider</code>
 *     when the <code>ApplicationContext</code> isn't available yet.
 * </p>
 *
 * @author Stephan Grundner
 */
final class DispatcherUIProvider extends UIProvider {

    private UIProvider delegate

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        if (delegate == null) {
            delegate = Holders.applicationContext.getBean("uiProvider")
        }
        return delegate.getUIClass(event)
    }

    @Override
    UI createInstance(UICreateEvent event) {
        delegate.createInstance(event)
    }

    @Override
    String getTheme(UICreateEvent event) {
        delegate.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        delegate.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        delegate.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        delegate.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        delegate.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        delegate.getPushTransport(event)
    }
}
