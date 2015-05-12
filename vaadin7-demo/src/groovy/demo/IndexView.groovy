package demo

import com.vaadin.annotations.DesignRoot
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.VaadinSession
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.vaadin.grails.navigator.Navigation
import org.vaadin.grails.stereotype.VaadinComponent
import org.vaadin.grails.ui.declarative.Design

@VaadinComponent
@Scope("ui")
@DesignRoot("/demo1IndexView")
class IndexView extends Panel implements View {

    Button button1
    Button button2
    Button button3

    Label lang

    @Autowired
    GrailsApplication grailsApplication

    IndexView() {
        Design.read(this)

        button1.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent event) {
                Navigation.navigateTo(path: "/demo1")
            }
        })

        button2.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent event) {
                Navigation.navigateTo(path: "/demo3")
            }
        })

        button3.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent event) {
                Navigation.navigateTo(path: "/demo4")
            }
        })

        lang.caption = "Locale"
        lang.value = VaadinSession.current.locale.toString()
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) { }
}
