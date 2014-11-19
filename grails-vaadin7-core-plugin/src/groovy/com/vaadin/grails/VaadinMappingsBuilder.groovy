package com.vaadin.grails
/**
 * @author Stephan Grundner
 */
public class VaadinMappingsBuilder extends BuilderSupport {

//    static class Mapping {
//        Class<? extends UI> ui
//        Class<? extends UIProvider> uiProvider
//    }

    final Closure mappingsClosure
    private Map<String, Object> mappings

    VaadinMappingsBuilder(Closure mappingsClosure) {
        this.mappingsClosure = mappingsClosure
    }

    VaadinMappingsBuilder(VaadinMappingsClass mappingsClass) {
        mappingsClosure = mappingsClass.getMappingsClosure()
    }

    Map<String, Object> build() {
        mappings = new HashMap<String, Object>()
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
        mappings.put(name, attributes)
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        throw new UnsupportedOperationException()
    }
}
