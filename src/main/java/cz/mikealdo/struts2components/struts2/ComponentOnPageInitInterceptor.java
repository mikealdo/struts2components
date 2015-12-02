package cz.mikealdo.struts2components.struts2;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.ComponentOnPageAware;
import cz.mikealdo.struts2components.struts2.components.Page;

public class ComponentOnPageInitInterceptor extends AbstractInterceptor {
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        if (action instanceof Page) {
            Page page = (Page) action;
            for (Component component : page.getChildren()) {
                if (component instanceof ComponentOnPageAware) {
                    ((ComponentOnPageAware) component).setDefiningPage(page);
                }
            }
        }
        return invocation.invoke();
    }
}
