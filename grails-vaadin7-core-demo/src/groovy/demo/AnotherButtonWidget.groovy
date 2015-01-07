package demo

import com.vaadin.grails.ui.VaadinComponent
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent

import javax.annotation.PostConstruct

@VaadinComponent
class AnotherButtonWidget extends CustomComponent {

    @PostConstruct
    void init() {
        compositionRoot = new Button("Another Button!")
    }
}
