package com.vaadin.grails.server
/**
 * @author Stephan Grundner
 */
interface MappingsProvider {

    Collection<Mapping> getAllMappings()
    Mapping getMapping(String path)
}
