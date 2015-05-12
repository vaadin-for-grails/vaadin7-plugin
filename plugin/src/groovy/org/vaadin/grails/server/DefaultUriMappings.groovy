package org.vaadin.grails.server

import com.vaadin.navigator.View
import com.vaadin.server.UIProvider
import com.vaadin.ui.UI

import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation for {@link UriMappings}.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class DefaultUriMappings implements UriMappings {

    protected final def uiClassByPath = new HashMap<String, Class<? extends UI>>()
    protected final def uiProviderClassByPath = new HashMap<String, Class<? extends UIProvider>>()
    protected final def viewClassByURI = new HashMap<URI, Class<? extends UIProvider>>()

    protected final Map<String, Object> propertiesByPath = new ConcurrentHashMap<>()
    protected final Map<String, Object> propertiesByPathAndFragment = new ConcurrentHashMap<>()

    Class<? extends UI> getUIClass(String path) {
        uiClassByPath.get(path)
    }

    void setUIClass(String path, Class<? extends UI> uiClass) {
        uiClassByPath.put(path, uiClass)
    }

    Class<? extends UIProvider> getUIProviderClass(String path) {
        uiProviderClassByPath.get(path)
    }

    void setUIProviderClass(String path, Class<? extends UIProvider> uiProviderClass) {
        uiProviderClassByPath.put(path, uiProviderClass)
    }

    protected URI createURI(String path, String fragment) {
        URI.create("$path#$fragment")
    }

    Class<? extends View> getViewClass(String path, String fragment) {
        def uri = createURI(path, fragment)
        viewClassByURI.get(uri)
    }

    void setViewClass(String path, String fragment, Class<? extends View> viewClass) {
        def uri = createURI(path, fragment)
        viewClassByURI.put(uri, viewClass)
    }

    List<String> getAllPaths() {
        uiClassByPath.keySet()
    }

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

    List<String> getAllFragments(String path) {
        viewClassByURI.keySet().findAll { it.path == path }.collect { it.fragment }
    }

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
    void clear() {
        uiClassByPath.clear()
        uiProviderClassByPath.clear()
        viewClassByURI.clear()
        propertiesByPath.clear()
        propertiesByPathAndFragment.clear()
    }
}
