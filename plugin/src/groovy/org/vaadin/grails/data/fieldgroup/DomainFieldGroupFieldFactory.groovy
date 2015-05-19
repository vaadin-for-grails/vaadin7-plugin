package org.vaadin.grails.data.fieldgroup

import com.vaadin.data.fieldgroup.FieldGroupFieldFactory
import com.vaadin.ui.*
import groovy.transform.Memoized
import org.apache.commons.lang.StringUtils
import org.vaadin.grails.util.GrailsUtils

/**
 * Factory for creating fields bound to Grails domain classes.
 *
 * @author Stephan Grundner
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
    protected Class<?> getFieldTypeByWidget(String widgetName) {
        ['textfield'   : TextField,
        'textarea'    : TextArea,
        'richtextarea': RichTextArea,
        'datefield'   : DateField].get(widgetName)
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

        def domainClass = GrailsUtils.getDomainClass(type)
        def property = domainClass.getPropertyByName(propertyId)
        def constrainedProperty = GrailsUtils.getConstrainedProperty(type, propertyId)

        if (fieldType == Field) {

            def widget = constrainedProperty?.widget
            if (StringUtils.isNotEmpty(widget)) {
                fieldType = getFieldTypeByWidget(widget) ?: Field
            }

            if (fieldType != Field) {
                field = createField(property.type, fieldType)
            } else if (Enum.isAssignableFrom(property.type)) {
                field = createField(property.type, ComboBox)
            }

        } else {

            if (Slider.isAssignableFrom(fieldType)) {
                int min = (int) constrainedProperty?.min ?: Integer.MIN_VALUE
                int max = (int) constrainedProperty?.max ?: Integer.MAX_VALUE
                int resolution = constrainedProperty?.scale ?: 0
                field = fieldType.newInstance(min, max, resolution)
            }

        }

        field
    }
}