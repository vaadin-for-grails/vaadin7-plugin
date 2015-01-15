
// Generate the VaadinConfig.groovy
def config = new File("${basedir}/grails-app/conf/VaadinConfig.groovy")
if (!config.exists() && !basedir.endsWith("plugin")) {
    def pathToTemplate = "${pluginBasedir}/src/templates/VaadinConfig.groovy"
    def pathToConfigScriptClass = "${basedir}/grails-app/conf/VaadinConfig.groovy"
    ant.copy(file: pathToTemplate, tofile: pathToConfigScriptClass)
}