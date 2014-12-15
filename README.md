Vaadin Core Plugin for Grails
=========================
Plugin for integrating Vaadin into Grails.

The Plugin uses plain [Vaadin](https://vaadin.com) classes, but made simpler by following the coding by convention paradigm. UIs and Views for example are accessible via the [Grails Artefact API](https://grails.org/Developer+-+Artefact+API). 


## Setup
Add the follwing line to your BuildConfig.groovy.

    repositories {
        mavenRepo "https://oss.sonatype.org/content/groups/public"
    }

    plugins {
        compile "com.github.vaadin-for-grails:vaadin-core:2.1-SNAPSHOT"
    }
    
## Usage
### Working with UIs
Create your [UI](https://vaadin.com/book/vaadin7/-/page/application.architecture.html) class somewhere below `grails-app/vaadin`.

    class MyUI extents UI {
    
        @Override
        protected void init(VaadinRequest vaadinRequest) { }
    }


Map your UI to an URI by adding the following lines to your `VaadinConfig.groovy`.

    mapping {
        
        "/app" {
            ui = "my"
        }
    }

Your Vaadin application is accessible via `http://localhost:8080/<grails app>/app`.

### Working with Views
Create your [View](https://vaadin.com/book/-/page/advanced.navigator.html) classes somewhere below `grails-app/vaadin`.

    class SimpleView extents CustomComponent implements View {

        @Override
        void enter(ViewChangeListener.ViewChangeEvent event) { }
    }


    mapping {
    
        "/app" {
            ui = "my"
        
            fragments {
                "about" {
                    view = "simple"
                }
            }
        }
    }

Your View is accessible via `http://localhost:8080/<grails app>/app#!about`.

### Switching between Views and UIs

Simply call `Vaadin.enter(view: "simple")` to switch the view in the current UI. If you'd like to jump into a different UI, call `Vaadin.enter(ui: "users", view: "list")`.

What about Parameters? Append a map: `Vaadin.enter(view: "simple", params: [foo: "bar"])`.
