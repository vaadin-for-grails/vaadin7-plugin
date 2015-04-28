package org.vaadin.grails

import com.vaadin.annotations.DesignRoot
import com.vaadin.ui.Component
import grails.gsp.PageRenderer
import grails.transaction.Transactional
import groovy.transform.Memoized
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.core.io.ResourceLocator
import org.codehaus.groovy.grails.plugins.GrailsPlugin
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import org.codehaus.groovy.grails.web.pages.discovery.GroovyPageScriptSource
import org.springframework.core.annotation.AnnotationUtils
import org.vaadin.grails.ui.util.ComponentUtils

@Transactional
class DesignService {

    static class DesignSource {
        GroovyPageScriptSource scriptSource
        GrailsPlugin plugin
        String view
    }

    PageRenderer groovyPageRenderer
    GrailsPluginManager pluginManager

    GroovyPagesTemplateEngine groovyPagesTemplateEngine
    ResourceLocator grailsResourceLocator

    InputStream getDesignAsStream(Map args) {
        def buffer = new ByteArrayOutputStream()
        groovyPageRenderer.renderTo(args, buffer)
        new ByteArrayInputStream(buffer.toByteArray())
    }

    @Memoized
    protected DesignSource lookupDesignSource(String view) {
        def pageLocator = groovyPageRenderer.groovyPageLocator
        def source = new DesignSource()
        def scriptSource = pageLocator.findViewByPath(view)
        if (scriptSource) {
            def plugin = pluginManager.allPlugins.find { GrailsPlugin plugin ->
                scriptSource.getURI().startsWith(plugin.pluginPath)
            }
            source.plugin = plugin
            source.scriptSource = scriptSource
            source.view = view
            return source
        }
        null
    }

    InputStream getDesignAsStream(Component rootComponent) {
        def rootComponentClass = AnnotationUtils.findAnnotationDeclaringClass(DesignRoot, rootComponent.class)
        if (rootComponentClass) {
            def annotation = rootComponentClass.getAnnotation(DesignRoot)
            def viewOrFileName = annotation.value()
            if (viewOrFileName.startsWith('/')) {
                def view = viewOrFileName
                def source = lookupDesignSource(view)
                if (source) {
                    def buffer = new ByteArrayOutputStream()
                    def locale = groovyPageRenderer.locale
                    try {
                        groovyPageRenderer.locale = ComponentUtils.getLocale(rootComponent)
                        def args = [view: source.view, plugin: source.plugin]
                        groovyPageRenderer.renderTo(args, buffer)
                    } finally {
                        groovyPageRenderer.locale = locale
                    }
                    return new ByteArrayInputStream(buffer.toByteArray())
                }
            } else {
                def fileName = viewOrFileName
                if (StringUtils.isEmpty(fileName)) {
                    fileName = rootComponentClass.simpleName + ".gsp"
                }

                if (fileName.endsWith('.gsp')) {
                    String uri
                    if (rootComponentClass.package) {
                        def path = rootComponentClass.package.name.replaceAll(/\./, '/')
                        uri = "classpath:${path}/${fileName}"
                    } else {
                        uri = "classpath:$fileName"
                    }

                    def resource = grailsResourceLocator.findResourceForURI(uri)
                    if (resource) {
                        def template = groovyPagesTemplateEngine.createTemplate(resource)
                        def writer = template.make().writeTo(new StringWriter())
                        return IOUtils.toInputStream(writer.toString())
                    }
                }
            }
        }
        null
    }
}
