package org.vaadin.grails.data.fieldgroup

import com.vaadin.data.fieldgroup.FieldGroupFieldFactory
import com.vaadin.ui.Field

/**
 * @author Stephan Grundner
 */
public interface DomainFieldGroupFieldFactory extends FieldGroupFieldFactory {

    FieldGroupFieldFactory getFallbackFieldFactory()

    void setFallbackFieldFactory(FieldGroupFieldFactory fallbackFieldFactory)

    /**
     * @see {@link FieldGroupFieldFactory#createField(java.lang.Class, java.lang.Class)}
     */
    def <T extends Field> T createField(Class<?> propertyType, Class<T> fieldType)

    /**
     * Create a field for the specified domain class type and its property
     *
     * @param type The domain class type
     * @param propertyId A domain class property
     * @param fieldType
     * @return
     */
    def <T extends Field> T createField(Class<?> type, String propertyId, Class<T> fieldType);
}