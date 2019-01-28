package org.reactome.web.diagram.context.dialogs.molecules;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class MoleculesTable_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.context.dialogs.molecules.MoleculesTable.Resources {
  private static MoleculesTable_Resources_default_InlineClientBundleGenerator _instance0 = new MoleculesTable_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.context.dialogs.molecules.MoleculesTable.ResourceCSS() {
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
        return (".org-reactome-web-diagram-context-dialogs-molecules-MoleculesTable-ResourceCSS-unselectedExpression {\n  background-color : " + ("transparent")  + " !important;\n  -webkit-transition : " + ("background-color"+ " " +"1s")  + ";\n  transition : " + ("background-color"+ " " +"1s")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-molecules-MoleculesTable-ResourceCSS-selectedExpression {\n  background-color : " + ("white")  + " !important;\n  -webkit-transition : " + ("background-color"+ " " +"1s")  + ";\n  transition : " + ("background-color"+ " " +"1s")  + ";\n}\n");
      }
      public java.lang.String selectedExpression() {
        return "org-reactome-web-diagram-context-dialogs-molecules-MoleculesTable-ResourceCSS-selectedExpression";
      }
      public java.lang.String unselectedExpression() {
        return "org-reactome-web-diagram-context-dialogs-molecules-MoleculesTable-ResourceCSS-unselectedExpression";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.context.dialogs.molecules.MoleculesTable.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.context.dialogs.molecules.MoleculesTable.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.context.dialogs.molecules.MoleculesTable.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.diagram.context.dialogs.molecules.MoleculesTable.Resources::getCSS()();
    }
    return null;
  }-*/;
}
