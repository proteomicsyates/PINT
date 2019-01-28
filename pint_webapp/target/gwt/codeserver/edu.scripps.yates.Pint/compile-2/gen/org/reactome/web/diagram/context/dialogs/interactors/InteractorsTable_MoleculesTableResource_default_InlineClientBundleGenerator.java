package org.reactome.web.diagram.context.dialogs.interactors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class InteractorsTable_MoleculesTableResource_default_InlineClientBundleGenerator implements org.reactome.web.diagram.context.dialogs.interactors.InteractorsTable.MoleculesTableResource {
  private static InteractorsTable_MoleculesTableResource_default_InlineClientBundleGenerator _instance0 = new InteractorsTable_MoleculesTableResource_default_InlineClientBundleGenerator();
  private void dataGridLoadingInitializer() {
    dataGridLoading = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "dataGridLoading",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ?externalImage_rtl : externalImage),
      0, 0, 43, 11, true, false
    );
  }
  private static class dataGridLoadingInitializer {
    static {
      _instance0.dataGridLoadingInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return dataGridLoading;
    }
  }
  public com.google.gwt.resources.client.ImageResource dataGridLoading() {
    return dataGridLoadingInitializer.get();
  }
  private void dataGridSortAscendingInitializer() {
    dataGridSortAscending = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "dataGridSortAscending",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ?externalImage_rtl0 : externalImage0),
      0, 0, 11, 7, false, false
    );
  }
  private static class dataGridSortAscendingInitializer {
    static {
      _instance0.dataGridSortAscendingInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return dataGridSortAscending;
    }
  }
  public com.google.gwt.resources.client.ImageResource dataGridSortAscending() {
    return dataGridSortAscendingInitializer.get();
  }
  private void dataGridSortDescendingInitializer() {
    dataGridSortDescending = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "dataGridSortDescending",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ?externalImage_rtl1 : externalImage1),
      0, 0, 11, 7, false, false
    );
  }
  private static class dataGridSortDescendingInitializer {
    static {
      _instance0.dataGridSortDescendingInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return dataGridSortDescending;
    }
  }
  public com.google.gwt.resources.client.ImageResource dataGridSortDescending() {
    return dataGridSortDescendingInitializer.get();
  }
  private void dataGridStyleInitializer() {
    dataGridStyle = new org.reactome.web.diagram.context.dialogs.interactors.InteractorsTable.MoleculesTableResource.MoleculesStyle() {
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
        return "dataGridStyle";
      }
      public String getText() {
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridWidget {\n  background-color : " + ("transparent")  + " !important;\n  font-size : " + ("smaller")  + ";\n  cursor : " + ("default")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridFooter {\n  color : " + ("black")  + ";\n  border-top : " + ("2px"+ " " +"solid"+ " " +"#6f7277")  + ";\n  text-align : " + ("right")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridHeader {\n  color : " + ("black")  + ";\n  border-bottom : " + ("2px"+ " " +"solid"+ " " +"#6f7277")  + ";\n  text-align : ") + (("center")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  padding : " + ("0")  + ";\n  text-shadow : " + ("none")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridCell {\n  font-size : " + ("smaller")  + ";\n  border : " + ("none")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  padding : " + ("0") ) + (";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridFirstColumnHeader {\n  text-align : " + ("right")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridLastColumnHeader {\n  text-align : " + ("left")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSortableHeader {\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSortableHeader:hover {\n  color : " + ("#6c6b6b")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridEvenRow {\n  background : " + ("rgba(" + "100"+ ","+ " " +"100"+ ","+ " " +"200"+ ","+ " " +"0.5" + ")")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridOddRow {\n  background : " + ("rgba(" + "150"+ ","+ " " +"150"+ ","+ " " +"250"+ ","+ " " +"0.5" + ")")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridHoveredRow {\n  color : " + ("black")  + ";\n  background : " + ("rgba(" + "255"+ ","+ " " +"255"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridKeyboardSelectedRow {\n  color : " + ("black")  + ";\n  background : ") + (("rgba(" + "200"+ ","+ " " +"200"+ ","+ " " +"200"+ ","+ " " +"0.5" + ")")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSelectedRow {\n  background : " + ("#628cd5")  + ";\n  color : " + ("white")  + ";\n  height : " + ("auto")  + ";\n  overflow : " + ("auto")  + ";\n}\n")) : ((".org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridWidget {\n  background-color : " + ("transparent")  + " !important;\n  font-size : " + ("smaller")  + ";\n  cursor : " + ("default")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridFooter {\n  color : " + ("black")  + ";\n  border-top : " + ("2px"+ " " +"solid"+ " " +"#6f7277")  + ";\n  text-align : " + ("left")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridHeader {\n  color : " + ("black")  + ";\n  border-bottom : " + ("2px"+ " " +"solid"+ " " +"#6f7277")  + ";\n  text-align : ") + (("center")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  padding : " + ("0")  + ";\n  text-shadow : " + ("none")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridCell {\n  font-size : " + ("smaller")  + ";\n  border : " + ("none")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  padding : " + ("0") ) + (";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridFirstColumnHeader {\n  text-align : " + ("left")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridLastColumnHeader {\n  text-align : " + ("right")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSortableHeader {\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSortableHeader:hover {\n  color : " + ("#6c6b6b")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridEvenRow {\n  background : " + ("rgba(" + "100"+ ","+ " " +"100"+ ","+ " " +"200"+ ","+ " " +"0.5" + ")")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridOddRow {\n  background : " + ("rgba(" + "150"+ ","+ " " +"150"+ ","+ " " +"250"+ ","+ " " +"0.5" + ")")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridHoveredRow {\n  color : " + ("black")  + ";\n  background : " + ("rgba(" + "255"+ ","+ " " +"255"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridKeyboardSelectedRow {\n  color : " + ("black")  + ";\n  background : ") + (("rgba(" + "200"+ ","+ " " +"200"+ ","+ " " +"200"+ ","+ " " +"0.5" + ")")  + ";\n}\n.org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSelectedRow {\n  background : " + ("#628cd5")  + ";\n  color : " + ("white")  + ";\n  height : " + ("auto")  + ";\n  overflow : " + ("auto")  + ";\n}\n"));
      }
      public java.lang.String dataGridCell() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridCell";
      }
      public java.lang.String dataGridEvenRow() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridEvenRow";
      }
      public java.lang.String dataGridEvenRowCell() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridEvenRowCell";
      }
      public java.lang.String dataGridFirstColumn() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridFirstColumn";
      }
      public java.lang.String dataGridFirstColumnFooter() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridFirstColumnFooter";
      }
      public java.lang.String dataGridFirstColumnHeader() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridFirstColumnHeader";
      }
      public java.lang.String dataGridFooter() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridFooter";
      }
      public java.lang.String dataGridHeader() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridHeader";
      }
      public java.lang.String dataGridHoveredRow() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridHoveredRow";
      }
      public java.lang.String dataGridHoveredRowCell() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridHoveredRowCell";
      }
      public java.lang.String dataGridKeyboardSelectedCell() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridKeyboardSelectedCell";
      }
      public java.lang.String dataGridKeyboardSelectedRow() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridKeyboardSelectedRow";
      }
      public java.lang.String dataGridKeyboardSelectedRowCell() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridKeyboardSelectedRowCell";
      }
      public java.lang.String dataGridLastColumn() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridLastColumn";
      }
      public java.lang.String dataGridLastColumnFooter() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridLastColumnFooter";
      }
      public java.lang.String dataGridLastColumnHeader() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridLastColumnHeader";
      }
      public java.lang.String dataGridOddRow() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridOddRow";
      }
      public java.lang.String dataGridOddRowCell() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridOddRowCell";
      }
      public java.lang.String dataGridSelectedRow() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSelectedRow";
      }
      public java.lang.String dataGridSelectedRowCell() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSelectedRowCell";
      }
      public java.lang.String dataGridSortableHeader() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSortableHeader";
      }
      public java.lang.String dataGridSortedHeaderAscending() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSortedHeaderAscending";
      }
      public java.lang.String dataGridSortedHeaderDescending() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridSortedHeaderDescending";
      }
      public java.lang.String dataGridWidget() {
        return "org-reactome-web-diagram-context-dialogs-interactors-InteractorsTable-MoleculesTableResource-MoleculesStyle-dataGridWidget";
      }
    }
    ;
  }
  private static class dataGridStyleInitializer {
    static {
      _instance0.dataGridStyleInitializer();
    }
    static org.reactome.web.diagram.context.dialogs.interactors.InteractorsTable.MoleculesTableResource.MoleculesStyle get() {
      return dataGridStyle;
    }
  }
  public org.reactome.web.diagram.context.dialogs.interactors.InteractorsTable.MoleculesTableResource.MoleculesStyle dataGridStyle() {
    return dataGridStyleInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/gif;base64,R0lGODlhKwALAPEAAP///0tKSqampktKSiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAKwALAAACMoSOCMuW2diD88UKG95W88uF4DaGWFmhZid93pq+pwxnLUnXh8ou+sSz+T64oCAyTBUAACH5BAkKAAAALAAAAAArAAsAAAI9xI4IyyAPYWOxmoTHrHzzmGHe94xkmJifyqFKQ0pwLLgHa82xrekkDrIBZRQab1jyfY7KTtPimixiUsevAAAh+QQJCgAAACwAAAAAKwALAAACPYSOCMswD2FjqZpqW9xv4g8KE7d54XmMpNSgqLoOpgvC60xjNonnyc7p+VKamKw1zDCMR8rp8pksYlKorgAAIfkECQoAAAAsAAAAACsACwAAAkCEjgjLltnYmJS6Bxt+sfq5ZUyoNJ9HHlEqdCfFrqn7DrE2m7Wdj/2y45FkQ13t5itKdshFExC8YCLOEBX6AhQAADsAAAAAAAAAAAA=";
  private static final java.lang.String externalImage_rtl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACsAAAALCAYAAADm8XT2AAAAMElEQVR42mNgAIJly5b9J4QZ8AC66SdGIT4L6ap/1LGjjh117KhjRx1Le/1DpQYDAInICKwfCLm7AAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAAHCAYAAADebrddAAAAiklEQVR42mNgwALyKrumFRf3iDAQAvmVXVVAxf/zKjq341WYV95hk1fZ+R+MK8C4HqtCkLW5FZ2PQYpyK6AaKjv/5VV1OmIozq3s3AFR0AXFUNMrO5/lV7WKI6yv6mxCksSGDyTU13Mw5JV2qeaWd54FWn0BRAMlLgPZl/NAuBKMz+dWdF0H2hwCAPwcZIjfOFLHAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage_rtl0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAAHCAYAAADebrddAAAAiklEQVR42mNgQAPFxT0ieZVd0xiIAXkVnduBiv/nV3ZVEVJYD8T/8yqhuLzDBrvCqk5HoIJ/IEW5IA0VYPoxyFkoCvOrWsWBip4hTO2CYqCGys4dcIUJ9fUcQMEDcKuRMUxzVWcTWDFQZ0huRdd1oOB5IL4MVHAZaP1lEDu3vPMskH0BROeVdqkCAJLDZIgWLbFCAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAAHCAYAAADebrddAAAAiklEQVR42mPIrewMya3oup5X2XkeiC/nVXRezgViEDu3vPMskH0BROeVdqkyJNTXcwAlDgDxfwxcAaWrOpsYYCC/qlUcKPgMLlnZBcWd/4E272BAB0DdjkDJf2AFFRBTgfTj4uIeEQZsAKigHmE6EJd32DDgA0DF20FOyK/sqmIgBEDWAhVPwyYHAJAqZIiNwsHKAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage_rtl1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAAHCAYAAADebrddAAAAiElEQVR42mPIK+1SzS3vPJtb0XkBROdVdl4Gsi/ngXAlGJ/Prei6nlvZGcIAAnlVnU1Awf9ABf/BNCY+kFBfz8EAA0CdOyASXVAM1/wsv6pVnAEZFBf3iACtfwxSkIuw4R/QVkcGbCCvvMMGbnUFGNcz4AP5lV1VYGdUdG5nIAYAFU8DOQtdHAD5g2SIRShEoQAAAABJRU5ErkJggg==";
  private static com.google.gwt.resources.client.ImageResource dataGridLoading;
  private static com.google.gwt.resources.client.ImageResource dataGridSortAscending;
  private static com.google.gwt.resources.client.ImageResource dataGridSortDescending;
  private static org.reactome.web.diagram.context.dialogs.interactors.InteractorsTable.MoleculesTableResource.MoleculesStyle dataGridStyle;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      dataGridLoading(), 
      dataGridSortAscending(), 
      dataGridSortDescending(), 
      dataGridStyle(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("dataGridLoading", dataGridLoading());
        resourceMap.put("dataGridSortAscending", dataGridSortAscending());
        resourceMap.put("dataGridSortDescending", dataGridSortDescending());
        resourceMap.put("dataGridStyle", dataGridStyle());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'dataGridLoading': return this.@com.google.gwt.user.cellview.client.DataGrid.Resources::dataGridLoading()();
      case 'dataGridSortAscending': return this.@com.google.gwt.user.cellview.client.DataGrid.Resources::dataGridSortAscending()();
      case 'dataGridSortDescending': return this.@com.google.gwt.user.cellview.client.DataGrid.Resources::dataGridSortDescending()();
      case 'dataGridStyle': return this.@org.reactome.web.diagram.context.dialogs.interactors.InteractorsTable.MoleculesTableResource::dataGridStyle()();
    }
    return null;
  }-*/;
}
