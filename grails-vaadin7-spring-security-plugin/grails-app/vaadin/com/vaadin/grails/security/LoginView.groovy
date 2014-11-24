package com.vaadin.grails.security

import com.vaadin.grails.Vaadin
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.Page
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

/**
 * @author Stephan Grundner
 */
class LoginView extends CustomComponent implements View {

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        TextField username
        PasswordField password
        compositionRoot = new VerticalLayout()
        compositionRoot.addComponent(username = new TextField("Username"))
        compositionRoot.addComponent(password = new PasswordField("Password"))
        compositionRoot.addComponent(new Button("Login", new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent e1) {
                def context = Vaadin.applicationContext
                def manager = context.getBean(AuthenticationManager)
                def authentication = new UsernamePasswordAuthenticationToken(
                        username.value,
                        password.value)

                try {
                    def result = manager.authenticate(authentication)
                    SecurityContextHolder.context.authentication = result

                    Page.current.reload()
                } catch (BadCredentialsException e2) {
                    println e2
                }
            }
        }))
    }
}
