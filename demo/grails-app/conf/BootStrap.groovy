import demo.Author

class BootStrap {

    def init = { servletContext ->

        new Author(firstName: "Max", lastName: "Mustermann").save(flush: true)
    }

    def destroy = {

    }
}
