package demo1

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
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
        compositionRoot.addComponent(new Label("Second."))
    }
}
