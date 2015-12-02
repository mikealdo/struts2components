/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.mikealdo.struts2components.actions;

import com.google.common.collect.ImmutableList;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Collection;

import com.opensymphony.xwork2.conversion.annotations.Conversion;
import cz.mikealdo.struts2components.components.FirstSimpleComponent;
import cz.mikealdo.struts2components.components.SecondSimpleComponent;
import cz.mikealdo.struts2components.components.SimpleComponent;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.Page;
import org.springframework.beans.factory.annotation.Autowired;

@Conversion()
public class IndexAction extends ActionSupport implements Page {

    private FirstSimpleComponent firstComponent;
    private SecondSimpleComponent secondComponent;

    @Autowired
    public IndexAction(FirstSimpleComponent firstComponent, SecondSimpleComponent secondComponent) {
        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
    }

    public String execute() throws Exception {
        return render();
    }

    @Override
    public Component getChild(String id) {
        for (Component component : getChildren()) {
            if (component.getId().equals(id))
                return component;
        }
        throw new IllegalArgumentException("No such component exist");
    }

    @Override
    public Collection<Component> getChildren() {
        return ImmutableList.of(firstComponent, secondComponent);
    }

    @Override
    public String getId() {
        return "indexAction";
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public String render() {
        firstComponent.render();
        secondComponent.render();
        return INPUT;
    }

    public SimpleComponent getFirstComponent() {
        return firstComponent;
    }

    public SimpleComponent getSecondComponent() {
        return secondComponent;
    }
}
