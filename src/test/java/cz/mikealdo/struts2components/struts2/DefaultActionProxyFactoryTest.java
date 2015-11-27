package cz.mikealdo.struts2components.struts2;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultActionProxyFactoryTest extends ActionProxyFactoryTestCase<DefaultActionProxyFactory> {

  @Override
  protected DefaultActionProxyFactory createFactory() {
    return new DefaultActionProxyFactory();
  }
}
