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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import cz.mikealdo.struts2components.components.SimpleComponent;
import cz.mikealdo.struts2components.struts2.components.Component;
import cz.mikealdo.struts2components.struts2.components.ComponentOnPageAware;
import cz.mikealdo.struts2components.struts2.components.Page;

@Conversion()
public class IndexAction extends ActionSupport implements Page {
    
    private Date now = new Date(System.currentTimeMillis());
    private SimpleComponent simpleComponent = new SimpleComponent(0);
    
    @TypeConversion(converter = "cz.mikealdo.struts2components.converter.DateConverter")
    public Date getDateNow() { return now; }
    
    public String execute() throws Exception {
        now = new Date(System.currentTimeMillis());
        return SUCCESS;
    }



    @Override
    public Component getChild(String id) {
        return simpleComponent;
    }

    @Override
    public Collection<Component> getChildren() {
        return ImmutableList.of(simpleComponent);
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
        return simpleComponent.render();
    }

    public SimpleComponent getSimpleComponent() {
        return simpleComponent;
    }
}
