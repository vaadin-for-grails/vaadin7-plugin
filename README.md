# Vaadin 7 Plugin for Grails

_Build Vaadin Applications on top of Grails in record time!_

The [Vaadin 7 Plugin](https://github.com/vaadin-for-grails/vaadin7-plugin) integrates [Vaadin](https://vaadin.com) into [Grails](https://grails.org) in an easy and fully extensible way.

## Features

* Map UIs and Views in the VaadinConfig.groovy file
* Databinding using Grails Domain classes
* [Spring Framework](http://projects.spring.io/spring-framework/) support
* I18n support (using Properties files)
* Vaadin specific UI [Bean Scope](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-factory-scopes)
* Seamless Spring Security integration with the [Vaadin 7 Spring Security Plugin](https://github.com/vaadin-for-grails/vaadin7-spring-security-plugin)
* [ComponentBuilder](https://github.com/vaadin-for-grails/vaadin7-plugin/blob/master/plugin/src/groovy/org/vaadin/grails/ui/builders/ComponentBuilder.groovy) class for building UIs the Groovy way
* Use Grails Controllers, Services, etc. in the same project with no restrictions

## Installation
Simply add the following line to your ```BuildConfig.groovy```.
```groovy
compile "com.github.vaadin-for-grails:vaadin7:LATEST"
```

## Usage

### Mapping & Navigation
Simply register your UIs and Views in the `VaadinConfig.groovy` file and navigate between them using the [Navigation]() class.

```groovy
Navigation.navigateTo(fragment: "my-view", params: [foo: 'bar'])
```

#### Mapping UIs
Create a [UI](https://vaadin.com/book/vaadin7/-/page/application.architecture.html) class.
```groovy
class MyUI extents UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) { }
}
```

Map your UI to an URI by adding the following lines to your `VaadinConfig.groovy`.
```groovy
mapping {
    
    "/app" {
        ui = "MyUI"
    }
}
```

Your Vaadin application is accessible via `http://localhost:8080/<grails app>/app`.

There is a default implementation for [UI](https://vaadin.com/api/com/vaadin/ui/UI.html): `org.vaadin.ui.DefaultUI`. So instead of creating emty UI classes use that class for mapping.

#### Mapping Views
Create a [View](https://vaadin.com/book/-/page/advanced.navigator.html) class.
```groovy
class SimpleView extents CustomComponent implements View {

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) { }
}
```

Map your View to an URI by adding the following lines to your VaadinConfig.groovy.
 
```groovy
mapping {

    "/app" {
        ui = "MyUI"
    
        fragments {
            "about" {
                view = "MyAboutView"
            }
        }
    }
}
```

Your View is accessible via `http://localhost:8080/<grails app>/app#!about`.

