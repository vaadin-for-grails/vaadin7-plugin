package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.GrailsClass;

public interface NamespaceAwareVaadinClass extends GrailsClass {

    public String getNamespace();
}
