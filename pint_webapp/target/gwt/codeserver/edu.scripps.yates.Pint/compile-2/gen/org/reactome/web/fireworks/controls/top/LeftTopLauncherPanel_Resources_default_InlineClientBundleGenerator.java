package org.reactome.web.fireworks.controls.top;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class LeftTopLauncherPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.controls.top.LeftTopLauncherPanel.Resources {
  private static LeftTopLauncherPanel_Resources_default_InlineClientBundleGenerator _instance0 = new LeftTopLauncherPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.controls.top.LeftTopLauncherPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-controls-top-LeftTopLauncherPanel-ResourceCSS-launcherPanel {\n  position : " + ("relative")  + ";\n  top : " + ("5px")  + ";\n  right : " + ("5px")  + ";\n}\n")) : ((".org-reactome-web-fireworks-controls-top-LeftTopLauncherPanel-ResourceCSS-launcherPanel {\n  position : " + ("relative")  + ";\n  top : " + ("5px")  + ";\n  left : " + ("5px")  + ";\n}\n"));
      }
      public java.lang.String launcherPanel() {
        return "org-reactome-web-fireworks-controls-top-LeftTopLauncherPanel-ResourceCSS-launcherPanel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.controls.top.LeftTopLauncherPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.controls.top.LeftTopLauncherPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.controls.top.LeftTopLauncherPanel.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.fireworks.controls.top.LeftTopLauncherPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
