package demo1

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class IndexView extends CustomComponent implements View {

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        compositionRoot = new VerticalLayout()
        compositionRoot.setMargin(true)
        compositionRoot.setSpacing(true)
        def title = new Label("First View (Index)")
        title.setStyleName("h1")
        compositionRoot.addComponent(title)
        def button = new Button("Go to second view", (Button.ClickListener)new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent e) {
                Vaadin.enter(path: "/demo2", params: [a: 1])
            }
        })
        compositionRoot.addComponent(button)
    }
}
