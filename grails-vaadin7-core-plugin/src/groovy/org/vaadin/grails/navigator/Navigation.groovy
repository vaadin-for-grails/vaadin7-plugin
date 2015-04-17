package org.vaadin.grails.navigator

import com.vaadin.server.VaadinSession
import org.vaadin.grails.server.UriMappings
import org.vaadin.grails.spring.ApplicationContextUtils

/**
 * @author Stephan Grundner
 *
 * @since 2.0
 */
abstract class Navigation {

    static Navigation getCurrent() {
        def session = VaadinSession.current
        def navigation = session.getAttribute(Navigation)
        if (navigation == null) {
            navigation = ApplicationContextUtils.instantiateType(Navigation)
            session.setAttribute(Navigation, navigation)
        }
        navigation
    }

    static void enter(Map args) {
        current.navigateTo(args)
    }

    abstract UriMappings getUriMappings()
    abstract String getPath()
    abstract Map getParams()

    abstract String toParamsString(Map params)
    abstract Map fromParamsString(String paramsString)

    abstract void navigateTo(Map args)
}
