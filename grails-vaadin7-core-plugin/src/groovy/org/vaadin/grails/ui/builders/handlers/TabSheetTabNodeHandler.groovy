package org.vaadin.grails.ui.builders.handlers

import com.vaadin.ui.TabSheet
import grails.util.GrailsNameUtils

/**
 * TabSheet.Tab node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class TabSheetTabNodeHandler extends AbstractNodeHandler {

    TabSheetTabNodeHandler(org.vaadin.grails.ui.builders.ComponentTree tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        def parent = node.parent
        parent?.name == "tabSheet" && node.name == "tab"
    }

    @Override
    void handle(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {

    }

    @Override
    void handleChildren(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        def tabSheet = node.parent.payload as TabSheet

        def children = node.children
        if (children.size() > 1) {
            throw new RuntimeException("Tab must not have more than one child")
        }

        def child = children?.first()
        def tab = tabSheet.addTab(child.payload)
        node.attributes.each { name, value ->
            def setterName = GrailsNameUtils.getSetterName(name)
            tab.invokeMethod(setterName, value)
        }
    }
}
