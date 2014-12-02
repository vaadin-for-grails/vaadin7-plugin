grails-vaadin-core-plugin
=========================
Plugin for integrating Vaadin into Grails.

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
Create your View classes somewhere below ```grails-app/vaadin```. Like UIs, View class names must have a "View" postfix. Views can be mapped to URI fragments like follows.
```
mapping {
    
    "/app" {
        ui = "my"
        
        fragments {
            "someview" {
                view = "xyz" // Class: XyzView.groovy
            }
        }
    }
}
```

Your View is accessible via ```http://localhost:8080/<grails app>/app#!xyz```.



