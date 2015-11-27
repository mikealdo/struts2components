package cz.mikealdo.struts2components.struts2;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StrutsActionProxyFactoryTest  extends ActionProxyFactoryTestCase<StrutsActionProxyFactory> {

  @Override
  protected StrutsActionProxyFactory createFactory() {
    return new StrutsActionProxyFactory();
  }

}