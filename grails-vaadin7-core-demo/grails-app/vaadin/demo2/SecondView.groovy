package demo2

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label

/**
 * @author Stephan Grundner
 */
class SecondView extends CustomComponent implements View {

    static namespace = "ns2"
    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        compositionRoot = new Label("Second!")
    }
}
