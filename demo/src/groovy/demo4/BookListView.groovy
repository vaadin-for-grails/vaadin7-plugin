package demo4

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

    class BookEditor extends Window {

        DomainFieldGroup<Book> bookFieldGroup = new DomainFieldGroup<Book>(Book)

        BookEditor() {
            content = ComponentBuilder.build {
                formLayout(sizeUndefined: true, margin: true) {
                    build(propertyId: 'title', fieldGroup: bookFieldGroup)
                    build(propertyId: 'author', fieldGroup: bookFieldGroup)
                    build(propertyId: 'type', fieldGroup: bookFieldGroup)
                    build(propertyId: 'released', fieldGroup: bookFieldGroup)
                    build(propertyId: 'rating', fieldGroup: bookFieldGroup, fieldType: Slider)
                    build(propertyId: 'available', fieldGroup: bookFieldGroup)
                    build(propertyId: 'related', fieldGroup: bookFieldGroup)
                    build(propertyId: 'note', fieldGroup: bookFieldGroup)

                    horizontalLayout(spacing: true) {
                        button(caption: "Save Book", clickListener: {
                            save()
                        })
                    }
                }
            }

            caption = ApplicationContextUtils.getMessage("book.editor.title", "Book Editor")
        }

        void create() {
            bookFieldGroup.itemDataSource = new DomainItem(Book)
        }

        void open() {
//            bookFieldGroup.discard()
            def ui = com.vaadin.ui.UI.current
            if (!ui.windows.contains(this)) {
                ui.addWindow(this)
            }
            center()
        }

        void save() {
            if (bookFieldGroup.commit(true)) {
                def item = bookFieldGroup.itemDataSource
                def saved = item.save(true)
                if (saved != null) {
                    close()
                    booksTable.addItem(saved)
                } else {
                    println item.errors
                }
            }
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

        bookEditor = new BookEditor()
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
