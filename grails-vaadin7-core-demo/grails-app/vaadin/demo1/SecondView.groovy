package demo1

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class SecondView extends CustomComponent implements View {

    SecondView() {

    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        compositionRoot = new VerticalLayout()
        compositionRoot.setMargin(true)
        compositionRoot.setSpacing(true)
        def title = new Label("Second View")
        title.setStyleName("h1")
        compositionRoot.addComponent(title)
        compositionRoot.addComponent(new Button("Go back", (Button.ClickListener)new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent e) {
                Vaadin.enter(view: "index", namespace: "ns1")
            }
        }))
        compositionRoot.addComponent(new Button("Go to Demo 2", (Button.ClickListener)new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent e) {
                Vaadin.enter(ui: "demo", namespace: "ns2")
            }
        }))
    }
}
