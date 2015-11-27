package cz.mikealdo.struts2components.struts2;

import cz.mikealdo.struts2components.struts2.components.Page;

/**
 * This interceptor overrides standard workflow interceptor due to introduced server component model. This is not
 * interceptor which do something in component, it not calls any logic.
 * <p/>
 * His goal is to do a workflow work either in Action or {@link cz.mikealdo.struts2components.struts2.components.Component}
 * and therefore was needed to improve this interceptor with own resolving of inner components.
 * In fact actions not implementing Page interface are not affected at all.
 *
 * @author mikealdo
 */
public class DefaultWorkflowInterceptor extends com.opensymphony.xwork2.interceptor.DefaultWorkflowInterceptor {

  private final DefaultActionInvocation.ComponentResolver componentResolver;

  public DefaultWorkflowInterceptor() {
    this(new DefaultActionInvocation.ComponentResolver());
  }

  DefaultWorkflowInterceptor(DefaultActionInvocation.ComponentResolver componentResolver) {
    this.componentResolver = componentResolver;
  }

  @Override
  protected String processInputConfig(Object action, String method, String currentResultName) throws Exception {
    if (action instanceof Page) {
      //Struts components
      DefaultActionInvocation.ComponentMethod componentMethod = componentResolver.findComponentMethod(new DefaultActionInvocation.ComponentMethod(action, action.getClass(), method));
      action = componentMethod.component;
      method = componentMethod.methodName;
      //END OF Struts components
    }

    return super.processInputConfig(action, method, currentResultName);
  }
}
