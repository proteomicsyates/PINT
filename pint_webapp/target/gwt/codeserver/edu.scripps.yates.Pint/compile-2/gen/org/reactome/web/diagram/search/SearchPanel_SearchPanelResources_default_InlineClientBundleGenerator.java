package org.reactome.web.diagram.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class SearchPanel_SearchPanelResources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.search.SearchPanel.SearchPanelResources {
  private static SearchPanel_SearchPanelResources_default_InlineClientBundleGenerator _instance0 = new SearchPanel_SearchPanelResources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.search.SearchPanel.SearchPanelCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-search-SearchPanel-SearchPanelCSS-searchPanel {\n  float : " + ("right")  + ";\n  max-width : " + ("315px")  + ";\n}\n")) : ((".org-reactome-web-diagram-search-SearchPanel-SearchPanelCSS-searchPanel {\n  float : " + ("left")  + ";\n  max-width : " + ("315px")  + ";\n}\n"));
      }
      public java.lang.String searchPanel() {
        return "org-reactome-web-diagram-search-SearchPanel-SearchPanelCSS-searchPanel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.search.SearchPanel.SearchPanelCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.search.SearchPanel.SearchPanelCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.search.SearchPanel.SearchPanelCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.diagram.search.SearchPanel.SearchPanelResources::getCSS()();
    }
    return null;
  }-*/;
}
