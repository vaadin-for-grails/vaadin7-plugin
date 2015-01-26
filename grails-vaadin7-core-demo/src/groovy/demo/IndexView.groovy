package demo

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.GridLayout

class IndexView extends CustomComponent implements View {

    IndexView() {
        GridLayout
        compositionRoot = Vaadin.build {
            gridLayout(sizeFull: true, rows: 2, columns: 3, margin: true, spacing: true) {

                panel(caption: "Working with UIs and Views") {
                    verticalLayout(margin: true, spacing: true) {
                        label(value: "Shows how to use UIs and Views")
                        label(styleName: "light", value: "See com.vaadin.grails.Vaadin#enter()")
                        button(caption: "Go to Demo", styleName: "primary", clickListener: {
                            Vaadin.enter(path: "/demo1")
                        })
                    }
                }

                panel(caption: "UI Attributes Demo") {
                    verticalLayout(margin: true, spacing: true) {
                        label(value: "Shows how UI scoped attributes are working")
                        label(styleName: "light", value: "See com.vaadin.grails.server.UIAttributesHolder")
                        button(caption: "Go to Demo", styleName: "primary", clickListener: {
                            Vaadin.enter(path: "/demo3")
                        })
                    }
                }

            }
        }
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) { }
}
