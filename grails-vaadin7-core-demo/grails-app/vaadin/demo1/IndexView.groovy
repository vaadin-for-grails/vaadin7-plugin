package demo1

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import grails.plugin.springsecurity.SpringSecurityUtils

class IndexView extends CustomComponent implements View {

    static namespace = "ns1"

    IndexView() {

    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        compositionRoot = new Button("Goto Second view", new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent e) {

                Vaadin.enter(ui: "demo", view: "second", namespace: "ns2")
            }
        })

    }
}
