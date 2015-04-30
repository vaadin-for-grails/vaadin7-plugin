package org.vaadin.grails.server

import com.vaadin.server.ClientConnector
import com.vaadin.server.SessionDestroyEvent
import com.vaadin.server.SessionDestroyListener
import com.vaadin.server.VaadinSession
import com.vaadin.ui.UI
import org.eclipse.jetty.util.ConcurrentHashSet
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * Initialize UIs and Sessions once.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
abstract class LazyInitializer {

    static LazyInitializer getCurrent() {
//        Singleton is required!
        ApplicationContextUtils.getSingletonBean(LazyInitializer)
    }

    protected final Set<UI> uis = new ConcurrentHashSet<UI>()
    protected final Set<VaadinSession> sessions = new ConcurrentHashSet<VaadinSession>()

    public final void ensureInitialized(VaadinSession session) {
        if (!sessions.contains(session)) {
            try {
                initialize(session)
                if (sessions.add(session)) {
                    session.service.addSessionDestroyListener(new SessionDestroyListener() {
                        @Override
                        void sessionDestroy(SessionDestroyEvent event) {
                            sessions.remove(event.session)
                        }
                    })
                }
            } catch (e) {
                throw new RuntimeException(e)
            }
        }
    }

    public final void ensureInitialized(UI ui, VaadinSession session) {
        if (session) {
            ensureInitialized(session)
        }
        if (!uis.contains(ui)) {
            try {
                initialize(ui)
                if (uis.add(ui)) {
                    ui.addDetachListener(new ClientConnector.DetachListener() {
                        @Override
                        void detach(ClientConnector.DetachEvent event) {
                            uis.remove(event.connector.getUI())
                        }
                    })
                }
            } catch (e) {
                throw new RuntimeException(e)
            }
        }
    }

    public final void ensureInitialized(UI ui) {
        ensureInitialized(ui, ui.session)
    }

    protected abstract void initialize(VaadinSession session)
    protected abstract void initialize(UI ui)
}
