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

eventPublishPluginStart = { pluginInfo ->
    println "##################################################"
    println "##################################################"
    println "######################  OK  ######################"
    println "##################################################"
    println "##################################################"
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
                    def workDir = new File("${basedir}/sass-work")
                    ant.mkdir(dir: workDir)

                    String releaseZipSource = "http://vaadin.com/download/release/7.3/$themeRelease/vaadin-all-${themeRelease}.zip"
                    String releaseZipTarget = "${workDir.absolutePath}/vaadin-all-${themeRelease}.zip"
                    ant.get(src: releaseZipSource, dest: releaseZipTarget)

                    ant.mkdir(dir: workDir)
                    String pathToWorkDir = workDir.absolutePath
                    ant.unzip(src: releaseZipTarget, dest: pathToWorkDir)
                    ant.delete(file: releaseZipTarget)

                    ant.java(classpath: "$pathToWorkDir/*;$pathToWorkDir/lib/*", classname: "com.vaadin.sass.SassCompiler", fork: true) {
                        arg(value: source.absolutePath)
                        arg(value: target.absolutePath)
                    }

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
