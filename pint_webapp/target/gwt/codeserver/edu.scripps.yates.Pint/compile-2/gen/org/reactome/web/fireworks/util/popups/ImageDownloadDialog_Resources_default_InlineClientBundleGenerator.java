package org.reactome.web.fireworks.util.popups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class ImageDownloadDialog_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.util.popups.ImageDownloadDialog.Resources {
  private static ImageDownloadDialog_Resources_default_InlineClientBundleGenerator _instance0 = new ImageDownloadDialog_Resources_default_InlineClientBundleGenerator();
  private void closeClickedInitializer() {
    closeClicked = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "closeClicked",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 24, 24, false, false
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
      0, 0, 24, 24, false, false
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
      0, 0, 24, 24, false, false
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
  private void headerIconInitializer() {
    headerIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "headerIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage3),
      0, 0, 24, 24, false, false
    );
  }
  private static class headerIconInitializer {
    static {
      _instance0.headerIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return headerIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource headerIcon() {
    return headerIconInitializer.get();
  }
  private void uploadNormalInitializer() {
    uploadNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "uploadNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage4),
      0, 0, 23, 20, false, false
    );
  }
  private static class uploadNormalInitializer {
    static {
      _instance0.uploadNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return uploadNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource uploadNormal() {
    return uploadNormalInitializer.get();
  }
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.util.popups.ImageDownloadDialog.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-popupPanel {\n  border : " + ("3px"+ " " +"solid"+ " " +"gray")  + " !important;\n  border-radius : " + ("15px")  + ";\n  padding : " + ("5px"+ " " +"10px"+ " " +"5px"+ " " +"10px")  + ";\n  background-color : " + ("#58c3e5")  + ";\n  background : " + ("-webkit-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-o-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-moz-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background-image : " + ("-ms-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : ") + (("small")  + " !important;\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-analysisPanel {\n  max-width : " + ("600px")  + ";\n  max-height : " + ("400px")  + ";\n  width : " + ("100%")  + ";\n  padding : " + ("2px"+ " " +"2px"+ " " +"0"+ " " +"0")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-header {\n  float : " + ("right")  + ";\n  padding : " + ("2px"+ " " +"0"+ " " +"2px"+ " " +"0")  + ";\n  width : " + ("100%")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-headerIcon {\n  float : " + ("right")  + ";\n  margin-left : " + ("8px")  + ";\n  margin-top : " + ("1px") ) + (";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-headerText {\n  float : " + ("right")  + ";\n  min-width : " + ("370px")  + ";\n  color : " + ("white")  + ";\n  font-weight : " + ("bolder")  + ";\n  font-size : " + ("large")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  cursor : " + ("default")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-close {\n  height : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : ") + (((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("left")  + ";\n  margin-bottom : " + ("10px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-close:hover {\n  height : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px") ) + (";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-close:active:hover {\n  height : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-unselectable {\n  -webkit-touch-callout : " + ("none")  + ";\n  -webkit-user-select : " + ("none")  + ";\n  -khtml-user-select : " + ("none")  + ";\n  -moz-user-select : " + ("none")  + ";\n  -ms-user-select : ") + (("none")  + ";\n  user-select : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-undraggable {\n  -webkit-user-drag : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-imagePanel {\n  background-color : " + ("white")  + ";\n  width : " + ("99%")  + ";\n  overflow : " + ("hidden")  + ";\n  text-align : " + ("center")  + ";\n  border-radius : " + ("5px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-image {\n  width : " + ("auto")  + ";\n  height : " + ("auto")  + ";\n  max-width : " + ("590px") ) + (";\n  max-height : " + ("320px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG {\n  background-color : " + ("#45b0d8")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  float : " + ("right")  + ";\n  font-size : " + ("large")  + ";\n  margin-top : " + ("6px")  + ";\n  padding : " + ("6px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG img {\n  vertical-align : " + ("middle")  + " !important;\n  padding-left : ") + (("3px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-infoLabel {\n  float : " + ("right")  + ";\n  width : " + ("300px")  + ";\n  color : " + ("white")  + ";\n  font-weight : " + ("bolder")  + ";\n  font-size : " + ("smaller")  + ";\n  margin-top : " + ("5px") ) + (";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace {\n  background-color : " + ("#45b0d8")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  float : " + ("left")  + ";\n  font-size : " + ("large")  + ";\n  margin-top : " + ("6px")  + ";\n  padding : " + ("6px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace img {\n  vertical-align : " + ("middle")  + " !important;\n  padding-left : " + ("3px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace:hover {\n  background-color : ") + (("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n")) : ((".org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-popupPanel {\n  border : " + ("3px"+ " " +"solid"+ " " +"gray")  + " !important;\n  border-radius : " + ("15px")  + ";\n  padding : " + ("5px"+ " " +"10px"+ " " +"5px"+ " " +"10px")  + ";\n  background-color : " + ("#58c3e5")  + ";\n  background : " + ("-webkit-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-o-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-moz-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background-image : " + ("-ms-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : ") + (("small")  + " !important;\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-analysisPanel {\n  max-width : " + ("600px")  + ";\n  max-height : " + ("400px")  + ";\n  width : " + ("100%")  + ";\n  padding : " + ("2px"+ " " +"0"+ " " +"0"+ " " +"2px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-header {\n  float : " + ("left")  + ";\n  padding : " + ("2px"+ " " +"0"+ " " +"2px"+ " " +"0")  + ";\n  width : " + ("100%")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-headerIcon {\n  float : " + ("left")  + ";\n  margin-right : " + ("8px")  + ";\n  margin-top : " + ("1px") ) + (";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-headerText {\n  float : " + ("left")  + ";\n  min-width : " + ("370px")  + ";\n  color : " + ("white")  + ";\n  font-weight : " + ("bolder")  + ";\n  font-size : " + ("large")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  cursor : " + ("default")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-close {\n  height : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : ") + (((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("right")  + ";\n  margin-bottom : " + ("10px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-close:hover {\n  height : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px") ) + (";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-close:active:hover {\n  height : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (ImageDownloadDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-unselectable {\n  -webkit-touch-callout : " + ("none")  + ";\n  -webkit-user-select : " + ("none")  + ";\n  -khtml-user-select : " + ("none")  + ";\n  -moz-user-select : " + ("none")  + ";\n  -ms-user-select : ") + (("none")  + ";\n  user-select : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-undraggable {\n  -webkit-user-drag : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-imagePanel {\n  background-color : " + ("white")  + ";\n  width : " + ("99%")  + ";\n  overflow : " + ("hidden")  + ";\n  text-align : " + ("center")  + ";\n  border-radius : " + ("5px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-image {\n  width : " + ("auto")  + ";\n  height : " + ("auto")  + ";\n  max-width : " + ("590px") ) + (";\n  max-height : " + ("320px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG {\n  background-color : " + ("#45b0d8")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  float : " + ("left")  + ";\n  font-size : " + ("large")  + ";\n  margin-top : " + ("6px")  + ";\n  padding : " + ("6px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG img {\n  vertical-align : " + ("middle")  + " !important;\n  padding-right : ") + (("3px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-infoLabel {\n  float : " + ("left")  + ";\n  width : " + ("300px")  + ";\n  color : " + ("white")  + ";\n  font-weight : " + ("bolder")  + ";\n  font-size : " + ("smaller")  + ";\n  margin-top : " + ("5px") ) + (";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace {\n  background-color : " + ("#45b0d8")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  float : " + ("right")  + ";\n  font-size : " + ("large")  + ";\n  margin-top : " + ("6px")  + ";\n  padding : " + ("6px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace img {\n  vertical-align : " + ("middle")  + " !important;\n  padding-right : " + ("3px")  + ";\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace:hover {\n  background-color : ") + (("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n"));
      }
      public java.lang.String analysisPanel() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-analysisPanel";
      }
      public java.lang.String close() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-close";
      }
      public java.lang.String downloadPNG() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-downloadPNG";
      }
      public java.lang.String genomespace() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-genomespace";
      }
      public java.lang.String header() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-header";
      }
      public java.lang.String headerIcon() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-headerIcon";
      }
      public java.lang.String headerText() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-headerText";
      }
      public java.lang.String image() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-image";
      }
      public java.lang.String imagePanel() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-imagePanel";
      }
      public java.lang.String infoLabel() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-infoLabel";
      }
      public java.lang.String popupPanel() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-popupPanel";
      }
      public java.lang.String undraggable() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-undraggable";
      }
      public java.lang.String unselectable() {
        return "org-reactome-web-fireworks-util-popups-ImageDownloadDialog-ResourceCSS-unselectable";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.util.popups.ImageDownloadDialog.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.util.popups.ImageDownloadDialog.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACdklEQVR42r1W3UsUURS/uc3uUz30VOAfUBL5B/R/SBAZZWs6jUhkQUFPFQa9RcWus3cGlW0hKEUlKPJNQeshMChMe+njTUQC2WaxTud3Z2e786UrG3vgsJd7z/mdu+fjd0eIXcQYkt05yx3OXnGms6bzmX83lfrraZzBRuxXskNOFwM4rNSkOvBpCtywZB87bOwDPNAN+O4KnjPlNd3poCnJMNNBcQYbfQ8YyTc3ZW/DyHJJ9NskLhRI5McoEwVhzQxKEn1FEhcLyjZn6YFlbzjnlnucD7YDAwAfu1Gm1x+/U395gcT5J9QxUFLA0AOXSwxeoDsv39PTt+t0eHhcBdIusQ1MLYAzHhwCyBgs0eTSGkG8nd/UMzZP4uwjMviWUKx77HkKRC6u0pGrE+xr/wvCmD64aZ/gDa9x+3yROm9VSJetao1OP5glceahUqy3ql7I5tTdKRKXivq/8IAtOHcjocLy7Q3O+8jz5RDAz181Onn7mVKsdbn+YlmlCL6hWjG24MVMtIAorDj3mEZfrYSAqpwuqC732Qa28MnFO21G1KcyGtnvnrxNE0vrlCY462AbBW4ltDJji/r4UzyI67chd8zcytcY+CzvCdVNRWWbMiubewfgrpr78K2FAC2mKNNEiloq8uheRU5qU0wpWq/ZNkVLo7WT2zQ6aDws3femIoPmJQxaOEjnzYoa0tigxanCVmMvF1YbzqCFGFUwfdR2/tRrsUZZvj1oJkYVaWR3iAms8u6LIjRMKQguIDsAgQAHyov05tMPOsrECJ9UsovTtePTNaiY2xDUHC0gKBzpVJSu6NpNp+u2PDhteTLb8uj/78+Wv8QQdIlXHbpRAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACbklEQVR42r1WS2tTQRS+bgRR0GqjxqRJBPG1sIuu3Ovaf9CNtNsurP4AuxBUjOZFXVRpF7pQpHTTRUFFcGGTO9c39S6sRIQK9UIjXaQ0Hs83zo0zN5OYgs2BA8PMOd+Zex7fXMfpIEdzYjBV9MZYZ1l91kCpr/bGYONsVVKT4nS64N1jAOpGpS37dAWeLoqL7LTaLbimq/D9F/gl3WmgwNoBdEDZGF/DGPa0FMVw06jk0cE7Lu29VaH+2y4lCnDUQTy515d1aV+2QodyrvT5G0gMG+CZfOUkH6yHBgAenHpHz6s1uvy0SntululIXkhgaJzXfQycXVyhJ58COj75Wvpol1gHpnZ7bzoEhzNu92gpIEi98YtG5pdp5/VFeUso1qO8F8qDjz/oxN03dDgn9HRNq7y7p1IFrx4exDglQ/ffky61eoMuPPZpx7VXUrGu1TcNm3MPl+gA+zYDMCawOYA3rhcpwV8Q4/xfffnNAPi50aCzMx+kYq3LBNsiZcm8iBTcG0dx5yKbMsCuG2Uqiu8G0AanC6oLbGALn3RLp4k5R02lGbn0J1XopLAWNsEZbGBrdlFTfUeNfsthhh3282fHuXALX9ZawBeW1+QZbDKltrMS9CTAdqdom4scbdOkmtKJLbQpWhoBErY2jQ4ahuU8D405aJuWQTODYDhjtkGLUgXGHWOP8Q9l1EIVI/Ofm+lCLUAv8byFKqJkl1Zkd4wJbNYPKFtekSmLa2QH4tvNBHjlWZVefK3Rmam30ifVjuxsdA0KBjAo2UbXSd7rV5SOTupI1+0eHIBEH5Tog5Ts9sHpyZPZk0f/f/+2/AaX3iLghaFcpQAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACMklEQVR42r1WTUsbYRDen6O9+CMEkUqiaxOTHMylPXgQPFRv/Qv1roeC9aogXks0u5uKIhbsoUJaesh+5KPtkqJZDSkZ51l35d2vNAHNwMDyzswz7zvvzPOuJA2QnGZM5TRrLV8xD/KaWWW1Pa1iDTb4SKNKVq2/KGjmBwaiYRS+iBkKnHf2Oq+av4cFf1TEcOxA8IJmvBWDcp4mgcbZgZFU76JwZFpUDEod67RQNmhJjZTEXZtnW5p9XrFvIZDYKAZ3fmJNsKHjOwD4zWmdLu072vreprkjnbJCkgx/p48N2qtdk9ZyaPmz5cYIm+gAUyiNtSMGZ1WDlKZDkF6f6P03m2ZKurtLKL43r2zypdToUJGTZBTxlNbOQ2kUa5IXur5B5uOunDVIlM6/Pr27/EXTn2qu4htroqxftNxyCqfoAhtdsy7WF7WV2fHjz78BAIcBV8+brjoh8F32xV0shRuBsflyzcNwdyDBLJfhQL8JAPX6fVdFgQ985eDu/S47lLwJjXQJSoUgpeVQksAGHznURYJWJW/046aT0mWdL86gL3/uIuBYgw0+heRZsceS4HlL9PyXHG5TlIZbbneENkVLyzGU8tCmoUHDsGzw0Iw6aBhOlCoyaBGq4HHH2GP8fQEthKkC9NHzcoBWQC8ZNYYqksgOBFZp3dI+ExqITQwG8b1kAtz+0aav7a5LjAPJLo6uQcEABiUDMNwlPqWk2Gfxf3Q9lgdnLE/mWB79p/5tuQeTqeRBz8pWpQAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAWCAYAAADafVyIAAACQElEQVR42rWVu4sTURTGrw/EB1ooNmshCFq4FqJbuCCigo9efOC/INpYqUWwElcEi20sbCxEs5WdhRpFKxFhNWynYZ0lhh0DJmMeM3n5+/SOjOMMu2PigY/cxznfd+fcc26MWaY1m81tvV7vZr/fnwNt0ARvWbtcLpfXm2Gs2+2ehcwbWGPcHUSM+acgCPZm4VzR6XSOcrqrYAqCnogYP4BoXy6XW+k4zjp8jrH32opU2+32ziWZXdfdiPOTQcwgv5LkXygUVrP30Io8XVIA53vWeYHxdXCH8Yy+Ki2mWq1uwuer4nzfHzdpJyHXp3D0QdBqtXZkyWl4MKVUXH9sKneQzkYubS5rIUB8LRL//vd91Ov1LSzMh8Q4XuLyDmYV4Iu3E3tRHJbrc61W2yzlKbvwUpVhhrRSqbQWrhc2XbcMk4+aUH4TZkQG1/6wPyTga1IsFtekBVAZe0jbIXDY4oj6hDzvSvIXlxXwTZj/1PL61cnnbXV9AzVQ13OhRks50LgVmNcdTNvJ43w+vypF4Bz7ndjz4CFwPO4rDnHZO5j++Yix4NqgV5CdJnCSPB7gJLsjb1GSwIlICicVKw6774o7vJQJFpz486BqsAJn1HwJAie1z/hNbM/5q2gqlcoGPukCeITDc1BgfNfzvK383k4Q+M76jUajMcbvfcUoVhziWla5qVnAot6Z8EWNPtlKA/gCPvxzTRP8Lp7/iEgAng3dOGpG0E/4gtlRNadEFmMCC2bUpv/h8ILN/zIJZPH/ARrhWGFdRdsyAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage3 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAABW0lEQVR42mNgGAWUghcvXnD//v3bDYg90LDTqlWrmCk1n/Hfv3+b/uMAf//+XQRSQ7RLgRoKgLgCCc+EGrTwz58/IcgYKLYSKrccTU8ByCwMC4CaArC5EuiDC/fv3+dAV3/mzBlWoNxmbHpAZmGzIBgqmfLjxw8VGH78+DEnSP7z58+iQLlQIA7/9u2bNEgMFAdANcowtSC9UDOC8VngjS4H9HYu0LWfgPgwEO8H4s9AsXosZniTbAGQ7ws08OWvX7/0YWIg1wLF7gPlkii2AGjQHqBr07GoDwTKXaKGBQ+ArjdGVw+MB1mg3DdqWHAYKBaDrh6U+YByN6kRB7Gg8Aa5GCb26dMnEaDYRWDQFVNsATQVdQIN/ADNVIuB7DdAenZ9fT0TVSwAgZ8/f2oBDS0EuRoYJ0bY1BBlAdB1d4H4PJn4Lk4LQDkSqOAIEJ+mEB8BmTVaD40C6gEAOl+6R9Hw2wgAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage4 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAUCAYAAABmvqYOAAAB5klEQVR42tVVvy9DYRRtTAgGJEIiwsDgD2BETCY2kbAZJWoxlgqJEGy2msRIJAyWrhYhYmhjKHnJC31t9Pfv1zqnuS956vU9ZXKTk/b77jnnu73ffa8u1w+jVCrNlsvla0ADisCLruvH2Wx20PXbUFW1FUbnFQl8LwFp0zqLg5d+bKhpWjsEy6hsD2K/mCjYm+dh5GQymX7J6wRyc47G+Xx+lEYVU2D9mk6ne+u0a0E4b8bBluHxeJpAehLylVR/gs8Zu4LAvaSGB1kSEolEN4y2xfiOBzVw2YtGQfT5kiwWi5NIRIw2cAoauXToJ0wtjGI9VU1glIawkRDTUySmU6lUj5UJ8mtWFxcKhZppiPyZHJCkLwU+MT6yqy4cDrdBFCPXjgefQ/Hz8TJUzm48Hu90uLSy6adr9XjoeZc8CypFOVbkYBypGU3O9r0NP0ZffglSkMvlhusQH2hWqQkp6qKWTx/JB9kjryxuFEVpMZFGOL9AoVInkMtAf4DLG6CGevpIz72uaDTagY2AkJ+xuQWsMgnsAzt8UVkYJ5kDdoF16qiXXIC+1SrxaPcZ7xBzQLAirfmwMH+U6dis2ffT71t/C4XCOMhuYIPAeszJnA+g8N3UN/zatTP/c/xr83fjT8GEWyfdJ1xXQtXWRid5AAAAAElFTkSuQmCC";
  private static com.google.gwt.resources.client.ImageResource closeClicked;
  private static com.google.gwt.resources.client.ImageResource closeHovered;
  private static com.google.gwt.resources.client.ImageResource closeNormal;
  private static com.google.gwt.resources.client.ImageResource downloadNormal;
  private static com.google.gwt.resources.client.ImageResource headerIcon;
  private static com.google.gwt.resources.client.ImageResource uploadNormal;
  private static org.reactome.web.fireworks.util.popups.ImageDownloadDialog.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      closeClicked(), 
      closeHovered(), 
      closeNormal(), 
      downloadNormal(), 
      headerIcon(), 
      uploadNormal(), 
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
        resourceMap.put("downloadNormal", downloadNormal());
        resourceMap.put("headerIcon", headerIcon());
        resourceMap.put("uploadNormal", uploadNormal());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'closeClicked': return this.@org.reactome.web.fireworks.util.popups.ImageDownloadDialog.Resources::closeClicked()();
      case 'closeHovered': return this.@org.reactome.web.fireworks.util.popups.ImageDownloadDialog.Resources::closeHovered()();
      case 'closeNormal': return this.@org.reactome.web.fireworks.util.popups.ImageDownloadDialog.Resources::closeNormal()();
      case 'downloadNormal': return this.@org.reactome.web.fireworks.util.popups.ImageDownloadDialog.Resources::downloadNormal()();
      case 'headerIcon': return this.@org.reactome.web.fireworks.util.popups.ImageDownloadDialog.Resources::headerIcon()();
      case 'uploadNormal': return this.@org.reactome.web.fireworks.util.popups.ImageDownloadDialog.Resources::uploadNormal()();
      case 'getCSS': return this.@org.reactome.web.fireworks.util.popups.ImageDownloadDialog.Resources::getCSS()();
    }
    return null;
  }-*/;
}
