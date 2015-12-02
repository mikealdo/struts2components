package cz.mikealdo.struts2components.components;

public class FirstSimpleComponent extends SimpleComponent {
    public FirstSimpleComponent() {
    }

    public FirstSimpleComponent(Integer integerInsideComponent) {
        super(integerInsideComponent);
    }


    @Override
    public String getId() {
        return "firstComponent";
    }
}
