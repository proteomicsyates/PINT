package org.reactome.web.fireworks.search.searchonfire.facets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class FacetTag_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.search.searchonfire.facets.FacetTag.Resources {
  private static FacetTag_Resources_default_InlineClientBundleGenerator _instance0 = new FacetTag_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.search.searchonfire.facets.FacetTag.FacetTagCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-base {\n  outline : " + ("none")  + ";\n  display : " + ("inline-block")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tag {\n  background-color : " + ("#a39da6")  + ";\n  color : " + ("black")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("4px")  + ";\n  opacity : " + ("0.9")  + ";\n  margin : " + ("3px"+ " " +"0"+ " " +"2px"+ " " +"5px")  + ";\n  padding : " + ("2px")  + ";\n  overflow : " + ("auto")  + " !important;\n  max-width : ") + (("200px")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("0.95em")  + " !important;\n  font-weight : " + ("bold")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tag img {\n  height : " + ("12px")  + ";\n  width : " + ("auto")  + ";\n  margin : " + ("2px"+ " " +"3px"+ " " +"0"+ " " +"3px")  + ";\n  float : " + ("right")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tagText {\n  float : " + ("right") ) + (";\n  margin-left : " + ("3px")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tagActive {\n  background-color : " + ("#1e94d0")  + ";\n  color : " + ("white")  + ";\n}\n")) : ((".org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-base {\n  outline : " + ("none")  + ";\n  display : " + ("inline-block")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tag {\n  background-color : " + ("#a39da6")  + ";\n  color : " + ("black")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("4px")  + ";\n  opacity : " + ("0.9")  + ";\n  margin : " + ("3px"+ " " +"5px"+ " " +"2px"+ " " +"0")  + ";\n  padding : " + ("2px")  + ";\n  overflow : " + ("auto")  + " !important;\n  max-width : ") + (("200px")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("0.95em")  + " !important;\n  font-weight : " + ("bold")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tag img {\n  height : " + ("12px")  + ";\n  width : " + ("auto")  + ";\n  margin : " + ("2px"+ " " +"3px"+ " " +"0"+ " " +"3px")  + ";\n  float : " + ("left")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tagText {\n  float : " + ("left") ) + (";\n  margin-right : " + ("3px")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tagActive {\n  background-color : " + ("#1e94d0")  + ";\n  color : " + ("white")  + ";\n}\n"));
      }
      public java.lang.String base() {
        return "org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-base";
      }
      public java.lang.String tag() {
        return "org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tag";
      }
      public java.lang.String tagActive() {
        return "org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tagActive";
      }
      public java.lang.String tagText() {
        return "org-reactome-web-fireworks-search-searchonfire-facets-FacetTag-FacetTagCSS-tagText";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.search.searchonfire.facets.FacetTag.FacetTagCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.search.searchonfire.facets.FacetTag.FacetTagCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.search.searchonfire.facets.FacetTag.FacetTagCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.fireworks.search.searchonfire.facets.FacetTag.Resources::getCSS()();
    }
    return null;
  }-*/;
}
