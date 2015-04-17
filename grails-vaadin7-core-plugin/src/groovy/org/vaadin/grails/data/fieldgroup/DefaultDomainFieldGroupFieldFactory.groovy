package org.vaadin.grails.data.fieldgroup

import com.vaadin.data.fieldgroup.FieldGroupFieldFactory
import com.vaadin.ui.Field
import com.vaadin.ui.TextArea
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsDomainClass

class DefaultDomainFieldGroupFieldFactory implements DomainFieldGroupFieldFactory {

    FieldGroupFieldFactory fallbackFieldFactory

    @Override
    def <T extends Field> T createField(Class<?> propertyType, Class<T> fieldType) {
        fallbackFieldFactory?.createField(propertyType, fieldType)
    }

    @Override
    def <T extends Field> T createField(Class<?> type, String propertyId, Class<T> fieldType) {
        T field = null

        GrailsDomainClass domainClass = Holders.grailsApplication.getDomainClass(type.name)
        def propertyConstraints = domainClass.getConstrainedProperties()?.get(propertyId)
        if (propertyConstraints) {
            def property = domainClass.getPropertyByName(propertyId)
            def widget = propertyConstraints['widget']
            switch (widget) {
                case 'textArea':
                    field = fallbackFieldFactory.createField(property.type, TextArea)
                    break
            }
        }

        field
    }
}
