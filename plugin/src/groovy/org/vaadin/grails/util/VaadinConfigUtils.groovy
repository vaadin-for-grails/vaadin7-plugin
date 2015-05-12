package org.vaadin.grails.util

import com.vaadin.navigator.View
import com.vaadin.ui.UI
import grails.util.Environment
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.vaadin.grails.server.UriMappings
import org.vaadin.grails.ui.DefaultUI

/**
 * Ease access to Vaadin config.
 *
 * @author Stephan Grundner
 */
class VaadinConfigUtils {

    private static final log = Logger.getLogger(VaadinConfigUtils)

    static ConfigObject loadConfig(GrailsApplication application, String name) {
        def configScriptClass
        try {
            configScriptClass = application.classLoader.loadClass(name)
        } catch (ClassNotFoundException e) {
            return new ConfigObject()
        }

        def parser = new ConfigSlurper(Environment.current.name)
        parser.parse(configScriptClass)
    }

    static ConfigObject loadConfig(GrailsApplication application) {
        loadConfig(application, "VaadinConfig")
    }

    static void loadUriMappings(GrailsApplication application, UriMappings uriMappings) {
        uriMappings.clear()

        def mappingsConfig = application.config.vaadin.mappings
        mappingsConfig.each { String path, ConfigObject pathConfig ->

            def ui = pathConfig.get("ui")

            uriMappings.putPathProperty(path, UriMappings.DEFAULT_FRAGMENT_PATH_PROPERTY, pathConfig.get(UriMappings.DEFAULT_FRAGMENT_PATH_PROPERTY) ?: "index")
            uriMappings.putPathProperty(path, UriMappings.THEME_PATH_PROPERTY, pathConfig.get(UriMappings.THEME_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.WIDGETSET_PATH_PROPERTY, pathConfig.get(UriMappings.WIDGETSET_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.PRESERVED_ON_REFRESH_PATH_PROPERTY, pathConfig.get(UriMappings.PRESERVED_ON_REFRESH_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.PAGE_TITLE_PATH_PROPERTY, pathConfig.get(UriMappings.PAGE_TITLE_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.PUSH_MODE_PATH_PROPERTY, pathConfig.get(UriMappings.PUSH_MODE_PATH_PROPERTY))
            uriMappings.putPathProperty(path, UriMappings.PUSH_TRANSPORT_PATH_PROPERTY, pathConfig.get(UriMappings.PUSH_TRANSPORT_PATH_PROPERTY))

            Class<? extends UI> uiClass

            if (ui instanceof String) {
                def classLoader = application.classLoader
                uiClass = classLoader.loadClass(ui)
            } else {
                uiClass = ui ?: DefaultUI
            }

            if (uiClass == null) {
                throw new RuntimeException("No class found for [${path}]")
            }
            log.debug("Register UI [${uiClass.name}] for path [${path}]")
            uriMappings.setUIClass(path, uiClass)


            def fragments = pathConfig.fragments
            fragments.each { String fragment, ConfigObject fragmentConfig ->

                def view = fragmentConfig.get("view")

                Class<? extends View> viewClass

                if (view instanceof String) {
                    def classLoader = application.classLoader
                    viewClass = classLoader.loadClass(view)
                } else {
                    viewClass = view
                }

                if (viewClass == null) {
                    throw new RuntimeException("No class found for view [${view}]")
                }
                log.debug("Register View [${viewClass.name}] for path [${path}#!${fragment}]")
                uriMappings.setViewClass(path, fragment, viewClass)
            }
        }
    }
}
