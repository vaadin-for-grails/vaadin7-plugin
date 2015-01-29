package demo4

import com.vaadin.grails.Vaadin
import com.vaadin.grails.data.fieldgroup.DomainFieldGroup
import com.vaadin.grails.data.util.DomainItem
import com.vaadin.grails.data.util.DomainItemContainer
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Table
import demo.Book

class BooksView extends CustomComponent implements View {

    Table booksTable

    DomainItemContainer<Book> booksContainer
    DomainFieldGroup<Book> bookFieldGroup

    @Override
    void attach() {
        super.attach()

        bookFieldGroup = new DomainFieldGroup(Book)
        booksContainer = new DomainItemContainer(Book)

        compositionRoot = Vaadin.build {
            formLayout(margin: true) {
                label(value: "Books", styleName: "h1 colored")
                booksTable = table(containerDataSource: booksContainer, pageLength: 5, width: "500px")
                build(propertyId: "title", fieldGroup: bookFieldGroup)
                build(propertyId: "released", fieldGroup: bookFieldGroup)
                horizontalLayout(spacing: true) {
                    button(caption: "Save", styleName: "primary", icon: FontAwesome.SAVE, clickListener: {save()})
                    button(caption: "New", styleName: "", icon: FontAwesome.FILE_O, clickListener: {create()})
                    button(caption: "Clear", styleName: "quiet", icon: FontAwesome.TRASH_O, clickListener: {clear()})
                }
            }
        }

        booksTable.addGeneratedColumn("action", new Table.ColumnGenerator() {
            @Override
            Object generateCell(Table source, Object itemId, Object columnId) {
                Vaadin.build {
                    horizontalLayout(spacing: true) {
                        button(caption: "Edit", styleName: "quiet small", clickListener: {
                            edit(itemId)
                        })
                        button(caption: "Remove", styleName: "quiet small", clickListener: {
                            remove(itemId)
                        })
                    }
                }
            }
        })
        booksTable.setColumnAlignment("action", Table.Align.RIGHT)
        booksTable.setColumnExpandRatio("title", 1f)

        setSizeFull()
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        loadAll()
        create()
    }

    void loadAll() {
        def books = Book.list()
        books.each {
            booksContainer.addItem(it)
        }
    }

    void create() {
        bookFieldGroup.itemDataSource = new DomainItem(new Book())
    }

    void save() {
        bookFieldGroup.commit()
        if (bookFieldGroup.validate()) {
            def bookItem = bookFieldGroup.itemDataSource
            if (bookItem.save(true)) {
                booksContainer.addItem(bookItem.object)
                booksTable.refreshRowCache()
                create()
            }
        }
    }

    void remove(Object itemId) {
        def bookItem = booksContainer.getItem(itemId)
        bookItem.delete(true)
        booksContainer.removeItem(itemId)
        booksTable.refreshRowCache()
    }

    void edit(Object itemId) {
        def bookItem = booksContainer.getItem(itemId)
        bookFieldGroup.itemDataSource = bookItem
    }

    void clear() {
        Book.executeUpdate("delete Book")
        booksContainer.removeAllItems()
        booksTable.refreshRowCache()
    }
}
