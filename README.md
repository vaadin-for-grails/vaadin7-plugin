grails-vaadin-core-plugin
=========================
Plugin for integrating Vaadin into Grails.

## Setup
Add the follwing line to your BuildConfig.groovy
```
compile ":vaadin-core:2.1"
```

## Example
Create your UI class somewhere below ```grails-app/vaadin```
```
class MyUI extents UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) { }
}
```
