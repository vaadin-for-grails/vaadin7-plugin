package org.vaadin.grails.data.util

import com.vaadin.data.Item
import com.vaadin.data.Property
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.springframework.validation.Errors
import org.vaadin.grails.util.GrailsUtils

/**
 * Domain Item.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class DomainItem<T> implements Item {

    final T object
    final Map<String, Property<?>> propertyById = new HashMap()

    DomainItem(T object) {
        this.object = object

        def domainClass = getDomainClass()
        if (domainClass == null) {
            throw new RuntimeException("No domain class")
        }

        domainClass.getPersistentProperties().each { property ->
            def id = property.name
            addItemProperty(id, new DomainItemProperty<T, Object>(this, id))
        }
    }

    DomainItem(Class<T> type, Object... args) {
        this(type.newInstance(args))
    }

    DomainItem(Class<T> type) {
        this(type.newInstance())
    }

    GrailsDomainClass getDomainClass() {
        GrailsUtils.getDomainClass(object.getClass())
    }

    @Override
    Property<?> getItemProperty(Object id) {
        propertyById.get(id)
    }

    @Override
    Collection<?> getItemPropertyIds() {
        propertyById.keySet()
    }

    @Override
    boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        propertyById.put(id, property)
    }

    boolean addItemProperty(Object id) throws UnsupportedOperationException {
        if (((String) id).contains('.')) {
            return addItemProperty(id, new NestedDomainItemProperty(this, id))
        }
        addItemProperty(id, new DomainItemProperty<T, Object>(this, id))
    }

    @Override
    boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        propertyById.remove(id)
    }

    Serializable getId() {
        object.invokeMethod("getId", null)
    }

    Errors getErrors() {
        object.invokeMethod("getErrors", null) as Errors
    }

    boolean validate() {
        object.invokeMethod("validate", null)
    }

    T save(boolean flush = false) {
        object.invokeMethod("save", [flush: flush])
    }

    void delete(boolean flush = false) {
        object.invokeMethod("delete", [flush: flush])
    }
}
