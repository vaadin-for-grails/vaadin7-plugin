package demo1

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label

class IndexView extends CustomComponent implements View {

    static namespace = "ns1"

    IndexView() {

    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        compositionRoot = new Label("Yes!")
    }
}
