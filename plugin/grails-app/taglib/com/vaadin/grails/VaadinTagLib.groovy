package com.vaadin.grails

import org.springframework.web.util.HtmlUtils
import org.vaadin.grails.server.UriMappings

/**
 * Vaadin GSP TagLib.
 *
 * @author Stephan Grundner
 */
class VaadinTagLib {

    static defaultEncodeAs = 'none'
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "vaadin"

    UriMappings uriMappings

    /**
     * Embedding a Vaadin UI.
     * <p>
     *     Example:
     *     <code>
     *         <vaadin:embed path="/vaadin" />
     *     </code>
     * </p>
     * @attr id DOM element id
     * @attr path The path mappted to an UI
     * @attr widgetset
     * @attr theme Name of the theme, such as one of the built-in themes (reindeer, runo, or chameleon) or a custom theme
     * @attr vaadinDir Relative path to the VAADIN directory
     * @attr heartbeatInterval The hearbeatInterval parameter defines the frequency of the keep-alive hearbeat for the UI in seconds
     * @attr debug The parameter defines whether the debug window is enabled
     */
    def embed = { attrs, body ->
        def id = attrs.remove("id") ?: UUID.randomUUID().toString()

        def path = attrs.remove("path") as String
        if (path == null) {
            throwTagError "Missing [path] attribute"
        }

        def widgetset = attrs.remove("widgetset") ?:
                uriMappings?.getPathProperty(path, 'widgetset') ?: "com.vaadin.DefaultWidgetSet"

        def theme = attrs.remove("theme") ?:
                uriMappings?.getPathProperty(path, 'theme') ?: 'valo'

        def vaadinDir = attrs.remove("vaadinDir") ?: "VAADIN"
        vaadinDir = createLink(uri: "/$vaadinDir")

        def heartbeatInterval = attrs.remove("heartbeatInterval") ?: 100
        def debug = attrs.remove("debug") ?: false
        def vaadinVersion = com.vaadin.shared.Version.fullVersion

        path = createLink(uri: path)

        attrs['class'] = 'v-app ' + (attrs['class'] ?: '')
        def attrsAsText = attrs.collect { "${it.key}='${HtmlUtils.htmlEscape(it.value)}'" }.join(' ')

        out << """
<script type='text/javascript' src='${"$vaadinDir/vaadinBootstrap.js"}'></script>
<iframe tabindex="-1" id="__gwt_historyFrame"
        style="position: absolute; width: 0; height: 0; border: 0; overflow: hidden"
        src="javascript:false">
</iframe>
<div id="$id" $attrsAsText>
     <div class=" v-app-loading"></div>
     <noscript>
         You have to enable javascript in your browser to use an application built with Vaadin.
     </noscript>
</div>
<script type="text/javascript">//<![CDATA[
    if (!window.vaadin) {
        alert("Failed to load the bootstrap JavaScript: ${vaadinDir}/vaadinBootstrap.js");
    } else {
        vaadin.initApplication("$id", {
            "browserDetailsUrl": "$path",
            "serviceUrl": "$path",
            "widgetset": "$widgetset",
            "theme": "$theme",
            "versionInfo": {"vaadinVersion": "$vaadinVersion"},
            "vaadinDir": "$vaadinDir/",
            "heartbeatInterval": $heartbeatInterval,
            "debug": $debug,
            "standalone": false,
            "authErrMsg": {
                "message": "Take note of any unsaved data, and <u>click here<\\/u> to continue.",
                "caption": "Authentication problem"
            },
            "comErrMsg": {
                "message": "Take note of any unsaved data, and <u>click here<\\/u> to continue.",
                "caption": "Communication problem"
            },
            "sessExpMsg": {
                "message": "Take note of any unsaved data, and <u>click here<\\/u> to continue.",
                "caption": "Session expired"
            }
        });
    }//]] >
</script>"""
    }
}
