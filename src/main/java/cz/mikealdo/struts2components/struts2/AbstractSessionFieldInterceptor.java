package cz.mikealdo.struts2components.struts2;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsException;
import org.apache.struts2.dispatcher.SessionMap;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author mikealdo
 */
public abstract class AbstractSessionFieldInterceptor extends AbstractInterceptor implements PreResultListener {

  protected static Map locks = new IdentityHashMap();

  protected static void unlock(Object o) {
    synchronized (o) {
      locks.remove(o);
      o.notify();
    }
  }

  protected static void lock(Object o, ActionInvocation invocation) throws Exception {
    synchronized (o) {
      int count = 3;
      Object previous = null;
      while ((previous = locks.get(o)) != null) {
        if (previous == invocation) {
          return;
        }
        if (count-- <= 0) {
          locks.remove(o);
          o.notify();

          throw new StrutsException("Deadlock in session lock");
        }
        o.wait(10000);
      }
      locks.put(o, invocation);
    }
  }

  protected void before(ActionInvocation invocation) throws Exception {
    invocation.addPreResultListener(this);
    Map<String, Object> session = getContextFromActionContext().getSession();
    if (session == null) {
      session = new SessionMap<>(ServletActionContext.getRequest());
      getContextFromActionContext().setSession(session);
    }
    lock(session, invocation);
  }


  protected void after() throws Exception {
    Map ses = getContextFromActionContext().getSession();
    if (ses != null) {
      unlock(ses);
    }
  }

  protected ActionContext getContextFromActionContext() {
    return ActionContext.getContext();
  }
}
