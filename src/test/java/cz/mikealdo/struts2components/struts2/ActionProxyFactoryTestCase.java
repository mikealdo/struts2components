package cz.mikealdo.struts2components.struts2;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.DefaultActionProxy;
import com.opensymphony.xwork2.DefaultActionProxyFactory;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.RuntimeConfiguration;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

abstract class ActionProxyFactoryTestCase<T extends DefaultActionProxyFactory> {


  private T factory;
  @Mock
  private Container container;
  @Mock
  private Configuration configuration;
  @Mock
  private RuntimeConfiguration runtimeConfiguration;
  @Mock
  private ActionConfig actionConfig;
  @Mock
  private ValueStackFactory valueStackFactory;
  @Mock
  private ValueStack valueStack;
  @Mock
  private ObjectFactory objectFactory;

  @Before
  public void before() throws Exception {
    System.err.print("Before method");
    MockitoAnnotations.initMocks(this);
    factory = createFactory();
    factory.setContainer(container);
  }

  protected abstract T createFactory();

  @Test
  public void testCreatedActionProxyContainsCustomDefaultActionInvocation() throws Exception {
    String namespace = "namespace";
    String actionName = "actionName";
    String methodName = "methodName";
    boolean executeResult = false;
    boolean cleanupContext = false;
    HashMap<String, Object> extraContext = new HashMap<String, Object>();
    mockContainerToInjectIntoActionProxyToPreventNPE(namespace, actionName, methodName);

    ActionProxy proxy = factory.createActionProxy(namespace, actionName, methodName, extraContext, executeResult, cleanupContext);

    assertEquals(proxy.getInvocation().getClass(), DefaultActionInvocation.class);
  }

  private void mockContainerToInjectIntoActionProxyToPreventNPE(String namespace, String actionName, String methodName) {
    assertNotNull(configuration);
    when(configuration.getRuntimeConfiguration()).thenReturn(runtimeConfiguration);
    when(runtimeConfiguration.getActionConfig(namespace, actionName)).thenReturn(actionConfig);
    when(actionConfig.isAllowedMethod(methodName)).thenReturn(true);
    when(valueStackFactory.createValueStack()).thenReturn(valueStack);
    doAnswer(new Answer<Void>() {


      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Object object = invocation.getArguments()[0];
        if (object instanceof DefaultActionProxy) {
          DefaultActionProxy actionProxy = (DefaultActionProxy)object;
          actionProxy.setConfiguration(configuration);
        }
        if (object instanceof com.opensymphony.xwork2.DefaultActionInvocation) {
          com.opensymphony.xwork2.DefaultActionInvocation actionInvocation = (com.opensymphony.xwork2.DefaultActionInvocation)object;
          
          actionInvocation.setContainer(container);
          actionInvocation.setValueStackFactory(valueStackFactory);
          actionInvocation.setObjectFactory(objectFactory);
        }
        return null;
      }
    }).when(container).inject(any(Object.class));
  }
}
