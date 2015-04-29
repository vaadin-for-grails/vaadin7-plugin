package org.vaadin.grails.ui.builders.handlers

import com.vaadin.ui.Component
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.SingleComponentContainer
import grails.util.GrailsNameUtils
import grails.util.Holders
import groovy.transform.Memoized
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.vaadin.grails.ui.builders.ComponentTreeHandler

/**
 * Component node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class ComponentNodeHandler extends AbstractNodeHandler implements ComponentTreeHandler.TreeNodeHandler {

    private static final def log = Logger.getLogger(ComponentNodeHandler)

    ComponentNodeHandler(ComponentTreeHandler tree) {
        super(tree)
    }

    @Memoized
    Class<? extends Component> getComponentClass(String prefix, String componentName) {
        def classLoader = Holders.grailsApplication.classLoader
        def componentClassName = GrailsNameUtils.getClassName(componentName)
        Class<? extends Component> componentClass = null
        if (StringUtils.isEmpty(prefix)) {
            prefix = 'com.vaadin.ui'
            try {
                componentClass = classLoader.loadClass("$prefix.$componentClassName")
            } catch (e) {}

            if (componentClass == null) {
                prefix = 'org.vaadin.grails.ui'
                try {
                    componentClass = classLoader.loadClass("$prefix.$componentClassName")
                } catch (e) {}

                if (componentClass == null) {
//                    class with no package!?
                    componentClass = classLoader.loadClass(componentClassName)
                }
            }
        } else {
            try {
                componentClass = classLoader.loadClass("$prefix.$componentClassName")
            } catch (e) {}
        }
        componentClass
    }

    Class<? extends Component> getComponentClass(ComponentTreeHandler.TreeNode node) {
        getComponentClass(node.prefix, node.name)
    }

    @Override
    boolean acceptNode(ComponentTreeHandler.TreeNode node) {
        node.name == "component" || (getComponentClass(node) != null)
    }

    @Override
    void handle(ComponentTreeHandler.TreeNode node) {
        def component
        if (node.name == "component") {
            component = node.value ?: node.attributes?.remove('instance')
        } else {
            def componentClass = getComponentClass(node)
            if (componentClass == null) {
                throw new RuntimeException("No class found for name [${node.name}] and prefix [${node.prefix}]")
            }
            component = componentClass.newInstance()
        }
        node.payload = component
        attach(node)
        applyAttributes(node)
    }

    protected void attach(ComponentTreeHandler.TreeNode node) {
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

    private void applyComponentAlignment(ComponentTreeHandler.TreeNode node, Object value) {
        def parent = node.parent
        def parentComponent = parent.payload
        def component = node.payload
        parentComponent.invokeMethod("setComponentAlignment", [component, value])
    }

    private void applyExpandRatio(ComponentTreeHandler.TreeNode node, Object value) {
        def parent = node.parent
        def parentComponent = parent.payload
        def component = node.payload
        parentComponent.invokeMethod("setExpandRatio", [component, value])
    }

    @Override
    protected void applyAttribute(ComponentTreeHandler.TreeNode node, String name, Object value) {
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
