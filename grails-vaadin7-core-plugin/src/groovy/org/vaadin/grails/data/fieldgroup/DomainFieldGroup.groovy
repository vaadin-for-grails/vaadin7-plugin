package org.vaadin.grails.data.fieldgroup

import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.grails.data.util.DomainItem
import com.vaadin.ui.AbstractField
import com.vaadin.ui.Field
import org.vaadin.grails.spring.ApplicationContextUtils
import org.vaadin.grails.util.DomainClassUtils

/**
 * @author Stephan Grundner
 */
class DomainFieldGroup<T> extends FieldGroup {

    final Class<T> type
    private ValidationHandler validationHandler

    public DomainFieldGroup(DomainItem<T> itemDataSource) {
        super(itemDataSource)
        type = itemDataSource.object.getClass()
        initFieldFactory()
    }

    public DomainFieldGroup(Class<T> type) {
        this.type = type
        initFieldFactory()
    }

    private void initFieldFactory() {
        def fallbackFieldFactory = fieldFactory
        def fieldFactory = ApplicationContextUtils
                .instantiateType(DomainFieldGroupFieldFactory, DefaultDomainFieldGroupFieldFactory)
        fieldFactory.fallbackFieldFactory = fallbackFieldFactory
        this.fieldFactory = fieldFactory
    }

    @Override
    DomainItem<T> getItemDataSource() {
        super.getItemDataSource()
    }

    @Override
    protected Class<?> getPropertyType(Object propertyId) throws BindException {
        if (itemDataSource) {
            return super.getPropertyType(propertyId)
        }
        def domainClass = DomainItem.getDomainClass(type)
        domainClass.getPropertyByName(propertyId)?.type
    }

    @Override
    void setItemDataSource(Item itemDataSource) {
        if (itemDataSource instanceof DomainItem) {
            super.setItemDataSource(itemDataSource)
        } else {
            throw new IllegalArgumentException()
        }
    }

    ValidationHandler getValidationHandler() {
        if (validationHandler == null) {
            validationHandler = ApplicationContextUtils
                    .instantiateType(ValidationHandler, DefaultValidationHandler)
        }
        validationHandler
    }

    void setValidationHandler(ValidationHandler validationHandler) {
        this.validationHandler = validationHandler
    }

    boolean validate() {
        def handler = getValidationHandler()
        if (handler.beforeValidate(this)) {
            def domainItem = getItemDataSource()
            def valid = domainItem.validate()
            handler.afterValidate(this, domainItem.errors)
            return valid
        }
        false
    }

    @Override
    boolean isValid() {
        super.isValid() && validate()
    }

    @Override
    void discard() {
        fields.each { field ->
            if (field instanceof AbstractField) {
                field.componentError = null
            }
        }
        super.discard()
    }

    @Override
    void commit() throws FieldGroup.CommitException {
        super.commit()
    }

    @Override
    void buildAndBindMemberFields(Object objectWithMemberFields) throws BindException {
        throw new UnsupportedOperationException()
    }

    @Override
    protected void buildAndBindMemberFields(Object objectWithMemberFields, boolean buildFields) throws BindException {
        throw new UnsupportedOperationException()
    }

    @Override
    Field<?> buildAndBind(Object propertyId) throws BindException {
        String caption = DomainClassUtils.getCaption(type, propertyId, null)
        buildAndBind(caption, propertyId)
    }

    @Override
    Field<?> buildAndBind(String caption, Object propertyId) throws BindException {
        buildAndBind(caption, propertyId, Field)
    }

    @Override
    def <T extends Field> T buildAndBind(String caption, Object propertyId, Class<T> fieldType) throws BindException {
        T field = build(caption, propertyId, fieldType)
        bind(field, propertyId)
        field
    }

    @Override
    protected <T extends Field> T build(String caption, Class<?> dataType, Class<T> fieldType) throws BindException {
        throw new UnsupportedOperationException()
    }

    protected <T extends Field> T build(String caption, String propertyId, Class<T> fieldType) throws BindException {
        T field

        def fieldFactory = getFieldFactory()
        if (fieldFactory instanceof DomainFieldGroupFieldFactory) {
            field = fieldFactory.createField(type, propertyId, fieldType)
        }

        if (field == null) {
            def propertyType = getPropertyType(propertyId)
            field = fieldFactory.createField(propertyType, fieldType)
        }

        field?.setCaption(caption)

        field
    }
}
