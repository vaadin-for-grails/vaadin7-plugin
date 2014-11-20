package com.vaadin.grails.navigator

import com.vaadin.grails.server.MappingsProvider
import com.vaadin.navigator.View
import com.vaadin.server.Page
import com.vaadin.ui.UI
import grails.util.Holders
import org.codehaus.groovy.grails.web.util.WebUtils

/**
 * Utils for ease navigation between views and uis.
 *
 * @author Stephan Grundner
 */
class NavigationUtils {

//    MappingsProvider mappingsProvider

    /**
     * Encode a map to a String.
     *
     * @param params A parameter map
     * @return A string representation of a map
     */
    String encodeParams(Map params) {
        def encoded = WebUtils.toQueryString(params)
        encoded = encoded.replace("&", "/")
        if (encoded.startsWith("?")) {
            encoded = encoded.substring(1, encoded.length())
        }
        encoded
    }

    /**
     * Decode a String to a map.
     *
     * @param encoded An encoded String
     * @return A map representation of an encoded String
     */
    Map decodeParams(String encoded) {
        WebUtils.fromQueryString(encoded.replace("/", "&"))
    }

    MappingsProvider getMappingsProvider() {
        Holders.applicationContext.getBean("mappingsProvider") as MappingsProvider
    }

    protected void enterView(Class<? extends UI> uiClass, Class<? extends View> viewClass, Map params) {
        def uiMapping = mappingsProvider.getMapping(uiClass)
        def path = uiMapping.path.substring(1)
        println "navigate to ${path}"
        if (viewClass) {

            def viewMapping = mappingsProvider.getMapping(viewClass)
            path = "${path}${viewMapping.path}"
            if (params) {
                path = "${path}/${encodeParams(params)}"
            }
        }
        Page.current.setLocation(path as String)
    }

    protected void enterView(Class<? extends View> viewClass, Map params) {
        def viewMapping = mappingsProvider.getMapping(viewClass)
        def navigationState = viewMapping.path
        if (params && params.size() > 0) {
            navigationState += "/${encodeParams(params)}"
        }
        UI.current.navigator.navigateTo(navigationState)
    }

    public void enter(Class<?> targetClass, Map params = null) {
        if (UI.isAssignableFrom(targetClass)) {
            println "navigate to ui ${targetClass}"
            enterView(targetClass, null, params)
        } else {
            println "navigate to view ${targetClass}"
            enterView(targetClass, params)
        }
    }

    public void enter(Class<? extends UI> uiClass, Class<? extends View> viewClass, Map params = null) {
        if (uiClass == UI.current.getClass()) {
            enterView(viewClass, params)
        } else {
            enterView(uiClass, viewClass, params)
        }
    }
}
