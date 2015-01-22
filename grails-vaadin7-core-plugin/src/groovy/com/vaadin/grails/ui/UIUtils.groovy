package com.vaadin.grails.ui

import com.vaadin.ui.UI

import java.util.concurrent.Future

/**
 * Utilities for UI related operations.
 *
 * @author Stephan Grundner
 * @since 1.0
 */
final class UIUtils {

    static Future<Void> access(Closure<Void> closure) {
        UI.current.access(new Runnable() {
            @Override
            void run() {
                closure?.call()
            }
        })
    }

    static void accessSynchronously(Closure<Void> closure) {
        UI.current.accessSynchronously(new Runnable() {
            @Override
            void run() {
                closure?.call()
            }
        })
    }

    private UIUtils() { }
}
