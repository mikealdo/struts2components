package cz.mikealdo.struts2components.struts2.components;


/***
 * This interface is usable for each component when is needed reference for {@link Page}
 * for some reason. Reference is not handled by any automatic stuff and have to be handled
 * in instantiation of Component.
 *
 * @param <T> Page when component is used.
 */
public interface ComponentOnPageAware<T extends Page> extends Component {
  public void setDefiningPage(T definingPage);
}
