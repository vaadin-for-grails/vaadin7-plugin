package org.vaadin.grails.ui.builders.handlers

import com.vaadin.data.fieldgroup.FieldGroup
import grails.util.GrailsNameUtils
import org.vaadin.grails.ui.builders.ComponentBuilder

/**
 * Build node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class BuildNodeHandler extends ComponentNodeHandler {

    BuildNodeHandler(ComponentBuilder builder) {
        super(builder)
    }

    @Override
    boolean acceptNode(ComponentBuilder.BuilderNode node) {
        node.name == "build"
    }

    @Override
    void handle(ComponentBuilder.BuilderNode node) {
        def caption = node.attributes.remove("caption")
        def propertyId = node.attributes.remove("propertyId")
        def fieldType = node.attributes.remove("fieldType")
        def fieldGroup = node.attributes.remove("fieldGroup") as FieldGroup

        if (fieldType) {
            if (caption == null) {
                caption = GrailsNameUtils.getNaturalName(propertyId)
            }
            node.payload = fieldGroup.buildAndBind(caption, propertyId, fieldType)
        } else {
            if (caption) {
                node.payload = fieldGroup.buildAndBind(caption, propertyId)
            } else {
                node.payload = fieldGroup.buildAndBind(propertyId)
            }
        }

        attach(node)
        applyAttributes(node)
    }
}
