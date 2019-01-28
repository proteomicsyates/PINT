package org.reactome.web.diagram.context.sections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class Section_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.context.sections.Section.Resources {
  private static Section_Resources_default_InlineClientBundleGenerator _instance0 = new Section_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.context.sections.Section.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-context-sections-Section-ResourceCSS-sectionHeader {\n  border-radius : " + ("5px")  + ";\n  font-weight : " + ("bold")  + ";\n  font-size : " + ("small")  + ";\n  margin-top : " + ("2px")  + ";\n  padding-right : " + ("5px")  + ";\n  background-color : " + ("#005a75")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerTable {\n  overflow : " + ("auto")  + ";\n  padding : " + ("0")  + ";\n  margin : " + ("0")  + ";\n  table-layout : " + ("fixed")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerTable tbody tr:first-child, .org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable tbody tr {\n  background-color : ") + (("#0785ab")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerTable tbody tr:first-child td {\n  font : " + ("bold"+ " " +"8px"+ " " +"arial")  + ";\n  min-width : " + ("50px")  + " !important;\n  max-width : " + ("50px")  + ";\n  table-layout : " + ("fixed")  + ";\n  cursor : " + ("pointer")  + ";\n  -webkit-transition : " + ("background-color"+ " " +"1s")  + ";\n  transition : " + ("background-color"+ " " +"1s")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerScrollPanel {\n  margin-right : " + ("53px")  + ";\n  margin-top : " + ("0")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable {\n  overflow : " + ("auto") ) + (";\n  padding : " + ("0")  + ";\n  margin : " + ("0")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable tbody tr:hover {\n  color : " + ("black")  + ";\n  background : " + ("rgba(" + "255"+ ","+ " " +"255"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable tbody tr td {\n  font : " + ("9px"+ " " +"arial")  + ";\n  cursor : " + ("pointer")  + ";\n  overflow : " + ("hidden")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  -webkit-transition : " + ("background-color"+ " " +"1s")  + ";\n  transition : ") + (("background-color"+ " " +"1s")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-highlightedRow {\n  background-color : " + ("darkorange")  + " !important;\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-highlightedCol {\n  background-color : " + ("blue")  + " !important;\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-selectedExpressionColumn {\n  border-left : " + ("1px"+ " " +"dashed"+ " " +"white")  + ";\n  border-right : " + ("1px"+ " " +"dashed"+ " " +"white")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataScrollPanel {\n  margin-bottom : " + ("3px")  + ";\n}\n")) : ((".org-reactome-web-diagram-context-sections-Section-ResourceCSS-sectionHeader {\n  border-radius : " + ("5px")  + ";\n  font-weight : " + ("bold")  + ";\n  font-size : " + ("small")  + ";\n  margin-top : " + ("2px")  + ";\n  padding-left : " + ("5px")  + ";\n  background-color : " + ("#005a75")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerTable {\n  overflow : " + ("auto")  + ";\n  padding : " + ("0")  + ";\n  margin : " + ("0")  + ";\n  table-layout : " + ("fixed")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerTable tbody tr:first-child, .org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable tbody tr {\n  background-color : ") + (("#0785ab")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerTable tbody tr:first-child td {\n  font : " + ("bold"+ " " +"8px"+ " " +"arial")  + ";\n  min-width : " + ("50px")  + " !important;\n  max-width : " + ("50px")  + ";\n  table-layout : " + ("fixed")  + ";\n  cursor : " + ("pointer")  + ";\n  -webkit-transition : " + ("background-color"+ " " +"1s")  + ";\n  transition : " + ("background-color"+ " " +"1s")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerScrollPanel {\n  margin-left : " + ("53px")  + ";\n  margin-top : " + ("0")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable {\n  overflow : " + ("auto") ) + (";\n  padding : " + ("0")  + ";\n  margin : " + ("0")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable tbody tr:hover {\n  color : " + ("black")  + ";\n  background : " + ("rgba(" + "255"+ ","+ " " +"255"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable tbody tr td {\n  font : " + ("9px"+ " " +"arial")  + ";\n  cursor : " + ("pointer")  + ";\n  overflow : " + ("hidden")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  -webkit-transition : " + ("background-color"+ " " +"1s")  + ";\n  transition : ") + (("background-color"+ " " +"1s")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-highlightedRow {\n  background-color : " + ("darkorange")  + " !important;\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-highlightedCol {\n  background-color : " + ("blue")  + " !important;\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-selectedExpressionColumn {\n  border-right : " + ("1px"+ " " +"dashed"+ " " +"white")  + ";\n  border-left : " + ("1px"+ " " +"dashed"+ " " +"white")  + ";\n}\n.org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataScrollPanel {\n  margin-bottom : " + ("3px")  + ";\n}\n"));
      }
      public java.lang.String dataScrollPanel() {
        return "org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataScrollPanel";
      }
      public java.lang.String dataTable() {
        return "org-reactome-web-diagram-context-sections-Section-ResourceCSS-dataTable";
      }
      public java.lang.String headerScrollPanel() {
        return "org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerScrollPanel";
      }
      public java.lang.String headerTable() {
        return "org-reactome-web-diagram-context-sections-Section-ResourceCSS-headerTable";
      }
      public java.lang.String highlightedCol() {
        return "org-reactome-web-diagram-context-sections-Section-ResourceCSS-highlightedCol";
      }
      public java.lang.String highlightedRow() {
        return "org-reactome-web-diagram-context-sections-Section-ResourceCSS-highlightedRow";
      }
      public java.lang.String sectionHeader() {
        return "org-reactome-web-diagram-context-sections-Section-ResourceCSS-sectionHeader";
      }
      public java.lang.String selectedExpressionColumn() {
        return "org-reactome-web-diagram-context-sections-Section-ResourceCSS-selectedExpressionColumn";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.context.sections.Section.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.context.sections.Section.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.context.sections.Section.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.diagram.context.sections.Section.Resources::getCSS()();
    }
    return null;
  }-*/;
}
