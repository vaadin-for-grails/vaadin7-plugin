package demo1

import com.vaadin.grails.Vaadin
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Button
import com.vaadin.ui.UI

class DemoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        content = new Button("I am an UI only!", new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent event) {
                Vaadin.enter(ui: "/demo1", view: "two", params: [back: true])
                println "Ok"
            }
        })
    }
}
