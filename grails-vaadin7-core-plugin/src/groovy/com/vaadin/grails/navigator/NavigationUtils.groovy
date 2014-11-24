package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.server.MappingsProvider
import com.vaadin.server.Page
import com.vaadin.ui.UI
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.beans.factory.annotation.Autowired

/**
 * Utils for ease navigation between views and uis.
 *
 * @author Stephan Grundner
 */
class NavigationUtils {

    @Autowired
    MappingsProvider mappingsProvider

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

    void enter(Map params) {
        def ui = params.remove("ui")
        def view = params.remove("view")
        def namespace = params.remove("namespace")

        if (ui) {
            enter(ui, view, namespace, params.remove("params"))
        } else {
            enter(view, namespace, params.remove("params"))
        }
    }

    void enter(String ui, String view, String namespace, Map params) {
        def uiClass = Vaadin.utils.getVaadinUIClass(ui, namespace)
        def viewClass = Vaadin.utils.getVaadinViewClass(view, namespace)

        println "enter ui=${uiClass}, view=${viewClass}"
        def mapping = mappingsProvider.allMappings.find { it.getUIClass() == uiClass }
        if (mapping) {
            def path = mapping.path
            def fragment = mapping.getFragment(viewClass.logicalPropertyName)

            def linkGenerator = Vaadin.applicationContext.getBean(LinkGenerator)

            Page.current.setLocation(linkGenerator.link(uri: path + "#!" + fragment))
        } else {
            throw new RuntimeException("No mapping found for ui [${ui}] and view [${view}]")
        }
    }

    void enter(String view, String namespace, Map params) {
        def viewClass = Vaadin.utils.getVaadinViewClass(view, namespace)
        def mapping = mappingsProvider.allMappings.find { it.getUIClass().clazz == UI.current.getClass() }
        if (mapping) {
            def navigator = UI.current.navigator
            def state = mapping.getFragment(viewClass.logicalPropertyName)
            println "navigate to ${state}"
            navigator.navigateTo(state)
        } else {
            throw new RuntimeException("No mapping found for view [${view}]")
        }
    }
}
