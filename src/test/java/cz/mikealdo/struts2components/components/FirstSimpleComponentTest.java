package cz.mikealdo.struts2components.components;

import org.junit.Test;

import static org.junit.Assert.*;

public class FirstSimpleComponentTest {

    @Test
    public void shouldReturnRightId() throws Exception {
        String id = new FirstSimpleComponent(0).getId();

        assertEquals("firstComponent", id);
    }
}