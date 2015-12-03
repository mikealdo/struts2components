package cz.mikealdo.struts2components.components;

import org.junit.Test;

import static org.junit.Assert.*;

public class SecondSimpleComponentTest {

    @Test
    public void shouldReturnRightId() throws Exception {
        String id = new SecondSimpleComponent(0).getId();

        assertEquals("secondComponent", id);
    }

}