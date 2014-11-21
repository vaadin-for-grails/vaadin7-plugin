package com.vaadin.grails.server
/**
 * Provides mappings between URIs such as /vaadin/book#!show/id=1 and Vaadin UIs or Views.
 *
 * @author Stephan Grundner
 */
public interface MappingsProvider {

    /**
     * Represents a single mapping entry in <code>VaadinMappings</code>
     */
    static interface Mapping {

        /**
         * Return the URI fragment.
         *
         * @return The URI fragment
         */
        String getPath()

        /**
         * Return the artefact name for the UI.
         *
         * @return The artefact name for the UI
         */
        String getUI()

        /**
         * Return the namespace if specified.
         *
         * @return The namespace if specified, otherwise null
         */
        String getNamespace()

//        /**
//         * Return the class of the UI.
//         *
//         * @return The class of the UI
//         */
//        Class<? extends UI> getClazz()

        String getTheme()
        String getWidgetset()
        String getPreservedOnRefresh()
        String getPageTitle()
        String getPushMode()
        String getPushTransport()

        Map<String, String> getViewMappings()
    }

    /**
     * Return true if there is a mapping for the specified path.
     *
     * @param path The URI fragment specified in <code>VaadinMappings</code>
     * @return True if there is a mapping for the specified path, otherwise false
     */
    boolean containsMapping(String path)

    /**
     * Return the mapping for the specified path.
     *
     * @param path The URI fragment specified in <code>VaadinMappings</code>
     * @return The mapping for the specified path, or null if no mapping was found
     */
    Mapping getMapping(String path)

    /**
     * Return all defined mappings.
     *
     * @param path The URI fragment specified in <code>VaadinMappings</code>
     * @return All defined mappings
     */
    Collection<Mapping> getAllMappings()
}