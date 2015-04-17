package org.vaadin.grails.ui.builders.handlers

import com.vaadin.ui.Component
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.SingleComponentContainer
import grails.util.GrailsNameUtils
import grails.util.Holders
import org.apache.log4j.Logger

/**
 * Component node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class ComponentNodeHandler extends org.vaadin.grails.ui.builders.handlers.AbstractNodeHandler implements org.vaadin.grails.ui.builders.ComponentTree.TreeNodeHandler {

    private static final def log = Logger.getLogger(ComponentNodeHandler)

    ComponentNodeHandler(org.vaadin.grails.ui.builders.ComponentTree tree) {
        super(tree)
    }

    Class<? extends Component> getComponentClass(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
//        TODO Cache classes
        def classLoader = Holders.grailsApplication.classLoader
        try {
            return classLoader.loadClass("${node.prefix}.${GrailsNameUtils.getClassName(node.name)}")
        } catch (e) {}
        null
    }

    @Override
    boolean acceptNode(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        node.name == "component" ||
                (getComponentClass(node) != null)
    }

    @Override
    void handle(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        def component = null
        if (node.name == "component") {
            component = node.attributes?.remove('instance')
        } else {
            try {
                def componentClass = Class.forName("${node.prefix}.${GrailsNameUtils.getClassName(node.name)}")
                log.debug("Class [${componentClass.name}] found for [${node.prefix}:${node.name}]")
                component = componentClass.newInstance()
            } catch (ClassNotFoundException e) {}

            if (component == null) {
                throw new RuntimeException("No class found for name [${node.name}] and prefix [${node.prefix}]")
            }
        }
        node.payload = component
        attach(node)
        applyAttributes(node)
    }

    protected void attach(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node) {
        if (node.payload) {
            def parent = node.parent
            def parentComponent = parent?.payload
            if (parentComponent instanceof SingleComponentContainer) {
                parentComponent.content = node.payload
            } else if (parentComponent instanceof ComponentContainer) {
                parentComponent.addComponent(node.payload)
            } else {
                log.warn("Unable to apply parent [${parent?.prefix}:${parent?.name}] for child [${node.prefix}:${node.name}]")
            }
        }
    }

    private void applyComponentAlignment(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node, Object value) {
        def parent = node.parent
        def parentComponent = parent.payload
        def component = node.payload
        parentComponent.invokeMethod("setComponentAlignment", [component, value])
    }

    private void applyExpandRatio(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node, Object value) {
        def parent = node.parent
        def parentComponent = parent.payload
        def component = node.payload
        parentComponent.invokeMethod("setExpandRatio", [component, value])
    }

    @Override
    protected void applyAttribute(org.vaadin.grails.ui.builders.ComponentTree.TreeNode node, String name, Object value) {
        switch (name) {
            case "componentAlignment":
                applyComponentAlignment(node, value)
                break
            case "expandRatio":
                applyExpandRatio(node, value)
                break
            default:
                super.applyAttribute(node, name, value)
        }
    }
}
