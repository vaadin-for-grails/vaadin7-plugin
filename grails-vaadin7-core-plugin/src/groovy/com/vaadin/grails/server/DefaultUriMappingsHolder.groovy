package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import com.vaadin.grails.VaadinUIClass
import com.vaadin.grails.VaadinViewClass
import grails.util.Holders
import org.apache.log4j.Logger

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation for {@link UriMappingsHolder}.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
class DefaultUriMappingsHolder implements UriMappings {

    static final def log = Logger.getLogger(DefaultUriMappingsHolder)

    Map<String, VaadinUIClass> uiClassByPath = new ConcurrentHashMap<>()
    Map<URI, VaadinViewClass> viewClassByURI = new ConcurrentHashMap<>()

    Map<String, Object> propertiesByPath = new ConcurrentHashMap<>()
    Map<String, Object> propertiesByPathAndFragment = new ConcurrentHashMap<>()

    @PostConstruct
    protected void init() {
        def mappingsConfig = Holders.config.vaadin.mappings
        mappingsConfig.each { String path, ConfigObject pathConfig ->
//            TODO check for duplicate ui classes!
            handlePathConfig(path, pathConfig)
            def fragments = pathConfig.fragments
            fragments.each { String fragment, ConfigObject fragmentConfig ->
                handleFragmentConfig(path, fragment, fragmentConfig)
            }
        }
    }

    protected void handlePathConfig(String path, ConfigObject pathConfig) {
        def ui = pathConfig.get("ui")
        def uiNamespace = pathConfig.get("namespace") ?: null

        setPathProperty(path, DEFAULT_FRAGMENT, pathConfig.get(DEFAULT_FRAGMENT) ?: "index")
        setPathProperty(path, THEME_PATH_PROPERTY, pathConfig.get(THEME_PATH_PROPERTY))
        setPathProperty(path, WIDGETSET_PATH_PROPERTY, pathConfig.get(WIDGETSET_PATH_PROPERTY))
        setPathProperty(path, PRESERVED_ON_REFRESH_PATH_PROPERTY, pathConfig.get(PRESERVED_ON_REFRESH_PATH_PROPERTY))
        setPathProperty(path, PAGE_TITLE_PATH_PROPERTY, pathConfig.get(PAGE_TITLE_PATH_PROPERTY))
        setPathProperty(path, PUSH_MODE_PATH_PROPERTY, pathConfig.get(PUSH_MODE_PATH_PROPERTY))
        setPathProperty(path, PUSH_TRANSPORT_PATH_PROPERTY, pathConfig.get(PUSH_TRANSPORT_PATH_PROPERTY))

        def uiClass = Vaadin.utils.getVaadinUIClass(ui, uiNamespace)
        if (uiClass == null) {
            throw new RuntimeException("No class found for [${path}]" + (uiNamespace ? " with namespace [${uiNamespace}]" : ""))
        }
        log.debug("Register UI [${uiClass.fullName}] for ui [${ui}]" + (uiNamespace ? " with namespace [${uiNamespace}]" : ""))
        addUIClass(path, uiClass)
    }

    protected void handleFragmentConfig(String path, String fragment, ConfigObject fragmentConfig) {
        def view = fragmentConfig.get("view")
        def viewNamespace = fragmentConfig.get("namespace") ?: null

        def viewClass = Vaadin.utils.getVaadinViewClass(view, viewNamespace)
        if (viewClass == null) {
            throw new RuntimeException("No class found for view [${view}]" + (viewNamespace ? " with namespace [${viewNamespace}]" : ""))
        }
        log.debug("Register View [${viewClass.fullName}] for [${path}]" + (viewNamespace ? " with namespace [${viewNamespace}]" : ""))
        addViewClass(path, fragment, viewClass)
    }

    void addUIClass(String path, VaadinUIClass uiClass) {
        uiClassByPath.put(path, uiClass)
    }

    @Override
    VaadinUIClass getUIClass(String path) {
        uiClassByPath.get(path)
    }

    @Override
    String getPath(VaadinUIClass uiClass) {
        uiClassByPath.find { it.value == uiClass }?.key
    }

    @Override
    Object getPathProperty(String path, String name) {
        def properties = propertiesByPath[path] as Map<String, Object>
        properties?.get(name)
    }

    Object setPathProperty(String path, String name, Object value) {
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

    void addViewClass(String path, String fragment, VaadinViewClass viewClass) {
        URI uri = createURI(path, fragment)
        viewClassByURI.put(uri, viewClass)
    }

    @Override
    VaadinViewClass getViewClass(String path, String fragment) {
        URI uri = createURI(path, fragment)
        viewClassByURI.get(uri)
    }

    @Override
    String getFragment(String path, VaadinViewClass viewClass) {
        viewClassByURI.find {
            it.key.path == path && it.value == viewClass
        }?.key?.fragment
    }

    @Override
    Object getFragmentProperty(String path, String fragment, String name) {
        def key = "${path}#${fragment}"
        def properties = propertiesByPathAndFragment.get(key) as Map<String, Object>
        properties?.get(name)
    }

    Object setFragmentProperty(String path, String fragment, String name, Object value) {
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
