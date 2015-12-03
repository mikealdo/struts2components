package cz.mikealdo.struts2components.components;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleComponentTest {

    private SimpleComponent simpleComponent = new SimpleComponent() {
        @Override
        public String getId() {
            return "id";
        }
    };

    @Test
    public void shouldReturnPlusOneRightResult() throws Exception {
        String result = simpleComponent.plusOne();

        assertEquals("success", result);
        assertEquals(simpleComponent.getIntegerInsideComponent(), Integer.valueOf(1));
    }
}