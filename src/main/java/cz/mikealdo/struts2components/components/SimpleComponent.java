package cz.mikealdo.struts2components.components;

import cz.mikealdo.struts2components.actions.IndexAction;
import cz.mikealdo.struts2components.struts2.annotations.SessionField;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.ComponentOnPageAware;

import static com.opensymphony.xwork2.Action.SUCCESS;

public abstract class SimpleComponent implements Component, ComponentOnPageAware<IndexAction> {

    @SessionField
    private Integer integerInsideComponent;

    public SimpleComponent() {
        integerInsideComponent = 0;
    }

    public SimpleComponent(Integer integerInsideComponent) {
        this.integerInsideComponent = integerInsideComponent;
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

    public void setIntegerInsideComponent(Integer integerInsideComponent) {
        this.integerInsideComponent = integerInsideComponent;
    }

    public String plusOne() {
        integerInsideComponent++;
        return render();
    }
}
