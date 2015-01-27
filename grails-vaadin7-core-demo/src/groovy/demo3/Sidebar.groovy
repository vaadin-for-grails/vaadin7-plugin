package demo3

import com.vaadin.grails.Vaadin
import com.vaadin.grails.ui.UIUtils
import com.vaadin.ui.CustomComponent

    class Sidebar extends CustomComponent {

        static Sidebar getInstance() {
            def sidebar = UIUtils.getAttribute(Sidebar)
            if (sidebar == null) {
                sidebar = new Sidebar()
                UIUtils.setAttribute(Sidebar, sidebar)
            }
            sidebar
        }

        Sidebar() {
            init()
        }

        protected void init() {
            compositionRoot = Vaadin.build {
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
