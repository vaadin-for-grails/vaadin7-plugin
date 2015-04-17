package com.vaadin.grails.ui.builders

/**
 * A helper class for creating a Vaadin component tree.
 *
 * @deprecated
 * @author Stephan Grundner
 * @since 1.0
 */
@Deprecated
class ComponentBuilder extends org.vaadin.grails.ui.builders.ComponentBuilder {

    ComponentBuilder(ComponentTree tree) {
        super(tree)
    }

    ComponentBuilder() {
        super()
    }

}
