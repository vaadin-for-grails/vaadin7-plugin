import demo.Demo3UI
import demo.DemoUI
import demo.SimpleView

/**
 * @author Stephan Grundner
 */
class VaadinMappings {

    static base = "/vaadin"

    static mappings = {

        "/" (ui: "default", pageTitle: "Default UI")

        "/demo2"(ui: "demo2", theme: "valo")
        "/demo"(ui: DemoUI)
        "/demo3"(ui: Demo3UI)
        "#!simple"(view: SimpleView, ui: ["demo2", DemoUI])
    }
}