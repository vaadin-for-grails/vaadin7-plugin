package org.vaadin.grails.ui.builders.handlers

import org.vaadin.grails.ui.builders.ComponentBuilder

/**
 * Accordion tab node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class AccordionTabNodeHandler extends TabSheetTabNodeHandler {

    AccordionTabNodeHandler(ComponentBuilder builder) {
        super(builder)
    }

    @Override
    boolean acceptNode(ComponentBuilder.BuilderNode node) {
        def parent = node.parent
        parent?.name == "accordion" && node.name == "tab"
    }
}
