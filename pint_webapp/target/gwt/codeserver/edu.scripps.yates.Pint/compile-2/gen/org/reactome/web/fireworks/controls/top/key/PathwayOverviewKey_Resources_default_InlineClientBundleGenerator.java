package org.reactome.web.fireworks.controls.top.key;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class PathwayOverviewKey_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.controls.top.key.PathwayOverviewKey.Resources {
  private static PathwayOverviewKey_Resources_default_InlineClientBundleGenerator _instance0 = new PathwayOverviewKey_Resources_default_InlineClientBundleGenerator();
  private void fireworkskeyInitializer() {
    fireworkskey = new com.google.gwt.resources.client.TextResource() {
      // jar:file:/C:/Users/salvador/.m2/repository/org/reactome/web/fireworks/1.3.7/fireworks-1.3.7.jar!/org/reactome/web/fireworks/controls/top/key/data/fireworkskey.html
      public String getText() {
        return "<ul>\n    <li>Node size depends on the number of entities inside the pathway.</li>\n\n    <li>The centre of each burst is a top-level pathway.</li>\n\n    <li>Every node in the outer rings represent a subpathway.</li>\n</ul>";
      }
      public String getName() {
        return "fireworkskey";
      }
    }
    ;
  }
  private static class fireworkskeyInitializer {
    static {
      _instance0.fireworkskeyInitializer();
    }
    static com.google.gwt.resources.client.TextResource get() {
      return fireworkskey;
    }
  }
  public com.google.gwt.resources.client.TextResource fireworkskey() {
    return fireworkskeyInitializer.get();
  }
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.controls.top.key.PathwayOverviewKey.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-canvases {\n  outline : " + ("none")  + ";\n  height : " + ("100px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-canvases canvas {\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-bullets {\n  text-align : " + ("justify")  + ";\n  font-family : " + ("Arial"+ ","+ " " +"serif")  + ";\n  font-size : " + ("13px")  + ";\n  margin : " + ("20px"+ " " +"0"+ " " +"0"+ " " +"30px")  + ";\n  width : " + ("175px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-bullets li {\n  list-style-type : " + ("square")  + ";\n  margin-top : " + ("5px")  + ";\n}\n")) : ((".org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-canvases {\n  outline : " + ("none")  + ";\n  height : " + ("100px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-canvases canvas {\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-bullets {\n  text-align : " + ("justify")  + ";\n  font-family : " + ("Arial"+ ","+ " " +"serif")  + ";\n  font-size : " + ("13px")  + ";\n  margin : " + ("20px"+ " " +"30px"+ " " +"0"+ " " +"0")  + ";\n  width : " + ("175px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-bullets li {\n  list-style-type : " + ("square")  + ";\n  margin-top : " + ("5px")  + ";\n}\n"));
      }
      public java.lang.String bullets() {
        return "org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-bullets";
      }
      public java.lang.String canvases() {
        return "org-reactome-web-fireworks-controls-top-key-PathwayOverviewKey-ResourceCSS-canvases";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.controls.top.key.PathwayOverviewKey.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.controls.top.key.PathwayOverviewKey.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.resources.client.TextResource fireworkskey;
  private static org.reactome.web.fireworks.controls.top.key.PathwayOverviewKey.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      fireworkskey(), 
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("fireworkskey", fireworkskey());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'fireworkskey': return this.@org.reactome.web.fireworks.controls.top.key.PathwayOverviewKey.Resources::fireworkskey()();
      case 'getCSS': return this.@org.reactome.web.fireworks.controls.top.key.PathwayOverviewKey.Resources::getCSS()();
    }
    return null;
  }-*/;
}
