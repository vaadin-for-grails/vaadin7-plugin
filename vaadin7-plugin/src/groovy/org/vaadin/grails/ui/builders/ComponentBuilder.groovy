package org.vaadin.grails.ui.builders

import org.apache.log4j.Logger

/**
 * A helper class for creating a Vaadin component tree.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class ComponentBuilder extends BuilderSupport {

    static class BuilderNode extends Node implements ComponentTreeHandler.TreeNode {

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

        String prefix
        Object payload
        ComponentTreeHandler.TreeNodeHandler handler

        @Override
        ComponentTreeHandler.TreeNode getParent() {
            super.parent()
        }

        @Override
        List<ComponentTreeHandler.TreeNode> getChildren() {
            def children = children()
            children.findAll { it != null }
        }

        @Override
        Object getName() {
            super.name()
        }

        @Override
        Map getAttributes() {
            super.attributes()
        }

        @Override
        Object getValue() {
            super.value()
        }
    }

    private static final def log = Logger.getLogger(ComponentBuilder)

    static Object build(ComponentTreeHandler tree, Closure<?> closure) {
        (new ComponentBuilder(tree)).call(closure)
    }

    static Object build(Closure<?> closure) {
        (new ComponentBuilder()).call(closure)
    }

    final ComponentTreeHandler treeHandler

    ComponentBuilder(ComponentTreeHandler treeHandler) {
        this.treeHandler = treeHandler
    }

    ComponentBuilder() {
        treeHandler = new ComponentTreeHandler()
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

        def node = new BuilderNode(current, prefix, name, attributes, value)
        treeHandler.handleNode(node)
        node
    }

    @Override
    protected Object postNodeCompletion(Object parent, Object node) {
        ((BuilderNode) node).payload
    }
}
