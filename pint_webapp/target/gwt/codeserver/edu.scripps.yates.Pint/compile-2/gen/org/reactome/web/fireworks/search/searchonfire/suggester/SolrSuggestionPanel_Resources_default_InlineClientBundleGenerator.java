package org.reactome.web.fireworks.search.searchonfire.suggester;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class SolrSuggestionPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel.Resources {
  private static SolrSuggestionPanel_Resources_default_InlineClientBundleGenerator _instance0 = new SolrSuggestionPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel.SolrSuggestionPanelCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-mainPanel {\n  background-color : " + ("rgba(" + "250"+ ","+ " " +"250"+ ","+ " " +"250"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("1px"+ " " +"none")  + ";\n  border-radius : " + ("5px")  + ";\n  position : " + ("relative")  + ";\n  margin : " + ("5px"+ " " +"5px"+ " " +"0"+ " " +"0")  + ";\n  float : " + ("right")  + ";\n  width : " + ("100%")  + ";\n  max-height : " + ("235px")  + ";\n  overflow-y : " + ("auto")  + " !important;\n  overflow-x : " + ("hidden")  + " !important;\n  outline : ") + (("none")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -webkit-transition : " + ("height"+ " " +"1s")  + ";\n  -moz-transition : " + ("height"+ " " +"1s")  + ";\n  transition : " + ("height"+ " " +"1s")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("small")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-list {\n  max-height : " + ("120px")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-pager {\n  height : " + ("30px") ) + (";\n}\n.org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-facetsPanel {\n  max-height : " + ("60px")  + ";\n  overflow : " + ("hidden")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-optionsPanel {\n  height : " + ("22px")  + ";\n  overflow : " + ("hidden")  + ";\n}\n")) : ((".org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-mainPanel {\n  background-color : " + ("rgba(" + "250"+ ","+ " " +"250"+ ","+ " " +"250"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("1px"+ " " +"none")  + ";\n  border-radius : " + ("5px")  + ";\n  position : " + ("relative")  + ";\n  margin : " + ("5px"+ " " +"0"+ " " +"0"+ " " +"5px")  + ";\n  float : " + ("left")  + ";\n  width : " + ("100%")  + ";\n  max-height : " + ("235px")  + ";\n  overflow-y : " + ("auto")  + " !important;\n  overflow-x : " + ("hidden")  + " !important;\n  outline : ") + (("none")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -webkit-transition : " + ("height"+ " " +"1s")  + ";\n  -moz-transition : " + ("height"+ " " +"1s")  + ";\n  transition : " + ("height"+ " " +"1s")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("small")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-list {\n  max-height : " + ("120px")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-pager {\n  height : " + ("30px") ) + (";\n}\n.org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-facetsPanel {\n  max-height : " + ("60px")  + ";\n  overflow : " + ("hidden")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-optionsPanel {\n  height : " + ("22px")  + ";\n  overflow : " + ("hidden")  + ";\n}\n"));
      }
      public java.lang.String facetsPanel() {
        return "org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-facetsPanel";
      }
      public java.lang.String list() {
        return "org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-list";
      }
      public java.lang.String mainPanel() {
        return "org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-mainPanel";
      }
      public java.lang.String optionsPanel() {
        return "org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-optionsPanel";
      }
      public java.lang.String pager() {
        return "org-reactome-web-fireworks-search-searchonfire-suggester-SolrSuggestionPanel-SolrSuggestionPanelCSS-pager";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel.SolrSuggestionPanelCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel.SolrSuggestionPanelCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void interactorInitializer() {
    interactor = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "interactor",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 16, 16, false, false
    );
  }
  private static class interactorInitializer {
    static {
      _instance0.interactorInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return interactor;
    }
  }
  public com.google.gwt.resources.client.ImageResource interactor() {
    return interactorInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel.SolrSuggestionPanelCSS getCSS;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABAUlEQVR42mNgIBKcPXRIb9LGjcdDZs16AsIgNkiMWP0MkzduPC3U2vqfoaICjEFskBjRBvjOmvWWobLyP0NDAwQD2WAxooCcnJbn9Ok/STdASkqdISxsCUNJyd/S5cv/Y/UCtsCpqa/3YQgKWgjU+BtsW0nJH9PCws09q1efxwhEbIFTtmTJP7DG0tK/DCEhy8AuISVwPKZN+8/g47MN6HdtskLXY8aM/wyFhT8ZvL17gEoESI7fuiVL3oH5IAMzMl4zODikAJUykZbCNDQcGOLizjHU1/8HYZ3i4usdK1ZcJTUlMjHY26eCXIEzGokEAl7Tp38nPyVSnJSpkZlwBTYAQRIcRtNV4lAAAAAASUVORK5CYII=";
  private static com.google.gwt.resources.client.ImageResource interactor;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      interactor(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("interactor", interactor());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel.Resources::getCSS()();
      case 'interactor': return this.@org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel.Resources::interactor()();
    }
    return null;
  }-*/;
}
