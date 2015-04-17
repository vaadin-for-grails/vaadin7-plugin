package org.vaadin.grails.ui.builders.handlers
/**
 * Accordion tab node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class AccordionTabNodeHandler extends TabSheetTabNodeHandler {

    AccordionTabNodeHandler(org.vaadin.grails.ui.builders.ComponentTree tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        def parent = node.parent
        parent?.name == "accordion" && node.name == "tab"
    }
}
