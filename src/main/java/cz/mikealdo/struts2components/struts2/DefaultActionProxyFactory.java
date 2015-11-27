package cz.mikealdo.struts2components.struts2;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;

import java.util.Map;

/***
 * This proxy factory is overridden in regards to introducing server component model based on Struts2. It is only needed
 * to pass our implementation of {@link DefaultActionInvocation} invocation object.
 * @author mikealdo
 */
public class DefaultActionProxyFactory extends com.opensymphony.xwork2.DefaultActionProxyFactory {

  //Struts components
  @Override
  public ActionProxy createActionProxy(String namespace, String actionName, String methodName, Map<String, Object> extraContext, boolean executeResult, boolean cleanupContext) {
    ActionInvocation inv = new DefaultActionInvocation(extraContext, true);
    container.inject(inv);
    return createActionProxy(inv, namespace, actionName, methodName, executeResult, cleanupContext);
  }
  //END OF Struts components

}
