package demo

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent

class SimpleView extends CustomComponent implements View {

    SimpleView() {

    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        compositionRoot = new Button("Click", new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent ce) {
//                Navigation.enter(DemoUI, SimpleView)
                Vaadin.enter(DemoUI, SimpleView, [bla: Vaadin.i18n("OK")])
            }
        })

    }
}
