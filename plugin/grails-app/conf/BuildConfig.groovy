grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
//    pom true
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
        mavenRepo "http://maven.apache.org"
        mavenRepo "http://oss.sonatype.org/content/repositories/vaadin-snapshots"
        mavenRepo "http://maven.vaadin.com/vaadin-addons"
    }

    vaadinVersion = "7.5.5"

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // runtime 'mysql:mysql-connector-java:5.1.27'

        compile "com.vaadin:vaadin-server:${vaadinVersion}"
        compile "com.vaadin:vaadin-client-compiled:${vaadinVersion}"
        compile "com.vaadin:vaadin-client:${vaadinVersion}"
        compile "com.vaadin:vaadin-client-compiler:${vaadinVersion}"
        compile "com.vaadin:vaadin-themes:${vaadinVersion}"
        compile "com.vaadin:vaadin-push:${vaadinVersion}"
        compile "javax.validation:validation-api:1.1.0.Final"
//        compile 'org.apache.maven.plugins:maven-gpg-plugin:1.6'
    }

    plugins {
        build(":release:3.0.1",
              ":rest-client-builder:1.0.3") {
            export = false
        }

    }
}

grails.project.repos.default = "sonatype"
grails.project.repos.sonatype.url = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
//grails.project.repos.sonatype.url = "https://oss.sonatype.org/content/repositories/snapshots"
grails.project.repos.sonatype.type = "maven"

// Add the following properties to ~/.grails/settings.groovy:

//grails.project.repos.sonatype.username
//grails.project.repos.sonatype.password
