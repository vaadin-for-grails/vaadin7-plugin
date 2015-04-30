package org.vaadin.grails.server

import com.vaadin.navigator.View
import com.vaadin.server.UIProvider
import com.vaadin.ui.UI

/**
 * Map paths to UIs and fragments to Views.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
public interface UriMappings {

    static final DEFAULT_FRAGMENT_PATH_PROPERTY = "defaultFragment"
    static final THEME_PATH_PROPERTY = "theme"
    static final WIDGETSET_PATH_PROPERTY = "widgetset"
    static final PRESERVED_ON_REFRESH_PATH_PROPERTY = "preservedOnRefresh"
    static final PAGE_TITLE_PATH_PROPERTY = "pageTitle"
    static final PUSH_MODE_PATH_PROPERTY = "pushMode"
    static final PUSH_TRANSPORT_PATH_PROPERTY = "pushTransport"

    Class<? extends UI> getUIClass(String path)
    void setUIClass(String path, Class<? extends UI> uiClass)

    Class<? extends UIProvider> getUIProviderClass(String path)
    void setUIProviderClass(String path, Class<? extends UIProvider> uiProviderClass)

    Class<? extends View> getViewClass(String path, String fragment)
    void setViewClass(String path, String fragment, Class<? extends View> viewClass)

    List<String> getAllPaths()
    Object getPathProperty(String path, String name)
    Object putPathProperty(String path, String name, Object value)

    List<String> getAllFragments(String path)
    Object getFragmentProperty(String path, String fragment, String name)
    Object putFragmentProperty(String path, String fragment, String name, Object value)

    void clear()
}