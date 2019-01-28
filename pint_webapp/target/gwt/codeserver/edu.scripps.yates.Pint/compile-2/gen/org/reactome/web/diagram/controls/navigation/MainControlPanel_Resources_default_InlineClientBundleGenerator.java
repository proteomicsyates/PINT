package org.reactome.web.diagram.controls.navigation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class MainControlPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources {
  private static MainControlPanel_Resources_default_InlineClientBundleGenerator _instance0 = new MainControlPanel_Resources_default_InlineClientBundleGenerator();
  private void fireworksClickedInitializer() {
    fireworksClicked = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "fireworksClicked",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 31, 32, false, false
    );
  }
  private static class fireworksClickedInitializer {
    static {
      _instance0.fireworksClickedInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return fireworksClicked;
    }
  }
  public com.google.gwt.resources.client.ImageResource fireworksClicked() {
    return fireworksClickedInitializer.get();
  }
  private void fireworksDisabledInitializer() {
    fireworksDisabled = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "fireworksDisabled",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 31, 32, false, false
    );
  }
  private static class fireworksDisabledInitializer {
    static {
      _instance0.fireworksDisabledInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return fireworksDisabled;
    }
  }
  public com.google.gwt.resources.client.ImageResource fireworksDisabled() {
    return fireworksDisabledInitializer.get();
  }
  private void fireworksHoveredInitializer() {
    fireworksHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "fireworksHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
      0, 0, 31, 32, false, false
    );
  }
  private static class fireworksHoveredInitializer {
    static {
      _instance0.fireworksHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return fireworksHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource fireworksHovered() {
    return fireworksHoveredInitializer.get();
  }
  private void fireworksNormalInitializer() {
    fireworksNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "fireworksNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
      0, 0, 31, 32, false, false
    );
  }
  private static class fireworksNormalInitializer {
    static {
      _instance0.fireworksNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return fireworksNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource fireworksNormal() {
    return fireworksNormalInitializer.get();
  }
  private void fitallClickedInitializer() {
    fitallClicked = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "fitallClicked",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage3),
      0, 0, 32, 32, false, false
    );
  }
  private static class fitallClickedInitializer {
    static {
      _instance0.fitallClickedInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return fitallClicked;
    }
  }
  public com.google.gwt.resources.client.ImageResource fitallClicked() {
    return fitallClickedInitializer.get();
  }
  private void fitallDisabledInitializer() {
    fitallDisabled = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "fitallDisabled",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage4),
      0, 0, 32, 32, false, false
    );
  }
  private static class fitallDisabledInitializer {
    static {
      _instance0.fitallDisabledInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return fitallDisabled;
    }
  }
  public com.google.gwt.resources.client.ImageResource fitallDisabled() {
    return fitallDisabledInitializer.get();
  }
  private void fitallHoveredInitializer() {
    fitallHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "fitallHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage5),
      0, 0, 32, 32, false, false
    );
  }
  private static class fitallHoveredInitializer {
    static {
      _instance0.fitallHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return fitallHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource fitallHovered() {
    return fitallHoveredInitializer.get();
  }
  private void fitallNormalInitializer() {
    fitallNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "fitallNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage6),
      0, 0, 32, 32, false, false
    );
  }
  private static class fitallNormalInitializer {
    static {
      _instance0.fitallNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return fitallNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource fitallNormal() {
    return fitallNormalInitializer.get();
  }
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.navigation.MainControlPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-mainControlPanel {\n  float : " + ("right")  + ";\n  margin-right : " + ("10px")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fitall {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getTop() + "px  no-repeat")  + ";\n  background-color : " + ("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : ") + (("32px")  + ";\n  float : " + ("right")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  position : " + ("relative")  + ";\n  outline : " + ("none")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fitall:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getWidth() + "px") ) + (";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fitall:active:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getTop() + "px  no-repeat")  + ";\n  background-color : ") + (("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : " + ("32px")  + ";\n  margin-right : " + ("10px")  + ";\n  float : " + ("right")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  position : " + ("relative") ) + (";\n  outline : " + ("none")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:disabled, .org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:disabled:hover, .org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:disabled:active {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : ") + (("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getTop() + "px  no-repeat")  + ";\n  cursor : " + ("auto")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:enabled:active:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getTop() + "px  no-repeat")  + ";\n}\n")) : ((".org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-mainControlPanel {\n  float : " + ("left")  + ";\n  margin-left : " + ("10px")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fitall {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getTop() + "px  no-repeat")  + ";\n  background-color : " + ("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : ") + (("32px")  + ";\n  float : " + ("left")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  position : " + ("relative")  + ";\n  outline : " + ("none")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fitall:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getWidth() + "px") ) + (";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fitall:active:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksNormal()).getTop() + "px  no-repeat")  + ";\n  background-color : ") + (("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : " + ("32px")  + ";\n  margin-left : " + ("10px")  + ";\n  float : " + ("left")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  position : " + ("relative") ) + (";\n  outline : " + ("none")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:disabled, .org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:disabled:hover, .org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:disabled:active {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : ") + (("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksDisabled()).getTop() + "px  no-repeat")  + ";\n  cursor : " + ("auto")  + ";\n}\n.org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks:enabled:active:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fireworksClicked()).getTop() + "px  no-repeat")  + ";\n}\n"));
      }
      public java.lang.String fireworks() {
        return "org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fireworks";
      }
      public java.lang.String fitall() {
        return "org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-fitall";
      }
      public java.lang.String mainControlPanel() {
        return "org-reactome-web-diagram-controls-navigation-MainControlPanel-ResourceCSS-mainControlPanel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.navigation.MainControlPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.navigation.MainControlPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAgCAYAAADqgqNBAAAE9UlEQVR42rVXfWxTVRR/RtrFL4IaxT/8g2AUNTExgURiJDGamGgkmBgSFaImJHVdK0yBaZjZFAnMSBiY6dzW10JhiM7hPtgcsC82MFBlUNjazQ473Bfb6Gy3bl3Xvh7Puet7e6/tttdOT3Lz2vvOOb97z/fjOLWk51dpjaYsrcFchculNfA+fIaiTxfbx/fEx/1XlJZhWq/R8+dROahdxE9yqaPqih7TGPj6ZEDjDoHypCcpXO0HJRtQ2LsY4Bf2nxJ/e0mfKuA79ab3UCC8GOCNxQ1w0zMOYSECz+wup70w6Z0fOL349WSA04wWOG67ATvKLyn2r/aNApGAK6fqsrgfJv2JkdMLVyDDqKjg0U9/gB6PHwa8E7Cz3BYHvPKzH+HW2CQD8QdDkH78Attfs7eC7QXDAjR2DcBDO47J5UYJJ97Per5Orvy2fwpEGvRNxoHvrrkCcppGsGBIANOFLskqCS2GODF+5l+d61ZEU6j0+a+r4xS5b4/PmDcCUN7mnhswZhGeBK4xmptjGc65BiGAoCHUjLqh758JePrzn5WBVdLIDna6o091MK5GtxCeWLlW4mYklukpBMo6aYOc6jZ4POcn+Atv6Rr2wQMfH1XwjYwHYF+dXRWwEIlAP8bQJyd/B8Ll0gx8phrBZ/f8grcMw/6z16S9J3PLmNk3m5ulvZcP1sL2skvw55CPXUDcfxPTbywwLbmRcDmt0Vyu1mQHG9phAiP7/ujt8/DGdKCHd5bKeDqkWCF30d6D249CQbMDHINe8OEB1n5VBYTLYWNwEsNdHx6GVXiT+XKaCgZRhb0H3uKb2EEKzzklnvsyj8D4VEgCp3ihoLzePwrrvz3DeO7ddiTKzzu5aFfCQmFj6UXCxa2dcA0FhtGfFGgtGHxf1ipTi0xHNUB+a4oNkhGpe2RMESPv4IFpRcF9HFUeytmQIEhCgekwlNq6WSDl17dDHUYzpVMs+fCgyxH8pfxaKGjqYJagmlDU2sVcZL3ogu9bnPBawWl4IqcM0zcA3Ri0ZGXCJXB/xdWbCqWt3UNxZl+IyJeHGtsVlqBF/s2tvgyeiSDjC+MlDtRfp3d+AnezlEGTD+HJ6hyJc5bSLJbIp7pj5+HFAzVwz9bDCwYspWUPNpzof7dUVpdmWtkpyW9zCZOP5e4hV7xy6NfUuh+V2SUZfK5agUeySiG78g/Yi7FQZZ9xFRWNZR9ZkwYnXE6jK3oulZOTb8X6vwczIekpB3FnOprB3JnKAfTYRom8k0GW40nIds42Fj2vSwWcgmwkmtebzE3JDJi62Z66MVcrVrpEi/zrxihN1DJLsCARiX184YWVDfEUPX2JvmRd7AhFYFTJpIEBw3t5TB5vsbawd1d6PWrAw4STeGo1WHbJmVdkn2C5P5taEdhsaVYoXIdTKhGV5oXBLbvmHSI1etN3coGGzgEckSKKcelt06x/xWZDk+p8kwzpVTM936E1WvbJB4wvatqwdHZAvbNfAqJBkd7RdCPSHOAR0kd61X846E1vaAzmW3JFd2N0N6IliGzuYQa2OjqtTmIziv9iQXnUk9on0/v5yzA687QZ5jH59EKmJ9pQeJZ1NCIqOBIw4+fzSH7xX4ubvlmqMfJbUHElzd48phXRbzeG4F0MQCJ7r4cypZL4iJ/7v+jERfda/1TIccr+d/YZR++2oCC0CYJgTVbPv7N21H0IifFYAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAgCAYAAADqgqNBAAADj0lEQVR42rWXvW4TQRDHTUOJeAQERR4BmjxAJBC8AQUSLVQUoUgJT8ADUEaigCoFUigS5dv5tp04iRPly0kUx3acT198zG91c1pvzs7agZFW9u3NzH92PnbmUilPmpqa6pH1Sdav6enpvKyKrHr0m2c/et+T+lc0OTn5SpSPyAo7WCPIdQ06Ojr6TE7xu0PQpoU8ejoCFqHXIly+D3Aul9P/ZfR5AU9MTLwVgeA+wOvr6+HV1VUILS0tsReg9y7gl50Az8zMhMfHx+H29nbT/sXFRai0u7ur+xjwMhF4bGzsiTCUVMH8/Lyx/vr6OtzZ2bkFvLCwENbrdQNwc3MTbm1tmf1MJhMDV6vVcG5uzpYrgXMLXF4M2cqDIIiVAOKC7+3thTY1Gg2zjo6OYq+08NiQm2B9rU6lirPZ7C1FGlfo5OSkHaBbBX0xuAj9cRlOT08NqBLuj5KnKbHgqVQq3slIWMDTS+SpbDZcJoCINQmzuLhoTnl5eRnOzs428eGh/f19L2A9BHrBxeUffQSXl5fNKYvFYryHUdDGxka8t7KyYrIfQ21Pra2tmcTUMIIL+A9flx0cHBgFenpOjCI7o+GxiT34Dw8PTQkiT/6AS5ZnNTs5Sbua5iRQuVw2p0URSpUnnU435QlEuM7Pz8N8Ph/zRPzZVNSVjKsoLxRSLggQT2JE8iWVFu/sU2O8XSVujmCwFaIK4EGSYm4u3EqMyeYkwlDAibOGBHCM5xkdeGZ1dTU2DIOikgwAr+FGm2q1WmKmtiO8BqBzo5n4UjH2pQUfuIAXtGRYrWoWi5Noc3PTdDArli0X+smB6LkQX6sIY2W7pCPGLuHSLrvfEKU24CuAS3EheaChwiD34vG8YgfoZs+7sRxDNLNJ2E7lwdWOluvGANqoJptPzK2Vs4fE992AA6int6/YuxZ4Mfjg4OBDvemSFvElS5NaJjUNaR/3WFnw3BGq1x2hAHOvS7eOC4WC2edG9ABmlOptNbX2txsqIMDcKbXVtJOQ4f13fSR8swWYw9yrlyHC7vtK7SYZ9PpMzw+E8Ys9YFBKXIm2IUwknuAN9KHX+8NBYvNGBItuDqgB3P8867RKU0kALqKnq0+m4eHhx2L1V1FStdumJiE9mo6WEPMqcsjf+2NxfHz8kSTLOznpT2ZvLS9OT31DZ2dnAe/hgz/1v0j69Atxc6ZUKn2WLvhBPJGW5++d6vkLf7r1XE3T68QAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAgCAYAAADqgqNBAAAEzklEQVR42rVXW2ibZRiON17K8EJ01lQ3dSrqhRdO1A4EUWQbG4jghaigDOYBRcSBXkzGwKG4jiTNtq4e1q6brrayoa7dpi1S163NqackTWzTNGubtklM0qRtjq/v8zX58+fU/kn1pR/9833v+z7f/55/lUohbT42sK1Ga/xErTNdVDeYnbxCvBKZ/07s4xx8qv+KNmuMu9V6Uy8DkOLF/JCrGvQOjWGrusFytSLQomW5Cj0VAd+jM+9h4eBGgPf85Mg+B6FPEfDdevMbLJDcCPC+Sy66uRinZJroubM27CWhd21gnXFXJcC1egt1OAJ0qHc6b3/Ut0ygFK8vb8xm95PQXxL4zvq+e5khkFXwxHcj5AnHyRtN0KG/pouAt58epXk+A0UTaTrQ7RH7L/5oF3txRu69uUiPNg3J5QLAKQLnCO2UKw8sJylLACkEP9rvJTkBLJZK09lRn2SVMpnQmQdcozW9VPRWSwlJcYydt7vNUaRoKhyTzPvreLA8YMECXi66teaeQoa+6Qit8MuzRYn/aDYSpx2t1qLAwsV6psKKg/GFH+wEvNV8Ptq/hTfThUw7zljp8LVp+ooD5unmUXKHYuQKxujhU3k+JD+7R2vwKgJmr4iXOHxthoCrUuuMHyoRfP6cjS2RpuOmOWmvji8Ieu/ypLT3ys9O+pyjfyK4QnWtNmn/7d9cFI6nJTcCl01ualdqslOWeVpKpOihxtW31xq94kKPNQ3neAbnpVgBFPYeYWt9O7RAjsAKLXJk7uT4Aa6KS6ANDPcdt9CzLdY1cxoFA3RpIkj7O13iIqeHFySeB04MUiSRlsDxiKC0+5fp9V/GBc/9zJMpvTZVpiuJQoH0irLClhEf2bhQ+Dji4aPrHHz1A/mpBdOhBsjfGrHhk2XJZCg/Rt7pmhQr8zsE8CRyNpHKKYYpO8YCIpAa2dTd7rAIlkKCDx//Zlj4GWaFJeb4Qi2c63BRmz1AzWyZ1y6O0zMcH0hfBC2sDFyARzrZjHK6MRMtMvt6FI6lBKDcElg7z4+JjPlnZbVood6fZD7gcrSbXdmUWeCb9bhL5yyit5Cg6OM/pujlDidtPbF+gQEGSrb4zbhSWX3w5CDtahsTfisnDB/L3QNXvHrh7+q6H8psTYP5oFIB+PdI3wxpDHN02RUSF/ByQG5rHKwYHLiqGp1hezU3h2+zXe3YgLdycMZd7Wg6s72aCxzonhLgoVhS5LhiWcbLtVOtcV814Agyf6b1vpvL3/UX4+V66sHzt2YrXanVxf71cKUq1TLPcE6Dsn1cwVBpA15eT79LY6wrHKEAhkomDQz8iKCT83z0u1ucjSwsKQFPAqfc1PqpnPnJ70dE7stTS97BsPa2O8QZSvN64NC/5hBZqzfp5QK9nkUxIuXGpTTt73JJ59lmAwOtNclAr5Lp+Ra11vKFfMD4un+Wmobm6U9PWALCoCiGjlardLEy4Gnog17lHw5aw14W9MoVbeGGgIkUZJ6LCjCMRaBl7p8lgL3QU9Un06b67k38AXiElYTl00s80+Le5B6NjlZiwg1DDvIb/li8XXP9tlqt6S0uDhcwe5+z+gXgwGyE3r8ymY32JM7BB37V/0XtTv9TPHRYr0wEP+t2Rz7ged2USqWaK9XzL45C4nZQR++uAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAgCAYAAADqgqNBAAAEgElEQVR42rVXW28bVRA2LzwifgKCB34CvPADKoGoCk2dQikQ1Eqg0gihVKRCFUKCIoEUWilCfetFAlpCRYFSaNr6FqDNpemFuiESTuxdx3Z8Xd/tXQ/zHXvXx2vH2Tgw0lHi45n55n7GLpdD2n1dfXrYp47t8SqX3D512e1Xc3zq4i9/xj2+B5/rv6Ihj/qC268EGIScHyUAuYFBd14LP8VKprcG2mXENPRsCXiPL/wiC2e3A/zh4rr5fxb6nIXZq+xnAX07wF8E07Re0ckgovfm47jTobe/x37l+a0ADwei5E+U6Mw/uY77laJOJn2zopn3OvT3zvGN0BPMkDYVHLwZE9anqwadDWldwO/cilGmZgiAitGgU8tZcX9kISHuAH8vW6U3/1iT5dLA6eG1ekVWrtUblvUAsYN/F86TTDqzQ+RarGhFpVe0gNMJ7InssHuVbXkFgtKj7QKyTqLSDC/MvJksbwjYZQDjWeDcEh47w4NclWqNpkdQnuLwj87FuwoLhi1mqo6LcYzTAjwBvOvq6pN82bAzAehcKEffrmp0aDZO8bJOa3xe/70jh5wegy5yCpwAm06c4xoCrmvYp4w6EXx/PiEicUlpAx1mA0EnHqatu4/uJuk0V3+UDR2dS1j3n3OUSq1MQg9wEfIppyH7WS2Iyja9vxjJC0UjUkWDxyR4irs3+Psr0SIpJZ1KnMfx26gfZQrgQTDs5WJ5dzbet6cxMECzqQpNsLcw5Ndo0eLZNxOlsiF1AB8UZbhYp+N/pQTPqzNmUSpBV+t1orMcKrQXhK+uFWmVBXJc8cgRis/eWvAYM0D2GrWRq7fRY5XOGoHBE+0U5QCuXwhrpNsUY3KhkH5SCrSYrlCDugk5fOvPNZHnX6LNlGAmwHiE3xsv0W8cmU/up0RUs6wYRbu32ZI6wAu3uEdlCmq1rrBvRsglAEc6Jxp9wPlFx+RbQwtx+ZEdAq7LHVBDZstgsMDLXjmHxXaCoq/+ztCxO0kplxsfjfVjZIvPjGuN1ddYeJyn2KE+RYccyybAl4/vJQd6+cSYHfKrx5wKIL9f8yv1faRAc6lmqmDQftvgcXKA63rJH3lmEMuRW3P+TzmccPIBbmtzUR4OYgCeUVCRi23fTNR5yBmvvZl6IgcGAUeRaa2+/lIasZsd4LXBz99/1Jx0vc4s5xeTqteTOc09DTLfcQdLZRB4HW/6y57wc/YVCmDSTiEqHUUn80xyq4FChZoTcB04PVcpt08Zl5nfti0VsOPEUqZzS72zLr5D+DcFZ/19l0gOy6QsgD1Mt61LE8F2fs3HBib232SUSSfb8yPDfvVTecG4wOPxMs/uu5mKBXRkIWEtHSZtAN6APuh1/jPJF97JgjFZ0SusHJEALedrAmysta1W+VHpARyDnsF+Mt0IPT7kVY+z5Zq8veitIvyM32i8aKCstOGCH3KQ3/aPxR2Xlx/b7Y2McMH8gN37eqzZXktalU4uNat9JV/T8T34wO/6vygQKz1brjcezCfLR29nKofrRmPBMIwzW9XzL8WMCtVPo07vAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage3 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACnUlEQVR42s1XXUsUURgeEGcVkaC7qCCw7roo+gsR/YGoi6AlKGp3FYqgpCiQIuoiqKtWZ2Z3E7QWRLoIyyvN1I2k8kbaPnYJCmpmA1tdddett/NM7Tijzvemc+CFYfad53n2/TrncJzTdVJsbYj1HG2MSvcbY9JkKCopfFQqw/CMd/gNPvDl6rYiwm5GIjIrMSOHBl8R33onDl9r4iPCLQZUcUG82irAAJY78tPxNj4qTvsgXmXiNDAdhrx7H8vlt/qR/zVgAtv2n/8PcoMI00gcudOsD3solqBtF/toZ2e/LwMGsAzpYFxr+Pmzids1p0N3hyhfmKN6LWABUxPBuNZrNbXad1zqp/nyMmVyMp3qHaNwatTU0q/zJE28t/QBBrCACexadxhaNBRJCDV1J5Kjquq2K2nLfG4930uzC2UqMeCWjqSlL7CwgK2lmHFqE04/ZCJ946rzlnMPLEGnPhdUv1+/iYZnvlr6AgsL2PphpU7MhjPCMb2zEwH7bwySMrek5RjPe66m3QogcHNsjsfdCGhuT1Iq84HiY+8Mhne8odrtBYCbY72ZsRMQTj2n7SsFZGsotuPSiK0AcCMCspmAAzcfUyYvU3Gxoq9gRwJm2TeTue9quiwiIHP/ttQ1Aq4PvSVlfiXPbgVo9cEwup68WVcAuE0FCONZ+lEq+xZQYAK6WY2YCrBKweF7T+mTUqSFStW1gBL75qNcpINsAlqmwEkRdg6+ci3gwsBLx0Vo24ZovZaOlKENe15kDaZvQ/g2tSectaGXQbS3a8AwiJDnXZcfehtE3kexovpV2Sh+NvPF+yj2uhkB9Cfr9cXlqiHc7jcjH9vxo6kciRNZ/9vx5h9IgnAk2/xDaSCO5YG4mATiahaIy+kGXc//AAHD1ggFumpLAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage4 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACFElEQVR42s1XWW7CMBDNZ/vRU7QH6G3KldoTFImehR82sQcUJEBiEQhQg0BIRCJsrl+EI0Nqx4a0ZKSRkjDz5uFZbBuGomQymadisfhWqVQ+y+VynqpN1T0pnvP4DTawNaKSXC73QoG/aACHKlFUBz7wvTpwOp1+oCDvFGyrEfhSt8AAllbwbDb7TJ3NGwJfqglMpeA0h6/UYRZhcKYzYKv8878I7pMQrgQtmEd+2WnuSL1eJ6Zp3qTAABafDsQKECiVSh/MqNPpENd1SVQCLGAyfMQKtBqrdrA+HA5kvV6TwWBA+v2+UJfLJZnP51IbYAALmMBm3XHWopRRirGDE6TRaEjzWavVyH6/94Cr1arUFlgQYHOrkPInHD9khsOhZxwG6jiOv8Sr1UpqCywIsPlh5U1MyiTBG6sQaLVaZLfb+QSwEs1mU5cAViEBAkkdAviOvNu2fab4dlHtKgSSIFAII4DccQUUqrDt9XoqBAoGffgWEcBS/1LBSgRYJwFDRACxjdN2GiAwnU693DLRJcAEtTKZTEQEXCEB5DQKAsBAjcgICFPQ7XbJZrMhx+NRmwB84Ntut+UpUCnC8XisTWA0GqkVoUobor3Yu0obwoZvybA21B5ElmUFBpFsdEsH0d1H8bWbEUDR6yg20QRU2oxu2Y4Xi0U02/HdDySxOJLd/VAai2N5LC4msbiaxeJy+l/X8x+iu3rSC4UgewAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage5 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACm0lEQVR42s1Xz08TQRTeox68GS+yCwRj4slfiTGevBj/hBL/BRO5cOBmvJjUQzXCFoigxh9FPYncxCgmBA2y3V0toLGJtEatYIvUFFikPN9bbZmV7nR2t8pO8pLN7pvvfTvvvW9mJElw7I6O75K7tYiiJvvkbuOF0qMvoFl/bMF+Z3/TIuQrNWo0xbR9spoclFW9pKg6iBj50hya6z/y+Wc7ECyKtiYauIbR3ChheYq95+pUG/6FGSDw3ytiEqZQ8L2XJw/hpFyjgjOWI+y6f/6PgldJuK5EU2xiJ7vszXEDDg6+gaM3UoGMMAiLTQfF2kJAiWuXKk7tw2nIFi1o1CAswqyuBMba0mqVaj+CrEtrZdByJeh8moWO0YyrjbxfhKGZAteHMAiLMAm70h2OFpVVY6DC7hxOonH81jQ3nweuvYYlqwzLPzegrc/g+hIWDcLeTIUxsKlwjMh0jX20nff3m1xQc37Z9iujPc8Wub6ERYOwWbGyFVPuSbazziIETt17C/mV9WqO6fkEZ8VqEbBJYGwJdbzfC4HWXhMezObhTuqbw+idEjc8EaDYmH/9ZT0CHU8ycPh6SrjfqdjOPp6rvwIYW2pW9Xk3Aqfvv4MkVvAPZwULEShigU7hXEqXGwGKLdnbaQ0CV17lHHn2SoCtj9jkF7cUWK4EEjN5WFwNTqCAGLexRlwJ8FJw5lEa5pYsWFnf8EyA9OHDdwsiD9P8FIgU4cWJz54JXBj/JFaEIm3Y2mug2pmONrw77TS2Dcm3hWlJfhv6EKKTiVlHgRbw+djNlD8h8ivFxtffUozlAWOZAFLsdzMiUNKHVWTQEg+wGQXZjodxO05g/gNvx9t+IAnFkWzbD6WhOJaH4mISiqtZOC6n/+l6/gtidBAXlLHDWgAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage6 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACcklEQVR42s1XzW7TQBD2EQ48BTwAj8ATIC5R3KhCXAoIwQUJcQdxoMeKQ1HLBYkLiAPnKpQ0aWhBlAZ6KK2gUpy4blIIJEqauImH+Uydboi8WceGeqSRos3sN593/nY1TVEuvto8k8iUk8msOatnym/1bKmqZ8udP8q/eQ3/wQa2WlSSWDTO6RnjCTtqspKiNrEHe8d2fGFx51RyyXioZ007gOO/1LSBAaxAzi+li2cZoDC+4yEtAFPxyEvneYMVoXNPLWCrfPm/cN4n4XsSiefGafHYJ3ImTa3s0vVVK5QCA1hiOOBriMBEzpj2jO5/3qdKu0tRCbCAefxxxvRwqR1l+zVm3e45tFW3aXarRo+++Gu+ekCvrZbUBhjAAiawveoYKFFenPfYYRPk5jtLGs8r+V1qdh3qMPDksim1BRYE2ML6fL/DiU1mbvuna3w5Lwf92rBdO4e1UGtLbYEFAbbYrNyOmVwq6qKxCoE7a1WqHzr9GNcPe3Tr/V5QAgTfmp4pPQ5CIMXH/WavRQvWoGJNz5mBCMA34r8yigBid3XVUq53JNvMZm00AfYNAhU/Anc/VmmbM7g1kMFqBJCgyH6ES0Kgoh2N1CECL43GQJyDEjjOD4deFBt+BDq+BNIc10YEBICxYDWlBHxD8GDjO1kHXbKd4ATaPXL33vu0PzIEI5Pw2U49MIGn334pJqFCGaa4vLxu55Vhmo9VVLEMYZsSSlJahuM0otsfKm7zERPthqR1SxtR2FYMGus/QrTicYcRQFtc60jQVC7EMAozjpd5HKejGMcnfiGJxZXsxC+lsbiWx+JhEounWSwep//ref4b7aRo6AFM0FUAAAAASUVORK5CYII=";
  private static com.google.gwt.resources.client.ImageResource fireworksClicked;
  private static com.google.gwt.resources.client.ImageResource fireworksDisabled;
  private static com.google.gwt.resources.client.ImageResource fireworksHovered;
  private static com.google.gwt.resources.client.ImageResource fireworksNormal;
  private static com.google.gwt.resources.client.ImageResource fitallClicked;
  private static com.google.gwt.resources.client.ImageResource fitallDisabled;
  private static com.google.gwt.resources.client.ImageResource fitallHovered;
  private static com.google.gwt.resources.client.ImageResource fitallNormal;
  private static org.reactome.web.diagram.controls.navigation.MainControlPanel.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      fireworksClicked(), 
      fireworksDisabled(), 
      fireworksHovered(), 
      fireworksNormal(), 
      fitallClicked(), 
      fitallDisabled(), 
      fitallHovered(), 
      fitallNormal(), 
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("fireworksClicked", fireworksClicked());
        resourceMap.put("fireworksDisabled", fireworksDisabled());
        resourceMap.put("fireworksHovered", fireworksHovered());
        resourceMap.put("fireworksNormal", fireworksNormal());
        resourceMap.put("fitallClicked", fitallClicked());
        resourceMap.put("fitallDisabled", fitallDisabled());
        resourceMap.put("fitallHovered", fitallHovered());
        resourceMap.put("fitallNormal", fitallNormal());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'fireworksClicked': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::fireworksClicked()();
      case 'fireworksDisabled': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::fireworksDisabled()();
      case 'fireworksHovered': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::fireworksHovered()();
      case 'fireworksNormal': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::fireworksNormal()();
      case 'fitallClicked': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::fitallClicked()();
      case 'fitallDisabled': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::fitallDisabled()();
      case 'fitallHovered': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::fitallHovered()();
      case 'fitallNormal': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::fitallNormal()();
      case 'getCSS': return this.@org.reactome.web.diagram.controls.navigation.MainControlPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
