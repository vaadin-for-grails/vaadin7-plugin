package com.vaadin.grails.server

import com.vaadin.grails.VaadinMappingsClass
import com.vaadin.navigator.View
import com.vaadin.ui.UI
import org.codehaus.groovy.grails.web.mapping.UrlMapping

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
         * Return the name representation of the View/UI.
         * <p>
         *     This may be name of the Grails artefact.
         * </p>
         *
         * @return The name representation of the View/UI
         */
        String getName()

        /**
         * Return the class of the View/UI.
         *
         * @return The class of the View/UI
         */
        Class<?> getClazz()
    }

    /**
     * UI mappings must start with "/".
     *
     * ^/([a-zA-Z0-9_]/?)*$
     */
    static interface UIMapping extends Mapping {
        String getTheme()
        String getWidgetset()
        String getPreservedOnRefresh()
        String getPageTitle()
        String getPushMode()
        String getPushTransport()

        Class<? extends UI> getClazz()
    }

    /**
     * View mappings must start with "#!".
     *
     * ^#\!([a-zA-Z0-9_]/?)*$
     */
    static interface ViewMapping extends Mapping {

        Collection<Class<? extends UI>> getOwners()
        Class<? extends View> getClazz()
    }

    /**
     * Return the Vaadin mappings artefact class.
     *
     * @return The Vaadin mappings artefact class
     */
    VaadinMappingsClass getMappingsClass()

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
     * Return the mapping for the specified class.
     *
     * @param path The URI fragment specified in <code>VaadinMappings</code>
     * @return The mapping for the specified class, or null if no mapping was found
     */
    Mapping getMapping(Class<?> clazz)

    /**
     * Return all defined mappings.
     *
     * @param path The URI fragment specified in <code>VaadinMappings</code>
     * @return All defined mappings
     */
    Collection<Mapping> getAllMappings()
}