package com.vaadin.grails.navigator

import com.vaadin.grails.Vaadin
import com.vaadin.grails.VaadinUIClass
import com.vaadin.grails.VaadinViewClass
import com.vaadin.grails.server.MappingsProvider
import com.vaadin.server.Page
import com.vaadin.ui.UI
import grails.util.GrailsWebUtil
import grails.util.Holders
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.request.RequestContextHolder

/**
 * Utils for ease navigation between Views and UIs.
 *
 * @since 2.0
 * @author Stephan Grundner
 */
class NavigationUtils {

    static final def log = Logger.getLogger(NavigationUtils)

    @Autowired
    MappingsProvider mappingsProvider

    @Autowired
    LinkGenerator linkGenerator

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

    protected VaadinUIClass resolveUIClass(Object ui, String namespace) {
        def utils = Vaadin.utils
        String name
        if (ui instanceof Map) {
            if (namespace) {
                throw new IllegalArgumentException("namespace")
            }
            name = ui["name"]
            namespace = ui["namespace"]
            return utils.getVaadinUIClass(name, namespace)
        } else {
            name = ui as String
        }
        def uiClass = utils.getVaadinUIClass(name, namespace)
        if (uiClass == null) {
            throw new RuntimeException("Unable to resolve UI for name [${name}]" +
                    (namespace ? " and namespace [${namespace}]" : ""))
        }
        uiClass
    }

    protected VaadinViewClass resolveViewClass(Object view, String namespace) {
        def utils = Vaadin.utils
        String name
        if (view instanceof Map) {
            if (namespace) {
                throw new IllegalArgumentException("namespace")
            }
            name = view["name"]
            namespace = view["namespace"]
        } else {
            name = view as String
        }
        def viewClass = utils.getVaadinViewClass(name, namespace)
        if (viewClass == null) {
            throw new RuntimeException("Unable to resolve View for name [${name}]" +
                    (namespace ? " and namespace [${namespace}]" : ""))
        }
        viewClass
    }

    /**
     * Switch between Views and UIs.
     *
     * @see #enter(com.vaadin.grails.VaadinUIClass, com.vaadin.grails.VaadinViewClass, java.util.Map)
     * @see #enter(com.vaadin.grails.VaadinViewClass, java.util.Map)
     * @param params Navigation params
     */
    void enter(Map params) {
        def ui = params.get("ui")
        def view = params.get("view")
        def namespace = params.get("namespace")

        if (ui) {
            if (view) {
                def uiClass = resolveUIClass(ui, namespace)
                def viewClass = resolveViewClass(view, namespace)
                enter(uiClass, viewClass, params.get("params"))
            } else {
                def uiClass = resolveUIClass(ui, namespace)
                enter(uiClass, null, params.get("params"))
            }
        } else if (view) {
            def viewClass = resolveViewClass(view, namespace)
            enter(viewClass, params.get("params"))
        }
    }

    /**
     * Enter the specified UI or a View within the specified UI.
     *
     * @param uiClass The Vaadin UI class
     * @param viewClass The Vaadin View class
     * @param params Additional parameters
     */
    void enter(VaadinUIClass uiClass, VaadinViewClass viewClass, Map params) {
        def utils = Vaadin.utils
        if (uiClass == utils.currentVaadinUIClass) {
            enter(viewClass, params)
        } else {
            if (log.debugEnabled) {
                def message = "Enter UI with class [${uiClass?.fullName}]"
                if (viewClass) {
                    message += " and View with class [${viewClass?.fullName}]"
                }
                if (params) {
                    message += " and params [${params}]"
                }
                log.debug(message)
            }

            def path = mappingsProvider.getPath(uiClass)
            def fragment = mappingsProvider.getFragment(path, viewClass)

            def uri = "${path}/${fragment}"

            if (params) {
                uri += "/${encodeParams(params)}"
            }

            Page.current.setLocation(uri)
        }
    }

    /**
     * Enter a View within the current UI.
     *
     * @param viewClass The Vaadin View class
     * @param params Additional parameters
     */
    void enter(VaadinViewClass viewClass, Map params) {
        def utils = Vaadin.utils
        log.debug("Enter View with class [${viewClass?.fullName}] with params [${params}]")
        def path = mappingsProvider.getPath(utils.currentVaadinUIClass)
        def fragment = mappingsProvider.getFragment(path, viewClass)
        if (params) {
            UI.current.navigator.navigateTo("${fragment}/${encodeParams(params)}")
        } else {
            UI.current.navigator.navigateTo(fragment)
        }
    }
}
