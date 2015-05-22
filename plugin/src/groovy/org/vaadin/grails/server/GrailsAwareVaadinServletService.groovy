package org.vaadin.grails.server

import com.vaadin.server.*

class GrailsAwareVaadinServletService extends VaadinServletService {

    GrailsAwareVaadinServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        super(servlet, deploymentConfiguration)
    }

    @Override
    void requestStart(VaadinRequest request, VaadinResponse response) {
        super.requestStart(request, response)

    }

    @Override
    void requestEnd(VaadinRequest request, VaadinResponse response, VaadinSession session) {
        super.requestEnd(request, response, session)
    }
}
