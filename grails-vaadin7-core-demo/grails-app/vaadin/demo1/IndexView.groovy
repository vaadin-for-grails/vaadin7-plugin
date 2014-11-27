package demo1

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.VerticalLayout
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.util.UrlPathHelper

class IndexView extends CustomComponent implements View {

    static namespace = "ns1"

    IndexView() {

    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        compositionRoot = new VerticalLayout()

        def button = new Button("Goto Second view", (Button.ClickListener)new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent e) {
                Vaadin.enter(view: [name: "second"], params: [a: 2])


            }
        })


        println Vaadin.utils.getVaadinUIClass(demo1.DemoUI)?.namespace

        compositionRoot.setMargin(true)
        compositionRoot.addComponent(button)
    }
}
