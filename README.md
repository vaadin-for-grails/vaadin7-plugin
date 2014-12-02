grails-vaadin-core-plugin
=========================
Plugin for integrating Vaadin into Grails.

The Plugin uses plain Vaadin classes, but made simpler by following the coding by convention paradigm. UIs and Views for example are accessible via the Grails Artefact API. 

## Setup
Add the follwing line to your BuildConfig.groovy.
```
compile ":vaadin-core:2.1"
```
## Usage
### Working with UIs
Create your UI class somewhere below ```grails-app/vaadin```.
```
class MyUI extents UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) { }
}
```

Map your UI to an URI by adding the following lines to your ```VaadinConfig.groovy```.
```
mapping {
    
    "/app" {
        ui = "my"
    }
}
```
Your Vaadin application is accessible via ```http://localhost:8080/<grails app>/app```.

### Working with Views
Create your View classes somewhere below ```grails-app/vaadin```.
```
class SimpleView extents CustomComponent implements View {

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) { }
}
```

```
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
```

Your View is accessible via ```http://localhost:8080/<grails app>/app#!about```.

### Switching between Views and UIs

Simply call ```Vaadin.enter(view: "simple")``` to switch the view in the current UI. If you'd like to jump into a different UI, call ```Vaadin.enter(ui: "users", view: "list")```.

What about Parameters? Append a map: ```Vaadin.enter(view: "simple", params: [foo: "bar"])```.
