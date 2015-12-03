package cz.mikealdo.struts2components.struts2;

import com.google.common.collect.ImmutableList;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.ComponentOnPageAware;
import cz.mikealdo.struts2components.struts2.components.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComponentOnPageInitInterceptorTest {
    @Mock
    private ActionProxy proxy;
    @Mock
    private ActionInvocation invocation;
    @InjectMocks
    ComponentOnPageInitInterceptor interceptor = new ComponentOnPageInitInterceptor();

    @Before
    public void setUp() throws Exception {
        when(invocation.getProxy()).thenReturn(proxy);
    }

    @Test
    public void shouldSetActionToInnerComponent() throws Exception {
        TestComponent component = new TestComponent();
        ActionWithField action = new ActionWithField(component);
        String actionName = "actionName";
        when(invocation.getAction()).thenReturn(action);
        when(proxy.getMethod()).thenReturn("component.method");
        when(proxy.getActionName()).thenReturn(actionName);

        interceptor.intercept(invocation);

        assertEquals(component.getPage(), action);
    }

    static class ActionWithField implements Page {
        private final TestComponent testComponent;

        ActionWithField(TestComponent component) {
            this.testComponent = component;
        }

        public TestComponent getComponent() {
            return testComponent;
        }

        @Override
        public Component getChild(String id) {
            return null;
        }

        @Override
        public Collection<Component> getChildren() {
            return ImmutableList.of(testComponent);
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public void setVisible(boolean visible) {

        }

        @Override
        public String render() {
            return null;
        }
    }

    static class TestComponent implements Component, ComponentOnPageAware<ActionWithField> {

        private ActionWithField page;

        @Override
        public void setDefiningPage(ActionWithField definingPage) {
            this.page = definingPage;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public void setVisible(boolean visible) {

        }

        @Override
        public String render() {
            return null;
        }

        public ActionWithField getPage() {
            return page;
        }
    }
}