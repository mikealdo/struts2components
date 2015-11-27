package cz.mikealdo.struts2components.struts2;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import cz.mikealdo.struts2components.struts2.components.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * @author mikealdo
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionFieldInterceptorTest {

  private final ComponentModelTestFactory componentModelTestFactory = new ComponentModelTestFactory();

  private SessionFieldInterceptor interceptor;
  @Mock
  private ActionInvocation invocation;
  @Mock
  private ActionContext actionContext;
  @Mock
  private ValueStack valueStack;
  @Mock
  private HashMap<String,Object> session;

  @Before
  public void before() throws Exception {
    interceptor = new SessionFieldInterceptor() {
      @Override
      protected ActionContext getContextFromActionContext() {
        return actionContext;
      }
    };
    when(actionContext.getSession()).thenReturn(session);
    when(actionContext.getValueStack()).thenReturn(valueStack);
  }

  @Test
  public void testInterceptOfActionWhichNotImplementsPage() throws Exception {
    ActionSupport action = new ActionSupport();
    when(invocation.getAction()).thenReturn(action);

    interceptor.intercept(invocation);

    verify(invocation, times(1)).getAction();
    verify(valueStack, times(0)).setValue(anyString(), anyObject());
  }

  @Test
  public void testInterceptOfActionWhichImplementsPageWithoutComponent() throws Exception {
    Page action = componentModelTestFactory.createPageActionWithoutComponent();
    when(invocation.getAction()).thenReturn(action);

    interceptor.intercept(invocation);

    verify(invocation, times(1)).getAction();
    verify(valueStack, times(0)).setValue(anyString(), anyObject());
  }

  @Test
  public void testInterceptOfActionWhichImplementsPageWithOneComponentWithoutSessionScopedField() throws Exception {
    Page action = componentModelTestFactory.createPageActionWithEmptyComponent();
    when(invocation.getAction()).thenReturn(action);

    interceptor.intercept(invocation);

    verify(invocation, times(1)).getAction();
    verify(valueStack, times(0)).setValue(anyString(), anyObject());
  }

  @Test
  public void testInterceptOfActionWhichImplementsPageWithOneComponentWithSessionScopedField() throws Exception {
    Page action = componentModelTestFactory.createPageActionWithComponentWithOneSessionScopedField();
    when(invocation.getAction()).thenReturn(action);

    interceptor.intercept(invocation);

    verify(invocation, times(1)).getAction();
    verify(valueStack, times(0)).setValue(anyString(), anyObject());
  }

  @Test
  public void testIfBeforeResultPutNoValueFromStackToSession() throws Exception {
    when(invocation.getAction()).thenReturn(new ActionSupport());

    interceptor.beforeResult(invocation, null);

    verify(invocation, times(1)).getAction();
  }

  @Test
  public void testIfBeforeResultPutNoValueFromStackToSessionWhenPageActionPassed() throws Exception {
    when(invocation.getAction()).thenReturn(componentModelTestFactory.createPageActionWithoutComponent());

    interceptor.beforeResult(invocation, null);

    verify(invocation, times(2)).getAction();
    verify(session, times(0)).put(anyString(), anyObject());
  }

  @Test
  public void testIfBeforeResultPutNoValueFromStackToSessionWhenPageActionPassedWithComponent() throws Exception {
    when(invocation.getAction()).thenReturn(componentModelTestFactory.createPageActionWithEmptyComponent());

    interceptor.beforeResult(invocation, null);

    verify(invocation, times(2)).getAction();
    verify(session, times(0)).put(anyString(), anyObject());
  }

  @Test
  public void testInterceptOfActionWhichImplementsPageWithOneComponentWithSessionFieldAnnotationAndSomeAttributeInSession() throws Exception {
    Page action = componentModelTestFactory.createPageActionWithComponentWithOneSessionScopedField();
    when(invocation.getAction()).thenReturn(action);
    when(session.get("component_masterGrid_customProperty")).thenReturn("value");

    interceptor.intercept(invocation);

    verify(invocation, times(1)).getAction();
    verify(valueStack, times(1)).setValue("masterGrid.customProperty", "value");
  }

  @Test
  public void testIfBeforeResultPutValueFromStackToSessionWhenPageActionPassedWithComponentWithSessionScopedField() throws Exception {
    when(invocation.getAction()).thenReturn(componentModelTestFactory.createPageActionWithComponentWithOneSessionScopedField());
    when(valueStack.findValue("masterGrid.customProperty")).thenReturn("value");

    interceptor.beforeResult(invocation, null);

    verify(invocation, times(2)).getAction();
    verify(session, times(1)).put("component_masterGrid_customProperty", "value");
  }

  @Test
  public void testIfValueFromCustomComponentWithSessionScopedFieldIsPropagatedToValueStack() throws Exception {
    when(invocation.getAction()).thenReturn(componentModelTestFactory.createPageActionWithCustomComponentWithOneSessionScopedField());
    when(session.get("component_firstCustomComponent_customInteger")).thenReturn("value");

    interceptor.intercept(invocation);

    verify(invocation, times(1)).getAction();
    verify(valueStack, times(1)).setValue("firstCustomComponent.customInteger", "value");
  }

  @Test
  public void testIfSessionScopedFieldIsPropagatedToValueStack() throws Exception {
    when(invocation.getAction()).thenReturn(componentModelTestFactory.createPageActionWithSessionScopedComponent());
    WrappedIntegerAsComponent value = new WrappedIntegerAsComponent(1);
    when(session.get("component_customComponentWithInteger")).thenReturn(value);

    interceptor.intercept(invocation);

    verify(invocation, times(1)).getAction();
    verify(valueStack, times(1)).setValue("customComponentWithInteger", value);
  }

  @Test
  public void testIfBeforeResultPutValueFromStackToSessionWhenPageActionPassedWithSessionScopedComponent() throws Exception {
    when(invocation.getAction()).thenReturn(componentModelTestFactory.createPageActionWithSessionScopedComponent());
    WrappedIntegerAsComponent value = new WrappedIntegerAsComponent(1);
    when(valueStack.findValue("customComponentWithInteger")).thenReturn(value);

    interceptor.beforeResult(invocation, null);

    verify(invocation, times(2)).getAction();
    verify(session, times(1)).put("component_customComponentWithInteger", value);
  }

}
