package org.vaadin.grails.navigator

import com.vaadin.server.Page
import com.vaadin.ui.UI
import grails.util.Holders
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.util.UrlPathHelper
import org.vaadin.grails.server.UriMappings

import javax.servlet.http.HttpServletRequest

class DefaultNavigation extends Navigation {

    UriMappings getUriMappings() {
        def applicationContext = Holders.applicationContext
        applicationContext.getBean(UriMappings)
    }

    protected HttpServletRequest getCurrentRequest() {
        GrailsWebRequest.lookup().nativeRequest
    }

    String getPath() {
        URL url

        def page = Page.current
        if (page == null) {
            url = new URL(currentRequest.parameterMap["v-loc"])
        } else {
            url = page.location.toURL()
        }

        def path = url.path
        def contextPath = currentRequest.contextPath
        if (contextPath.length() > 0) {
            path = path.substring(contextPath.length(), path.length())
        }

        path
    }

    protected int indexOfParameters(String fragment) {
        int i = fragment?.indexOf("=") ?: -1
        if (i != -1) {
            return fragment.lastIndexOf("/", i)
        }
        i
    }

    Map getParams() {
        def page = Page.current
        def fragment = page.location.fragment

        int i = indexOfParameters(fragment)
        if (i != -1) {
            def paramsString = fragment.substring(i)
            return fromParamsString(paramsString)
        }

        Collections.EMPTY_MAP
    }

    String toParamsString(Map params) {
        def encoded = WebUtils.toQueryString(params)
        encoded = encoded.replace("&", "/")
        if (encoded.startsWith("?")) {
            encoded = encoded.substring(1, encoded.length())
        }
        encoded
    }

    Map fromParamsString(String paramsString) {
        WebUtils.fromQueryString(paramsString.replace("/", "&"))
    }

    void navigateTo(Map args) {
        def targetPath = args.remove('path')
        def targetFragment = args.remove('fragment')
        def params = args.remove('params')

        if (targetPath && !targetPath.equals(path)) {
            def helper = new UrlPathHelper()
            def contextPath = helper.getContextPath(currentRequest)
            if (contextPath.endsWith("/")) {
                contextPath.substring(0, contextPath.length() - 1)
            }

            def uri = contextPath + targetPath

            if (targetFragment) {
                uri += "#!${targetFragment}"
            }

            if (params) {
                if (targetFragment == null) {
                    uri += "#!"
                }
                uri += "/${toParamsString(params)}"
            }

            Page.current.setLocation(uri)
        } else {
            if (params) {
                UI.current.navigator.navigateTo("$targetFragment/${toParamsString(params)}")
            } else {
                UI.current.navigator.navigateTo(targetFragment ?: '')
            }
        }
    }
}
