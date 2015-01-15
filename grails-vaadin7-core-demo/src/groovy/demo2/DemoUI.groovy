package demo2

import com.vaadin.grails.Vaadin
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout

class DemoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        content = new VerticalLayout()
        content.setMargin(true)
        content.setSpacing(true)
        def title = new Label("Demo 2 (UI only)")
        title.setStyleName("h1")
        content.addComponent(title)
        content.addComponent(new Button("Go to Demo 1", (Button.ClickListener)new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent e) {
                Vaadin.enter(demo1.DemoUI)
            }
        }))
    }
}
