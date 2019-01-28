package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class SolrSelectionInfoPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.search.searchonfire.infopanel.SolrSelectionInfoPanel.Resources {
  private static SolrSelectionInfoPanel_Resources_default_InlineClientBundleGenerator _instance0 = new SolrSelectionInfoPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.search.searchonfire.infopanel.SolrSelectionInfoPanel.ResourceCSS() {
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
        return (".org-reactome-web-fireworks-search-searchonfire-infopanel-SolrSelectionInfoPanel-ResourceCSS-container {\n  overflow : " + ("auto")  + ";\n  max-height : " + ("380px")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n}\n");
      }
      public java.lang.String container() {
        return "org-reactome-web-fireworks-search-searchonfire-infopanel-SolrSelectionInfoPanel-ResourceCSS-container";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.search.searchonfire.infopanel.SolrSelectionInfoPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.search.searchonfire.infopanel.SolrSelectionInfoPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.search.searchonfire.infopanel.SolrSelectionInfoPanel.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.fireworks.search.searchonfire.infopanel.SolrSelectionInfoPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
