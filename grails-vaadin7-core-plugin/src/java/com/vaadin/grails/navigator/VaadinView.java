package com.vaadin.grails.navigator;

import com.vaadin.grails.ui.VaadinComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@VaadinComponent
public @interface VaadinView {

}