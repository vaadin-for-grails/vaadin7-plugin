package com.vaadin.grails.ui.builders.handlers

import com.vaadin.grails.ui.builders.ComponentTree

/**
 * Accordion tab node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class AccordionTabNodeHandler extends TabSheetTabNodeHandler {

    AccordionTabNodeHandler(ComponentTree tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(ComponentTree.TreeNode node) {
        def parent = node.parent
        parent?.name == "accordion" && node.name == "tab"
    }
}
