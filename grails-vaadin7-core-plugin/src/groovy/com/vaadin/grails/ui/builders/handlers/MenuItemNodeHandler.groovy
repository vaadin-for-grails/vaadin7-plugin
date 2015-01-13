package com.vaadin.grails.ui.builders.handlers

import com.vaadin.grails.ui.builders.ComponentTree
import com.vaadin.ui.MenuBar

/**
 * Menu item node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class MenuItemNodeHandler extends AbstractNodeHandler {

    static final class CloseCommandAdapter implements MenuBar.Command {

        final Closure<Void> closure

        CloseCommandAdapter(Closure<Void> closure) {
            this.closure = closure
        }

        @Override
        void menuSelected(MenuBar.MenuItem menuItem) {
            closure.call(menuItem)
        }
    }

    MenuItemNodeHandler(ComponentTree tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(ComponentTree.TreeNode node) {
        node.name == "menuItem" || node.name == "separator"
    }

    @Override
    void handle(ComponentTree.TreeNode node) {

        def parentComponent = node.parent.payload
        MenuBar.MenuItem menuItem
        def caption = node.attributes?.remove("caption")
        def separator = node.name == "separator" || node.attributes?.remove("separator")

        if (parentComponent instanceof MenuBar) {
            menuItem = parentComponent.addItem(caption, null)
        } else if (parentComponent instanceof MenuBar.MenuItem) {
            if (separator) {
                menuItem = parentComponent.addSeparator()
            } else {
                menuItem = parentComponent.addItem(caption, null)
            }
        }
        node.payload = menuItem

        def command = node.attributes?.remove("command")
        if (command) {

            if (command instanceof Closure) {
                menuItem.command = new CloseCommandAdapter(command)
            } else {
                menuItem.command = command
            }

        }

        applyAttributes(node)
    }
}
