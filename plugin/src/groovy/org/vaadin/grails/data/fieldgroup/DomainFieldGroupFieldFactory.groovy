package org.vaadin.grails.data.fieldgroup

import com.vaadin.data.fieldgroup.FieldGroupFieldFactory
import com.vaadin.ui.DateField
import com.vaadin.ui.Field
import com.vaadin.ui.RichTextArea
import com.vaadin.ui.TextArea
import com.vaadin.ui.TextField
import groovy.transform.Memoized
import org.vaadin.grails.util.GrailsUtils

/**
 * @author Stephan Grundner
 *
 * @since 1.0
 */
class DomainFieldGroupFieldFactory implements FieldGroupFieldFactory {

    private final FieldGroupFieldFactory fieldFactory

    DomainFieldGroupFieldFactory(FieldGroupFieldFactory fieldFactory) {
        this.fieldFactory = fieldFactory
    }

    @Override
    final def <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
        fieldFactory.createField(dataType, fieldType)
    }

    @Memoized
    protected Class<?> getFieldTypeByWidgetName(String widgetName) {
        ['textfield'   : TextField,
        'textarea'    : TextArea,
        'richtextarea': RichTextArea,
        'datefield'   : DateField].get(widgetName)
    }

    protected def <T extends Field> T createFieldByWidget(Class<?> type, String propertyId) {
        def domainClass = GrailsUtils.getDomainClass(type)
        def property = domainClass.getPropertyByName(propertyId)

        T field = null

        def constraints = domainClass.getConstrainedProperties()?.get(property.name)
        if (constraints) {
            def widgetName = constraints['widget'] as String
            if (widgetName) {
                def requiredFieldType = getFieldTypeByWidgetName(widgetName)
                if (requiredFieldType) {
                    field = createField(property.type, requiredFieldType)
                }
            }
        }

        field
    }

    /**
     * Create a field for the specified domain class type and its property
     *
     * @param type The domain class type
     * @param propertyId A domain class property
     * @param fieldType
     * @return
     */
    public def <T extends Field> T createField(Class<?> type, String propertyId, Class<T> fieldType) {
        T field = null

        if (fieldType == Field) {
            field = createFieldByWidget(type, propertyId)
        }

        field
    }
}