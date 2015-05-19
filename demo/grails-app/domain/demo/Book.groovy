package demo

class Book {

    static enum Type {
        PRINT,
        AUDIO,
        EBOOK
    }

    String title
    Author author
    Type type
    Date released
    boolean available
    double rating

    Book related
    String note

    static constraints = {
        title widget: 'textArea', nullable: true
        author nullable: true
        released nullable: true
        related nullable: true
        rating min: 1d, max: 5d
        note nullable: true, blank: true, widget: "textarea"
    }
}
