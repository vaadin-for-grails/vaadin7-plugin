package org.vaadin.grails.data.util

import com.vaadin.data.Property
import groovy.transform.Memoized
import org.apache.commons.lang.ArrayUtils

/**
 * Nested Domain Item Property.
 *
 * @param < T > The item type
 * @param < P > The property type
 *
 * @author Stephan Grundner
 * @since 2.0
 */
class NestedDomainItemProperty<T, P> extends DomainItemProperty<T, P> {

    NestedDomainItemProperty(DomainItem<T> item, String propertyId) {
        super(item, propertyId)
    }

    @Memoized
    protected Object getNestedObject(T object, String propertyId) {
        def propertyNames = propertyId.split(/\./)
        def length = propertyNames.length - 1
        propertyNames = ArrayUtils.remove(propertyNames, length)

        def current = object
        propertyNames.each { String propertyName ->
            current = current[propertyName]
        }

        current
    }

    protected Object getNestedObject() {
        getNestedObject(item.object, propertyId)
    }

    @Memoized
    protected String getNestedPropertyId(String propertyId) {
        def i = propertyId.lastIndexOf('.')
        propertyId.substring(i + 1)
    }

    protected String getNestedPropertyId() {
        getNestedPropertyId(propertyId)
    }

    @Override
    P getValue() {
        nestedObject[nestedPropertyId]
    }

    @Override
    void setValue(P value) throws Property.ReadOnlyException {
        nestedObject[nestedPropertyId] = value
    }
}
