package com.vaadin.grails.data.fieldgroup

import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.grails.*
import com.vaadin.grails.data.util.DomainItem
import com.vaadin.server.UserError
import com.vaadin.server.VaadinSession
import com.vaadin.ui.AbstractField

/**
 * @author Stephan Grundner
 */
class DomainFieldGroup<T> extends FieldGroup {

    final Class<T> type
    Locale locale

    public DomainFieldGroup(DomainItem<T> itemDataSource) {
        super(itemDataSource)
        type = itemDataSource.object.getClass()
    }

    public DomainFieldGroup(Class<T> type) {
        this.type = type
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

    private final Locale determineLocale() {
        locale ?: VaadinSession.current.locale ?: Locale.default
    }

    /**
     * Validate the domain object using GORM standard validation mechanism.
     *
     * @return True if the underlying domain object is valid, otherwise true.
     */
    boolean validate() {
        fields.each { field ->
            if (field instanceof AbstractField) {
                field.componentError = null
            }
        }

        def domainItem = getItemDataSource()
        if (!domainItem.validate()) {
            def errors = domainItem.errors
            errors.fieldErrors.each { fieldError ->
                def field = getField(fieldError.field)
                if (field instanceof AbstractField) {
                    def errorMessage = Vaadin.applicationContext.getMessage(fieldError, determineLocale())
                    field.componentError = new UserError(errorMessage)
                }
            }
            return false
        }

        true
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
}
