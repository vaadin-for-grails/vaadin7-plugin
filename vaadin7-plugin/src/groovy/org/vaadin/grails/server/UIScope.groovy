package org.vaadin.grails.server

import com.vaadin.server.ClientConnector
import com.vaadin.ui.UI
import org.apache.log4j.Logger
import org.springframework.beans.BeansException
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.config.Scope

import java.util.concurrent.ConcurrentHashMap

/**
 * A spring bean scope for binding beans to the current UI.
 *
 * Usage:
 * <code>
 * @Component
 * @Scope("ui")
 * public class MyComponent extends CustomComponent {
 *  ...
 * }
 * </code>
 *
 * @author Stephan Grundner
 * @since 2.0
 */
class UIScope implements Scope, BeanFactoryPostProcessor, ClientConnector.DetachListener {

    private static final log = Logger.getLogger(UIScope)

    final Map<Integer, Map<String, Object>> objectMapByUIId = new ConcurrentHashMap<Integer, Map<String, Object>>()
    final Map<Integer, Map<String, Object>> destructionCallbackMapByUIId = new ConcurrentHashMap<Integer, Map<String, Object>>()

    @Override
    Object get(String name, ObjectFactory<?> objectFactory) {
        log.debug("Get bean [$name] from scope")
        def uiId = UIIdHolder.getCurrent()
        if (uiId != null) {
            def objectMap = objectMapByUIId.get(uiId)
            if (objectMap == null) {
                objectMap = new HashMap<String, Object>()
                objectMapByUIId.put(uiId, objectMap)
                def ui = UI.getCurrent()
                ui.addDetachListener(this)
            }

            def object = objectMap.get(name)
            if (object == null) {
                object = objectFactory.getObject()
                objectMap.put(name, object)
            }

            return object
        }
        null
    }

    @Override
    Object remove(String name) {
        log.debug("Remove bean [$name] from scope")
        def uiId = UIIdHolder.getCurrent()
        if (uiId) {
            def objectMap = objectMapByUIId.get(uiId)
            return objectMap?.remove(name)
        }
        null
    }

    @Override
    void registerDestructionCallback(String name, Runnable callback) {
        def uiId = UIIdHolder.getCurrent()
        if (uiId) {
            log.debug("Register destruction callback for bean [$name] and UI Id [${uiId}]")
            def destructionCallbackMap = destructionCallbackMapByUIId.get(uiId)
            if (destructionCallbackMap == null) {
                destructionCallbackMap = new HashMap<String, Runnable>()
                destructionCallbackMapByUIId.put(uiId, destructionCallbackMap)
            }
            destructionCallbackMap.put(name, callback)
        }
    }

    @Override
    Object resolveContextualObject(String key) { null }

    @Override
    String getConversationId() {
        UIIdHolder.getCurrent()
    }

    @Override
    final void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerScope("ui", this)
    }

    @Override
    void detach(ClientConnector.DetachEvent event) {
        UI ui = event.source
        def uiId = ui?.getUIId()
        log.debug("Detach scope from UI [${uiId}]")
        try {
            Map destructionCallbackMap = destructionCallbackMapByUIId.get(uiId)
            if (destructionCallbackMap) {
                ui.accessSynchronously(new Runnable() {
                    @Override
                    void run() {
                        destructionCallbackMap.values().each { Runnable callback ->
                            callback.run()
                        }
                    }
                })
            }

            objectMapByUIId.remove(uiId)
            destructionCallbackMapByUIId.remove(uiId)

            ui.removeDetachListener(this)
        } catch (e) {
            log.error("Error detaching scope from UI ${uiId}", e)
            throw new RuntimeException(e)
        }
    }
}
