package org.reactome.web.diagram.controls.settings.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class AboutTabPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.settings.tabs.AboutTabPanel.Resources {
  private static AboutTabPanel_Resources_default_InlineClientBundleGenerator _instance0 = new AboutTabPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.settings.tabs.AboutTabPanel.ResourceCSS() {
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
        return (".org-reactome-web-diagram-controls-settings-tabs-AboutTabPanel-ResourceCSS-aboutPanel {\n  padding : " + ("0"+ " " +"10px"+ " " +"0"+ " " +"10px")  + ";\n  font-size : " + ("medium")  + " !important;\n  overflow : " + ("hidden")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-AboutTabPanel-ResourceCSS-htmlPanel {\n  height : " + ("242px")  + ";\n  margin-top : " + ("5px")  + ";\n  font-size : " + ("small")  + " !important;\n  overflow : " + ("auto")  + ";\n  -ms-overflow-style : " + ("none")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-AboutTabPanel-ResourceCSS-htmlPanel p {\n  margin : " + ("0"+ " " +"0"+ " " +"10px"+ " " +"0")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-AboutTabPanel-ResourceCSS-htmlPanel::-webkit-scrollbar {\n  width : " + ("0")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-AboutTabPanel-ResourceCSS-tabHeader {\n  margin-top : ") + (("5px")  + ";\n  font-weight : " + ("bold")  + ";\n  background-color : " + ("#1e94d0")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  text-align : " + ("center")  + ";\n  padding-bottom : " + ("2px")  + ";\n}\n");
      }
      public java.lang.String aboutPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-AboutTabPanel-ResourceCSS-aboutPanel";
      }
      public java.lang.String htmlPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-AboutTabPanel-ResourceCSS-htmlPanel";
      }
      public java.lang.String tabHeader() {
        return "org-reactome-web-diagram-controls-settings-tabs-AboutTabPanel-ResourceCSS-tabHeader";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.settings.tabs.AboutTabPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.settings.tabs.AboutTabPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.controls.settings.tabs.AboutTabPanel.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.diagram.controls.settings.tabs.AboutTabPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
