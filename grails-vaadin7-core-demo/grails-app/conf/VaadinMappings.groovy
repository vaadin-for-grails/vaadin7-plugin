import demo.DemoUI

/**
 * @author Stephan Grundner
 */
class VaadinMappings {

    static mappings = {
        "/demo"(ui: DemoUI)
        "#list"(view: "list")
    }
}