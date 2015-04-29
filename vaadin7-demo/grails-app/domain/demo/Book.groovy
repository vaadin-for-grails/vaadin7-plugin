package demo

class Book {

    String title
    String author
    Date released
    Boolean available
    double rating

    static constraints = {
        title widget: 'textArea'
    }
}
