package org.vaadin.grails.ui.builders.handlers

import grails.util.GrailsNameUtils
import org.apache.log4j.Logger
import org.vaadin.grails.ui.builders.ComponentTreeHandler

/**
 * Abstract node handler.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
abstract class AbstractNodeHandler implements ComponentTreeHandler.TreeNodeHandler {

    private static final def log = Logger.getLogger(AbstractNodeHandler)

    final ComponentTreeHandler treeHandler

    AbstractNodeHandler(ComponentTreeHandler treeHandler) {
        this.treeHandler = treeHandler
    }

    @Override
    abstract boolean acceptNode(ComponentTreeHandler.TreeNode node)

    @Override
    abstract void handle(ComponentTreeHandler.TreeNode node)

    @Override
    void handleChildren(ComponentTreeHandler.TreeNode node) { }

    protected void invokeSetter(Object object, String setterName, Object value) {
        def setters = object.class.getMethods().findAll { it.name == setterName }

        if (setters.size() == 1) {
            def setter = setters.first()
            if (setter.parameterTypes.length == 0) {
//                    call setter only, if argument is true e.g. sizeFull="true"
                if (value) {
                    setter.invoke(object)
                    log.debug("invoked ${setterName}() on ${object?.class}")
                    return
                }
            } else {
                setter.invoke(object, value)
            }
        } else {
            object.invokeMethod(setterName, value)
        }

        log.debug("invoked ${setterName}(${value} as ${value?.class}) on ${object?.class}")
    }

    protected void applyListener(ComponentTreeHandler.TreeNode node, String listenerName, Object listener) {
        def expectedListenerClassName = GrailsNameUtils.getClassName(listenerName)
        def object = node.payload
        object.invokeMethod("add${expectedListenerClassName}", listener)
    }

    protected void applyAttribute(ComponentTreeHandler.TreeNode node, String name, Object value) {
        def component = node.payload
        if (name.endsWith("Listener")) {
            applyListener(node, name, value)
        } else {
            invokeSetter(component, GrailsNameUtils.getSetterName(name), value)
        }
    }

    protected void applyAttributes(ComponentTreeHandler.TreeNode node) {
        def component = node.payload
        if (component) {
            node.attributes.each { name, value ->
                applyAttribute(node, name, value)
            }
        } else {
            log.warn("Unable to set attributes on null for [${node.prefix}:${node.name}]")
        }
    }
}
