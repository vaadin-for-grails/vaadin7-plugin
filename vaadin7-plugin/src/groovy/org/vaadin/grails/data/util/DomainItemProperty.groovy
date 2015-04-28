package org.vaadin.grails.data.util

import com.vaadin.data.Property
import org.codehaus.groovy.grails.commons.GrailsDomainClass

/**
 * Domain Item Property.
 *
 * @author Stephan Grundner
 *
 * @since 1.0
 */
class DomainItemProperty<T, P> implements Property<P> {

    protected final DomainItem<T> item
    final String propertyId

    boolean readOnly = false

    DomainItemProperty(DomainItem<T> item, String propertyId) {
        this.item = item
        this.propertyId = propertyId
    }

    T getObject() {
        item.object
    }

    GrailsDomainClass getDomainClass() {
        item.domainClass
    }

    @Override
    P getValue() {
        def object = getObject()
        object[propertyId]
    }

    @Override
    void setValue(P value) throws Property.ReadOnlyException {
        def object = getObject()
        object[propertyId] = value
    }

    @Override
    Class<? extends P> getType() {
        def domainClass = getDomainClass()
        domainClass.getPropertyByName(propertyId)?.type
    }

    @Override
    boolean isReadOnly() {
        if (item.readOnly == false) {
            return false
        }
        readOnly
    }

    @Override
    void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly
    }
}
