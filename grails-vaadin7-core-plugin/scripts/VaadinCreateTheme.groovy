import grails.util.BuildSettingsHolder

includeTargets << grailsScript("_GrailsInit")

target(vaadinCreateTheme: "The description of the script goes here!") {

    def pluginDir = BuildSettingsHolder.settings.pluginDirectories.find {
        it.name == "grails-vaadin7-core-plugin" || it.name.startsWith("vaadin-core")
    }

    def templatesDir = "${pluginDir}/src/templates"
    def themesDir = "${basedir}/web-app/VAADIN/themes"

    ant.mkdir(dir: themesDir)
    ant.copy(file: "${templatesDir}/styles.scss", tofile: "${themesDir}/styles.scss")
}

setDefaultTarget(vaadinCreateTheme)
