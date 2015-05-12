package org.vaadin.grails.data.fieldgroup

import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.ui.AbstractField
import com.vaadin.ui.Field
import org.vaadin.grails.data.util.DomainItem
import org.vaadin.grails.util.ApplicationContextUtils
import org.vaadin.grails.util.GrailsUtils

/**
 * @author Stephan Grundner
 *
 * @since 1.0
 */
class DomainFieldGroup<T> extends FieldGroup {

    final Class<T> type

//    @since 2.0
    private DomainFieldGroupValidator validator

    public DomainFieldGroup(DomainItem<T> itemDataSource) {
        super(itemDataSource)
        type = itemDataSource.object.getClass()
        initFieldFactory()
    }

    public DomainFieldGroup(Class<T> type) {
        this.type = type
        initFieldFactory()
    }

    protected void initFieldFactory() {
        def fallbackFieldFactory = fieldFactory
        def fieldFactory = ApplicationContextUtils.getBeanOrInstance(
                DomainFieldGroupFieldFactory,
                fallbackFieldFactory)

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
        def domainClass = GrailsUtils.getDomainClass(type)
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

    DomainFieldGroupValidator getValidator() {
        if (validator == null) {
            validator = ApplicationContextUtils.getBeanOrInstance(DomainFieldGroupValidator)
        }
        validator
    }

    void setValidator(DomainFieldGroupValidator validator) {
        this.validator = validator
    }

    boolean validate() {
        def validator = getValidator()
        if (validator) {
            return validator.validate(this)
        }
        itemDataSource.validate()
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
    void buildAndBindMemberFields(Object objectWithMemberFields) throws BindException {
        throw new UnsupportedOperationException()
    }

    @Override
    protected void buildAndBindMemberFields(Object objectWithMemberFields, boolean buildFields) throws BindException {
        throw new UnsupportedOperationException()
    }

    @Override
    Field<?> buildAndBind(Object propertyId) throws BindException {
        String caption = GrailsUtils.getCaption(type, propertyId)
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

    protected <T extends Field> T build(String caption, String propertyId, Class<T> fieldType) throws BindException {
        def fieldFactory = getFieldFactory()
        if (fieldFactory instanceof DomainFieldGroupFieldFactory) {
            T field = fieldFactory.createField(type, propertyId, fieldType)
            if (field) {
                field.setCaption(caption)
                return field
            }
        }

        def domainClass = GrailsUtils.getDomainClass(type)
        def property = domainClass.getPropertyByName(propertyId)
        return super.build(caption, property.type, fieldType)
    }

    @Override
    protected <T extends Field> T build(String caption, Class<?> dataType, Class<T> fieldType) throws BindException {
        throw new UnsupportedOperationException()
    }
}
