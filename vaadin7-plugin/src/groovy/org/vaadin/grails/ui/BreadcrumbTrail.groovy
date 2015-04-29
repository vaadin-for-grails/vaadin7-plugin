package org.vaadin.grails.ui

import com.vaadin.data.Container
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.server.FontAwesome
import com.vaadin.server.Resource
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.*

/**
 * A breadcrumb component.
 *
 * @author Stephan Grundner
 *
 * @since 2.0
 */
class BreadcrumbTrail extends CustomComponent implements Container.ItemSetChangeListener {

    @Override
    void containerItemSetChange(Container.ItemSetChangeEvent event) {
        if (containerDataSource != event.container) {
            throw new IllegalStateException()
        }
        rebuildTrail()
    }

    static class Breadcrumb {
        FontAwesome icon
        String caption
        String description
        Resource resource
    }

    protected BeanItemContainer<Breadcrumb> containerDataSource
    protected final HorizontalLayout trailContainer

    BreadcrumbTrail(BeanItemContainer<Breadcrumb> containerDataSource) {
        this.containerDataSource = containerDataSource
        trailContainer = new HorizontalLayout()
        compositionRoot = trailContainer
        addListeners()
        styleName = 'breadcrumb-trail'
    }

    BreadcrumbTrail() {
        this(new BeanItemContainer(Breadcrumb))
    }

    @Override
    void attach() {
        super.attach()
        rebuildTrail()
    }

    protected void applyContainerDataSource(BeanItemContainer<Breadcrumb> containerDataSource) {
        removeListeners()
        this.containerDataSource = containerDataSource
        addListeners()
    }

    void rebuildTrail() {
        trailContainer.removeAllComponents()
        def size = containerDataSource.size()
        for (int i; i < size; i++) {
            def breadcrumb = containerDataSource.getIdByIndex(i)
            if (i == size - 1) {
                trailContainer.addComponent(createCurrentBreadcrumbComponent(breadcrumb))
            } else {
                trailContainer.addComponent(createBreadcrumbComponent(breadcrumb))
                trailContainer.addComponent(createSeparatorComponent())
            }
        }
    }

    protected Component createSeparatorComponent() {
        def label = new Label(contentMode: ContentMode.HTML)
        label.styleName = 'separator'
        label.value = "&nbsp;/&nbsp;"
        label
    }

    protected Component createBreadcrumbComponent(Breadcrumb breadcrumb) {
        def link = new Link(styleName: 'breadcrumb small')
        link.caption = breadcrumb.caption
        link.icon = breadcrumb.icon
        link.description = breadcrumb.description
        link.resource = breadcrumb.resource
        link
    }

    protected Component createCurrentBreadcrumbComponent(Breadcrumb breadcrumb) {
        def label = new Label(styleName: 'breadcrumb breadcrumb-current small')
        if (breadcrumb.icon) {
            def caption = breadcrumb.caption ?: ''
            label.value = "${breadcrumb.icon.html} ${caption}"
            label.contentMode = ContentMode.HTML
        } else {
            label.value = breadcrumb.caption
        }
        label.description = breadcrumb.description
        label
    }

    BeanItemContainer<Breadcrumb> getContainerDataSource() {
        return containerDataSource
    }

    protected void removeListeners() {
        containerDataSource?.removeItemSetChangeListener(this)
    }

    protected void addListeners() {
        containerDataSource?.addItemSetChangeListener(this)
    }

    void setContainerDataSource(BeanItemContainer<Breadcrumb> containerDataSource) {
        applyContainerDataSource(containerDataSource)
    }

    Breadcrumb addBreadcrumb(Breadcrumb breadcrumb) {
        containerDataSource.addBean(breadcrumb)?.bean
    }

    boolean removeBreadcrumb(int index) {
        def id = containerDataSource.getIdByIndex(index)
        containerDataSource.removeItem(id)
    }
}
