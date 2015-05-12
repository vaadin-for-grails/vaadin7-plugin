package demo3

import com.vaadin.ui.CustomComponent
import org.springframework.context.annotation.Scope
import org.vaadin.grails.stereotype.VaadinComponent
import org.vaadin.grails.ui.builders.ComponentBuilder

@VaadinComponent("mySideBar")
@Scope('ui')
class Sidebar extends CustomComponent {

        Sidebar() {
            init()
        }

        protected void init() {
            compositionRoot = ComponentBuilder.build {
                panel(sizeFull: true) {
                    verticalLayout(margin: true, sizeFull: true) {
                        tree(sizeFull: true) {
                            treeItem(itemId: "1", caption: "One")
                            treeItem(itemId: "2", caption: "Two")
                            treeItem(itemId: "3", caption: "Three") {
                                treeItem(itemId: "3.1", caption: "Three/One")
                            }
                            treeItem(itemId: "4", caption: "Four")
                            treeItem(itemId: "5", caption: "Five")
                        }
                    }
                }
            }
        }
    }
