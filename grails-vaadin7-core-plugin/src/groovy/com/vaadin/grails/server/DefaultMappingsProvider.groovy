package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import com.vaadin.grails.VaadinUIClass
import com.vaadin.grails.VaadinViewClass
import grails.util.Holders
import org.apache.log4j.Logger

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation for {@link MappingsProvider}.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
class DefaultMappingsProvider implements MappingsProvider {

    static final def log = Logger.getLogger(DefaultMappingsProvider)

    Map<String, VaadinUIClass> uiClassByPath = new ConcurrentHashMap<>()
    Map<URI, VaadinViewClass> viewClassByURI = new ConcurrentHashMap<>()

    Map<String, Object> uiSettingsByPath = new ConcurrentHashMap<>()

    @PostConstruct
    protected void init() {
        def mappingsConfig = Holders.config.vaadin.mappings
        mappingsConfig.each { String path, ConfigObject pathConfig ->
            mapPath(path, pathConfig)
            def fragments = pathConfig.fragments
            fragments.each { String fragment, ConfigObject fragmentConfig ->
                mapFragment(path, fragment, fragmentConfig)
            }
        }
    }

    protected void mapPath(String path, ConfigObject pathConfig) {
        def ui = pathConfig.get("ui")
        def uiNamespace = pathConfig.get("namespace") ?: null

        uiSettingsByPath.put(path, [:])
        uiSettingsByPath[path]["theme"] = pathConfig.get("theme")
        uiSettingsByPath[path]["widgetset"] = pathConfig.get("widgetset")
        uiSettingsByPath[path]["preservedOnRefresh"] = pathConfig.get("preservedOnRefresh")
        uiSettingsByPath[path]["pageTitle"] = pathConfig.get("pageTitle")
        uiSettingsByPath[path]["pushMode"] = pathConfig.get("pushMode")
        uiSettingsByPath[path]["pushTransport"] = pathConfig.get("pushTransport")

        def uiClass = Vaadin.utils.getVaadinUIClass(ui, uiNamespace)
        if (uiClass == null) {
            throw new RuntimeException("No class found for ui [${ui}]" + (uiNamespace ? " with namespace [${uiNamespace}]" : ""))
        }
        log.debug("Register class [${uiClass.fullName}] for ui [${ui}]" + (uiNamespace ? " with namespace [${uiNamespace}]" : ""))
        addUIClass(path, uiClass)
    }

    protected void mapFragment(String path, String fragment, ConfigObject fragmentConfig) {
        def view = fragmentConfig.get("view")
        def viewNamespace = fragmentConfig.get("namespace") ?: null

        def viewClass = Vaadin.utils.getVaadinViewClass(view, viewNamespace)
        if (viewClass == null) {
            throw new RuntimeException("No class found for view [${view}]" + (viewNamespace ? " with namespace [${viewNamespace}]" : ""))
        }
        log.debug("Register class [${viewClass.fullName}] for view [${view}]" + (viewNamespace ? " with namespace [${viewNamespace}]" : ""))
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
    String getTheme(String path) {
        uiSettingsByPath[path]["theme"]
    }

    @Override
    String getWidgetset(String path) {
        uiSettingsByPath[path]["widgetset"]
    }

    @Override
    boolean isPreservedOnRefresh(String path) {
        uiSettingsByPath[path]["preservedOnRefresh"]
    }

    @Override
    String getPageTitle(String path) {
        uiSettingsByPath[path]["pageTitle"]
    }

    @Override
    String getPushMode(String path) {
        uiSettingsByPath[path]["pushMode"]
    }

    @Override
    String getPushTransport(String path) {
        uiSettingsByPath[path]["pushTransport"]
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
    Collection<String> getAllFragments(String path) {
        viewClassByURI.keySet().collect { it.fragment }
    }
}
