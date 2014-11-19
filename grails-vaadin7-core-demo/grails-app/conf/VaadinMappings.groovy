import demo.DemoUI
import demo.SimpleView

/**
 * @author Stephan Grundner
 */
class VaadinMappings {

    static mappings = {

        "/vaadin" (ui: "default")

        "/demo2"(ui: "demo2", theme: "valo")
        "/demo"(ui: DemoUI)
        "#list"(view: SimpleView)
    }
}