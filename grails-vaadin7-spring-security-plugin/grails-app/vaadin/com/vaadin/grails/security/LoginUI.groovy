package com.vaadin.grails.security

import com.vaadin.grails.Vaadin
import com.vaadin.server.Page
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Button
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

/**
 * @author Stephan Grundner
 */
class LoginUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        TextField username
        PasswordField password
        content = new VerticalLayout()
        content.addComponent(username = new TextField("Username"))
        content.addComponent(password = new PasswordField("Password"))
        content.addComponent(new Button("Login", new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent event) {
                def context = Vaadin.applicationContext
                def manager = context.getBean(AuthenticationManager)
                def authentication = new UsernamePasswordAuthenticationToken(
                        username.value,
                        password.value)

                try {
                    def result = manager.authenticate(authentication)
                    SecurityContextHolder.context.authentication = result

                    Page.current.reload()
                } catch (BadCredentialsException e) {
                    println e
                }
            }
        }))
    }
}
