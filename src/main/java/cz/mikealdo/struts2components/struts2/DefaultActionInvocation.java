package cz.mikealdo.struts2components.struts2;

import cz.mikealdo.struts2components.struts2.components.Page;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/***
 * Overriding of the default DefaultActionInvocation was introduced in regards to introducing server component model.
 * Key goal is to call business logic inside component (if any) instead of encapsulating Action (implementing
 * {@link Page} interface).
 */
public class DefaultActionInvocation extends com.opensymphony.xwork2.DefaultActionInvocation {

	private static final long serialVersionUID = -6829373734083546656L;
	private static final Logger LOG = LoggerFactory.getLogger(com.opensymphony.xwork2.DefaultActionInvocation.class);
	private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
	private final ComponentResolver componentResolver;

	public DefaultActionInvocation(Map<String, Object> extraContext, boolean pushAction) {
		this(extraContext, pushAction, new ComponentResolver());
	}

	public DefaultActionInvocation(Map<String, Object> extraContext, boolean pushAction, ComponentResolver componentResolver) {
		super(extraContext, pushAction);
		this.componentResolver = componentResolver;
	}

	@Override
	protected String invokeAction(Object action, ActionConfig actionConfig) throws Exception {
		String methodName = proxy.getMethod();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Executing action method = " + actionConfig.getMethodName());
		}

		String timerKey = "invokeAction: " + proxy.getActionName();
		Class<? extends Object> actionClass = action.getClass();
		try {
			UtilTimerStack.push(timerKey);

			boolean methodCalled = false;
			Object methodResult = null;
			Method method = null;

      if (action instanceof Page) {
        // Struts components
        ComponentMethod componentMethod = componentResolver.findComponentMethod(new ComponentMethod(action, actionClass, methodName));
        action = componentMethod.component;
        actionClass = componentMethod.componentClass;
        methodName = componentMethod.methodName;
        // END OF Struts components
      }
			try {
				method = actionClass.getMethod(methodName, EMPTY_CLASS_ARRAY);
			} catch (NoSuchMethodException e) {
				// hmm -- OK, try doXxx instead
				try {
					String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
					method = actionClass.getMethod(altMethodName, EMPTY_CLASS_ARRAY);
				} catch (NoSuchMethodException e1) {
					// well, give the unknown handler a shot
					if (unknownHandlerManager.hasUnknownHandlers()) {
						try {
							methodResult = unknownHandlerManager.handleUnknownMethod(action, methodName);
							methodCalled = true;
						} catch (NoSuchMethodException e2) {
							// throw the original one
							throw e;
						}
					} else {
						throw e;
					}
				}
			}

			if (!methodCalled) {
				methodResult = method.invoke(action, new Object[0]);
			}

			if (methodResult instanceof Result) {
				this.explicitResult = (Result) methodResult;

				// Wire the result automatically
				container.inject(explicitResult);
				return null;
			} else {
				return (String) methodResult;
			}
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("The " + methodName + "() is not defined in action " + actionClass + "");
		} catch (InvocationTargetException e) {
			// We try to return the source exception.
			Throwable t = e.getTargetException();

			if (actionEventListener != null) {
				String result = actionEventListener.handleException(t, getStack());
				if (result != null) {
					return result;
				}
			}
			if (t instanceof Exception) {
				throw (Exception) t;
			} else {
				throw e;
			}
		} finally {
			UtilTimerStack.pop(timerKey);
		}
	}


	static class ComponentResolver implements Serializable {
		private static final long serialVersionUID = -5389727189450172227L;
		private static final String COMPONENT_SEPARATOR = ".";
		private static final int MAX_LEVEL = 20;

		private final FieldValueAccessor fieldValueAccessor;

		ComponentResolver() {
			fieldValueAccessor = new FieldValueAccessor();
		}

		public ComponentMethod findComponentMethod(ComponentMethod m) {
			ComponentMethod result = m;
			int index = result.methodName == null ? -1 : result.methodName.indexOf(COMPONENT_SEPARATOR);
			int level = 0;
			while (index > 0 && level < MAX_LEVEL) {
				level ++;
				String propertyName = result.methodName.substring(0, index);
				try {
					Object component  = fieldValueAccessor.getValue(result.component, propertyName);
					Class<?> componentClass = component.getClass();
					String methodName = result.methodName.substring(index + 1);
					result = new ComponentMethod(component, componentClass, methodName);
				} catch (Exception e) {
					throw new IllegalArgumentException("Cannot find component " + propertyName + " in " + result.component.getClass(), e);
				}
				index = result.methodName == null ? -1 : result.methodName.indexOf(COMPONENT_SEPARATOR);
			}
			return result;
		}
	}

	static final class ComponentMethod {
		ComponentMethod(Object component, Class<?> componentClass, String methodName) {
			this.component = component;
			this.componentClass = componentClass;
			this.methodName = methodName;
		}
		public final Object component;
		public final Class<?> componentClass;
		public final String methodName;

		@Override
		public String toString() {
			return componentClass.getName() + "." + methodName;
		}
	}


	static final class FieldValueAccessor {
		Object getValue(Object object, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
			if (fieldName == null || fieldName.trim().isEmpty())
				throw new IllegalArgumentException("Component name is not specified");
			String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method method = null;
			method = ReflectionUtils.findMethod(object.getClass(), methodName);
			if (method != null)
				return method.invoke(object);
			throw new IllegalArgumentException("The " + fieldName + "component is not accessible");
		}
	}

}
