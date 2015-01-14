package com.vaadin.grails.spring

import com.vaadin.grails.ui.VaadinComponent
import grails.util.GrailsNameUtils
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry

class VaadinComponentBeanNameGenerator extends org.springframework.context.annotation.AnnotationBeanNameGenerator {

    String getBeanPackage(String beanClassName) {
        String beanPackage = null
        int i = beanClassName.lastIndexOf('.')
        if (i != -1) {
            beanPackage = beanClassName.substring(0, i+1)
        }
        beanPackage
    }

    @Override
    String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanName = null

        if (definition instanceof AnnotatedBeanDefinition &&
                definition.metadata.hasMetaAnnotation(VaadinComponent.name)) {

            def beanClassName = definition.beanClassName
            def beanPackage = getBeanPackage(beanClassName) ?: ""
            beanName = "${beanPackage}${GrailsNameUtils.getPropertyName(beanClassName)}"
        }

        if (beanName == null) {
            beanName = super.generateBeanName(definition, registry)
        }

        beanName
    }
}
