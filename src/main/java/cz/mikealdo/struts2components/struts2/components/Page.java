package cz.mikealdo.struts2components.struts2.components;

import java.util.Collection;

/***
 * Page is the base interface for all Struts2 action which can contain some components.
 * Based on this interface is there the possibility to call methods inside particular
 * component instead of action.
 *
 * {@link cz.mikealdo.struts2components.struts2.DefaultActionInvocation} this overridden invocation object overrides behaviour
 *  responsible for calling methods inside Action classes and adding functionality for Component model.
 */
public interface Page extends Component {
  /***
   * Returns child (component) of page based on id
   * @param id given component id
   * @return component which is integral part of Page
   */
  Component getChild(String id);

  /***
   * Returns all components available for particular Page.
   * @return all components belonging to Page.
   */
  Collection<Component> getChildren();
}
