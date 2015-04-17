package org.vaadin.grails.ui

import com.vaadin.ui.Table
import grails.util.GrailsNameUtils
import org.vaadin.grails.data.util.DomainItemContainer
import org.vaadin.grails.spring.ApplicationContextUtils

final class TableUtils {

    static void setColumnHeaders(Table table) {
        def container = table.containerDataSource
        if (container instanceof DomainItemContainer) {
            def typeName = GrailsNameUtils.getPropertyName(container.type)
            def columnHeaders = []
            table.visibleColumns.each { String propertyId ->
                if (!propertyId.contains('.')) {
                    def key = "$typeName.$propertyId"
                    columnHeaders.add ApplicationContextUtils.getMessage(key)
                }
            }
            table.columnHeaders = columnHeaders as String[]
        }
    }

    private TableUtils() { }
}
