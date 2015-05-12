package org.vaadin.grails.ui.builders.handlers

import com.vaadin.ui.Tree
import org.vaadin.grails.ui.builders.ComponentBuilder

/**
 * Tree item node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class TreeItemNodeHandler extends AbstractNodeHandler {

    TreeItemNodeHandler(ComponentBuilder builder) {
        super(builder)
    }

    Tree getTree(ComponentBuilder.BuilderNode node) {
        def current = node
        while (true) {

            if (current.payload instanceof Tree) {
                break
            }
            current = current.parent
        }
        current?.payload
    }

    @Override
    boolean acceptNode(ComponentBuilder.BuilderNode node) {
        def parent = node.parent
        (parent?.name == "tree" || parent?.name == "treeItem") &&
                node.name == "treeItem"
    }

    @Override
    void handle(ComponentBuilder.BuilderNode node) {
        node.payload = node.attributes.get("itemId")
        applyAttributes(node)
    }

    @Override
    void handleChildren(ComponentBuilder.BuilderNode node) {
        def tree = getTree(node)
        def parent = node.parent
        if (parent) {
            def itemId = parent.payload
            def expand = parent.attributes?.get("expand")
            if (expand) {
                if (!tree.expandItem(itemId)) {
                    tree.expandItemsRecursively(itemId)
                }
            }
        }
    }

    @Override
    protected void applyAttribute(ComponentBuilder.BuilderNode node, String name, Object value) {
        throw new UnsupportedOperationException()
    }

    @Override
    protected void applyAttributes(ComponentBuilder.BuilderNode node) {
        def tree = getTree(node)
        def itemId = node.payload
        tree.addItem(itemId)
        tree.setChildrenAllowed(itemId, false)
        def parentId = node.parent?.payload
        tree.setChildrenAllowed(parentId, true)
        tree.setParent(itemId, parentId)
        def caption = node.attributes?.get("caption")
        tree.setItemCaption(itemId, caption)
        def icon = node.attributes?.get("icon")
        if (icon) {
            tree.setItemIcon(itemId, icon)
            def iconAlternateText = node.attributes?.get("iconAlternateText")
            if (iconAlternateText) {
                tree.setItemIconAlternateText(itemId, iconAlternateText)
            }
        }
    }
}
