class VaadinMappings {

    static mappings = {

        "/demo1" (ui: "demo", namespace: "ns1")
        "/vaadin" (ui: "demo", namespace: "ns2", theme: "valo") {
            "list" (view: "index")
        }
    }
}