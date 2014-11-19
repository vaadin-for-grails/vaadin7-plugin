package demo

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label

class SimpleView extends CustomComponent implements View {

    SimpleView() {

    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        compositionRoot = new Label("OK")
    }
}
