package org.reactome.web.diagram.search.suggester;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class SuggestionPanel_SuggestionResources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.search.suggester.SuggestionPanel.SuggestionResources {
  private static SuggestionPanel_SuggestionResources_default_InlineClientBundleGenerator _instance0 = new SuggestionPanel_SuggestionResources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.search.suggester.SuggestionPanel.SuggestionPanelCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-search-suggester-SuggestionPanel-SuggestionPanelCSS-suggestionPanel {\n  background-color : " + ("rgba(" + "250"+ ","+ " " +"250"+ ","+ " " +"250"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("1px"+ " " +"none")  + ";\n  border-radius : " + ("5px")  + ";\n  position : " + ("relative")  + ";\n  margin : " + ("5px"+ " " +"5px"+ " " +"0"+ " " +"0")  + ";\n  float : " + ("right")  + ";\n  width : " + ("100%")  + ";\n  max-height : " + ("100px")  + ";\n  overflow : " + ("auto")  + " !important;\n  outline : " + ("none")  + ";\n  font-size : ") + (("small")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -webkit-transition : " + ("height"+ " " +"1s")  + ";\n  -moz-transition : " + ("height"+ " " +"1s")  + ";\n  transition : " + ("height"+ " " +"1s")  + ";\n}\n")) : ((".org-reactome-web-diagram-search-suggester-SuggestionPanel-SuggestionPanelCSS-suggestionPanel {\n  background-color : " + ("rgba(" + "250"+ ","+ " " +"250"+ ","+ " " +"250"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("1px"+ " " +"none")  + ";\n  border-radius : " + ("5px")  + ";\n  position : " + ("relative")  + ";\n  margin : " + ("5px"+ " " +"0"+ " " +"0"+ " " +"5px")  + ";\n  float : " + ("left")  + ";\n  width : " + ("100%")  + ";\n  max-height : " + ("100px")  + ";\n  overflow : " + ("auto")  + " !important;\n  outline : " + ("none")  + ";\n  font-size : ") + (("small")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -webkit-transition : " + ("height"+ " " +"1s")  + ";\n  -moz-transition : " + ("height"+ " " +"1s")  + ";\n  transition : " + ("height"+ " " +"1s")  + ";\n}\n"));
      }
      public java.lang.String suggestionPanel() {
        return "org-reactome-web-diagram-search-suggester-SuggestionPanel-SuggestionPanelCSS-suggestionPanel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.search.suggester.SuggestionPanel.SuggestionPanelCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.search.suggester.SuggestionPanel.SuggestionPanelCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.search.suggester.SuggestionPanel.SuggestionPanelCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.diagram.search.suggester.SuggestionPanel.SuggestionResources::getCSS()();
    }
    return null;
  }-*/;
}
