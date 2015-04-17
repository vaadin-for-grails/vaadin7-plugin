package demo

class Book {

    String title
    String author
    Date released

    static constraints = {
        title widget: 'textArea'
    }
}
