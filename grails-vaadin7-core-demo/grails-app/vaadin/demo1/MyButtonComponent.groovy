package demo1

import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent

import javax.annotation.PostConstruct

//@VaadinComponent
class MyButtonComponent extends CustomComponent {


    @PostConstruct
    void init() {
        compositionRoot = new Button("My Button!")
    }
}
