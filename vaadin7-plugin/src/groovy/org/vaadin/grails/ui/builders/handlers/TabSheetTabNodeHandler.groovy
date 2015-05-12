package org.vaadin.grails.ui.builders.handlers

import com.vaadin.ui.TabSheet
import grails.util.GrailsNameUtils
import org.vaadin.grails.ui.builders.ComponentBuilder

/**
 * TabSheet.Tab node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class TabSheetTabNodeHandler extends AbstractNodeHandler {

    TabSheetTabNodeHandler(ComponentBuilder builder) {
        super(builder)
    }

    @Override
    boolean acceptNode(ComponentBuilder.BuilderNode node) {
        def parent = node.parent
        parent?.name == "tabSheet" && node.name == "tab"
    }

    @Override
    void handle(ComponentBuilder.BuilderNode node) { }

    @Override
    void handleChildren(ComponentBuilder.BuilderNode node) {
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
