package demo4

import com.vaadin.event.ShortcutAction
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.*
import demo.Book
import org.vaadin.grails.data.fieldgroup.DomainFieldGroup
import org.vaadin.grails.data.util.DomainItem
import org.vaadin.grails.data.util.DomainItemContainer
import org.vaadin.grails.ui.builders.ComponentBuilder
import org.vaadin.grails.util.ApplicationContextUtils
import org.vaadin.grails.util.GrailsUtils

class BookListView extends Panel implements View {

    static class BookEditor extends Window {

        DomainFieldGroup<Book> bookFieldGroup = new DomainFieldGroup<Book>(Book)

        Field fieldWithFocus
        final BookListView view

        BookEditor(BookListView view) {
            this.view = view

            Button saveButton
            content = ComponentBuilder.build {
                formLayout(sizeUndefined: true, margin: true) {
                    fieldWithFocus = build(propertyId: 'title', fieldGroup: bookFieldGroup)
                    build(propertyId: 'author', fieldGroup: bookFieldGroup)
                    build(propertyId: 'type', fieldGroup: bookFieldGroup)
                    build(propertyId: 'released', fieldGroup: bookFieldGroup)
                    build(propertyId: 'rating', fieldGroup: bookFieldGroup, fieldType: Slider)
                    build(propertyId: 'available', fieldGroup: bookFieldGroup)
                    build(propertyId: 'related', fieldGroup: bookFieldGroup)
                    build(propertyId: 'note', fieldGroup: bookFieldGroup)

                    horizontalLayout(spacing: true) {
                        saveButton = button(caption: "Save Book", styleName: "primary default", clickListener: {
                            view.save()

                        })
                    }
                }
            }

            saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER)
            caption = ApplicationContextUtils.getMessage("book.editor.title", "Book Editor")
        }

        void create() {
            def book = new Book()
            book.type = Book.Type.AUDIO
            book.rating = 2d
            bookFieldGroup.itemDataSource = new DomainItem(book)
        }

        void edit(Serializable id) {
            def book = Book.get(id)
            bookFieldGroup.itemDataSource = new DomainItem(book)
        }

        void open() {
            def ui = com.vaadin.ui.UI.current
            if (!ui.windows.contains(this)) {
                ui.addWindow(this)
            }
            center()
            fieldWithFocus.focus()
        }
    }

    Table booksTable
    Button createBookButton
    private BookEditor bookEditor

    BookListView() {
        styleName = 'borderless'
        content = ComponentBuilder.build {
            verticalLayout(spacing: true, margin: true) {
                label(value: "Book Demo", styleName: "h1 colored")
                booksTable = table(sizeFull: true, pageLength: 0)
                createBookButton = button(caption: "Create Book", styleName: "primary", clickListener: {
                    bookEditor.create()
                    bookEditor.open()
                })
            }
        }

        def container = new DomainItemContainer(Book)
        booksTable.containerDataSource = container
        booksTable.columnHeaders = GrailsUtils.getCaptionList(Book, booksTable.visibleColumns, locale)

        booksTable.addGeneratedColumn('edit', new Table.ColumnGenerator() {
            @Override
            Object generateCell(Table components, Object o, Object o2) {
                ComponentBuilder.build {
                    button(caption: "Edit", styleName: "small", clickListener: {
                        bookEditor.edit(o)
                        bookEditor.open()
                    })
                }
            }
        })
        booksTable.immediate = true
        bookEditor = new BookEditor(this)
    }

    void save() {
        if (bookEditor.bookFieldGroup.commit(true)) {
            def item = bookEditor.bookFieldGroup.itemDataSource
            def saved = item.merge(true)
            if (saved != null) {
                booksTable.addItem(saved)
                bookEditor.close()
            } else {
                println item.errors
            }
        }
    }

    void reload() {
        booksTable.removeAllItems()
        booksTable.addItems(Book.list())
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        reload()
    }
}
