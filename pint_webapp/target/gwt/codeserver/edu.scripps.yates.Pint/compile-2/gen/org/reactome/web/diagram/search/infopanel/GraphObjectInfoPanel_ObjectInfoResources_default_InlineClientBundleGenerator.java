package org.reactome.web.diagram.search.infopanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class GraphObjectInfoPanel_ObjectInfoResources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.search.infopanel.GraphObjectInfoPanel.ObjectInfoResources {
  private static GraphObjectInfoPanel_ObjectInfoResources_default_InlineClientBundleGenerator _instance0 = new GraphObjectInfoPanel_ObjectInfoResources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.search.infopanel.GraphObjectInfoPanel.SuggestionPanelCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-objectInfoPanel {\n  background-color : " + ("rgba(" + "182"+ ","+ " " +"232"+ ","+ " " +"249"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("1px"+ " " +"none")  + ";\n  border-radius : " + ("5px")  + ";\n  position : " + ("relative")  + ";\n  margin : " + ("10px"+ " " +"5px"+ " " +"-20px"+ " " +"0")  + ";\n  float : " + ("right")  + ";\n  width : " + ("100%")  + ";\n  overflow : " + ("auto")  + " !important;\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : ") + (("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -webkit-transition : " + ("height"+ " " +"1s")  + ";\n  -moz-transition : " + ("height"+ " " +"1s")  + ";\n  transition : " + ("height"+ " " +"1s")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-objectInfoContent {\n  padding : " + ("10px")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-infoHeader {\n  font-weight : " + ("bolder")  + ";\n  font-size : " + ("medium")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectListPanel {\n  margin-top : " + ("10px")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectListTitle {\n  font-weight : " + ("bolder")  + ";\n  margin-top : " + ("5px")  + ";\n  background-color : " + ("rgba(" + "165"+ ","+ " " +"226"+ ","+ " " +"249"+ ","+ " " +"0.8" + ")") ) + (";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectList {\n  margin-right : " + ("5px")  + ";\n  max-height : " + ("75px")  + ";\n  overflow : " + ("auto")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-listItem {\n  height : " + ("20px")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-listItemLink {\n  margin-right : " + ("3px")  + ";\n}\n")) : ((".org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-objectInfoPanel {\n  background-color : " + ("rgba(" + "182"+ ","+ " " +"232"+ ","+ " " +"249"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("1px"+ " " +"none")  + ";\n  border-radius : " + ("5px")  + ";\n  position : " + ("relative")  + ";\n  margin : " + ("10px"+ " " +"0"+ " " +"-20px"+ " " +"5px")  + ";\n  float : " + ("left")  + ";\n  width : " + ("100%")  + ";\n  overflow : " + ("auto")  + " !important;\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : ") + (("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -webkit-transition : " + ("height"+ " " +"1s")  + ";\n  -moz-transition : " + ("height"+ " " +"1s")  + ";\n  transition : " + ("height"+ " " +"1s")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-objectInfoContent {\n  padding : " + ("10px")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-infoHeader {\n  font-weight : " + ("bolder")  + ";\n  font-size : " + ("medium")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectListPanel {\n  margin-top : " + ("10px")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectListTitle {\n  font-weight : " + ("bolder")  + ";\n  margin-top : " + ("5px")  + ";\n  background-color : " + ("rgba(" + "165"+ ","+ " " +"226"+ ","+ " " +"249"+ ","+ " " +"0.8" + ")") ) + (";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectList {\n  margin-left : " + ("5px")  + ";\n  max-height : " + ("75px")  + ";\n  overflow : " + ("auto")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-listItem {\n  height : " + ("20px")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n}\n.org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-listItemLink {\n  margin-left : " + ("3px")  + ";\n}\n"));
      }
      public java.lang.String databaseObjectList() {
        return "org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectList";
      }
      public java.lang.String databaseObjectListPanel() {
        return "org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectListPanel";
      }
      public java.lang.String databaseObjectListTitle() {
        return "org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-databaseObjectListTitle";
      }
      public java.lang.String infoHeader() {
        return "org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-infoHeader";
      }
      public java.lang.String listItem() {
        return "org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-listItem";
      }
      public java.lang.String listItemLink() {
        return "org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-listItemLink";
      }
      public java.lang.String objectInfoContent() {
        return "org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-objectInfoContent";
      }
      public java.lang.String objectInfoPanel() {
        return "org-reactome-web-diagram-search-infopanel-GraphObjectInfoPanel-SuggestionPanelCSS-objectInfoPanel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.search.infopanel.GraphObjectInfoPanel.SuggestionPanelCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.search.infopanel.GraphObjectInfoPanel.SuggestionPanelCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.search.infopanel.GraphObjectInfoPanel.SuggestionPanelCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.diagram.search.infopanel.GraphObjectInfoPanel.ObjectInfoResources::getCSS()();
    }
    return null;
  }-*/;
}
