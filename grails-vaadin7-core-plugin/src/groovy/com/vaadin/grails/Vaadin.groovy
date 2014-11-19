package com.vaadin.grails

import com.vaadin.navigator.View
import com.vaadin.server.Page
import com.vaadin.ui.UI
import grails.util.Holders
import org.codehaus.groovy.grails.web.util.WebUtils

final class Vaadin {

    /**
     * Encode a map to a String.
     *
     * @param params A parameter map
     * @return A string representation of a map
     */
    static String encodeParams(Map params) {
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
    static Map decodeParams(String encoded) {
        WebUtils.fromQueryString(encoded.replace("/", "&"))
    }

    static void enter(Class<? extends UI> uiClass, Class<? extends View> viewClass, Map params) {
        def applicationContext = Holders.applicationContext
        def provider = applicationContext.getBean(MappingsProvider)

        def location = "" //"${ui.path()}#!${view.path()}"

        if (params) {
            location += "/${encodeParams(params)}"
        }

        Page.current.setLocation(location)
    }

    private Vaadin() { }
}
