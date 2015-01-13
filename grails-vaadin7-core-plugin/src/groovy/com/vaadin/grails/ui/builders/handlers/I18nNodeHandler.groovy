package com.vaadin.grails.ui.builders.handlers

import com.vaadin.grails.*
import com.vaadin.grails.ui.builders.ComponentTree

/**
 * I18n node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class I18nNodeHandler extends AbstractNodeHandler {

    I18nNodeHandler(ComponentTree tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(ComponentTree.TreeNode node) {
        node.name == "i18n"
    }

    @Override
    void handle(ComponentTree.TreeNode node) {
        if (node.value) {
            node.payload = Vaadin.i18n(node.value)
        } else {
            def attributes = node.attributes
            def key = attributes.get("key")
            if (key == null) {
                throw new RuntimeException("Attribute [key] is required for [i18n]")
            }
            def args = attributes.get("args")
            def defaultValue = attributes.get("defaultValue")
            def locale = attributes.get("locale")
            node.payload = Vaadin.i18n(key, args, defaultValue, locale)
        }
    }
}
