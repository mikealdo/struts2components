package cz.mikealdo.struts2components.struts2;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.UnknownHandlerManager;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultActionInvocationTest {
  private DefaultActionInvocation actionInvocation;
  private final Map<String, Object> extraContext = new HashMap<>();

  @Mock
  private ActionConfig actionConfig;
  @Mock
  private ActionProxy proxy;
  @Mock
  private ValueStackFactory valueStackFactory;
  @Mock
  private ValueStack valueStack;
  @Mock
  private ObjectFactory objectFactory;
  @Mock
  private UnknownHandlerManager unknownHandlerManager;


  @Before
  public void before() throws Exception {
    actionInvocation = new DefaultActionInvocation(extraContext, false);
    actionInvocation.setValueStackFactory(valueStackFactory);
    actionInvocation.setObjectFactory(objectFactory);
    actionInvocation.setUnknownHandlerManager(unknownHandlerManager);
    when(valueStackFactory.createValueStack()).thenReturn(valueStack);
    when(proxy.getConfig()).thenReturn(actionConfig);
    actionInvocation.init(proxy);
  }


  @Test
  public void testMethodInvocation() throws Exception {
    String expectedResult = "resultOfMethod";
    Object action = new TestAction(expectedResult);
    when(proxy.getMethod()).thenReturn("method");

    assertActionInvocation(action, expectedResult);
  }


  private void assertActionInvocation(Object action, String expectedResult) throws Exception {
    setFieldValue(actionInvocation, "action", action);
    assertEquals(actionInvocation.invokeAction(action, actionConfig), expectedResult);
  }


  @Test
  public void testMethodInvocationOnProperty() throws Exception {
    String result = "resultOfMethod";
    String expectedResult = "resultOfMethodOfField";
    Object action = new ActionWithField(result, new TestAction(expectedResult));

    when(proxy.getMethod()).thenReturn("component.method");

    assertActionInvocation(action, expectedResult);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMethodInvocationOnNonexistingPropertyThrowsException() throws Exception {
    String result = "resultOfMethod";
    String resultOfMethodOfField = "resultOfMethodOfField";
    Object action = new ActionWithField(result, new TestAction(resultOfMethodOfField));

    when(proxy.getMethod()).thenReturn("wrongComponent.method");

    assertActionInvocation(action, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMethodInvocationOnNonPulicPropertyThrowsException() throws Exception {
    String result = "resultOfMethod";
    String resultOfMethodOfField = "resultOfMethodOfField";
    Object action = new ActionWithField(result, new TestAction(resultOfMethodOfField));

    when(proxy.getMethod()).thenReturn("component.hiddenMethod");

    assertActionInvocation(action, resultOfMethodOfField);
  }

  @Test
  public void testMethodInvocationOnPropertyOfProperty() throws Exception {
    String result = "resultOfMethod";
    String resultOfMethodOfField = "resultOfMethodOfField";
    String expectedResult = "expectedResult";
    Object action = new ActionWithField(result, new ActionWithField(resultOfMethodOfField, new TestAction(expectedResult)));

    when(proxy.getMethod()).thenReturn("component.component.method");

    assertActionInvocation(action, expectedResult);
  }

  static class TestAction implements Page {
    private final String result;

    TestAction(String result) {
      this.result = result;
    }

    public String method() {
      return result;
    }

    String hiddenMethod() {
      return result;
    }

    @Override
    public String getId() {
      return null;
    }

    @Override
    public boolean isVisible() {
      return false;
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public String render() {
      return null;
    }

    @Override
    public Component getChild(String id) {
      return null;
    }

    @Override
    public Collection<Component> getChildren() {
      return null;
    }
  }

  static class ActionWithField extends TestAction {
    private final TestAction componentWithDifferentNameThenGetter;

    ActionWithField(String result, TestAction component) {
      super(result);
      this.componentWithDifferentNameThenGetter = component;
    }

    public TestAction getComponent() {
      return componentWithDifferentNameThenGetter;
    }
  }

  private static Field findFieldOfBean(Object bean, String fieldName) {
    Field field = null;
    Class<?> clazz = bean.getClass();
    while (clazz != null && field == null) {
      field = findField(clazz.getDeclaredFields(), fieldName);
      clazz = clazz.getSuperclass();
    }
    if (field == null)
      throw new IllegalArgumentException("Field " + fieldName + " not found");
    return field;
  }
/*
  public static Object getFieldValue(Object bean, String fieldName) throws IllegalAccessException {
    Field field = findFieldOfBean(bean, fieldName);
    field.setAccessible(true);
    return field.get(bean);
  }
*/
/* public */
  private static Field findField(Field[] fields, String name) {
    for (Field field : fields)
      if (field.getName().equals(name))
        return field;
    return null;
  }

  /* public */
  private static void setFieldValue(Object bean, String fieldName, Object value) throws IllegalAccessException {
    Field field = findFieldOfBean(bean, fieldName);
    field.setAccessible(true);
    field.set(bean, value);
  }
}
