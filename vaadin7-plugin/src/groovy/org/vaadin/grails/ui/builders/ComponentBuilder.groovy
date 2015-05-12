package org.vaadin.grails.ui.builders

import org.apache.log4j.Logger
import org.vaadin.grails.ui.builders.handlers.*

import java.util.concurrent.ConcurrentLinkedDeque

/**
 * A helper class for building Vaadin component trees the Groovy way.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class ComponentBuilder extends BuilderSupport {

    static class BuilderNode extends Node {

        String prefix
        Object payload
        BuilderNodeHandler handler

        BuilderNode(Node parent, String prefix, Object name) {
            super(parent, name)
            this.prefix = prefix
        }

        BuilderNode(Node parent, String prefix, Object name, Object value) {
            super(parent, name, value)
            this.prefix = prefix
        }

        BuilderNode(Node parent, String prefix, Object name, Map attributes) {
            super(parent, name, attributes)
            this.prefix = prefix
        }

        BuilderNode(Node parent, String prefix, Object name, Map attributes, Object value) {
            super(parent, name, attributes, value)
            this.prefix = prefix
        }

        BuilderNode getParent() {
            super.parent()
        }

        List<BuilderNode> getChildren() {
            def children = children()
            children.findAll { it != null }
        }

        Object getName() {
            super.name()
        }

        Map getAttributes() {
            super.attributes()
        }

        Object getValue() {
            super.value()
        }
    }

    static interface BuilderNodeHandler {

        boolean acceptNode(BuilderNode node)
        void handle(BuilderNode node)
        void handleChildren(BuilderNode node)
    }

    private static final def log = Logger.getLogger(ComponentBuilder)

    static final Deque<BuilderNodeHandler> nodeHandlers

    static {
        nodeHandlers = new ConcurrentLinkedDeque<BuilderNodeHandler>()
        addNodeHandler(new ComponentNodeHandler())
        addNodeHandler(new BuildNodeHandler())
        addNodeHandler(new I18nNodeHandler())
        addNodeHandler(new MenuItemNodeHandler())
        addNodeHandler(new TreeItemNodeHandler())
        addNodeHandler(new TabSheetTabNodeHandler())
        addNodeHandler(new AccordionTabNodeHandler())
    }

    synchronized static boolean addNodeHandler(BuilderNodeHandler nodeHandler) {
        assert nodeHandler != null
        def existingNodeHandler = nodeHandlers.find { it.getClass() == nodeHandler.getClass() }
        if (existingNodeHandler) {
            if (existingNodeHandler == nodeHandler) {
                return false
            } else {
                if (!nodeHandlers.remove(existingNodeHandler)) {
                    return false
                }
            }
        }
        return nodeHandlers.push(nodeHandler)
    }

    synchronized static boolean removeNodeHandler(BuilderNodeHandler nodeHandler) {
        assert nodeHandler != null
        nodeHandlers.remove(nodeHandler)
    }

    static Object build(Closure<?> closure) {
        (new ComponentBuilder()).call(closure)
    }

    Object call(Closure<?> closure) {
        closure.delegate = this
        closure.call()
    }

    @Override
    protected final void setParent(Object parent, Object child) { }

    @Override
    protected final Object createNode(Object name) {
        createNode(name, null, null)
    }

    @Override
    protected final Object createNode(Object name, Object value) {
        createNode(name, null, value)
    }

    @Override
    protected final Object createNode(Object name, Map attributes) {
        createNode(name, attributes, null)
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        log.debug("Create node with name [${name}], attributes [${attributes?.keySet()}] and value [${value}]")

        String prefix = null
        if (name instanceof String) {
            def i = name.lastIndexOf(":")
            if (i != -1) {
                prefix = name.substring(0, i)
                name = name.substring(i + 1)
            }
        }

        def node = new BuilderNode((BuilderNode) current, prefix, name, attributes, value)

        def handler = nodeHandlers.find { it.acceptNode(node) }
        if (handler) {
            def attributesCopy = node.attributes?.clone()
            try {
                node.handler = handler
                handler.handle(node)
                node.parent?.handler?.handleChildren(node.parent)
            } finally {
                if (attributesCopy) {
                    node.attributes.putAll(attributesCopy)
                }
            }
        } else {
            throw new RuntimeException("Unexpected node with name [${node.name}]" +
                    (node.prefix ? " and prefix [${node.prefix}]" : ""))
        }

        node
    }

    @Override
    protected Object postNodeCompletion(Object parent, Object node) {
        ((BuilderNode) node).payload
    }
}
