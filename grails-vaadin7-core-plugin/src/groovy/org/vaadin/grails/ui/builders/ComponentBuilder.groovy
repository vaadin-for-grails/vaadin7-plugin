package org.vaadin.grails.ui.builders

import com.vaadin.data.fieldgroup.FieldGroup
import org.apache.log4j.Logger

/**
 * A helper class for creating a Vaadin component tree.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class ComponentBuilder extends BuilderSupport {

    static class BuilderNode extends Node implements ComponentTree.TreeNode {

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
        ComponentTree.TreeNodeHandler handler

        @Override
        ComponentTree.TreeNode getParent() {
            super.parent()
        }

        @Override
        List<ComponentTree.TreeNode> getChildren() {
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

    static Object build(ComponentTree tree, Closure<?> closure) {
        (new ComponentBuilder(tree)).call(closure)
    }

    static Object build(Closure<?> closure) {
        (new ComponentBuilder()).call(closure)
    }

    final ComponentTree tree

    ComponentBuilder(ComponentTree tree) {
        this.tree = tree
    }

    ComponentBuilder() {
        tree = new ComponentTree()
    }

    Object call(Closure<?> closure) {
        tree.clear()
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
        log.debug("create node with name [${name}], attributes [${attributes?.keySet()}] and value [${value}]")
        String prefix = null
        if (name instanceof String) {
            def i = name.lastIndexOf(":")
            if (i != -1) {
                prefix = name.substring(0, i)
                name = name.substring(i + 1)
            }
        }

        if (prefix == null) {
            prefix = "com.vaadin.ui"
        }

        def node = new BuilderNode(current, prefix, name, attributes, value)
        tree.processNode(node)
        node
    }

    @Override
    protected Object postNodeCompletion(Object parent, Object node) {
        ((BuilderNode) node).payload
    }

    FieldGroup getFieldGroup(String key) {
        tree.getFieldGroup(key)
    }

    void setFieldGroup(String key, FieldGroup fieldGroup) {
        tree.setFieldGroup(key, fieldGroup)
    }

    Object getComponent(String id) {
        tree.getComponent(id)
    }
}
