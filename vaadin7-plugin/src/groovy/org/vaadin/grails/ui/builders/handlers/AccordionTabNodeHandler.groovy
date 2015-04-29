package org.vaadin.grails.ui.builders.handlers

import org.vaadin.grails.ui.builders.ComponentTreeHandler

/**
 * Accordion tab node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class AccordionTabNodeHandler extends TabSheetTabNodeHandler {

    AccordionTabNodeHandler(ComponentTreeHandler tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(ComponentTreeHandler.TreeNode node) {
        def parent = node.parent
        parent?.name == "accordion" && node.name == "tab"
    }
}
