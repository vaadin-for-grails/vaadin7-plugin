package org.vaadin.grails.server

import com.vaadin.server.DeploymentConfiguration
import com.vaadin.server.ServiceException
import com.vaadin.server.SessionInitListener
import com.vaadin.server.VaadinServlet
import com.vaadin.server.VaadinServletService
import org.vaadin.grails.util.ApplicationContextUtils

import javax.servlet.ServletException

/**
 * Grails specific implementation of {@link VaadinServlet}.
 *
 * @author Stephan Grundner
 */
class GrailsAwareVaadinServlet extends VaadinServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized()

        def sessionInitListener = ApplicationContextUtils
                .getBeanOrInstance(SessionInitListener, GrailsAwareSessionInitListener)
        service.addSessionInitListener(sessionInitListener)
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        def servletService = new GrailsAwareVaadinServletService(this, deploymentConfiguration)
        servletService.init()
        servletService
    }
}
