package cz.mikealdo.struts2components.components;

public class SecondSimpleComponent extends SimpleComponent {
    public SecondSimpleComponent() {
    }

    public SecondSimpleComponent(Integer integerInsideComponent) {
        super(integerInsideComponent);
    }

    @Override
    public String getId() {
        return "secondComponent";
    }
}
