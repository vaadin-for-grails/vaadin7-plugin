package demo3

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.CustomComponent
import org.vaadin.grails.ui.builders.ComponentBuilder
import org.vaadin.grails.util.ApplicationContextUtils

class IndexView extends CustomComponent implements View {

    IndexView() {
        init()
    }

    protected void init() {
        compositionRoot = ComponentBuilder.build {
            verticalLayout(sizeFull: true, margin: true, spacing: true) {
                label(value: "Index view", styleName: "h1 colored")
                horizontalLayout(sizeFull: true, spacing: true) {
                    component(instance: ApplicationContextUtils.applicationContext.getBean(Sidebar), sizeUndefined: true)
                    verticalLayout(sizeFull: true, expandRatio: 1, spacing: true) {
                        label(value: "Welcome to the Demo", styleName: "h2")
                        label(value: "Select a menu item and click next!", styleName: "light")
                        button(caption: "Next view", styleName: "primary", clickListener: {
                            Vaadin.enter(fragment: "different")
                        })
                        button(caption: "See other demos", styleName: "", clickListener: {
                            Vaadin.enter(path: "/")
                        })
                    }
                }
            }
        }
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
