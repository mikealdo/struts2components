package cz.mikealdo.struts2components.struts2;

import cz.mikealdo.struts2components.struts2.DefaultActionInvocation.ComponentMethod;
import cz.mikealdo.struts2components.struts2.DefaultActionInvocation.ComponentResolver;
import cz.mikealdo.struts2components.struts2.components.Page;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.AnnotationUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

/**
 * This interceptor overrides standard validation interceptor due to introduced server component model. This is not
 * interceptor which do something in component, it not calls any logic.
 * <p/>
 * His goal is to do a validation either in Action or {@link cz.mikealdo.struts2components.struts2.components.Component}
 * and therefore was needed to improve this interceptor with own resolving of inner components.
 * In fact actions not implementing Page interface are not affected at all.
 */
public class AnnotationValidationInterceptor extends org.apache.struts2.interceptor.validation.AnnotationValidationInterceptor {

  private static final long serialVersionUID = -4220712112323479296L;
  private final ComponentResolver componentResolver;

  public AnnotationValidationInterceptor() {
    this(new ComponentResolver());
  }

  AnnotationValidationInterceptor(ComponentResolver componentResolver) {
    this.componentResolver = componentResolver;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected String doIntercept(ActionInvocation invocation) throws Exception {

    Object action = invocation.getAction();
    if (action != null) {
      Class<? extends Object> actionClass = action.getClass();
      String methodName = invocation.getProxy().getMethod();

      if (action instanceof Page) {
        //Struts components
        ComponentMethod componentMethod = componentResolver.findComponentMethod(new ComponentMethod(action, actionClass, methodName));
        action = componentMethod.component;
        actionClass = componentMethod.componentClass;
        methodName = componentMethod.methodName;
        //END OF Struts components
      }

      Method method = getActionMethod(actionClass, methodName);
      Collection<Method> annotatedMethods = AnnotationUtils.getAnnotatedMethods(actionClass, SkipValidation.class);
      if (annotatedMethods.contains(method))
        return invocation.invoke();

      //check if method overwrites an annotated method
      Class<? extends Object> clazz = actionClass.getSuperclass();
      while (clazz != null) {
        annotatedMethods = AnnotationUtils.getAnnotatedMethods(clazz, SkipValidation.class);
        if (annotatedMethods != null) {
          for (Method annotatedMethod : annotatedMethods) {
            if (annotatedMethod.getName().equals(method.getName())
                && Arrays.equals(annotatedMethod.getParameterTypes(), method.getParameterTypes())
                && Arrays.equals(annotatedMethod.getExceptionTypes(), method.getExceptionTypes()))
              return invocation.invoke();
          }
        }
        clazz = clazz.getSuperclass();
      }
    }

    doBeforeInvocation(invocation);

    return invocation.invoke();
  }
}
