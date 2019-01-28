package org.reactome.web.diagram.context;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class ContextDialogPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.context.ContextDialogPanel.Resources {
  private static ContextDialogPanel_Resources_default_InlineClientBundleGenerator _instance0 = new ContextDialogPanel_Resources_default_InlineClientBundleGenerator();
  private void closeClickedInitializer() {
    closeClicked = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "closeClicked",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 16, 16, false, false
    );
  }
  private static class closeClickedInitializer {
    static {
      _instance0.closeClickedInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return closeClicked;
    }
  }
  public com.google.gwt.resources.client.ImageResource closeClicked() {
    return closeClickedInitializer.get();
  }
  private void closeHoveredInitializer() {
    closeHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "closeHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 16, 16, false, false
    );
  }
  private static class closeHoveredInitializer {
    static {
      _instance0.closeHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return closeHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource closeHovered() {
    return closeHoveredInitializer.get();
  }
  private void closeNormalInitializer() {
    closeNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "closeNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
      0, 0, 16, 16, false, false
    );
  }
  private static class closeNormalInitializer {
    static {
      _instance0.closeNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return closeNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource closeNormal() {
    return closeNormalInitializer.get();
  }
  private void idClickedInitializer() {
    idClicked = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "idClicked",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
      0, 0, 16, 16, false, false
    );
  }
  private static class idClickedInitializer {
    static {
      _instance0.idClickedInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return idClicked;
    }
  }
  public com.google.gwt.resources.client.ImageResource idClicked() {
    return idClickedInitializer.get();
  }
  private void idHoveredInitializer() {
    idHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "idHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage3),
      0, 0, 16, 16, false, false
    );
  }
  private static class idHoveredInitializer {
    static {
      _instance0.idHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return idHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource idHovered() {
    return idHoveredInitializer.get();
  }
  private void idNormalInitializer() {
    idNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "idNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage4),
      0, 0, 16, 16, false, false
    );
  }
  private static class idNormalInitializer {
    static {
      _instance0.idNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return idNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource idNormal() {
    return idNormalInitializer.get();
  }
  private void pinClickedInitializer() {
    pinClicked = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "pinClicked",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage5),
      0, 0, 16, 16, false, false
    );
  }
  private static class pinClickedInitializer {
    static {
      _instance0.pinClickedInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return pinClicked;
    }
  }
  public com.google.gwt.resources.client.ImageResource pinClicked() {
    return pinClickedInitializer.get();
  }
  private void pinHoveredInitializer() {
    pinHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "pinHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage6),
      0, 0, 16, 16, false, false
    );
  }
  private static class pinHoveredInitializer {
    static {
      _instance0.pinHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return pinHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource pinHovered() {
    return pinHoveredInitializer.get();
  }
  private void pinNormalInitializer() {
    pinNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "pinNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage7),
      0, 0, 16, 16, false, false
    );
  }
  private static class pinNormalInitializer {
    static {
      _instance0.pinNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return pinNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource pinNormal() {
    return pinNormalInitializer.get();
  }
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.context.ContextDialogPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup {\n  background-color : " + ("#58c3e5")  + ";\n  background : " + ("-webkit-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-o-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-moz-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  opacity : " + ("0.95")  + ";\n  color : " + ("white")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  padding : ") + (("2px")  + ";\n  border-radius : " + ("15px")  + ";\n  border : " + ("solid"+ " " +"2px"+ " " +"#0785ab")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("small")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup table, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup tbody, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup th, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup tr, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup td {\n  margin : " + ("0")  + " !important;\n  padding : " + ("0")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popupSelected {\n  border : " + ("solid"+ " " +"3px"+ " " +"#077295")  + ";\n  -webkit-box-shadow : " + ("5px"+ " " +"5px"+ " " +"8px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("5px"+ " " +"5px"+ " " +"8px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("5px"+ " " +"5px"+ " " +"8px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")") ) + (";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popupSelected .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header span {\n  color : " + ("midnightblue")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header {\n  margin : " + ("0"+ " " +"4px"+ " " +"2px"+ " " +"0")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header:hover, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header:active:hover {\n  cursor : " + ("move")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header button {\n  float : " + ("left")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header span {\n  float : " + ("right")  + ";\n  color : " + ("#005a75")  + ";\n  font-weight : " + ("bolder")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  width : ") + (("242px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header img {\n  float : " + ("right")  + ";\n  margin : " + ("0"+ " " +"2px"+ " " +"0"+ " " +"5px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labels {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getTop() + "px  no-repeat")  + ";\n  float : " + ("left")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none") ) + (";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("5px")  + ";\n  left : " + ("50px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labels:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labelsActive, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labels:active:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getHeight() + "px")  + ";\n  width : ") + (((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("5px")  + ";\n  left : " + ("50px") ) + (";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pin {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getTop() + "px  no-repeat")  + ";\n  float : " + ("left")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  position : ") + (("absolute")  + ";\n  top : " + ("5px")  + ";\n  left : " + ("30px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pin:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pinActive, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pin:active:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getTop() + "px  no-repeat") ) + (";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("5px")  + ";\n  left : " + ("30px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-close {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px")  + ";\n  overflow : ") + (("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  margin-left : " + ("5px")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("5px")  + ";\n  left : " + ("5px") ) + (";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-close:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-close:active:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n")) : ((".org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup {\n  background-color : " + ("#58c3e5")  + ";\n  background : " + ("-webkit-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-o-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-moz-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  opacity : " + ("0.95")  + ";\n  color : " + ("white")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  padding : ") + (("2px")  + ";\n  border-radius : " + ("15px")  + ";\n  border : " + ("solid"+ " " +"2px"+ " " +"#0785ab")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("small")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup table, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup tbody, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup th, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup tr, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup td {\n  margin : " + ("0")  + " !important;\n  padding : " + ("0")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popupSelected {\n  border : " + ("solid"+ " " +"3px"+ " " +"#077295")  + ";\n  -webkit-box-shadow : " + ("5px"+ " " +"5px"+ " " +"8px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("5px"+ " " +"5px"+ " " +"8px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("5px"+ " " +"5px"+ " " +"8px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")") ) + (";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popupSelected .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header span {\n  color : " + ("midnightblue")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header {\n  margin : " + ("0"+ " " +"0"+ " " +"2px"+ " " +"4px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header:hover, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header:active:hover {\n  cursor : " + ("move")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header button {\n  float : " + ("right")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header span {\n  float : " + ("left")  + ";\n  color : " + ("#005a75")  + ";\n  font-weight : " + ("bolder")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  width : ") + (("242px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header img {\n  float : " + ("left")  + ";\n  margin : " + ("0"+ " " +"5px"+ " " +"0"+ " " +"2px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labels {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idNormal()).getTop() + "px  no-repeat")  + ";\n  float : " + ("right")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none") ) + (";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("5px")  + ";\n  right : " + ("50px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labels:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labelsActive, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labels:active:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getHeight() + "px")  + ";\n  width : ") + (((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.idClicked()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("5px")  + ";\n  right : " + ("50px") ) + (";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pin {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinNormal()).getTop() + "px  no-repeat")  + ";\n  float : " + ("right")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  position : ") + (("absolute")  + ";\n  top : " + ("5px")  + ";\n  right : " + ("30px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pin:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pinActive, .org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pin:active:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.pinClicked()).getTop() + "px  no-repeat") ) + (";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("5px")  + ";\n  right : " + ("30px")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-close {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px")  + ";\n  overflow : ") + (("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  cursor : " + ("pointer")  + ";\n  margin-right : " + ("5px")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("5px")  + ";\n  right : " + ("5px") ) + (";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-close:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-close:active:hover {\n  height : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (ContextDialogPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n"));
      }
      public java.lang.String close() {
        return "org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-close";
      }
      public java.lang.String header() {
        return "org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-header";
      }
      public java.lang.String labels() {
        return "org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labels";
      }
      public java.lang.String labelsActive() {
        return "org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-labelsActive";
      }
      public java.lang.String pin() {
        return "org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pin";
      }
      public java.lang.String pinActive() {
        return "org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-pinActive";
      }
      public java.lang.String popup() {
        return "org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popup";
      }
      public java.lang.String popupSelected() {
        return "org-reactome-web-diagram-context-ContextDialogPanel-ResourceCSS-popupSelected";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.context.ContextDialogPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.context.ContextDialogPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABPElEQVR42mNgQAdZswxYs+ZPZ8uedwOIv0PxDZAYSI4BJwjt42TPnjcTqPgfEP/Hgf+B1IDUYmhmzZl3AI9GFAxSi2IIa/a8GTDJ1u0X/s84dP0/e858uAYQ+8vP3/+N2zcgDAHqgfsZ5mzjtg3/H7798v/Hn7//1114AFe84sw9sNjd15/+B87YA/cOOExYM+dMgykEGXD9+Yf/IPDp+6//3bsugTGIDQLXX3z4b9S6HuEKoF4GaGjDBRWqV/5//fk7WMOXX3/ATgcBkBhIDi08boAM+IEeSGadG/9/hNoKAh++/QSLYQnQHzgNeA/UBDcAaBg+A3B64euv32Bv4PUCxYGIHI1BwCi6+uw9OMpAUYccjd9//wFHMcgSlGhET0igxPLj91+MhARKXC3bLmBJSNRIypRnJgqyMwCn/xPvrOlO4QAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABP0lEQVR42q1TzU7CQBDulWdQyitw8g2ExMfQ9xET7YI34gm9eVCvnDhosuWgSdFIYuIBElGRYFsoO+63tWVbbC91kslO5ufbnZlvDSMlW43bqmn1m+aJ7cjTVRraTcSMLNk+7JVk4qlp2UIqZahADnI3isvHdjenMKHITYCYFm9FwaO7EZ3dv1GF9eMC2PPliurnjgbEW1rP4bNrHYdeZwvyA0E3z59x8uXjh/K9TH3avxrG7aiZVBhnUSIAnt49gswWK2J8rBQ2BLHdjqO9jDMjnPC6v532A03cQBV8y1vnS6Fs+BBLzEPWyv5tLz2kvYsBff3eCpn6gfL9MVAvEwBFkQAsGyCvBfl8tJHbQuEh6ms8uB7SYOKqlWF1+ho96cOKa2sAEVNbJxLIAoA0kUAukGyDSP9C5cKfqch3/gFN5mL5A2xz9gAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABMUlEQVR42q1TO07DQBB1y224TBRzhfQEcQXoA1wBQUFLQ4ihIEVSEVHjtVFs0hDZ3mBnh3mLs14cHAoz0kirmXnP83l2nJp17sS+OwrPuiPx4npBBtdvjiHnNFnn0t9z78UFAxQ7NbhCDWq3wZ4Y7gDWXAx/kHS98HyTvH5d0u1bQgcPoQHgLdeKjqaRiQFTzVy23Z9EFMuCckX09J6Z4sc407E5506eF2YcvRNuZ7ApBIFIC4Kl/MUbf6kdb1jAucNJZI8ycMptm2BvPKePfK0BK8bJ76eOIWfXAssdBLK+pONpTEkJhCWF0rFfFiqbCQpVETBZI8GuEdD+Sv05Qssl2mc8nS3IT3N9MpzOPuMnx3DifkWgjLRtIUEsIKgLCeK6YpFtCelfpNz6Z2rzO38BSvm4fsrEo3UAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABZElEQVR42mNgQAdZswxYs+ZPZ8uedwOIv0PxDZAYSI4BJwjt42TPnjcTqPgfEP/Hgf+B1IDUYmhmzZl3AI9GFAxSi2IIa/a8GTBJ0ZIl/3kLFmJoUqhe8d936q7/7DnzIYYA9cD9jOzs999+/l955h6GASVrT/4HAe68BXDvgMOENXPONGSFvbsv/4+bf4AYA/6D9DJAQxsumLns6H/Xidvh3mnccu5/09bzYIxuAEgvyIAfyAY8fv/l/+zDN8DsXdee/P/37///Zx++/f/99x82A37gNECucgVYQ/uOi/85cuf/33DhIU4DbmAzwK53C1iD95SdOMMA7AX0QIQZYNG5CawhYs4+sHjVhjPYAxE9GmEGCBYt/v/j95//R+68AMfK68/fsUcjekK6/+bz/+kHr4PZlRtOgwMRZBDIUBCbC2oAIiGhJWWQAlhqA2Gp8mX/RYDRCWLz5C/EnpQpz0wUZGcA8VIBKxftGrIAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage3 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABZ0lEQVR42mNgQAPS/acM5KZemC435fwNIP0djCHs6SA5BlxApu8YJ1DhTLmp5/8B8X8c+B9IDUgthmbZyecP4NGIgkFqUQyRm3puBkxSZ86l/6ozLmJoMltw5X/clrv/5addgIqdm4HkZ4SzP/z483/T7fcYBjQdefofBJRnXIB7Bxwm8tPOTUNWOOP8y/+5ux8QY8B/kF4GSAgjFJbvf/Q/fMMduHd6Tz3/3wfF6AaA9AL9f/4HsgHPPv/6v+TqGzD74KNP//8BNb34+vv/77//MQ0A6sVpgAkw0EBg8pkX/xWAAbfj3gccBqB5AWZA4NpbYA2xm+/iDAOwF9ADEWaAz+qbYA0ZO+6DxduPP8MeiOjRCDNAY9al/z/+/Pt/8tkXcKy8/f4HezSiJ6RHn37+X3j5NZjdduwZOBBBBoEMBbGVpqMlJPSkDFKASG3n/xvMu/xfe/YlMFsFmkIxkjLFmYmS7AwACZlUmOJIg3UAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage4 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABW0lEQVR42mNgQAOh+58YRBx6Nj380JMbEYeffgdhMBsoBpJjwAVCVz3mjDj4ZCZQwz8g/o8D/wOpAanF1Hz4yQE8GtHwkwMohoQffjYDJpl0/Pn/2KPPMDRlnXrxv+Pq2/+RRyByID0IPyM5+8vvf/+Pvf6GYcCiex//g0AMwvB/4DABOmcassLNT778n3zzPTEGgLwyjQEa2nDBWbc//G++/AbundUPP8ExugEgvUAXPP2BbMCbn3//73n+Fcy++P7H/39ATe+AYn/+/cfigqc/cBqQcfIFWMP6R5//RwED7tSb79gNQPcCzIC6i6/BGtqvvMUZBlAvoAYizICqCxAD+q+/A4svu/8JeyCiRyPMgIRjz///Avr7+sef4Fj59Psv9mhET0ivfvz5v/MZJBCX3v8IDkSQQSBDQexo9ISEnpRBCmCpDYTTTrwARyeIjbAdLSlTnJkoyc4AmGau1lj8h68AAAAASUVORK5CYII=";
  private static final java.lang.String externalImage5 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABQUlEQVR42mNgQAdZswxYs+ZPZ8uedwOIv0PxDZAYSI4BJwjt42TPnjcTqPgfEP/Hgf+B1IDUYmhmzZl3AI/G/xadm/4rVq8Es0FqUQxhzZ43A59mucoV/998+fH/zutPCEOAeuB+xuds9pz5/3dff/ofBm6/+vhfoXoF2DvgMGHNnDMNXRNX3oL/eSuP/1955t7/bVce/0cHU/ZfhbgCqJcBGtooBrhO3P7/z99/GBp//fn7v37z2f+cuQtgam+ADPiBzemRc/ajGPLx+6//Jm0b0NX9wGmAXe+W/1nLj8IN+fDtJ7Yw+oHVC6BAevX5O9iv0fMgLjn94DU2A25gBCIoAI/cefG/edt5cAyAxECGeE/ZiWEAOBDRo7F201msirGlSnjShiUkUCJRqllJjGakhERkUkbRjJ6UKc9MFGRnAPQo9HNMFkNjAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage6 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABRklEQVR42mNgQAPS/acM5KZemC435fwNIP0djCHs6SA5BlxApu8YJ1DhTLmp5/8B8X8c+B9IDUgthmbZyecP4NH433vVzf9mC6+C2SC1KIbITT03A59mkwVX/r/7/uf/gw8/4YaA9CD5Gbez5add+H/w0af/MHAfZAjQQJAecJjITzs3DV2T0vQL/2sOPfm/6fb7/3sffPyPDuZdeg01/Nw0BkgIoxoQvuHO/z//MPT9//X33//uk8//KwItAKsF6gX6//wPbE7P3HkfxZBPP//+d195A13dD5wGBK699b/iwGO4IR9//sEWRj+wegEUSG++/Qb7NXvnA7AhF15+xTQA5AX0QAQF4MlnX/73n34BjgGQGMiQ2M13scQQMBDRo7HrxDOsirGlSnjShiUkUCIxhycUQhiakIhNysgYIylTnJkoyc4Am2NK0YgHph4AAAAASUVORK5CYII=";
  private static final java.lang.String externalImage7 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABP0lEQVR42mNgQAOh+58YRBx6Nj380JMbEYeffgdhMBsoBpJjwAVCVz3mjDj4ZCZQwz8g/o8D/wOpAanF1Hz4yQE8Gv9Xnn/9P+vUCyj/yQEUQ8IPP5uBT3PGyRf/P/3++//F9z9wQ0B6EH7G4+zII8/+X3z/4z8MPEcY8g8cJkDnTEPXFA3UNO/uh//HXn/7f+4dQjMMbH/2BeaVaQzQ0EYxoPnym/9//2OCP//+/1/58NP/KKAFYG8A9QJd8PQHNqdPuP4OxZBvQN3l516hq/uB04C6i6//z77zAW7IV6ABWNT9wOoFUCB9/PUX7NeJNyAuufP5F4YBUC+gBiIoAK9//Pl/zaPP4BgAiYEMab/yFosLgIGIHo0rHnzCoRgzVcKTNiwhgZyeDU9t+DE8IRGblNGcfgB7fiA3M1GSnQGMB6g+CWkhlwAAAABJRU5ErkJggg==";
  private static com.google.gwt.resources.client.ImageResource closeClicked;
  private static com.google.gwt.resources.client.ImageResource closeHovered;
  private static com.google.gwt.resources.client.ImageResource closeNormal;
  private static com.google.gwt.resources.client.ImageResource idClicked;
  private static com.google.gwt.resources.client.ImageResource idHovered;
  private static com.google.gwt.resources.client.ImageResource idNormal;
  private static com.google.gwt.resources.client.ImageResource pinClicked;
  private static com.google.gwt.resources.client.ImageResource pinHovered;
  private static com.google.gwt.resources.client.ImageResource pinNormal;
  private static org.reactome.web.diagram.context.ContextDialogPanel.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      closeClicked(), 
      closeHovered(), 
      closeNormal(), 
      idClicked(), 
      idHovered(), 
      idNormal(), 
      pinClicked(), 
      pinHovered(), 
      pinNormal(), 
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("closeClicked", closeClicked());
        resourceMap.put("closeHovered", closeHovered());
        resourceMap.put("closeNormal", closeNormal());
        resourceMap.put("idClicked", idClicked());
        resourceMap.put("idHovered", idHovered());
        resourceMap.put("idNormal", idNormal());
        resourceMap.put("pinClicked", pinClicked());
        resourceMap.put("pinHovered", pinHovered());
        resourceMap.put("pinNormal", pinNormal());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'closeClicked': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::closeClicked()();
      case 'closeHovered': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::closeHovered()();
      case 'closeNormal': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::closeNormal()();
      case 'idClicked': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::idClicked()();
      case 'idHovered': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::idHovered()();
      case 'idNormal': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::idNormal()();
      case 'pinClicked': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::pinClicked()();
      case 'pinHovered': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::pinHovered()();
      case 'pinNormal': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::pinNormal()();
      case 'getCSS': return this.@org.reactome.web.diagram.context.ContextDialogPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
