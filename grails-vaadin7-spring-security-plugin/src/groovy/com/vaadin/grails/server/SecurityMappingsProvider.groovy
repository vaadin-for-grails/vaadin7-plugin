package com.vaadin.grails.server

import com.vaadin.grails.Vaadin
import org.apache.log4j.Logger

import javax.annotation.PostConstruct

/**
 * @author Stephan Grundner
 */
class SecurityMappingsProvider extends DefaultMappingsProvider {

    static final def log = Logger.getLogger(SecurityMappingsProvider)

    SecurityMappingsProvider() {

    }

    @PostConstruct
    @Override
    protected void init() {
        println "OK2"
        super.init()
    }

    @Override
    protected Mapping createMapping(String path, ConfigObject mappingConfig) {
        def mapping = new SecuredMapping()
        mapping.path = path
        String ui = mappingConfig.ui
        String namespace = mappingConfig.namespace ?: null
        mapping.theme = mappingConfig.get("theme")
        mapping.widgetset = mappingConfig.get("widgetset")
        mapping.preservedOnRefresh = mappingConfig.get("preservedOnRefresh")
        mapping.pageTitle = mappingConfig.get("pageTitle")
        mapping.pushMode = mappingConfig.get("pushMode")
        mapping.pushTransport = mappingConfig.get("pushTransport")
        mapping.uiClass = Vaadin.utils.getVaadinUIClass(ui, namespace)
        mapping.access = mappingConfig.get("access") ?: []

        log.debug("Mapping uri [${path}] to ui [${ui}]" + namespace ? " with namespace [${namespace}]" : "")
        mappingConfig.views.each { String fragment, ConfigObject viewMappingConfig ->
            String view = viewMappingConfig.view
            def viewClass = Vaadin.utils.getVaadinViewClass(view, namespace)
            mapping.addViewClass(fragment, viewClass)
            def access = viewMappingConfig.access ?: []
            mapping.addFragmentAccess(fragment, access as String[])

            log.debug("Mapping fragment [${fragment}] to view [${view}]" + namespace ? " with namespace [${namespace}]" : "")
        }
        mapping
    }
}
