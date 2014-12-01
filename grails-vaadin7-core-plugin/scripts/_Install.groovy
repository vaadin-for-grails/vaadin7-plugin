
// Generate the VaadinConfig.groovy
def config = new File("${basedir}/grails-app/conf/VaadinConfig.groovy")
if (!config.exists()) {
    def pathToTemplate = "${pluginBasedir}/src/templates/VaadinConfig.groovy"
    def pathToConfigScriptClass = "${basedir}/grails-app/conf/VaadinConfig.groovy"
    ant.copy(file: pathToTemplate, tofile: pathToConfigScriptClass)
}

// Create the vaadin directory if it doesn't exist
def vaadinDir = new File("${basedir}/grails-app/vaadin")
if (!vaadinDir.exists()) {
    ant.mkdir(dir: vaadinDir)
}