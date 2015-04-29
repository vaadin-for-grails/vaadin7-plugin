package org.vaadin.grails.ui.util

import com.vaadin.server.VaadinSession
import com.vaadin.ui.AbstractTextField
import com.vaadin.ui.Component
import com.vaadin.ui.HasComponents
import com.vaadin.ui.UI
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Convenience methods for {@link Component}s.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
final class ComponentUtils {

    static Locale getLocale(Component component) {
        def locale = component.locale
        if (locale == null) {
            def ui = component.getUI() ?: UI.current
            locale = ui?.locale
            if (locale == null) {
                locale = VaadinSession.current.locale ?: LocaleContextHolder.locale
            }
        }
        locale
    }

    static void withEachComponent(Component parent, Closure<?> closure) {
        closure.call(parent)
        if (parent instanceof HasComponents) {
            parent.each { child ->
                closure.call(child)
                withEachComponent(child, closure)
            }
        }
    }

    static void withEachComponent(UI ui, Closure<?> closure) {
        ui.windows.each { window ->
            withEachComponent(window, closure)
        }
        withEachComponent(ui, closure)
    }

    static setNullRepresentation(Component parent, String nullRepresentation) {
        withEachComponent(parent) { Component child ->
            if (child instanceof AbstractTextField) {
                child.setNullRepresentation(nullRepresentation)
            }
        }
    }

    private ComponentUtils() { }
}
