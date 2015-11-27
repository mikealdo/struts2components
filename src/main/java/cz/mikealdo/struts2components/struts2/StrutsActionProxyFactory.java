package cz.mikealdo.struts2components.struts2;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;

import java.util.Map;

/***
 * Please see {@link DefaultActionProxyFactory} for details. This Factory is only particular implementation of
 * DefaultActionProxyFactory and both of them have to be overridden with usage of {@link DefaultActionInvocation}
 */
public class StrutsActionProxyFactory extends org.apache.struts2.impl.StrutsActionProxyFactory {

  //Struts components
  @Override
  public ActionProxy createActionProxy(String namespace, String actionName, String methodName, Map<String, Object> extraContext, boolean executeResult, boolean cleanupContext) {
    ActionInvocation inv = new DefaultActionInvocation(extraContext, true);
    container.inject(inv);
    return createActionProxy(inv, namespace, actionName, methodName, executeResult, cleanupContext);
  }
  //END OF Struts components

}
