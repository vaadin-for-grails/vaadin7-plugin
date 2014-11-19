package com.vaadin.grails

import com.vaadin.navigator.View
import com.vaadin.ui.UI
import grails.util.GrailsNameUtils
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsClassUtils

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

class DefaultMappingsProvider implements MappingsProvider {

    static class DefaultMapping implements MappingsProvider.Mapping {

        String path
        String theme
        String widgetset
        String preservedOnRefresh
        String pageTitle
        String pushMode
        String pushTransport

        private String uiName
        private Class<? extends UI> uiClass

        String viewName
        Class<? extends View> viewClass

        String getUIName() {
            return uiName
        }

        void setUIName(String uiName) {
            this.uiName = uiName
        }

        Class<? extends UI> getUIClass() {
            uiClass
        }

        void setUIClass(Class<? extends UI> uiClass) {
            this.uiClass = uiClass
        }
    }

    static class MappingsBuilder extends BuilderSupport {

        final Closure mappingsClosure
        private Map<String, DefaultMapping> mappings

        MappingsBuilder(Closure mappingsClosure) {
            this.mappingsClosure = mappingsClosure
        }

        Map<String, DefaultMapping> build() {
            mappings = new HashMap<String, DefaultMapping>()
            mappingsClosure.delegate = this
            mappingsClosure.call()
//            TODO reset original delegate
            mappings
        }

        @Override
        protected void setParent(Object parent, Object child) { }

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
            def mapping = new DefaultMapping()

            mapping.setPath(attributes.remove("path"))

            def ui = attributes.remove("ui")
            if (ui) {
                if (ui instanceof Class) {
                    mapping.setUIClass(ui)
                    mapping.setUIName(GrailsNameUtils.getLogicalPropertyName(ui.name, "UI"))
                } else {
                    mapping.setUIName(ui)
                    def artefact = Holders.grailsApplication.getArtefacts("UI")
                            .find { it.logicalPropertyName == mapping.getUIName() }
                    if (artefact) {
                        mapping.setUIClass(artefact.clazz)
                    }
                }
            }

            def view = attributes.remove("view")
            if (view) {
                if (view instanceof Class) {
                    mapping.setViewClass(view)
                    mapping.setViewName(GrailsNameUtils.getLogicalPropertyName(view.name, "View"))
                } else {
                    throw new UnsupportedOperationException("Views must be specified as classes")
                }
            }

//            Other attributes like "theme", "widgetset", ...
            attributes.each { entry ->
                mapping[entry.key] = entry.value
            }

            println "built mappin ${mapping}"
            mappings.put(mapping.path, mapping)
            mapping
        }
    }

    final def mappings = new ConcurrentHashMap<String, DefaultMapping>()

    DefaultMappingsProvider() {

    }

    DefaultMappingsProvider(Closure mappingsClosure) {
        init(mappingsClosure)
    }

    DefaultMappingsProvider(Class mappingsClass) {
        def mappingsClosure = GrailsClassUtils.getStaticPropertyValue(mappingsClass, "mappings")
        init(mappingsClosure)
    }

    @Override
    VaadinMappingsClass getMappingsClass() {
        def artefacts = Holders.grailsApplication.getArtefacts("VaadinMappings")
        if (artefacts?.size() > 0) {
            return artefacts.first()
        }
//        TODO merge artefacts
        null
    }

    @PostConstruct
    protected void init() {
        def mappingsClass = getMappingsClass()
        if (mappingsClass) {
            init(mappingsClass.mappingsClosure)
        } else {
            throw new RuntimeException("No VaadinMappings class found")
        }
    }

    protected void init(Closure mappingsClosure) {
        def builder = new MappingsBuilder(mappingsClosure)
        mappings.putAll(builder.build())
        println "built: ${mappings}"
    }

    @Override
    MappingsProvider.Mapping getMapping(String path) {
        mappings.get(path)
    }

    @Override
    MappingsProvider.Mapping getMapping(Class<? extends UI> uiClass) {
        mappings.find { path, mapping -> mapping.uiClass == uiClass }
    }

    @Override
    Map<String, MappingsProvider.Mapping> getUIMappings() {
        mappings.findAll { it.key.startsWith("/") }
    }

    @Override
    Map<String, MappingsProvider.Mapping> getViewMappings() {
        mappings.findAll { it.key.startsWith("#") }
    }
}
