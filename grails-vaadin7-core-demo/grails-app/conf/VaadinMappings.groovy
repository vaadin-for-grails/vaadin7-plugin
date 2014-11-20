import demo.DemoUI
import demo.SimpleView

/**
 * @author Stephan Grundner
 */
class VaadinMappings {

    static base = "/vaadin"

    static mappings = {

        "/" (ui: "default")

        "/demo2"(ui: "demo2", theme: "valo")
        "/demo"(ui: DemoUI)
        "#simple"(view: SimpleView, ui: ["demo2", DemoUI])
    }
}