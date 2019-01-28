package org.reactome.web.diagram.controls.settings.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class ProfilesTabPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.settings.tabs.ProfilesTabPanel.Resources {
  private static ProfilesTabPanel_Resources_default_InlineClientBundleGenerator _instance0 = new ProfilesTabPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.settings.tabs.ProfilesTabPanel.ResourceCSS() {
      private boolean injected;
      public boolean ensureInjected() {
        if (!injected) {
          injected = true;
          com.google.gwt.dom.client.StyleInjector.inject(getText());
          return true;
        }
        return false;
      }
      public String getName() {
        return "getCSS";
      }
      public String getText() {
        return (".org-reactome-web-diagram-controls-settings-tabs-ProfilesTabPanel-ResourceCSS-profilesPanel {\n  padding : " + ("0"+ " " +"10px"+ " " +"0"+ " " +"10px")  + ";\n  font-size : " + ("medium")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-ProfilesTabPanel-ResourceCSS-profilesPanel select {\n  background-color : " + ("transparent")  + ";\n  color : " + ("white")  + ";\n  border : " + ("solid"+ " " +"1px"+ " " +"white")  + ";\n  height : " + ("1.5em")  + ";\n  width : " + ("100%")  + ";\n  margin-top : " + ("4px")  + ";\n  outline : " + ("none")  + ";\n  font-size : " + ("medium")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-ProfilesTabPanel-ResourceCSS-profilesPanel select option {\n  color : ") + (("black")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-ProfilesTabPanel-ResourceCSS-tabHeader {\n  margin-top : " + ("5px")  + ";\n  font-weight : " + ("bold")  + ";\n  background-color : " + ("#1e94d0")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  text-align : " + ("center")  + ";\n  padding-bottom : " + ("2px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-ProfilesTabPanel-ResourceCSS-profileLabel {\n  margin-top : " + ("5px")  + ";\n  color : " + ("#005a75")  + ";\n}\n");
      }
      public java.lang.String profileLabel() {
        return "org-reactome-web-diagram-controls-settings-tabs-ProfilesTabPanel-ResourceCSS-profileLabel";
      }
      public java.lang.String profilesPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-ProfilesTabPanel-ResourceCSS-profilesPanel";
      }
      public java.lang.String tabHeader() {
        return "org-reactome-web-diagram-controls-settings-tabs-ProfilesTabPanel-ResourceCSS-tabHeader";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.settings.tabs.ProfilesTabPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.settings.tabs.ProfilesTabPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.controls.settings.tabs.ProfilesTabPanel.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.controls.settings.tabs.ProfilesTabPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
