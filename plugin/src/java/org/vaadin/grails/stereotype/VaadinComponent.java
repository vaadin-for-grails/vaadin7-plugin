package org.vaadin.grails.stereotype;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Alias for {@link org.springframework.stereotype.Component}
 * to avoid naming conficts with {@link com.vaadin.ui.Component}.
 *
 * @author Stephan Grundner
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface VaadinComponent {

    String value() default "";
}
