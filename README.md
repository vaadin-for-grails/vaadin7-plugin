Vaadin Core Plugin for Grails
=========================
Plugin for integrating Vaadin into Grails.

Manage your [Vaadin](https://vaadin.com) UI and View classes in one simple file.

![UIs and Views](https://github.com/vaadin-for-grails/organization/wiki/uis_and_views.png)

## Setup
Add the follwing line to your BuildConfig.groovy.

    repositories {
        mavenRepo "https://oss.sonatype.org/content/groups/public"
    }

    plugins {
        compile "com.github.vaadin-for-grails:vaadin-core:1.0-SNAPSHOT"
    }
    
## Usage
### Working with UIs
Create your [UI](https://vaadin.com/book/vaadin7/-/page/application.architecture.html) class.

    class MyUI extents UI {
    
        @Override
        protected void init(VaadinRequest vaadinRequest) { }
    }


Map your UI to an URI by adding the following lines to your `VaadinConfig.groovy`.

    mapping {
        
        "/app" {
            ui = MyUI
        }
    }

Your Vaadin application is accessible via `http://localhost:8080/<grails app>/app`.

### Working with Views
Create your [View](https://vaadin.com/book/-/page/advanced.navigator.html) class.

    class SimpleView extents CustomComponent implements View {

        @Override
        void enter(ViewChangeListener.ViewChangeEvent event) { }
    }


    mapping {
    
        "/app" {
            ui = MyUI
        
            fragments {
                "about" {
                    view = SimpleView
                }
            }
        }
    }

Your View is accessible via `http://localhost:8080/<grails app>/app#!about`.

### Switching between Views and UIs

Simply call `Vaadin.enter(fragment: "simple")` to switch the view in the current UI. If you'd like to jump into a different UI, call `Vaadin.enter(path: "/other", fragment: "list")`.

What about parameters? Append a map: `Vaadin.enter(fragment: "simple", params: [foo: "bar"])`.

## Embedding UIs in GSPs
Add the following line to your GSP

    <vaadin:embed path="/vaadin" />
    
The `embed` tag supports the following attributes:
* id: DOM element id
* ui: The name of the UI
* namespace: The namespace the UI is assigned to
* widgetset: The widgetset to be used
* theme: The name of the theme, such as one of the built-in themes (reindeer, runo, or chameleon) or a custom theme
* vaadinDir: Relative path to the VAADIN directory
* heartbeatInterval: The hearbeatInterval parameter defines the frequency of the keep-alive hearbeat for the UI in seconds
* debug: The parameter defines whether the debug window is enabled
