import org.vaadin.grails.ui.builders.ComponentTreeHandler

class BootStrap {

    def init = { servletContext ->

        println ComponentTreeHandler.nodeHandlers

//        ComponentTreeHandler.
//            addNodeHandler(new BreadcrumbNodeHandler())
//
//        ComponentTreeHandler.
//                addNodeHandler(new BreadcrumbNodeHandler())

        println ComponentTreeHandler.nodeHandlers
    }

    def destroy = {

    }
}
