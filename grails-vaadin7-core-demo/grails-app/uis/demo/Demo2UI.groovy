package demo

import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class Demo2UI extends UI {

    GrailsApplication grailsApplication

    @Autowired
    ApplicationContext applicationContext

    @Override
    protected void init(VaadinRequest request) {
        println "app: ${grailsApplication}"
        println "ctx: ${applicationContext}"
    }
}
