package org.vaadin.grails.data.util

import com.vaadin.data.Property
import com.vaadin.data.util.AbstractProperty
import groovy.transform.Memoized
import org.codehaus.groovy.grails.commons.GrailsDomainClass

/**
 * Domain Item Property.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
class DomainItemProperty<T, P> extends AbstractProperty<P> implements Property<P> {

    protected final DomainItem<T> item
    final String propertyId

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
        object[propertyId]
    }

    @Override
    void setValue(P value) throws Property.ReadOnlyException {
        object[propertyId] = value
        fireValueChange()
    }

    @Memoized
    public static Class<?> fromPrimitiveType(Class<?> type) {
        if (type.isPrimitive()) {
            if (type.equals(Boolean.TYPE)) {
                type = Boolean.class
            } else if (type.equals(Integer.TYPE)) {
                type = Integer.class
            } else if (type.equals(Float.TYPE)) {
                type = Float.class
            } else if (type.equals(Double.TYPE)) {
                type = Double.class
            } else if (type.equals(Byte.TYPE)) {
                type = Byte.class
            } else if (type.equals(Character.TYPE)) {
                type = Character.class
            } else if (type.equals(Short.TYPE)) {
                type = Short.class
            } else if (type.equals(Long.TYPE)) {
                type = Long.class
            }
        }
        type
    }

    @Override
    Class<? extends P> getType() {
        def domainClass = getDomainClass()
        def type = domainClass.getPropertyByName(propertyId)?.type
        fromPrimitiveType(type)
    }
}
