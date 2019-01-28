package org.reactome.web.fireworks.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class Tooltip_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.util.Tooltip.Resources {
  private static Tooltip_Resources_default_InlineClientBundleGenerator _instance0 = new Tooltip_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.util.Tooltip.ResourceCSS() {
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
        return (".org-reactome-web-fireworks-util-Tooltip-ResourceCSS-popup {\n  display : " + ("inline-block")  + ";\n  color : " + ("#000")  + ";\n  position : " + ("relative")  + ";\n  padding : " + ("5px")  + ";\n  background-color : " + ("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("rgba(" + "30"+ ","+ " " +"149"+ ","+ " " +"207"+ ","+ " " +"0.8" + ")"+ " " +"solid"+ " " +"1px")  + ";\n  -webkit-border-radius : " + ("5px")  + ";\n  -moz-border-radius : " + ("5px")  + ";\n  border-radius : " + ("5px")  + ";\n  -webkit-box-shadow : " + ("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  -moz-box-shadow : ") + (("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  box-shadow : " + ("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  page-break-inside : " + ("avoid")  + ";\n  overflow : " + ("hidden")  + ";\n  -ms-overflow-style : " + ("none")  + ";\n}\n");
      }
      public java.lang.String popup() {
        return "org-reactome-web-fireworks-util-Tooltip-ResourceCSS-popup";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.util.Tooltip.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.util.Tooltip.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.util.Tooltip.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.fireworks.util.Tooltip.Resources::getCSS()();
    }
    return null;
  }-*/;
}
