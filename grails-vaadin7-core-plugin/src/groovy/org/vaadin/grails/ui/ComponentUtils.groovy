package org.vaadin.grails.ui

import com.vaadin.server.VaadinSession
import com.vaadin.ui.AbstractTextField
import com.vaadin.ui.Component
import com.vaadin.ui.HasComponents
import com.vaadin.ui.UI
import org.springframework.context.i18n.LocaleContextHolder

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

    static setNullRepresentation(Component component, String nullRepresentation) {
        if (component instanceof AbstractTextField) {
            component.setNullRepresentation(nullRepresentation)
        }
        withEachComponent(component) { Component child ->
            setNullRepresentation(child, nullRepresentation)
        }
    }

    static setNullRepresentation(String nullRepresentation) {
        setNullRepresentation(UI.current, nullRepresentation)
    }

    static void withEachComponent(UI ui, Closure<?> closure) {
        ui.windows.each { window ->
            withEachComponent(window, closure)
        }
        withEachComponent(ui, closure)
    }

    static void withEachComponent(Component parent, Closure<?> closure) {
        if (parent instanceof HasComponents) {
            parent.each { child ->
                closure.call(child)
                withEachComponent(child, closure)
            }
        }
    }

    private ComponentUtils() { }
}
