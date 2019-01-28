package org.reactome.web.diagram.context.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class PathwaysDialogPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.context.dialogs.PathwaysDialogPanel.Resources {
  private static PathwaysDialogPanel_Resources_default_InlineClientBundleGenerator _instance0 = new PathwaysDialogPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.context.dialogs.PathwaysDialogPanel.ResourceCSS() {
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
        return (".org-reactome-web-diagram-context-dialogs-PathwaysDialogPanel-ResourceCSS-loaderIcon {\n  width : " + ("auto")  + ";\n  height : " + ("12px")  + ";\n  margin : " + ("5px"+ " " +"5px"+ " " +"0"+ " " +"5px")  + ";\n}\n");
      }
      public java.lang.String loaderIcon() {
        return "org-reactome-web-diagram-context-dialogs-PathwaysDialogPanel-ResourceCSS-loaderIcon";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.context.dialogs.PathwaysDialogPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.context.dialogs.PathwaysDialogPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void loaderInitializer() {
    loader = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "loader",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 16, 16, true, false
    );
  }
  private static class loaderInitializer {
    static {
      _instance0.loaderInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return loader;
    }
  }
  public com.google.gwt.resources.client.ImageResource loader() {
    return loaderInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.context.dialogs.PathwaysDialogPanel.ResourceCSS getCSS;
  private static final java.lang.String externalImage = "data:image/gif;base64,R0lGODlhEAAQAPIAAABl1gAAAABNowAaNwAAAAAmUgAzbQA6eyH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==";
  private static com.google.gwt.resources.client.ImageResource loader;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      loader(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("loader", loader());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.context.dialogs.PathwaysDialogPanel.Resources::getCSS()();
      case 'loader': return this.@org.reactome.web.diagram.context.dialogs.PathwaysDialogPanel.Resources::loader()();
    }
    return null;
  }-*/;
}
