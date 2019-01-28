package org.reactome.web.fireworks.search.searchonfire.facets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class FacetsPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.search.searchonfire.facets.FacetsPanel.Resources {
  private static FacetsPanel_Resources_default_InlineClientBundleGenerator _instance0 = new FacetsPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.search.searchonfire.facets.FacetsPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-title {\n  font-size : " + ("1.1em")  + ";\n  font-weight : " + ("bold")  + ";\n  color : " + ("#1e94d0")  + ";\n  margin-right : " + ("5px")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-tagContainer {\n  width : " + ("100%")  + ";\n  height : " + ("40px")  + ";\n  overflow-x : " + ("visible")  + ";\n  overflow-y : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-outerContainer {\n  overflow-x : " + ("hidden")  + ";\n  overflow-y : ") + (("auto")  + ";\n}\n")) : ((".org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-title {\n  font-size : " + ("1.1em")  + ";\n  font-weight : " + ("bold")  + ";\n  color : " + ("#1e94d0")  + ";\n  margin-left : " + ("5px")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-tagContainer {\n  width : " + ("100%")  + ";\n  height : " + ("40px")  + ";\n  overflow-x : " + ("visible")  + ";\n  overflow-y : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-outerContainer {\n  overflow-x : " + ("hidden")  + ";\n  overflow-y : ") + (("auto")  + ";\n}\n"));
      }
      public java.lang.String outerContainer() {
        return "org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-outerContainer";
      }
      public java.lang.String tagContainer() {
        return "org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-tagContainer";
      }
      public java.lang.String title() {
        return "org-reactome-web-fireworks-search-searchonfire-facets-FacetsPanel-ResourceCSS-title";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.search.searchonfire.facets.FacetsPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.search.searchonfire.facets.FacetsPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.search.searchonfire.facets.FacetsPanel.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.fireworks.search.searchonfire.facets.FacetsPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
