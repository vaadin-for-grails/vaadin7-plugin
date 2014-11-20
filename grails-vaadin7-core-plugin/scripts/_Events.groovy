import grails.util.Environment
import org.codehaus.gant.GantState

eventCreateWarStart = { name, stagingDir ->
    GantState.verbosity = GantState.VERBOSE
    ant.logger.setMessageOutputLevel(GantState.verbosity)

//    def mappingsClass = classLoader.loadClass("VaadinMappings")
//    def mappingsClosure = GrailsClassUtils.getStaticPropertyValue(mappingsClass, "mappings")
//    def builder = new MappingsBuilder(mappingsClosure)
//    def mappings = builder.build().findAll { it.key.startsWith("/") && it.value["theme"] != null }
    def mappings = [:]

    String sassCompile = null//config.sassCompile
    if (!sassCompile) {
        sassCompile = '7.3.3'
    }

    if (Environment.current == Environment.PRODUCTION) {
        ant.echo('SASS compilation: Starting')
        mappings.each { entry ->
            def theme = entry["theme"]
            try {
                String path = "${basedir}/web-app/VAADIN/themes/$theme/"
                File sassSource = new File("${path}styles.scss")
                File sassDestination = new File("${path}styles.css")
                ant.echo("SASS compilation: Input file: $sassSource")
                ant.echo("SASS compilation: Output file: $sassDestination")
                if (!sassSource.exists()) {
                    ant.echo("Source file for SASS compilation do not exist: $sassSource")
                } else {
                    File temp = new File("${basedir}/vaadin-sass-temp")
                    File tempUnzip = new File("${basedir}/vaadin-sass-temp/unzipped")
                    ant.echo("SASS compilation: Temp file for Vaadin libraries: $temp")
                    ant.mkdir(dir: temp)
                    ant.echo("SASS compilation: Temp file for Vaadin libraries: $tempUnzip")
                    ant.mkdir(dir: tempUnzip)

                    String vaadinAllInSource = "http://vaadin.com/download/release/7.3/$sassCompile/vaadin-all-${sassCompile}.zip"
                    ant.echo("SASS compilation: $vaadinAllInSource")
                    String vaadinAllIn = temp.absolutePath + "/vaadin-all-${sassCompile}.zip"
                    ant.echo("SASS compilation: $vaadinAllIn")

                    ant.get(src: vaadinAllInSource, dest: vaadinAllIn)

                    String unzipped = tempUnzip.absolutePath
                    ant.unzip(src: vaadinAllIn, dest: unzipped)
                    ant.java(classpath: "$unzipped/*;$unzipped/lib/*", classname: 'com.vaadin.sass.SassCompiler', fork: true) {
                        arg(value: sassSource.absolutePath)
                        arg(value: sassDestination.absolutePath)
                    }
                    ant.delete(dir: temp)
                    ant.echo('SASS compilation: Success')
                }
            } catch (Exception e) {
                ant.echo("SASS compilation: Failed")
                e.printStackTrace()
            }
        }
    }

    boolean removeClientJar = true //config?.removeClientJar ?: true
    if (removeClientJar) {
        ant.delete(dir: "${stagingDir}/WEB-INF/lib/", includes: "vaadin-client-7.*.*.jar", verbose: true)
    }
    ant.delete(dir:"${stagingDir}/WEB-INF/lib/", includes: "vaadin-client-compiler-7.*.*.jar", verbose: true)
    ant.delete(dir:"${stagingDir}/WEB-INF/lib/", includes: "vaadin-theme-compiler-7.*.*.jar", verbose: true)
    ant.delete(dir:"${stagingDir}/VAADIN/gwt-unitCache", verbose: true)

    GantState.verbosity = GantState.WARNINGS_AND_ERRORS
    ant.logger.setMessageOutputLevel(GantState.verbosity)
}
