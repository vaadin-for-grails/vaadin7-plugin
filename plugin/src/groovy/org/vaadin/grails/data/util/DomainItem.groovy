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
class DomainItem<T> implements Item, DomainObjectProvider<T> {

    protected T object
    final Map<String, Property<?>> propertyById = new HashMap()

    DomainItem(T object) {
        this.object = object
        init()
    }

    DomainItem(Class<T> type, Object... args) {
        object = type.newInstance(args)
        init()
    }

    public DomainItem(Class<T> type) {
        object = type.newInstance()
        init()
    }

    @Override
    T getObject() {
        object
    }

    protected void init() {
        def domainClass = getDomainClass()
        if (domainClass == null) {
            throw new RuntimeException("No domain class: ${object?.getClass()}")
        }

        for (def property : domainClass.getProperties()) {
            def id = property.name
            addItemProperty(id, new DomainItemProperty<T, Object>(this, id))
        }
    }

    GrailsDomainClass getDomainClass() {
        GrailsUtils.getDomainClass(object)
    }

    @Override
    Property<?> getItemProperty(Object id) {
        propertyById?.get(id)
    }

    @Override
    Collection<?> getItemPropertyIds() {
        propertyById.keySet()
    }

    @Override
    boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        propertyById.put(id?.toString(), property)
    }

    boolean addItemProperty(Object id) throws UnsupportedOperationException {
        if (((String) id).contains('.')) {
            return addItemProperty(id, new NestedDomainItemProperty(this, id?.toString()))
        }
        addItemProperty(id, new DomainItemProperty<T, Object>(this, id?.toString()))
    }

    @Override
    boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        propertyById.remove(id)
    }

    Serializable getId() {
        (Serializable) object.invokeMethod("getId", null)
    }

    Errors getErrors() {
        (Errors) object.invokeMethod("getErrors", null) as Errors
    }

    boolean validate() {
        (boolean) object.invokeMethod("validate", null)
    }

    T save(boolean flush = false) {
        (T) object.invokeMethod("save", [flush: flush])
    }

    void delete(boolean flush = false) {
        object.invokeMethod("delete", [flush: flush])
    }

    void refresh() {
        object.invokeMethod("refresh", null)
    }
}
