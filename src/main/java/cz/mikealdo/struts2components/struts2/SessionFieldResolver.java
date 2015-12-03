package cz.mikealdo.struts2components.struts2;

import cz.mikealdo.struts2components.struts2.annotations.SessionField;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.Page;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resolver is responsible for moving values between ValueStack and Session in processing of particular struts2 action.
 *
 * @author mikealdo
 */
public class SessionFieldResolver {

  private static final Object NULL = new NULLClass();
  private ActionContext actionContext;

  public SessionFieldResolver(ActionContext actionContext) {
    this.actionContext = actionContext;
  }

  public List<Field> findAllSessionFieldsFor(Class<? extends Component> componentClass) {
    List<Field> result = new ArrayList<>();
    Class clazz = componentClass;
    while (clazz != null) {
      result.addAll(findSessionFieldsFor(clazz));
      clazz = clazz.getSuperclass();
    }
    return result;
  }

  private List<Field> findSessionFieldsFor(Class clazz) {
    List<Field> result = new ArrayList<>();
    for (Field field : clazz.getDeclaredFields()) {
      if (findSessionFieldAnnotation(field) != null) {
        result.add(field);
      }
    }
    return result;
  }

  private static SessionField findSessionFieldAnnotation(Field field) {
    return field.getAnnotation(SessionField.class);
  }

  public void moveValuesFromValueStackToSession(ActionInvocation invocation, Map<String, Object> session) {
    if (session != null && invocation.getAction() instanceof Page) {
      Page action = (Page) invocation.getAction();
      if (action.getChildren() != null && !action.getChildren().isEmpty()) {
        for (Component component : action.getChildren()) {
          putValuesFromComponentIntoSession(action, component);
        }
      }
    }
  }

  public void putValuesFromComponentIntoSession(Page action, Component component) {
    if (isComponentMarkedAsSessionField(action, component)) {
      putValueIntoSession(createSessionId(component.getId()), component.getId());
    } else {
      putSessionFieldValuesFromComponentIntoSession(component);
    }
  }

  private ValueStack getValueStack() {
    return actionContext.getValueStack();
  }

  public String createSessionId(String componentId, Field field) {
    String fieldName = "";
    if (field != null) {
      fieldName = "_" + field.getName();
    }
    String componentPrefix = "component";
    componentId = "_" + componentId;
    return componentPrefix + componentId + fieldName;
  }

  private String createSessionId(String componentId) {
    return createSessionId(componentId, null);
  }

  public void putSessionFieldValuesFromComponentIntoSession(Component component) {
    for (Field field : findAllSessionFieldsFor(component.getClass())) {
      String sessionId = createSessionId(component.getId(), field);
      String ognlExpr = createOgnlExpression(component, field);
      putValueIntoSession(sessionId, ognlExpr);
    }
  }

  private String createOgnlExpression(Component component, Field field) {
    return component.getId() + "." + field.getName();
  }

  private void putValueIntoSession(String sessionId, String expr) {
    Map<String, Object> session = actionContext.getSession();
    Object value = getValueStack().findValue(expr);
    session.put(sessionId, nullConvert(value));
  }

  /**
   * This is essential functionality which move all values from @SessionField marked fields which have some value
   * inside http session to ValueStack for accessibility inside action.
   *
   * @param action particular action with session fields
   */
  public void moveValuesFromSessionToValueStack(Object action) throws Exception {
    if (action instanceof Page) {
      Page page = (Page) action;
      if (page.getChildren() != null && !page.getChildren().isEmpty()) {
        for (Component component : page.getChildren()) {
          if (isComponentMarkedAsSessionField(page, component)) {
            moveValuesFromSessionFieldComponentToValueStack(component);
          } else {
            moveValuesFromSessionFieldsInsideComponentToValueStack(component);
          }
        }
      }
    }
  }

  private boolean isComponentMarkedAsSessionField(Page action, Component component) {
    Class clazz = action.getClass();
    Field field = null;
    while (field == null && clazz != null) {
      try {
        field = clazz.getDeclaredField(component.getId());
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    if (field == null) {
      throw new IllegalArgumentException("Component id is not exactly same as name of field in action class/superclass.");
    }
    return findSessionFieldAnnotation(field) != null;
  }

  private void moveValuesFromSessionFieldComponentToValueStack(Component component) {
    String sessionId = createSessionId(component.getId());
    Object sessionValue = actionContext.getSession().get(sessionId);
    if (sessionValue != null) {
      String ognlExpr = component.getId();
      getValueStack().setValue(ognlExpr, nullConvert(sessionValue));
    }
  }

  public void moveValuesFromSessionFieldsInsideComponentToValueStack(Component component) {
    for (Field sessionFieldInsideComponent : findAllSessionFieldsFor(component.getClass())) {
      String sessionId = createSessionId(component.getId(), sessionFieldInsideComponent);
      Object sessionValue = actionContext.getSession().get(sessionId);
      if (sessionValue != null) {
        String ognlExpr = createOgnlExpression(component, sessionFieldInsideComponent);
        getValueStack().setValue(ognlExpr, nullConvert(sessionValue));
      }
    }
  }

  // Since 2.0.7. Avoid null references on session serialization (WW-1803).
  private static class NULLClass implements Serializable {

    @Override
    public String toString() {
      return "NULL";
    }

    @Override
    public int hashCode() {
      return 1; // All instances of this class are equivalent
    }

    @Override
    public boolean equals(Object obj) {
      return obj == null || (obj instanceof NULLClass);
    }
  }

  private static Object nullConvert(Object o) {
    if (o == null) {
      return NULL;
    }

    if (o == NULL || NULL.equals(o)) {
      return null;
    }

    return o;
  }
}
