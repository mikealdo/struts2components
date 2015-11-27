package cz.mikealdo.struts2components.struts2;

import cz.mikealdo.struts2components.struts2.annotations.SessionField;
import cz.mikealdo.struts2components.struts2.components.Component;
import com.opensymphony.xwork2.ActionInvocation;

import java.util.Map;

/**
 * Interceptor handles {@link SessionField} annotation. It is used for own handling of session scoped parameters
 * and replaces/is able to work besides {@link org.apache.struts2.interceptor.ScopeInterceptor}. It was introduced
 * mainly for handling of session scoped information inside {@link Component} which are not really suitable with
 * ScopeInterceptor.
 * <p/>
 * This handling is based on saving/retrieving field values (marked with SessionField annotation) to/from session and ValueStack.
 * Please see internal comments inside this interceptor to visit particular way how is this achieved.
 * <p/>
 * BEWARE: This interceptor is functional in common way without Components also. So you can get rid of
 * ScopeInterceptor completely.
 * <p/>
 * BEWARE: This is still experimental functionality and have to be used with caution. Even when it works in simple
 * and tested cases it can be dysfunctional for special cases. In that case please use standard
 * {@link org.apache.struts2.interceptor.ScopeInterceptor} for storing/retrieving field values to/from session.
 *
 * @author mikealdo
 */
public class SessionFieldInterceptor extends AbstractSessionFieldInterceptor {

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    String result = null;
    try {
      before(invocation);
      SessionFieldResolver sessionFieldResolver = new SessionFieldResolver(getContextFromActionContext());
      sessionFieldResolver.moveValuesFromSessionToValueStack(invocation.getAction());
      result = invocation.invoke();
    } finally {
      after();
    }
    return result;
  }

  @Override
  public void beforeResult(ActionInvocation invocation, String resultCode) {
    Map<String, Object> session = getContextFromActionContext().getSession();
    SessionFieldResolver sessionFieldResolver = new SessionFieldResolver(getContextFromActionContext());
    sessionFieldResolver.moveValuesFromValueStackToSession(invocation, session);
    unlock(session);
  }

}
