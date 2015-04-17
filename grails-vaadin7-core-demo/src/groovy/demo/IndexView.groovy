package demo

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.CustomComponent
import org.vaadin.grails.navigator.Navigation

class IndexView extends CustomComponent implements View {

    IndexView() {
        compositionRoot = Vaadin.build {
            gridLayout(sizeFull: true, rows: 2, columns: 3, margin: true, spacing: true) {

                panel(caption: "Working with UIs and Views") {
                    verticalLayout(margin: true, spacing: true) {
                        label(value: "Shows how to use UIs and Views")
                        label(styleName: "light", value: "See com.vaadin.grails.Vaadin#enter()")
                        button(caption: "Go to Demo", styleName: "primary", clickListener: {
                            println Navigation.current.params
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

                panel(caption: "Book Demo") {
                    verticalLayout(margin: true, spacing: true) {
                        label(value: "Books")
                        label(styleName: "light", value: "See com.vaadin.grails.server.UIAttributesHolder")
                        button(caption: "Go to Demo", styleName: "primary", clickListener: {
                            Vaadin.enter(path: "/demo4")
                        })
                    }
                }

            }
        }
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        println "params on enter: " + Navigation.current.params
    }
}
