package org.vaadin.grails.data.fieldgroup

import com.vaadin.data.fieldgroup.FieldGroupFieldFactory
import com.vaadin.ui.*
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsDomainClass

class DefaultDomainFieldGroupFieldFactory implements DomainFieldGroupFieldFactory {

    static final FIELD_TYPE_BY_NAME = [
            'textfield': TextField,
            'textarea': TextArea,
            'richtextarea': RichTextArea,
            'datefield': DateField,
    ]

    FieldGroupFieldFactory fallbackFieldFactory

    @Override
    def <T extends Field> T createField(Class<?> propertyType, Class<T> fieldType) {
        fallbackFieldFactory?.createField(propertyType, fieldType)
    }

    @Override
    def <T extends Field> T createField(Class<?> type, String propertyId, Class<T> fieldType) {
        def applicationContext = Holders.grailsApplication
        GrailsDomainClass domainClass = applicationContext.getDomainClass(type.name)
        def property = domainClass.getPropertyByName(propertyId)

        T field = null

        if (fieldType == Field) {
            def constraints = domainClass.getConstrainedProperties()?.get(property.name)
            if (constraints) {
                def fieldName = constraints['widget'] as String
                if (fieldName) {
                    def requiredFieldType = FIELD_TYPE_BY_NAME[fieldName.toLowerCase()]
                    if (requiredFieldType) {
                        field = createField(property.type, requiredFieldType)
                    }
                }
            }
        }

        if (field == null) {
            field = createField(property.type, fieldType)
        }

        field
    }
}
