package com.vaadin.grails

import com.vaadin.grails.server.UriMappingsHolder

/**
 * Vaadin GSP TagLib.
 *
 * @author Stephan Grundner
 */
class VaadinTagLib {

    static defaultEncodeAs = 'none'
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "vaadin"

    UriMappingsHolder uriMappingsHolder

    /**
     * Embedding a Vaadin UI.
     * <p>
     *     Example:
     *     <code>
     *         <vaadin:embed ui="default" />
     *     </code>
     * </p>
     * @attr id DOM element id
     * @attr ui The name of the UI
     * @attr namespace The namespace the UI is assigned to
     * @attr widgetset
     * @attr theme Name of the theme, such as one of the built-in themes (reindeer, runo, or chameleon) or a custom theme
     * @attr vaadinDir Relative path to the VAADIN directory
     * @attr heartbeatInterval The hearbeatInterval parameter defines the frequency of the keep-alive hearbeat for the UI in seconds
     * @attr debug The parameter defines whether the debug window is enabled
     */
    def embed = { attrs, body ->
        def id = attrs.remove("id") ?: UUID.randomUUID().toString()
        def style = attrs.remove("style") ?: ""

        def ui = attrs.remove("ui")
        if (ui == null) {
            throwTagError "Missing [ui] attribute"
        }
        def namespace = attrs.remove("namespace") ?: null
        def widgetset = attrs.remove("widgetset") ?: "com.vaadin.DefaultWidgetSet"
        def theme = attrs.remove("theme") ?: "valo"
        def vaadinDir = attrs.remove("vaadinDir") ?: "VAADIN"
        def heartbeatInterval = attrs.remove("heartbeatInterval") ?: 100
        def debug = attrs.remove("debug") ?: false

        def vaadinVersion = com.vaadin.shared.Version.fullVersion

        def uiClass = Vaadin.utils.getVaadinUIClass(ui, namespace)
        if (uiClass == null) {
            throwTagError "No ui found for name [${ui}]" + (namespace != null ? " and namespace [${namespace}]" : "")
        }
        def uiPath = uriMappingsHolder.getPath(uiClass)

        if (uiPath.startsWith("/")) {
            uiPath = uiPath.substring(1)
        }

        out << """
<script type='text/javascript' src='./${vaadinDir}/vaadinBootstrap.js'></script>
<iframe tabindex="-1" id="__gwt_historyFrame"
        style="position: absolute; width: 0; height: 0; border: 0; overflow: hidden"
        src="javascript:false">
</iframe>
<div style="$style" id="$id" class="v-app">
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
            "browserDetailsUrl": "$uiPath",
            "serviceUrl": "$uiPath",
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
