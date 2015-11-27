package cz.mikealdo.struts2components.struts2.components;

/***
 * This interface is useful when you have a component which is based on some Struts2 action itself. You can
 * initialize component by calling inherited action methods for example.
 *
 * BEWARE: This interface is not automatically handled in any way so you have to implement your initializing logic inside
 * {@link Page} by yourself. This can be improved by introducing some new interceptor which will calls common
 * initialization logic automatically on each {@link Page} and all {@link Component}s belonging to Page.
 */
public interface InitializationAware {
  public void initialize();
}
