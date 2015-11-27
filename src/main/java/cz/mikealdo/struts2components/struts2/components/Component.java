package cz.mikealdo.struts2components.struts2.components;

/**
 * Basic server component usable in {@link Page}.
 *
 * DO NOT STORE IN SESSION IF THE COMPONENT CONTAINS SERVICE
 * (UNLESS THE SERVICE IS CLEARED AT THE REQUEST END AND AFTER REQUEST START)
 */
public interface Component {

  /***
   * Id should return string with field name in component variable inside Page.
   * @return component id
   */
  public String getId();

  /***
   * This is particularly usable for dynamic showing/hiding component on page
   * @return if component is visible on page.
   */
  public boolean isVisible();

  /***
   * Set visibility of component.
   * @param visible current visibility flag.
   */
  public void setVisible(boolean visible);

  /***
   * Method responsible for prepare component guts needed for rendering.
   * This can be base on session/request/application parameters of course.
   *
   * @return particular Struts2 result.
   */
  public String render();
}
