package org.vaadin.grails.ui.builders.handlers

import com.vaadin.ui.Tree

/**
 * Tree item node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class TreeItemNodeHandler extends AbstractNodeHandler {

    TreeItemNodeHandler(org.vaadin.grails.ui.builders.ComponentTree tree) {
        super(tree)
    }

    Tree getTree(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
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
    boolean acceptNode(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        def parent = node.parent
        (parent?.name == "tree" || parent?.name == "treeItem") &&
                node.name == "treeItem"
    }

    @Override
    void handle(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        node.payload = node.attributes.get("itemId")
        applyAttributes(node)
    }

    @Override
    void handleChildren(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
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
    protected void applyAttribute(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node, String name, Object value) {
        throw new UnsupportedOperationException()
    }

    @Override
    protected void applyAttributes(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
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
