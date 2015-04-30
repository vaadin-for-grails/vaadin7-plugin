package demo4

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.ui.Button
import com.vaadin.ui.Panel
import com.vaadin.ui.Table
import com.vaadin.ui.Window
import demo.Book
import demo.IndexView
import org.vaadin.grails.data.fieldgroup.DomainFieldGroup
import org.vaadin.grails.data.util.DomainItem
import org.vaadin.grails.data.util.DomainItemContainer
import org.vaadin.grails.ui.BreadcrumbTrail
import org.vaadin.grails.ui.builders.ComponentBuilder
import org.vaadin.grails.util.ApplicationContextUtils
import org.vaadin.grails.util.DomainClassUtils

class BookListView extends Panel implements View {

    static final breadcrumb = new BreadcrumbTrail.Breadcrumb(icon: FontAwesome.BOOK, caption: "Books")

    class BookEditor extends Window {

        DomainFieldGroup<Book> bookFieldGroup
//        BeanFieldGroup<Book> bookFieldGroup

        BookEditor() {
            bookFieldGroup = new DomainFieldGroup<Book>(Book)
//            bookFieldGroup = new BeanFieldGroup<>(Book)
            content = ComponentBuilder.build {
                formLayout(sizeUndefined: true, margin: true) {
                    build(propertyId: 'title', fieldGroup: bookFieldGroup)
                    build(propertyId: 'author', fieldGroup: bookFieldGroup)
                    build(propertyId: 'released', fieldGroup: bookFieldGroup)
                    build(propertyId: 'rating', fieldGroup: bookFieldGroup)
                    build(propertyId: 'available', fieldGroup: bookFieldGroup)

                    horizontalLayout(spacing: true) {
                        button(caption: i18n('book.save.button'), clickListener: {
                            save()
                        })
                    }
                }
            }

            caption = ApplicationContextUtils.getMessage("book.editor.title", "Book Editor")
        }

        void create() {
            bookFieldGroup.itemDataSource = new DomainItem(Book)
//            bookFieldGroup.itemDataSource = new BeanItem(new Book())
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

    BreadcrumbTrail trail
    Table booksTable
    Button createBookButton
    private BookEditor bookEditor

    BookListView() {
        styleName = 'borderless'
        content = ComponentBuilder.build {
            verticalLayout(spacing: true, margin: true) {
                trail = breadcrumbTrail() {
                    breadcrumb(IndexView.breadcrumb)
                    breadcrumb(this.breadcrumb)
                }
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
        booksTable.columnHeaders = DomainClassUtils.getCaptionList(Book, booksTable.visibleColumns, locale)

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
