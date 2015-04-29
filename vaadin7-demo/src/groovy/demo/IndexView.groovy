package demo

import com.vaadin.annotations.DesignRoot
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.ui.Button
import com.vaadin.ui.Panel
import org.vaadin.grails.navigator.Navigation
import org.vaadin.grails.navigator.NavigationResource
import org.vaadin.grails.ui.BreadcrumbTrail
import org.vaadin.grails.ui.declarative.Design

@DesignRoot("/demo1IndexView")
class IndexView extends Panel implements View {

    static final breadcrumb = new BreadcrumbTrail.Breadcrumb(icon: FontAwesome.HOME, caption: "Demos", resource: new NavigationResource(path: "/"))

    BreadcrumbTrail breadcrumbTrail

    Button button1
    Button button2
    Button button3
//    Button button4

    IndexView() {
        Design.read(this)

        breadcrumbTrail.addBreadcrumb(this.breadcrumb)

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
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) { }
}
