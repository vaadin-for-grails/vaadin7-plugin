package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.ui.UI
import grails.util.Holders
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.GrailsApplication

import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation for {@link UriMappingsHolder}.
 *
 * @since 1.0
 * @author Stephan Grundner
 */
class DefaultUriMappingsHolder implements UriMappings {

    static final def log = Logger.getLogger(DefaultUriMappingsHolder)

    protected final Map<String, Class<? extends UI>> uiClassByPath = new ConcurrentHashMap<>()
    protected final Map<URI, Class<? extends View>> viewClassByURI = new ConcurrentHashMap<>()

    protected final Map<String, Object> propertiesByPath = new ConcurrentHashMap<>()
    protected final Map<String, Object> propertiesByPathAndFragment = new ConcurrentHashMap<>()

    @Override
    void reload() {
        uiClassByPath.clear()
        viewClassByURI.clear()
        propertiesByPath.clear()
        propertiesByPathAndFragment.clear()

        def mappingsConfig = Holders.config.vaadin.mappings
        mappingsConfig.each { String path, ConfigObject pathConfig ->
            handlePathConfig(path, pathConfig)
            def fragments = pathConfig.fragments
            fragments.each { String fragment, ConfigObject fragmentConfig ->
                handleFragmentConfig(path, fragment, fragmentConfig)
            }
        }
    }

    protected void handlePathConfig(String path, ConfigObject pathConfig) {
        def ui = pathConfig.get("ui")

        putPathProperty(path, DEFAULT_FRAGMENT, pathConfig.get(DEFAULT_FRAGMENT) ?: "index")
        putPathProperty(path, THEME_PATH_PROPERTY, pathConfig.get(THEME_PATH_PROPERTY))
        putPathProperty(path, WIDGETSET_PATH_PROPERTY, pathConfig.get(WIDGETSET_PATH_PROPERTY))
        putPathProperty(path, PRESERVED_ON_REFRESH_PATH_PROPERTY, pathConfig.get(PRESERVED_ON_REFRESH_PATH_PROPERTY))
        putPathProperty(path, PAGE_TITLE_PATH_PROPERTY, pathConfig.get(PAGE_TITLE_PATH_PROPERTY))
        putPathProperty(path, PUSH_MODE_PATH_PROPERTY, pathConfig.get(PUSH_MODE_PATH_PROPERTY))
        putPathProperty(path, PUSH_TRANSPORT_PATH_PROPERTY, pathConfig.get(PUSH_TRANSPORT_PATH_PROPERTY))

        Class<? extends UI> uiClass

        if (ui instanceof String) {
            def classLoader = Vaadin.getInstance(GrailsApplication).classLoader
            uiClass = classLoader.loadClass(ui)
        } else {
            uiClass = ui
        }

        if (uiClass == null) {
            throw new RuntimeException("No class found for [${path}]")
        }
        log.debug("Register UI [${uiClass.name}] for path [${path}]")
        putUIClass(path, uiClass)
    }

    protected void handleFragmentConfig(String path, String fragment, ConfigObject fragmentConfig) {
        def view = fragmentConfig.get("view")

        Class<? extends View> viewClass

        if (view instanceof String) {
            def classLoader = Vaadin.getInstance(GrailsApplication).classLoader
            viewClass = classLoader.loadClass(view)
        } else {
            viewClass = view
        }

        if (viewClass == null) {
            throw new RuntimeException("No class found for view [${view}]")
        }
        log.debug("Register View [${viewClass.name}] for path [${path}#!${fragment}]")
        putViewClass(path, fragment, viewClass)
    }

    void putUIClass(String path, Class<? extends UI> uiClass) {
        uiClassByPath.put(path, uiClass)
    }

    @Override
    Class<? extends UI> getUIClass(String path) {
        uiClassByPath.get(path)
    }

    @Override
    Object getPathProperty(String path, String name) {
        def properties = propertiesByPath[path] as Map<String, Object>
        properties?.get(name)
    }

    Object putPathProperty(String path, String name, Object value) {
        Map<String, Object> properties = propertiesByPath.get(path)
        if (properties == null) {
            properties = [:]
            propertiesByPath.put(path, properties)
        }
        properties.put(name, value)
    }

    @Override
    Collection<String> getAllPaths() {
        uiClassByPath.keySet()
    }

    protected URI createURI(String path, String fragment) {
        URI.create("${path}#${fragment}")
    }

    void putViewClass(String path, String fragment, Class<? extends View> viewClass) {
        URI uri = createURI(path, fragment)
        viewClassByURI.put(uri, viewClass)
    }

    @Override
    Class<? extends View> getViewClass(String path, String fragment) {
        URI uri = createURI(path, fragment)
        viewClassByURI.get(uri)
    }

    @Override
    Object getFragmentProperty(String path, String fragment, String name) {
        def key = "${path}#${fragment}"
        def properties = propertiesByPathAndFragment.get(key) as Map<String, Object>
        properties?.get(name)
    }

    Object putFragmentProperty(String path, String fragment, String name, Object value) {
        def key = "${path}#${fragment}"
        Map<String, Object> properties = propertiesByPathAndFragment.get(key)
        if (properties == null) {
            properties = [:]
            propertiesByPathAndFragment.put(key, properties)
        }
        properties.put(name, value)
    }

    @Override
    Collection<String> getAllFragments(String path) {
        viewClassByURI.keySet().findAll { it.path == path }.collect { it.fragment }
    }
}
