package com.vaadin.grails.server

import com.vaadin.server.DefaultUIProvider
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.ui.UI
import com.vaadin.grails.ui.VaadinUI
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

class AnnotatedUIProvider extends com.vaadin.server.UIProvider {

    private static final Logger log = Logger.getLogger(DefaultUIProvider)

    final def typesByPaths = new ConcurrentHashMap<String, Class<? extends UI>>()

    @Autowired
    ApplicationContext applicationContext

    @PostConstruct
    void init() {
        def beanNames = applicationContext.getBeanNamesForAnnotation(VaadinUI)
        beanNames.each { beanName ->
            def registered = applicationContext.findAnnotationOnBean(beanName, VaadinUI)
            def type = applicationContext.getType(beanName)
            def path = registered.path()
            typesByPaths.put(path, type)
            log.debug("Register UI of type [${type}] with path [${path}]")
        }
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        String path = event.request.pathInfo

        typesByPaths.get(path)
    }

    @Override
    UI createInstance(UICreateEvent event) {
        def type = event.getUIClass()
        log.debug("Create UI of type [${type}]")
        applicationContext.getBean(type)
    }
}
