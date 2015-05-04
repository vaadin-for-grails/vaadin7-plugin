import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript("_GrailsInit")

target(vaadinCreateTheme: "The description of the script goes here!") {

    def pluginDir = vaadin7PluginDir
    def templateEngine = new SimpleTemplateEngine()

    List params = argsMap.params

    if (params.size() != 1) {
        errorMessage "Error creating theme: Single parameter required!"
        return 1
    }

    def themeName = params.first()

    def templatesDir = "${pluginDir}/src/templates"
    def vaadinDir = "${basedir}/web-app/VAADIN"
    def themesDir = "${vaadinDir}/themes"
    def themeDir = "${themesDir}/${themeName}"

    def targetFile = new File("$themeDir/styles.scss")
    if (!targetFile.exists()) {
        if (!new File(themeDir).exists()) {
            ant.mkdir(dir: themeDir)
        }

        def templateFile = new File("$templatesDir/styles.scss")
        def template = templateEngine.createTemplate(templateFile)

        targetFile.withWriter { writer ->
            template.make([themeName: themeName]).writeTo(writer)
        }
    } else {
        errorMessage "Error creating theme: File [${targetFile}] already exists!"
        return 1
    }

    printMessage "Successfully created theme [${themeName}]"
}

printMessage = { String message -> event('StatusUpdate', [message]) }
errorMessage = { String message -> event('StatusError', [message]) }

setDefaultTarget(vaadinCreateTheme)
