import org.codehaus.gant.GantState

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsClasspath")
includeTargets << grailsScript("_GrailsRun")

target(main: "Compile a GWT WidgetSet") {
    depends(classpath, compile)
    GantState.verbosity = GantState.NORMAL
    ant.logger.setMessageOutputLevel(GantState.verbosity)

    def params = ((String) args).tokenize()

    if (params.size() == 0) {
        ant.echo(message: "No widgetset specified!")
        return
    }

    def widgetset = params.first()

    ant.java(classname: "com.google.gwt.dev.Compiler",
            maxmemory: "512m",
            failonerror: true,
            fork: true,
            classpathref: "grails.compile.classpath"
    ) {
        ant.classpath {
            pathelement location: "${basedir}/src/java"
            pathelement location: "${basedir}/target/classes"
        }
        arg(value: "-logLevel")
        arg(value: "INFO")
        arg(value: "-localWorkers")
        arg(value: "3")
        arg(value: "-war")
        arg(value: "web-app/VAADIN/widgetsets")
        arg(value: widgetset)
        jvmarg(value: "-Xss1024k")
        jvmarg(value: "-Djava.awt.headless=true")
    }
}

setDefaultTarget(main)
