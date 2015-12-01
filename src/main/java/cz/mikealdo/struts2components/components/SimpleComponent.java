package cz.mikealdo.struts2components.components;

import cz.mikealdo.struts2components.actions.IndexAction;
import cz.mikealdo.struts2components.struts2.annotations.SessionField;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.ComponentOnPageAware;

import static com.opensymphony.xwork2.Action.SUCCESS;

public class SimpleComponent implements Component, ComponentOnPageAware<IndexAction> {

    @SessionField
    private Integer integerInsideComponent;

    public SimpleComponent(Integer integerInsideComponent) {
        this.integerInsideComponent = integerInsideComponent;
    }

    @Override
    public String getId() {
        return "simpleComponent";
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
        return SUCCESS;
    }

    @Override
    public void setDefiningPage(IndexAction definingPage) {

    }

    public Integer getIntegerInsideComponent() {
        return integerInsideComponent;
    }

    public void plusOne() {
        integerInsideComponent++;
    }
}
