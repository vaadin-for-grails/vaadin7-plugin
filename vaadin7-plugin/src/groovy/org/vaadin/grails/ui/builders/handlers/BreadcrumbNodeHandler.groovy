package org.vaadin.grails.ui.builders.handlers

import org.vaadin.grails.ui.BreadcrumbTrail
import org.vaadin.grails.ui.builders.ComponentTreeHandler

/**
 * Breadcrumb node handler.
 *
 * @author Stephan Grundner
 * @since 2.0
 */
class BreadcrumbNodeHandler extends AbstractNodeHandler {

    BreadcrumbNodeHandler(ComponentTreeHandler treeHandler) {
        super(treeHandler)
    }

    @Override
    boolean acceptNode(ComponentTreeHandler.TreeNode node) {
        node.name == "breadcrumb"
    }

    @Override
    void handle(ComponentTreeHandler.TreeNode node) {
        def parentComponent = node.parent.payload

        if (parentComponent instanceof BreadcrumbTrail) {
            BreadcrumbTrail.Breadcrumb breadcrumb = node.value
            if (breadcrumb == null) {
                new BreadcrumbTrail.Breadcrumb()
                breadcrumb.icon = node.attributes?.remove("icon")
                breadcrumb.caption = node.attributes?.remove("caption")
                breadcrumb.description = node.attributes?.remove("description")
                breadcrumb.resource = node.attributes?.remove("resource")
            }
            node.payload = breadcrumb
            parentComponent.addBreadcrumb(breadcrumb)
        }
    }
}
