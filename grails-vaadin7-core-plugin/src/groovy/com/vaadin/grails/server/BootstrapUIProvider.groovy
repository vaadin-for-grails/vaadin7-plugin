package com.vaadin.grails.server

import com.vaadin.grails.VaadinMappingsBuilder
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.ui.UI
import grails.util.Holders

class BootstrapUIProvider extends com.vaadin.server.UIProvider {

    private Map<String, Object> mappings

    BootstrapUIProvider() {
        buildMappings()
    }

    private void buildMappings() {
        def mappingsClass = Holders.grailsApplication.getArtefacts("VaadinMappings").first()
        def builder = new VaadinMappingsBuilder(mappingsClass)
        mappings = builder.build()
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        def path = event.request.pathInfo
        if (path == null) {
            path = "/"
        }
        mappings.get(path)?.ui
    }

}
