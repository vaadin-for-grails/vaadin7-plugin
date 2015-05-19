package org.vaadin.grails.data.util

/**
 * Provide a Grails domain object.
 *
 * @author Stephan Grundner
 * @since 2.2
 * @param < T > The Grails domain class
 */
interface DomainObjectProvider<T> {

    /**
     * Get a Grails domain object.
     *
     * @return A Grails domain object
     */
    T getObject()
}
