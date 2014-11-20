
def vaadinMappings = new File("${basedir}/grails-app/conf/VaadinMappings.groovy")
if (!vaadinMappings.exists()) {
    def pathToTemplate = "${pluginBasedir}/src/templates/VaadinMappings.groovy"
    def pathToVaadinMappingsClass = "${basedir}/grails-app/conf/VaadinMappings.groovy"
    ant.copy(file: pathToTemplate, tofile: pathToVaadinMappingsClass)
}