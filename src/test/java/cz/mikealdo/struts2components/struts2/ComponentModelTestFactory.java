package cz.mikealdo.struts2components.struts2;

import cz.mikealdo.struts2components.struts2.annotations.SessionField;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.Page;

import java.util.Arrays;
import java.util.Collection;

public class ComponentModelTestFactory {

  public ComponentModelTestFactory() {
  }

  Page createPageActionWithoutComponent() {
    return new Page() {
      @Override
      public Component getChild(String id) {
        return null;
      }

      @Override
      public Collection<Component> getChildren() {
        return null;
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
    };
  }

  Page createPageActionWithEmptyComponent() {
    return new Page() {
      Component masterGrid = createEmptyComponent();

      @Override
      public Component getChild(String id) {
        return masterGrid;
      }

      @Override
      public Collection<Component> getChildren() {
        return Arrays.asList(masterGrid);
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
    };
  }

  Page createPageActionWithComponentWithOneSessionScopedField() {
    return new Page() {

      Component masterGrid = createComponentWithOneSessionField();

      @Override
      public Component getChild(String id) {
        return masterGrid;
      }

      @Override
      public Collection<Component> getChildren() {
        return Arrays.asList(masterGrid);
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
    };
  }

  Page createPageActionWithSessionScopedComponent() {
    return new Page() {

      @SessionField
      public WrappedIntegerAsComponent customComponentWithInteger = new WrappedIntegerAsComponent(1);

      @Override
      public Component getChild(String id) {
        return customComponentWithInteger;
      }

      @Override
      public Collection<Component> getChildren() {
        return Arrays.asList((Component) customComponentWithInteger);
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

    };
  }

  Page createPageActionWithCustomComponentWithOneSessionScopedField() {
    return new Page() {

      FirstCustomComponent firstCustomComponent = createCustomComponent();

      @Override
      public Component getChild(String id) {
        return firstCustomComponent;
      }

      @Override
      public Collection<Component> getChildren() {
        return Arrays.asList((Component) firstCustomComponent);
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

    };
  }

  private FirstCustomComponent createCustomComponent() {
    return new FirstCustomComponent();
  }

  class FirstCustomComponent implements Component {
    @SessionField
    Integer customInteger;
    @Override
    public String getId() {
      return "firstCustomComponent";
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
      return "";
    }

    Integer getCustomInteger() {
      return customInteger;
    }

    void setCustomInteger(Integer customInteger) {
      this.customInteger = customInteger;
    }
  }

  Component createEmptyComponent() {
    return new Component() {
      @Override
      public String getId() {
        return "masterGrid";
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
    };
  }

  Component createComponentWithOneSessionField() {
    return new Component() {
      @SessionField
      Integer customProperty = 1;
      @Override
      public String getId() {
        return "masterGrid";
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

      public Integer getCustomProperty() {
        return customProperty;
      }

      public void setCustomProperty(Integer customProperty) {
        this.customProperty = customProperty;
      }
    };
  }

}