package org.vaadin.grails.ui.declarative

import com.vaadin.ui.Component
import com.vaadin.ui.declarative.Design as DefaultDesign
import com.vaadin.ui.declarative.Design.ComponentFactory
import com.vaadin.ui.declarative.DesignContext
import grails.util.Holders
import org.vaadin.grails.DesignService
import org.vaadin.grails.util.ApplicationContextUtils

/**
 * A facade for {@link com.vaadin.ui.declarative.Design}.
 *
 * @author Stephan Grundner
 *
 * @see {@link DesignService}
 * @since 2.0
 */
class Design {

    private static final getCurrent() {
        ApplicationContextUtils.getSingletonBean(Design)
    }

    protected static DesignService getDesignService() {
        Holders.applicationContext.getBean(DesignService)
    }

    static final ComponentFactory getComponentFactory() {
        DefaultDesign.getComponentFactory()
    }

    static void setComponentFactory(ComponentFactory componentFactory) {
        DefaultDesign.setComponentFactory(componentFactory)
    }

    static final Component read(Map args) {
        current.readDesign(args)
    }

    static final DesignContext read(Component rootComponent) {
        current.readDesign(rootComponent)
    }

    static final Component read(InputStream design) {
        current.readDesign(design)
    }

    static final DesignContext read(InputStream design, Component rootComponent) {
        current.readDesign(design, rootComponent)
    }

    /**
     * @see {@link #read(java.util.Map)}
     * @param args
     * @return
     */
    protected Component readDesign(Map args) {
        DefaultDesign.read(designService.getDesignAsStream(args))
    }

    /**
     * @see {@link #read(com.vaadin.ui.Component)}
     * @param rootComponent
     * @return
     */
    protected Object readDesign(Component rootComponent) {
        def design = designService.getDesignAsStream(rootComponent)
        if (design) {
            return DefaultDesign.read(design, rootComponent)
        }
        DefaultDesign.read(rootComponent)
    }

    /**
     * @see {@link #read(java.io.InputStream)}
     * @param design
     * @return
     */
    protected Component readDesign(InputStream design) {
        DefaultDesign.read(design)
    }

    /**
     * @see {@link #read(java.io.InputStream, com.vaadin.ui.Component)}
     * @param design
     * @param rootComponent
     * @return
     */
    protected Object readDesign(InputStream design, Component rootComponent) {
        DefaultDesign.read(design, rootComponent)
    }
}
