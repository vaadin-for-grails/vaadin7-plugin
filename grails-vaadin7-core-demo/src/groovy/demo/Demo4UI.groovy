package demo

import com.vaadin.grails.server.GrailsResource
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.BrowserFrame
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout

@VaadinUI
class Demo4UI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        def browserFrame = new BrowserFrame()
        browserFrame.source = new GrailsResource(controller: "some")

        content = new VerticalLayout()
        content.addComponent(new Label(value: "Demo 4 (Embedded GSP)", styleName: "h1"))
        content.addComponent(browserFrame)
    }
}
