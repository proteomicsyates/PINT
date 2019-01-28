package org.reactome.web.diagram.controls.settings.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class InteractorsTabPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.Resources {
  private static InteractorsTabPanel_Resources_default_InlineClientBundleGenerator _instance0 = new InteractorsTabPanel_Resources_default_InlineClientBundleGenerator();
  private void aboutThisInitializer() {
    aboutThis = new com.google.gwt.resources.client.TextResource() {
      // jar:file:/C:/Users/salvador/.m2/repository/org/reactome/web/diagram/3.2.1/diagram-3.2.1.jar!/org/reactome/web/diagram/controls/settings/tabs/InteractorsInfo.txt
      public String getText() {
        return "Select a resource to visualise protein–protein and protein–chemical interaction data as extensions to Reactome pathways.";
      }
      public String getName() {
        return "aboutThis";
      }
    }
    ;
  }
  private static class aboutThisInitializer {
    static {
      _instance0.aboutThisInitializer();
    }
    static com.google.gwt.resources.client.TextResource get() {
      return aboutThis;
    }
  }
  public com.google.gwt.resources.client.TextResource aboutThis() {
    return aboutThisInitializer.get();
  }
  private void addNewItemInitializer() {
    addNewItem = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "addNewItem",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 16, 16, false, false
    );
  }
  private static class addNewItemInitializer {
    static {
      _instance0.addNewItemInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return addNewItem;
    }
  }
  public com.google.gwt.resources.client.ImageResource addNewItem() {
    return addNewItemInitializer.get();
  }
  private void deleteHoveredInitializer() {
    deleteHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "deleteHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 10, 12, false, false
    );
  }
  private static class deleteHoveredInitializer {
    static {
      _instance0.deleteHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return deleteHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource deleteHovered() {
    return deleteHoveredInitializer.get();
  }
  private void deleteNormalInitializer() {
    deleteNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "deleteNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
      0, 0, 11, 13, false, false
    );
  }
  private static class deleteNormalInitializer {
    static {
      _instance0.deleteNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return deleteNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource deleteNormal() {
    return deleteNormalInitializer.get();
  }
  private void downloadNormalInitializer() {
    downloadNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "downloadNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
      0, 0, 24, 22, false, false
    );
  }
  private static class downloadNormalInitializer {
    static {
      _instance0.downloadNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return downloadNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource downloadNormal() {
    return downloadNormalInitializer.get();
  }
  private void loadingSpinnerInitializer() {
    loadingSpinner = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "loadingSpinner",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage3),
      0, 0, 16, 16, true, false
    );
  }
  private static class loadingSpinnerInitializer {
    static {
      _instance0.loadingSpinnerInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return loadingSpinner;
    }
  }
  public com.google.gwt.resources.client.ImageResource loadingSpinner() {
    return loadingSpinnerInitializer.get();
  }
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorsPanel {\n  padding : " + ("0"+ " " +"10px"+ " " +"0"+ " " +"10px")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("small")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorLabel {\n  margin-top : " + ("2px")  + ";\n  color : " + ("#005a75")  + ";\n  font-size : " + ("medium")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveInteractorLabel {\n  margin-right : " + ("10px")  + ";\n  display : " + ("block")  + ";\n  color : " + ("#005a75")  + ";\n  font-size : " + ("medium")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceBtn {\n  margin-right : ") + (("10px")  + ";\n  display : " + ("inline-block")  + ";\n  width : " + ("200px")  + ";\n  color : " + ("#005a75")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceBtn label {\n  margin-right : " + ("5px")  + ";\n  cursor : " + ("pointer")  + ";\n  color : " + ("white")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceBtn:hover label {\n  margin-right : " + ("5px")  + ";\n  cursor : " + ("pointer")  + ";\n  color : " + ("#066b9e")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveResourcesOuterPanel {\n  height : " + ("85px") ) + (";\n  width : " + ("245px")  + ";\n  margin-right : " + ("10px")  + ";\n  background-color : " + ("transparent")  + ";\n  overflow-x : " + ("hidden")  + ";\n  overflow-y : " + ("scroll")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveResourcesInnerPanel {\n  color : " + ("white")  + ";\n  height : " + ("auto")  + ";\n  width : " + ("244px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-customResourcesOuterPanel {\n  height : " + ("45px")  + ";\n  width : " + ("245px")  + ";\n  margin-right : ") + (("10px")  + ";\n  background-color : " + ("transparent")  + ";\n  overflow-x : " + ("hidden")  + ";\n  overflow-y : " + ("scroll")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-customResourcesInnerPanel {\n  color : " + ("white")  + ";\n  height : " + ("auto")  + ";\n  width : " + ("244px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-loadingPanel {\n  margin-right : " + ("13px")  + ";\n  color : " + ("#066b9e")  + ";\n  font-size : " + ("small")  + ";\n  white-space : " + ("pre") ) + (";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-errorPanel {\n  margin-right : " + ("13px")  + ";\n  color : " + ("#9e322b")  + ";\n  font-size : " + ("small")  + ";\n  white-space : " + ("pre")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn {\n  float : " + ("right")  + ";\n  width : " + ("200px")  + ";\n  white-space : " + ("nowrap")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn input {\n  cursor : ") + (("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn label {\n  margin-right : " + ("5px")  + ";\n  cursor : " + ("pointer")  + ";\n  color : " + ("white")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn:hover label {\n  margin-right : " + ("5px")  + ";\n  cursor : " + ("pointer")  + ";\n  color : " + ("#066b9e")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtnDisabled {\n  display : " + ("inline-block")  + ";\n  width : " + ("200px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtnDisabled label {\n  margin-right : " + ("5px")  + ";\n  color : " + ("dimgray") ) + (";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-summaryLb {\n  background-color : " + ("white")  + ";\n  border : " + ("none")  + ";\n  height : " + ("12px")  + ";\n  min-width : " + ("12px")  + ";\n  text-align : " + ("center")  + ";\n  border-radius : " + ("6px")  + ";\n  color : " + ("#1e94d0")  + ";\n  padding : " + ("0"+ " " +"1px"+ " " +"3px"+ " " +"1px")  + ";\n  margin-left : " + ("15px")  + ";\n  outline : " + ("none")  + ";\n  float : ") + (("left")  + ";\n  font-size : " + ("smaller")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn {\n  background-color : " + ("#1e94d0")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  margin-top : " + ("5px")  + ";\n  padding : " + ("2px"+ " " +"5px"+ " " +"2px"+ " " +"5px")  + ";\n  outline : " + ("none")  + ";\n  font-size : " + ("medium")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn span {\n  vertical-align : " + ("middle") ) + (" !important;\n  display : " + ("inline-block")  + ";\n  min-width : " + ("60px")  + ";\n  max-width : " + ("160px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  padding-left : " + ("3px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn:disabled {\n  background-color : " + ("#a39da6")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn {\n  background-color : ") + (("#1e94d0")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  margin-top : " + ("5px")  + ";\n  padding : " + ("1px"+ " " +"3px"+ " " +"4px"+ " " +"5px")  + ";\n  outline : " + ("none")  + ";\n  font-size : " + ("small")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn span {\n  vertical-align : " + ("middle")  + " !important;\n  display : " + ("inline-block")  + ";\n  min-width : " + ("60px") ) + (";\n  max-width : " + ("160px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  padding-left : " + ("3px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn:disabled {\n  background-color : " + ("#a39da6")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-delete {\n  height : " + ((InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getHeight() + "px")  + ";\n  width : " + ((InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getWidth() + "px")  + ";\n  overflow : ") + (("hidden")  + ";\n  background : " + ("url(\"" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getSafeUri().asString() + "\") -" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getLeft() + "px -" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("left")  + ";\n  margin-left : " + ("15px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-delete:hover, .org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-delete:active:hover {\n  height : " + ((InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getHeight() + "px")  + ";\n  width : " + ((InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden") ) + (";\n  background : " + ("url(\"" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getSafeUri().asString() + "\") -" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getLeft() + "px -" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getTop() + "px  no-repeat")  + ";\n}\n")) : ((".org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorsPanel {\n  padding : " + ("0"+ " " +"10px"+ " " +"0"+ " " +"10px")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("small")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorLabel {\n  margin-top : " + ("2px")  + ";\n  color : " + ("#005a75")  + ";\n  font-size : " + ("medium")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveInteractorLabel {\n  margin-left : " + ("10px")  + ";\n  display : " + ("block")  + ";\n  color : " + ("#005a75")  + ";\n  font-size : " + ("medium")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceBtn {\n  margin-left : ") + (("10px")  + ";\n  display : " + ("inline-block")  + ";\n  width : " + ("200px")  + ";\n  color : " + ("#005a75")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceBtn label {\n  margin-left : " + ("5px")  + ";\n  cursor : " + ("pointer")  + ";\n  color : " + ("white")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceBtn:hover label {\n  margin-left : " + ("5px")  + ";\n  cursor : " + ("pointer")  + ";\n  color : " + ("#066b9e")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveResourcesOuterPanel {\n  height : " + ("85px") ) + (";\n  width : " + ("245px")  + ";\n  margin-left : " + ("10px")  + ";\n  background-color : " + ("transparent")  + ";\n  overflow-x : " + ("hidden")  + ";\n  overflow-y : " + ("scroll")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveResourcesInnerPanel {\n  color : " + ("white")  + ";\n  height : " + ("auto")  + ";\n  width : " + ("244px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-customResourcesOuterPanel {\n  height : " + ("45px")  + ";\n  width : " + ("245px")  + ";\n  margin-left : ") + (("10px")  + ";\n  background-color : " + ("transparent")  + ";\n  overflow-x : " + ("hidden")  + ";\n  overflow-y : " + ("scroll")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-customResourcesInnerPanel {\n  color : " + ("white")  + ";\n  height : " + ("auto")  + ";\n  width : " + ("244px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-loadingPanel {\n  margin-left : " + ("13px")  + ";\n  color : " + ("#066b9e")  + ";\n  font-size : " + ("small")  + ";\n  white-space : " + ("pre") ) + (";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-errorPanel {\n  margin-left : " + ("13px")  + ";\n  color : " + ("#9e322b")  + ";\n  font-size : " + ("small")  + ";\n  white-space : " + ("pre")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn {\n  float : " + ("left")  + ";\n  width : " + ("200px")  + ";\n  white-space : " + ("nowrap")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn input {\n  cursor : ") + (("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn label {\n  margin-left : " + ("5px")  + ";\n  cursor : " + ("pointer")  + ";\n  color : " + ("white")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn:hover label {\n  margin-left : " + ("5px")  + ";\n  cursor : " + ("pointer")  + ";\n  color : " + ("#066b9e")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtnDisabled {\n  display : " + ("inline-block")  + ";\n  width : " + ("200px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtnDisabled label {\n  margin-left : " + ("5px")  + ";\n  color : " + ("dimgray") ) + (";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-summaryLb {\n  background-color : " + ("white")  + ";\n  border : " + ("none")  + ";\n  height : " + ("12px")  + ";\n  min-width : " + ("12px")  + ";\n  text-align : " + ("center")  + ";\n  border-radius : " + ("6px")  + ";\n  color : " + ("#1e94d0")  + ";\n  padding : " + ("0"+ " " +"1px"+ " " +"3px"+ " " +"1px")  + ";\n  margin-right : " + ("15px")  + ";\n  outline : " + ("none")  + ";\n  float : ") + (("right")  + ";\n  font-size : " + ("smaller")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn {\n  background-color : " + ("#1e94d0")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  margin-top : " + ("5px")  + ";\n  padding : " + ("2px"+ " " +"5px"+ " " +"2px"+ " " +"5px")  + ";\n  outline : " + ("none")  + ";\n  font-size : " + ("medium")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn span {\n  vertical-align : " + ("middle") ) + (" !important;\n  display : " + ("inline-block")  + ";\n  min-width : " + ("60px")  + ";\n  max-width : " + ("160px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  padding-right : " + ("3px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn:disabled {\n  background-color : " + ("#a39da6")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn {\n  background-color : ") + (("#1e94d0")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  margin-top : " + ("5px")  + ";\n  padding : " + ("1px"+ " " +"5px"+ " " +"4px"+ " " +"3px")  + ";\n  outline : " + ("none")  + ";\n  font-size : " + ("small")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn span {\n  vertical-align : " + ("middle")  + " !important;\n  display : " + ("inline-block")  + ";\n  min-width : " + ("60px") ) + (";\n  max-width : " + ("160px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  padding-right : " + ("3px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn:disabled {\n  background-color : " + ("#a39da6")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-delete {\n  height : " + ((InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getHeight() + "px")  + ";\n  width : " + ((InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getWidth() + "px")  + ";\n  overflow : ") + (("hidden")  + ";\n  background : " + ("url(\"" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getSafeUri().asString() + "\") -" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getLeft() + "px -" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("right")  + ";\n  margin-right : " + ("15px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-delete:hover, .org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-delete:active:hover {\n  height : " + ((InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getHeight() + "px")  + ";\n  width : " + ((InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden") ) + (";\n  background : " + ("url(\"" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getSafeUri().asString() + "\") -" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getLeft() + "px -" + (InteractorsTabPanel_Resources_default_InlineClientBundleGenerator.this.deleteHovered()).getTop() + "px  no-repeat")  + ";\n}\n"));
      }
      public java.lang.String addNewResourceBtn() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-addNewResourceBtn";
      }
      public java.lang.String customResourcesInnerPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-customResourcesInnerPanel";
      }
      public java.lang.String customResourcesOuterPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-customResourcesOuterPanel";
      }
      public java.lang.String delete() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-delete";
      }
      public java.lang.String downloadBtn() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-downloadBtn";
      }
      public java.lang.String errorPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-errorPanel";
      }
      public java.lang.String interactorLabel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorLabel";
      }
      public java.lang.String interactorResourceBtn() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceBtn";
      }
      public java.lang.String interactorResourceListBtn() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtn";
      }
      public java.lang.String interactorResourceListBtnDisabled() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorResourceListBtnDisabled";
      }
      public java.lang.String interactorsPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-interactorsPanel";
      }
      public java.lang.String liveInteractorLabel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveInteractorLabel";
      }
      public java.lang.String liveResourcesInnerPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveResourcesInnerPanel";
      }
      public java.lang.String liveResourcesOuterPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-liveResourcesOuterPanel";
      }
      public java.lang.String loadingPanel() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-loadingPanel";
      }
      public java.lang.String summaryLb() {
        return "org-reactome-web-diagram-controls-settings-tabs-InteractorsTabPanel-ResourceCSS-summaryLb";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.resources.client.TextResource aboutThis;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABTUlEQVR42p1Ty0rEQBAcBBUEUfTmF+jHiJ6UvQh+g95UctGj60UU/ynHIELWx2Vz2JjEhIQQ89AqmYFmSFZxoEjSXV3T1XSUso7jOAtt2553XTcF2i8cPF8ROwuCYEXNO03T7IJcm0J5GAMyCJ30FiNxA0IuCkog1ChF/APcsV18yoR1Y4SODoAjili5nN3+FNMXW+tp+T3Lss2iKLZsAZ2vOS/efjHg+TeBlsNWeHkTnmOBRgjUVq7UNVMKdPojpK84jteIJEnWXddd9H1/me8mDs4huDNd86lESxTYT9N0w8DzvCUKyBg4IyMAC7fsYCIsRAK2BZkzFh45xAeS/znESxWG4aq9A38UeK6qatus8J7cQrFII+C4Z5Ei3H5nb+OYCWuVZxpylXNwr4f+h3sQXgYWq6FVdjv3j4SvHQhdgfyki3gmHDbnZfO/AYJsGwxyNzf3AAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAMCAYAAABbayygAAAAwUlEQVR42mNggIHESaJs2fNuA/FvIP4DxFcZ4qYIw+WZM+eEsmbNn86WNW8bUPIha/a8GSAMZD8F4k1guYy5gQxs2XPOE8Ks2fNPMjAk9AsATTiMDzNkTeVhYEibKQK04j8Q32XNnruXLXvuGaAzzgEV7AKKPQDJgQxDVriAJWOWO3vm3CK2nDllLOlz7IBiK4eWQmDoQxV+Bvr0BZB+C8TvgPg5EH8BK4zt5gbHDlvOvLVAgX9QDcj4L1vWnMUgNQDoerytMcjaYwAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAANCAYAAAB/9ZQ7AAAAmElEQVR42mNgQAJ///6d9+/fv1//gQBI/wTypyPLM/z8+VMPKFgJxDVABf+A9AwgbgfiuUDuHyBdAcJAdVoMIIH/RACguh8Mr1694gHqbAHiNjy46dmzZ1xgpxAyHSj/He5ukGIgfnL//n0OIH0WSh+H0u+wKX4MZZ+G0seg9NtBrJhoD/4kEHTvkdNEByxNYIs5UFIAqQMAea6PlEX0lLUAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAWCAYAAADafVyIAAACQElEQVR42rWVu4sTURTGrw/EB1ooNmshCFq4FqJbuCCigo9efOC/INpYqUWwElcEi20sbCxEs5WdhRpFKxFhNWynYZ0lhh0DJmMeM3n5+/SOjOMMu2PigY/cxznfd+fcc26MWaY1m81tvV7vZr/fnwNt0ARvWbtcLpfXm2Gs2+2ehcwbWGPcHUSM+acgCPZm4VzR6XSOcrqrYAqCnogYP4BoXy6XW+k4zjp8jrH32opU2+32ziWZXdfdiPOTQcwgv5LkXygUVrP30Io8XVIA53vWeYHxdXCH8Yy+Ki2mWq1uwuer4nzfHzdpJyHXp3D0QdBqtXZkyWl4MKVUXH9sKneQzkYubS5rIUB8LRL//vd91Ov1LSzMh8Q4XuLyDmYV4Iu3E3tRHJbrc61W2yzlKbvwUpVhhrRSqbQWrhc2XbcMk4+aUH4TZkQG1/6wPyTga1IsFtekBVAZe0jbIXDY4oj6hDzvSvIXlxXwTZj/1PL61cnnbXV9AzVQ13OhRks50LgVmNcdTNvJ43w+vypF4Bz7ndjz4CFwPO4rDnHZO5j++Yix4NqgV5CdJnCSPB7gJLsjb1GSwIlICicVKw6774o7vJQJFpz486BqsAJn1HwJAie1z/hNbM/5q2gqlcoGPukCeITDc1BgfNfzvK383k4Q+M76jUajMcbvfcUoVhziWla5qVnAot6Z8EWNPtlKA/gCPvxzTRP8Lp7/iEgAng3dOGpG0E/4gtlRNadEFmMCC2bUpv/h8ILN/zIJZPH/ARrhWGFdRdsyAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage3 = "data:image/gif;base64,R0lGODlhEAAQAPIAAABl1gAAAABNowAaNwAAAAAmUgAzbQA6eyH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==";
  private static com.google.gwt.resources.client.ImageResource addNewItem;
  private static com.google.gwt.resources.client.ImageResource deleteHovered;
  private static com.google.gwt.resources.client.ImageResource deleteNormal;
  private static com.google.gwt.resources.client.ImageResource downloadNormal;
  private static com.google.gwt.resources.client.ImageResource loadingSpinner;
  private static org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      aboutThis(), 
      addNewItem(), 
      deleteHovered(), 
      deleteNormal(), 
      downloadNormal(), 
      loadingSpinner(), 
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("aboutThis", aboutThis());
        resourceMap.put("addNewItem", addNewItem());
        resourceMap.put("deleteHovered", deleteHovered());
        resourceMap.put("deleteNormal", deleteNormal());
        resourceMap.put("downloadNormal", downloadNormal());
        resourceMap.put("loadingSpinner", loadingSpinner());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'aboutThis': return this.@org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.Resources::aboutThis()();
      case 'addNewItem': return this.@org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.Resources::addNewItem()();
      case 'deleteHovered': return this.@org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.Resources::deleteHovered()();
      case 'deleteNormal': return this.@org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.Resources::deleteNormal()();
      case 'downloadNormal': return this.@org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.Resources::downloadNormal()();
      case 'loadingSpinner': return this.@org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.Resources::loadingSpinner()();
      case 'getCSS': return this.@org.reactome.web.diagram.controls.settings.tabs.InteractorsTabPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
