package org.reactome.web.fireworks.controls.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class RightContainerPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.controls.settings.RightContainerPanel.Resources {
  private static RightContainerPanel_Resources_default_InlineClientBundleGenerator _instance0 = new RightContainerPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.controls.settings.RightContainerPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-controls-settings-RightContainerPanel-ResourceCSS-container {\n  position : " + ("absolute")  + ";\n  left : " + ("-290px")  + ";\n  border-radius : " + ("15px")  + ";\n  margin-top : " + ("-150px")  + ";\n  top : " + ("50%")  + ";\n}\n")) : ((".org-reactome-web-fireworks-controls-settings-RightContainerPanel-ResourceCSS-container {\n  position : " + ("absolute")  + ";\n  right : " + ("-290px")  + ";\n  border-radius : " + ("15px")  + ";\n  margin-top : " + ("-150px")  + ";\n  top : " + ("50%")  + ";\n}\n"));
      }
      public java.lang.String container() {
        return "org-reactome-web-fireworks-controls-settings-RightContainerPanel-ResourceCSS-container";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.controls.settings.RightContainerPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.controls.settings.RightContainerPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.controls.settings.RightContainerPanel.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.fireworks.controls.settings.RightContainerPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
