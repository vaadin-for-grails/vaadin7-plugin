package demo4

import com.vaadin.grails.data.util.DomainItem
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Table
import com.vaadin.ui.Window
import demo.Book
import org.vaadin.grails.data.fieldgroup.DomainFieldGroup
import org.vaadin.grails.data.util.DomainItemContainer
import org.vaadin.grails.spring.ApplicationContextUtils
import org.vaadin.grails.ui.TableUtils
import org.vaadin.grails.ui.builders.ComponentBuilder

class BookListView extends CustomComponent implements View {

    class BookEditor extends Window {

        DomainFieldGroup<Book> bookFieldGroup

        BookEditor() {
            bookFieldGroup = new DomainFieldGroup<Book>(Book)

            content = ComponentBuilder.build {
                formLayout(sizeUndefined: true, margin: true) {
                    build(propertyId: 'title', fieldGroup: bookFieldGroup)
                    build(propertyId: 'author', fieldGroup: bookFieldGroup)
                    build(propertyId: 'released', fieldGroup: bookFieldGroup)
                    horizontalLayout(spacing: true) {
                        button(caption: i18n('book.save.button'), clickListener: {
                            save()
                        })
                    }
                }
            }

            caption = ApplicationContextUtils.getMessage("book.editor.title")
        }

        void create() {
            bookFieldGroup.itemDataSource = new DomainItem(new Book())
        }

        void open() {
            bookFieldGroup.discard()

            def ui = com.vaadin.ui.UI.current
            if (!ui.windows.contains(this)) {
                ui.addWindow(this)
            }
            center()
        }

        void save() {
            bookFieldGroup.commit()
            if (bookFieldGroup.validate()) {
                bookFieldGroup.itemDataSource.save(true)
                close()
                BookListView.this.reload()
            }
        }
    }

    Table booksTable
    BookEditor bookEditor

    BookListView() {
        compositionRoot = ComponentBuilder.build {
            verticalLayout(margin: true, spacing: true) {
                booksTable = table(sizeFull: true, pageLength: 0)
                button(caption: i18n("book.create.button"), clickListener: {
                    bookEditor.create()
                    bookEditor.open()
                })
            }
        }

        booksTable.containerDataSource = new DomainItemContainer(Book)
        TableUtils.setColumnHeaders(booksTable)

        bookEditor = new BookEditor()
    }

    void reload() {
        booksTable.removeAllItems()
        def books = Book.list()
        booksTable.addItems(books)
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        reload()
    }
}
