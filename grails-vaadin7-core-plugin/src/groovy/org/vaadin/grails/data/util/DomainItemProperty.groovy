package org.vaadin.grails.data.util

import com.vaadin.data.Property

/**
 * @author Stephan Grundner
 */
class DomainItemProperty<T, P> implements Property<P> {

    protected final DomainItem<T> item
    final String propertyId

    boolean readOnly = false

    DomainItemProperty(DomainItem<T> item, String propertyId) {
        this.item = item
        this.propertyId = propertyId
    }

    @Override
    P getValue() {
        def object = item.object
        object[propertyId]
    }

    @Override
    void setValue(P value) throws Property.ReadOnlyException {
        def object = item.object
        object[propertyId] = value
    }

    @Override
    Class<? extends P> getType() {
        def domainClass = item.domainClass
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
