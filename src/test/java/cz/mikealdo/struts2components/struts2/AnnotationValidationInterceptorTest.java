package cz.mikealdo.struts2components.struts2;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import cz.mikealdo.struts2components.struts2.DefaultActionInvocationTest.ActionWithField;
import cz.mikealdo.struts2components.struts2.DefaultActionInvocationTest.TestAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static cz.mikealdo.struts2components.struts2.DefaultActionInvocation.ComponentResolver;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AnnotationValidationInterceptorTest {

  private AnnotationValidationInterceptor interceptor;
  @Mock
  private ActionProxy proxy;
  @Mock
  private ActionValidatorManager actionValidatorManager;
  @Mock
  private ActionInvocation invocation;
  @Spy
  private ComponentResolver componentResolver;

  @Before
  public void before() throws Exception {
    interceptor = new AnnotationValidationInterceptor(componentResolver);
    when(invocation.getProxy()).thenReturn(proxy);
    interceptor.setActionValidatorManager(actionValidatorManager);
  }

  @Test
  public void testInterceptWithActionWithoutImplementedPageInterface() throws Exception {
    TestActionWithoutImplementedPage action = new TestActionWithoutImplementedPage("result");
    String actionName = "actionName";
    when(invocation.getAction()).thenReturn(action);
    when(proxy.getMethod()).thenReturn("method");
    when(proxy.getActionName()).thenReturn(actionName);

    interceptor.intercept(invocation);

    verify(componentResolver, times(0)).findComponentMethod(any(DefaultActionInvocation.ComponentMethod.class));
  }


  @Test
  public void testInterceptWithActionMethod() throws Exception {
    TestAction action = new TestAction("result");
    String actionName = "actionName";
    when(invocation.getAction()).thenReturn(action);
    when(proxy.getMethod()).thenReturn("method");
    when(proxy.getActionName()).thenReturn(actionName);

    interceptor.intercept(invocation);

    verify(componentResolver, times(1)).findComponentMethod(any(DefaultActionInvocation.ComponentMethod.class));
  }


  @Test
  public void testInterceptWithComponentMethod() throws Exception {
    Object action = new ActionWithField("result1", new TestAction("result2"));
    String actionName = "actionName";
    when(invocation.getAction()).thenReturn(action);
    when(proxy.getMethod()).thenReturn("component.method");
    when(proxy.getActionName()).thenReturn(actionName);

    interceptor.intercept(invocation);

    verify(componentResolver, times(1)).findComponentMethod(any(DefaultActionInvocation.ComponentMethod.class));
  }

  static class TestActionWithoutImplementedPage {
    private final String result;

    TestActionWithoutImplementedPage(String result) {
      this.result = result;
    }

    public String method() {
      return result;
    }

    String hiddenMethod() {
      return result;
    }
  }

}
