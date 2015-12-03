package cz.mikealdo.struts2components;

import cz.mikealdo.struts2components.actions.IndexAction;
import cz.mikealdo.struts2components.components.FirstSimpleComponent;
import cz.mikealdo.struts2components.components.SecondSimpleComponent;
import cz.mikealdo.struts2components.components.SimpleComponent;
import cz.mikealdo.struts2components.struts2.components.Component;
import junit.framework.TestCase;

import com.opensymphony.xwork2.Action;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.mockito.Matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class IndexActionTest {

    private final FirstSimpleComponent firstComponent = new FirstSimpleComponent();
    private final SecondSimpleComponent secondComponent = new SecondSimpleComponent();
    private IndexAction action = new IndexAction(firstComponent, secondComponent);;

    @Test
    public void shouldReturnRightResultName() throws Exception {
        assertEquals(Action.INPUT, action.execute());
    }

    @Test
    public void shouldReturnFirstComponent() throws Exception {
        Component component = action.getChild("firstComponent");

        assertEquals(component, firstComponent);
    }


    @Test
    public void shouldReturnSecondComponent() throws Exception {
        Component component = action.getChild("secondComponent");

        assertEquals(component, secondComponent);
    }

    @Test
    public void shouldReturnTwoComponents() throws Exception {
        assertEquals(action.getChildren().size(), 2);

    }
}
