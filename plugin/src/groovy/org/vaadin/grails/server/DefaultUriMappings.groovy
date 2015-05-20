package org.vaadin.grails.server

import com.vaadin.navigator.View
import com.vaadin.server.UIProvider
import com.vaadin.ui.UI
import org.apache.log4j.Logger

/**
 * Default implementation for {@link UriMappings}.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class DefaultUriMappings implements UriMappings {

    private static final log = Logger.getLogger(DefaultUriMappings)

    protected abstract class AbstractMapping {

        final Map<String, Object> properties = new HashMap()
    }

    protected class FragmentMapping extends AbstractMapping {

        Class<? extends View> viewClass
    }

    protected class PathMapping extends AbstractMapping {

        Class<? extends UI> uiClass
        Class<? extends UIProvider> uiProviderClass

        private final Map<String, FragmentMapping> mappingByFragment = new HashMap()
        private final Map<Class<? extends View>, String> primaryFragmentByViewClass = new IdentityHashMap()

        FragmentMapping getFragmentMapping(String fragment) {
            def mapping = mappingByFragment.get(fragment)
            if (mapping == null) {
                synchronized (DefaultUriMappings.this) {
                    mapping = mappingByFragment.get(fragment)
                    if (mapping == null) {
                        mapping = new FragmentMapping()
                        mappingByFragment.put(fragment, mapping)
                    }
                }
            }
            mapping
        }
    }

    private final Map<String, PathMapping> mappingByPath = new HashMap()
    private final Map<Class<? extends UI>, String> primaryPathByUIClass = new IdentityHashMap()

    protected PathMapping getPathMapping(String path) {
        def mapping = mappingByPath.get(path)
        if (mapping == null) {
            synchronized (this) {
                mapping = mappingByPath.get(path)
                if (mapping == null) {
                    mapping = new PathMapping()
                    mappingByPath.put(path, mapping)
                }
            }
        }
        mapping
    }

    @Override
    Class<? extends UI> getUIClass(String path) {
        getPathMapping(path).uiClass
    }

    @Override
    void setUIClass(String path, Class<? extends UI> uiClass) {
        getPathMapping(path).uiClass = uiClass
    }

    @Override
    Class<? extends UIProvider> getUIProviderClass(String path) {
        getPathMapping(path).uiProviderClass
    }

    @Override
    void setUIProviderClass(String path, Class<? extends UIProvider> uiProviderClass) {
        getPathMapping(path).uiProviderClass = uiProviderClass
    }

    @Override
    Class<? extends View> getViewClass(String path, String fragment) {
        def pathMapping = getPathMapping(path)
        pathMapping.getFragmentMapping(fragment).viewClass
    }

    @Override
    void setViewClass(String path, String fragment, Class<? extends View> viewClass) {
        def pathMapping = getPathMapping(path)
        def fragmentMapping = pathMapping.getFragmentMapping(fragment)
        fragmentMapping.viewClass = viewClass
    }

    @Override
    List<String> getAllPaths() {
        def allPaths = mappingByPath.keySet()
        allPaths.asList()
    }

    @Override
    Object getPathProperty(String path, String name) {
        def pathMapping = getPathMapping(path)
        pathMapping.properties.get(name)
    }

    @Override
    Object putPathProperty(String path, String name, Object value) {
        def pathMapping = getPathMapping(path)
        pathMapping.properties.put(name, value)
    }

    @Override
    List<String> getAllFragments(String path) {
        def pathMapping = getPathMapping(path)
        def allFragments = pathMapping.mappingByFragment.keySet()
        allFragments.asList()
    }

    @Override
    Object getFragmentProperty(String path, String fragment, String name) {
        def pathMapping = getPathMapping(path)
        def fragmentMapping = pathMapping.getFragmentMapping(fragment)
        fragmentMapping.properties.get(name)
    }

    @Override
    Object putFragmentProperty(String path, String fragment, String name, Object value) {
        def pathMapping = getPathMapping(path)
        def fragmentMapping = pathMapping.getFragmentMapping(fragment)
        fragmentMapping.properties.put(name, value)
    }

    @Override
    String getPrimaryPath(Class<? extends UI> uiClass) {
        def primaryPath = primaryPathByUIClass.get(uiClass)
        if (primaryPath == null) {
            primaryPath = mappingByPath.findResult { path, mapping ->
                if (mapping.uiClass == uiClass) {
                    log.warn("No primary path set for UI [$uiClass], using [${path}]")
                    primaryPathByUIClass.put(uiClass, path)
                    return path
                }
                null
            }
        }
        primaryPath
    }

    @Override
    void setPrimaryPath(Class<? extends UI> uiClass, String primaryPath) {
        primaryPathByUIClass.put(uiClass, primaryPath)
    }

    @Override
    String getPrimaryFragment(String path, Class<? extends View> viewClass) {
        def pathMapping = getPathMapping(path)
        def primaryFragment = pathMapping.primaryFragmentByViewClass.get(viewClass)
        if (primaryFragment == null) {
            primaryFragment = pathMapping.mappingByFragment.findResult { fragment, mapping ->
                if (mapping.viewClass == viewClass) {
                    log.warn("No primary fragment set for View [$viewClass] and path [$path], using [${fragment}]")
                    pathMapping.primaryFragmentByViewClass.put(viewClass, fragment)
                    return fragment
                }
                null
            }
        }
        primaryFragment
    }

    @Override
    void setPrimaryFragment(String path, Class<? extends View> viewClass, String primaryFragment) {
        def pathMapping = getPathMapping(path)
        pathMapping.primaryFragmentByViewClass.put(viewClass, primaryFragment)
    }

    @Override
    void clear() {
        mappingByPath.clear()
        primaryPathByUIClass.clear()
    }
}
