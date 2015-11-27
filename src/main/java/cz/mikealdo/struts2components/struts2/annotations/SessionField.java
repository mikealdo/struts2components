package cz.mikealdo.struts2components.struts2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Session field is custom annotation used for marking action/component fields as session scoped without using
 * {@link org.apache.struts2.interceptor.ScopeInterceptor} and definition of session scoped fields inside xml configuration.
 *
 * It is useful for each field inside action or inside component. When this annotation is present on field,
 * {@link cz.mikealdo.struts2components.struts2.SessionFieldInterceptor} is called to retrieve/save field value to/from
 * OGNL/ValueStack.
 *
 * BEWARE: This is still experimental functionality and even when it is working with basic usages it does not mean that
 * it will work in all cases which you are facing. This is especially true when inside your session scoped field is some
 * spring managed bean. Please see interceptor documentation when you are interested in and official documentation for
 * this functionality.
 *
 * @author mikealdo
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionField {
}
