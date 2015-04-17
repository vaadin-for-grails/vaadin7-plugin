package org.vaadin.grails.ui.builders.handlers

import com.vaadin.data.fieldgroup.FieldGroup
import grails.util.GrailsNameUtils

/**
 * Build node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class BuildNodeHandler extends ComponentNodeHandler {

    BuildNodeHandler(org.vaadin.grails.ui.builders.ComponentTree tree) {
        super(tree)
    }

    @Override
    boolean acceptNode(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        node.name == "build"
    }

    FieldGroup resolveFieldGroup(Object fieldGroupOrKey) {
        FieldGroup fieldGroup
        if (fieldGroupOrKey instanceof String) {

            String key = fieldGroupOrKey
            fieldGroup = tree.getFieldGroup(key)

            if (fieldGroup == null) {
                throw new RuntimeException("No field group found for key [${key}]")
            }
        } else if (fieldGroupOrKey instanceof FieldGroup) {
            fieldGroup = fieldGroupOrKey
        } else if (fieldGroupOrKey == null) {
            fieldGroup = tree.defaultFieldGroup

            if (fieldGroup == null) {
                throw new RuntimeException("No field group specified")
            }
        }
        fieldGroup
    }

    @Override
    void handle(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        def caption = node.attributes.remove("caption")
        def propertyId = node.attributes.remove("propertyId")
        def fieldType = node.attributes.remove("fieldType")
        def fieldGroup = resolveFieldGroup(node.attributes.remove("fieldGroup"))

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
