package org.vaadin.grails.ui.builders.handlers

import org.vaadin.grails.ui.builders.ComponentBuilder
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * I18n node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class I18nNodeHandler extends AbstractNodeHandler {

    I18nNodeHandler(ComponentBuilder builder) {
        super(builder)
    }

    @Override
    boolean acceptNode(ComponentBuilder.BuilderNode node) {
        node.name == "i18n"
    }

    @Override
    void handle(ComponentBuilder.BuilderNode node) {
        if (node.value) {
            node.payload = ApplicationContextUtils.getMessage(node.value)
        } else {
            def attributes = node.attributes
            def key = attributes.get("key")
            if (key == null) {
                throw new RuntimeException("Attribute [key] is required for [${node.name}]")
            }
            def args = attributes.get("args")
            def defaultValue = attributes.get("defaultValue")
            def locale = attributes.get("locale")
            node.payload = ApplicationContextUtils.getMessage(key, args, defaultValue, locale)
        }
    }
}
