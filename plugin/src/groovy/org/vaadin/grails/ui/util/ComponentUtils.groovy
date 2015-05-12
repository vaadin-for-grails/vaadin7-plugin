package org.vaadin.grails.ui.util

import com.vaadin.ui.AbstractTextField
import com.vaadin.ui.Component
import com.vaadin.ui.HasComponents
import com.vaadin.ui.UI

/**
 * Convenience methods for working with {@link Component}s.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
final class ComponentUtils {

    static void withEachChild(Component parent, Closure<?> closure) {
        if (parent instanceof HasComponents) {
            parent.each { child ->
                closure.call(child)
                withEachChild(child, closure)
            }
        }
    }

    static void withEachChild(UI ui, Closure<?> closure) {
        ui.windows.each { window ->
            withEachChild(window, closure)
        }
        withEachChild(ui, closure)
    }

    static setNullRepresentation(Component component, String nullRepresentation) {
        if (component instanceof AbstractTextField) {
            component.setNullRepresentation(nullRepresentation)
        }
        withEachChild(component) { Component child ->
            if (child instanceof AbstractTextField) {
                child.setNullRepresentation(nullRepresentation)
            }
        }
    }

    private ComponentUtils() { }
}
