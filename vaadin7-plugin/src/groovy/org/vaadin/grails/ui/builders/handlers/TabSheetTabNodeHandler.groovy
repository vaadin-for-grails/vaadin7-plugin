package org.vaadin.grails.ui.builders.handlers

import com.vaadin.ui.TabSheet
import grails.util.GrailsNameUtils
import org.vaadin.grails.ui.builders.ComponentTreeHandler

/**
 * TabSheet.Tab node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class TabSheetTabNodeHandler extends AbstractNodeHandler {

    TabSheetTabNodeHandler(ComponentTreeHandler tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(ComponentTreeHandler.TreeNode node) {
        def parent = node.parent
        parent?.name == "tabSheet" && node.name == "tab"
    }

    @Override
    void handle(ComponentTreeHandler.TreeNode node) {

    }

    @Override
    void handleChildren(ComponentTreeHandler.TreeNode node) {
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
