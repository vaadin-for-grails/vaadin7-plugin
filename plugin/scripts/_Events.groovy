import grails.util.Environment
import grails.util.Holders
import org.codehaus.gant.GantState
import org.codehaus.groovy.grails.commons.GrailsApplication

ConfigObject loadConfig(GrailsApplication application) {
    def configScriptClass
    try {
        configScriptClass = application.classLoader.loadClass("VaadinConfig")
    } catch (ClassNotFoundException e) {
        return null
    }

    def parser = new ConfigSlurper(Environment.current.name)
    parser.parse(configScriptClass)
}

eventCreatePluginArchiveStart = { stagingDir ->

}

eventCreateWarStart = { name, stagingDir ->
    GantState.verbosity = GantState.VERBOSE
    ant.logger.setMessageOutputLevel(GantState.verbosity)

    def config = loadConfig(Holders.grailsApplication)?.vaadin

    def themes = new HashSet()
    config.mappings.each {
        def theme = it.value.get("theme")
        if (theme) {
            themes.add(theme)
        }
    }

    String themeRelease = config.get("themeRelease") ?: "7.3.3"
    if (Environment.current == Environment.PRODUCTION) {
        themes.each { theme ->
            try {
                String themeDir = "${basedir}/web-app/VAADIN/themes/$theme/"
                def source = new File("${themeDir}styles.scss")
                def target = new File("${themeDir}styles.css")
                ant.echo("Compiling Vaadin theme [${theme}]")
                if (source.exists()) {
                    def workDir = new File("${basedir}/target/sass-work")
                    if (workDir.exists()) {
                        ant.delete(dir: workDir)
                    }
                    ant.mkdir(dir: workDir)

                    String releaseFileName = "vaadin-all-${themeRelease}.zip"
                    def releaseFile = new File("${basedir}/target/${releaseFileName}")

                    if (!releaseFile.exists()) {
                        String releaseSource = "http://vaadin.com/download/release/7.3/$themeRelease/${releaseFileName}"
                        ant.get(src: releaseSource, dest: releaseFile.absolutePath)
                    }

                    String pathToWorkDir = workDir.absolutePath
                    ant.unzip(src: releaseFile.absolutePath, dest: pathToWorkDir)
                    ant.java(classpath: "$pathToWorkDir/*;$pathToWorkDir/lib/*", classname: "com.vaadin.sass.SassCompiler", fork: true) {
                        arg(value: source.absolutePath)
                        arg(value: target.absolutePath)
                    }

//                    ant.copy(file: target.absolutePath, toDir: "${stagingDir}/VAADIN/themes/$theme")
                    ant.delete(dir: workDir)
                } else {
                    ant.echo("Missing SASS source [$source]")
                }
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }

    ant.delete(dir: "${stagingDir}/WEB-INF/lib/", includes: "vaadin-client-7.*.*.jar", verbose: true)
    ant.delete(dir: "${stagingDir}/WEB-INF/lib/", includes: "vaadin-client-compiler-7.*.*.jar", verbose: true)
    ant.delete(dir: "${stagingDir}/WEB-INF/lib/", includes: "vaadin-theme-compiler-7.*.*.jar", verbose: true)
    ant.delete(dir: "${stagingDir}/VAADIN/gwt-unitCache", verbose: true)

    GantState.verbosity = GantState.WARNINGS_AND_ERRORS
    ant.logger.setMessageOutputLevel(GantState.verbosity)
}
