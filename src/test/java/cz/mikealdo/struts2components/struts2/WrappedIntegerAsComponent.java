package cz.mikealdo.struts2components.struts2;

import cz.mikealdo.struts2components.struts2.components.Component;

/**
* @author mikealdo
*/
public class WrappedIntegerAsComponent implements Component {

  private Integer integerInsideComponent;

  WrappedIntegerAsComponent(Integer integerInsideComponent) {
    this.integerInsideComponent = integerInsideComponent;
  }

  @Override
  public String getId() {
    return "customComponentWithInteger";
  }

  @Override
  public boolean isVisible() {
    return true;
  }

  @Override
  public void setVisible(boolean visible) {
  }

  @Override
  public String render() {
    return "";
  }
}
