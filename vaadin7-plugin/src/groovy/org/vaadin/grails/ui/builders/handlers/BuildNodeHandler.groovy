package org.vaadin.grails.ui.builders.handlers

import com.vaadin.data.fieldgroup.FieldGroup
import grails.util.GrailsNameUtils
import org.vaadin.grails.ui.builders.ComponentTreeHandler

/**
 * Build node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class BuildNodeHandler extends ComponentNodeHandler {

    BuildNodeHandler(ComponentTreeHandler tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(ComponentTreeHandler.TreeNode node) {
        node.name == "build"
    }

    @Override
    void handle(ComponentTreeHandler.TreeNode node) {
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
