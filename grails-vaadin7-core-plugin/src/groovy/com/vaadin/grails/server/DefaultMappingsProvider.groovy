package com.vaadin.grails.server

import com.vaadin.grails.Vaadin

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

/**
 * The default implementation for {@link  MappingsProvider}.
 * <p>
 *     This class is accessible as a bean with the name <code>mappingsProvider</code>.
 * </p>
 * @author Stephan Grundner
 */
class DefaultMappingsProvider implements MappingsProvider {

    static class DefaultMapping implements MappingsProvider.Mapping {

        private String ui
        String path
        String namespace
        Map<String, String> viewMappings = [:]

        String theme
        String widgetset
        String preservedOnRefresh
        String pageTitle
        String pushMode
        String pushTransport

        @Override
        String getUI() {
            ui
        }

        void setUI(String ui) {
            this.ui = ui
        }
    }

    static class MappingsBuilder extends BuilderSupport {

        Map<String, MappingsProvider.Mapping> mappings

        public Map<String, MappingsProvider.Mapping> build(Closure closure) {
            def delegate = closure.delegate
            try {
                mappings = new HashMap<String, MappingsProvider.Mapping>()
                closure.delegate = this
                closure.call()
            } finally {
                closure.delegate = delegate
            }
            mappings
        }

        @Override
        protected void setParent(Object parent, Object child) {}

        @Override
        protected Object createNode(Object name) {
            throw new UnsupportedOperationException()
        }

        @Override
        protected Object createNode(Object name, Object value) {
            throw new UnsupportedOperationException()
        }

        @Override
        protected Object createNode(Object name, Map attributes) {
            createNode(name, attributes, null)
        }

        @Override
        protected Object createNode(Object name, Map attributes, Object value) {
            Object node

            def resolveAttribute = { String attributeName ->
                def resolved = attributes.remove(attributeName)
                if (resolved == null) {
                    throw new RuntimeException("Missing required attribute [${name}]")
                }
                resolved
            }

            if (current instanceof MappingsProvider.Mapping) {
                def m = current.viewMappings
                def view = resolveAttribute("view")
                m.put(name, view)
                node = m
            } else {
                def path = name as String
                if (path?.startsWith("/")) {
                    def m = new DefaultMapping()
                    m.path = path
                    m.ui = resolveAttribute("ui")
                    m.namespace = attributes.remove("namespace")
                    m.theme = attributes.remove("theme")
                    m.widgetset = attributes.remove("widgetset")
                    m.preservedOnRefresh = attributes.remove("preservedOnRefresh")
                    m.pageTitle = attributes.remove("pageTitle")
                    m.pushMode = attributes.remove("pushMode")
                    m.pushTransport = attributes.remove("pushTransport")
                    mappings.put(path, m)
                    node = m
                } else {
//                TODO Illegal mapping!
                }
            }
            node
        }
    }

    protected final def mappings = new ConcurrentHashMap<String, MappingsProvider.Mapping>()

    DefaultMappingsProvider() {

    }

    DefaultMappingsProvider(Closure mappingsClosure) {
        def builder = new MappingsBuilder()
        mappings.putAll builder.build(mappingsClosure)
    }

    @PostConstruct
    protected void init() {
        def builder = new MappingsBuilder()
        def vaadinMappingsClasses = Vaadin.vaadinUtils.vaadinMappingsClasses
        vaadinMappingsClasses.each { vaadinMappingsClass ->
            mappings.putAll builder.build(vaadinMappingsClass.mappingsClosure)
        }
    }

    @Override
    boolean containsMapping(String path) {
        mappings.contains(path)
    }

    @Override
    MappingsProvider.Mapping getMapping(String path) {
        mappings.get(path)
    }

    @Override
    Collection<MappingsProvider.Mapping> getAllMappings() {
        mappings.values()
    }
}
