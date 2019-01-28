package org.reactome.web.fireworks.controls.navigation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class MainControlPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources {
  private static MainControlPanel_Resources_default_InlineClientBundleGenerator _instance0 = new MainControlPanel_Resources_default_InlineClientBundleGenerator();
  private void diagramClickedInitializer() {
    diagramClicked = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "diagramClicked",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 32, 32, false, false
    );
  }
  private static class diagramClickedInitializer {
    static {
      _instance0.diagramClickedInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return diagramClicked;
    }
  }
  public com.google.gwt.resources.client.ImageResource diagramClicked() {
    return diagramClickedInitializer.get();
  }
  private void diagramDisabledInitializer() {
    diagramDisabled = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "diagramDisabled",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 32, 32, false, false
    );
  }
  private static class diagramDisabledInitializer {
    static {
      _instance0.diagramDisabledInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return diagramDisabled;
    }
  }
  public com.google.gwt.resources.client.ImageResource diagramDisabled() {
    return diagramDisabledInitializer.get();
  }
  private void diagramHoveredInitializer() {
    diagramHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "diagramHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
      0, 0, 32, 32, false, false
    );
  }
  private static class diagramHoveredInitializer {
    static {
      _instance0.diagramHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return diagramHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource diagramHovered() {
    return diagramHoveredInitializer.get();
  }
  private void diagramNormalInitializer() {
    diagramNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "diagramNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
      0, 0, 32, 32, false, false
    );
  }
  private static class diagramNormalInitializer {
    static {
      _instance0.diagramNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return diagramNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource diagramNormal() {
    return diagramNormalInitializer.get();
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
    getCSS = new org.reactome.web.fireworks.controls.navigation.MainControlPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-mainControlPanel {\n  float : " + ("right")  + ";\n  margin-right : " + ("10px")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-fitall {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getTop() + "px  no-repeat")  + ";\n  background-color : " + ("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : ") + (("32px")  + ";\n  float : " + ("right")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  position : " + ("relative")  + ";\n  outline : " + ("none")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-fitall:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getWidth() + "px") ) + (";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-fitall:active:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getTop() + "px  no-repeat")  + ";\n  background-color : ") + (("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : " + ("32px")  + ";\n  margin-right : " + ("10px")  + ";\n  float : " + ("right")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  position : " + ("relative") ) + (";\n  outline : " + ("none")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:disabled, .org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:disabled:hover, .org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:disabled:active {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : ") + (("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getTop() + "px  no-repeat")  + ";\n  cursor : " + ("auto")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:enabled:active:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getTop() + "px  no-repeat")  + ";\n}\n")) : ((".org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-mainControlPanel {\n  float : " + ("left")  + ";\n  margin-left : " + ("10px")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-fitall {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallNormal()).getTop() + "px  no-repeat")  + ";\n  background-color : " + ("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : ") + (("32px")  + ";\n  float : " + ("left")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  position : " + ("relative")  + ";\n  outline : " + ("none")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-fitall:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getWidth() + "px") ) + (";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-fitall:active:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.fitallClicked()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramNormal()).getTop() + "px  no-repeat")  + ";\n  background-color : ") + (("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : " + ("32px")  + ";\n  margin-left : " + ("10px")  + ";\n  float : " + ("left")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  position : " + ("relative") ) + (";\n  outline : " + ("none")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:disabled, .org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:disabled:hover, .org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:disabled:active {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : ") + (("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramDisabled()).getTop() + "px  no-repeat")  + ";\n  cursor : " + ("auto")  + ";\n}\n.org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram:enabled:active:hover {\n  height : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getHeight() + "px")  + ";\n  width : " + ((MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getSafeUri().asString() + "\") -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getLeft() + "px -" + (MainControlPanel_Resources_default_InlineClientBundleGenerator.this.diagramClicked()).getTop() + "px  no-repeat")  + ";\n}\n"));
      }
      public java.lang.String diagram() {
        return "org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-diagram";
      }
      public java.lang.String fitall() {
        return "org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-fitall";
      }
      public java.lang.String mainControlPanel() {
        return "org-reactome-web-fireworks-controls-navigation-MainControlPanel-ResourceCSS-mainControlPanel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.controls.navigation.MainControlPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.controls.navigation.MainControlPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAD6klEQVR42s1Xa0hTYRgeiLfCqOhCEPQjI4Oiy4+I+tGvIOpHRZn9KIgKUadSUSRmlkZERpFl5XSb5T3NvCGWbiG00sxLmpmaqZWTTS3nZZvb1Lf3/dLl3I47Z0b0wQPbOed7n/d7759IxHedkPm4hSYHuIvlie6h8kpPsbzPQyw3Eeg3PaN39A19K/prK0TqiyQyhB4BPEHfymiv68THL3t5hEhvoCCzAOKZMJMMkiWMPFCy2kMsa3BG4BmaAsvOZ8DCM6lOFJE1kEyeJk/ahL7UOCNPf9sOprFx6B8ZhQGDCfQmC8wPf8T5Pckk2U5Pzoe8rLkbHC1SZrZ9TAlOS/jf9uZjdsKSc+nsxL3DRjCYx9ACZlDr9LDlWgGPuEB3IJcdv0dwSpzQINt49RlUdmjhoEQJqyKz+e9FLkep5lK057/vgrWXcwVnh02KeoakSPckvIBTaa/ssCE2D5aiybmE5dV3wrorTwUrTpzWCkeFo6jxK3AtUs6P45SfNDqIKqxxpUboWcV0C5IeoQfFjd8ckk8gGrp/sFTbFV9qIyQ48zU0qn/CqGUMfC/lOCSi1JznAPSOuEVYxyWzKTBqGQevsBQ4nKyEQaMZ/JOUVuErI7IgoaIZjsorwDvMtgYcSFTA6qgcVqRoUbZMhw4PJM5SlYowN6toAwlIVrXa4e7Lj1ahu+89Zxsj8t/B/oflkF3TAZbxCWalAOlLRhqYoWLvaGmGjHAS4+in3mR3MErjY7KKVrJArxDfXS2ph55BA7zt7IUezP0pN9V87YP67z9Ydaz71g8T+NCIJ63u6uNU4JBEMSiabKm8yBecToXvAyOY9wr2PwhjIKP6C8jftMJy7An07I6yicWF4pMaNmNh2nGzmFVIIpyO4VEL7HtQbhGkwEWM9lefNU4bVG5tJyhbesAP03MqhSkYZ4K4bVxAvo1FE09HZMHvFFtxIZP5f3tcsVNFfU4/BlW7hrklBmVwKovc1iDcFlcETT0Ddr5S6wxMifsY7XQyvtZStKhZPJC5qZhxNKcqaxrGY7TPtswozE9AyV0TnQOy120whKkb/qQSdt4qYZhhAYm1EJ3JrQI9Rq2jpfqihcVn03iTr8cTx5TUseinTjkFLaZldHGtdYBhhWiqFFNQfMDobe8dgjbtILRqddCCZbYWU2qRAHLC1uuF0IeR72hRMZusmnrr8EqNYQ5znx3I5xMcrqRAJgv8aUZzbMeOQCfcm1DGSq4R+wSVcwpIWtQ/7NqxqwOJM1DToT5CoPpAoOC0H0gEjmRzA8dIJmQodRWzD6UCx3KXyJ2O5QIvJoLMzvti8l9czf6Ly+k/up7/AtmC9bywLDJ4AAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAC30lEQVR42s2XWW4aQRCGeUwecorkALlNfKXkBLGUnCOPfsE4MosZY7PZINssYjfbGMLiTn8tumUPzEwPRJFL+gUM3VV/19Y1sZilxOPxD+fn51/S6fT3VCp1JtGV+LMB38/4jzWsjf0rSSQSn6Tin9KAKyEs4bKHvXsbPjk5eSeVfJXKFhEMe7FAB7oiGT89Pf0oNzthBqRykc1mxcXFRRgRB51WxmUMP8sNrTDj/X5fPD8/i9VqpbBer0Umkwna00K3zclDjY9GI7FLIBKyt+XrCZkw723cDnA7J14ul8oLfF8sFiKfz9vkhYOtLQLJZPJb1CS7vr4W0+lUVCoVcXl5ab0PW1ultm+2Pz4+ilwuF7k6XpWoZPSjXC6Lu7u7LVxdXSmXBxFgTVTi2DQdjsYxHA6Fn9zc3PiecjabiUajsU+PcFXHlEyOeBBE4OnpSWV4qVR6peT+/l79RyL6EaQ06RdebLxwBIHjIAIoZwOJBgk+tXLHcUSn0xHVatUo1bi9vVWJSZPSel4CXTLEvyDwW3e1bre7hXa7bZSSJ2ys1+vKwGAwMEQhATnyhv8QypTf7PEKpSv3lGJScSdK7JrNplJM+VH7WlzXNeHgU5+a534EJOFRbHOdWhknnhjlhDoHaMm9Xs9UCh6DwHg8Vo2pUCgowhj0QupZRiKA6yeTSegFRWgg8LKEIe8Ftl+FAAO4+CV4ptsvruRENp6CKO5HR8DajklCFFPTXsF91DnZzslsvYUHdKz9GhW2TRkSuyAJqvVdYC1VBIGHhwdRLBYVPASOTSOq1Wpq8S4h4y2GDgNOjOv1rKChval1qUakWzFxIwTz+dyA38QxinFA9mNsl3DIjSddM7xyMRww9+30gJ/gCQ5kLqNDr2O/+HOBedsvQu/Yuo73HUhshlYvILc1kEQdyQ7E7pEsylB6AFqh47ntWL6P8dCxPOqLSRS3W7+YvIlXszfxcvq/Xs//AlSRhNy/HJk7AAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAD2UlEQVR42s1XSWgTYRTOUQ/exIsmdV9wRRBUrCiiIKgHLxWXg6Ii6EkRFYSeilaxgm20ta2K2lpcilU0UtFqK91slkn3mi5WjdOkbZK22Zrl+d7fZmycTGcmFfGHj8BM/ve9/y3f/0ajUbhmZn6aoc02pun0plxttqVGl2N2IoLjcLJn7J0xjf6r+VtrTpZxoVZvKtTqzV6d3gxKQP+lPbQ3eeb0imloLBMxqpQ4AWhvJtlSxT3rRsMCPAUnR5By0wIrCxphyW1OLiIc2VREPvt6/RrcxMuRP2sfhNFIFAb9YfAEw+ALRWBBrmWyPTzZlj25EvKPvUOQaJEzMnt5yUjMyaqeriTshOUYdl8oCv2+EPjDUXAHI8B7Q7CjpF1JgXLEJXJAd9N4RW2RbXvUCg28F44aumHdvSble5FL1GrJVruh0w2bHrSo7o64FtXqLQUHX3TCmXdfRdha3AorCqySxl7Z3LD5oWoHgDh/KxwKR3m3B6TWAXQuVYLky2AAMmvtSThg9jLF1OaY9tGDtxIORBHN/X5wB8KQ9twWZ+RcRS+04rsAFuKG+80Jiag1598SgzmB3BrU8bzJHAii8bkoOMex0Iaw2o/hb8z42rtNcMfqhJPlPTDvVrwGHHnVBevRKRIpWn7smonwoK0LlT8MmH9zLW0gA0XN/SIUcg7B6P4yGxOdjOofcBgJyr64IBQdc/TEmx448robzr7vZe9oObFNT2MduTB6fy5q41PlPe2aFL3ZoSZ31z/z0Ic9b8L240dCQposfT5ownSQOjY6fewZpcbU55V04Jihy6Nh16lC8sV5HNiHR+Ho666xGvjwDUpRkktaBmBVYSN7lm9xsLqo/DYM20vaYM/TDqaQRDgRI6MRjFRnSJUDl2vsUGcfkb2gXtpcUIUOpBa1CC1MxfgniDsuBRnVdsiq/xmHSzVjLbYaT0j5340nknN0US7HHOUcPrTBSzuL3EIR7nrSAW0DflGueG+YnfwuVjudTGm0KAUYZRZuEjMJLagV2jCfc8Jki4orVYXibcQWLMbaGEYvLlZ+h72lHQxx/0NuQYjSq74zbxOtegznsnyrYvItRa1wDUNP4kX9HgO15dW6n8IAw4QoJsVUFJSCbncQutwBsLkCTGatmMelt62qZHbn43YY8IcTHobEjFRTkOLYZTSFuU8EynlUIpUUCYqAcBlN9TpOBDrhoZedTHJJjEjOqYZonUftEF3HyQ4kcqBLh+4RQso4qDhFA4nakWwqkBzJ1AylUwAvO54rHcuTIZcdy9V+mKgJu+IPk//i0+z/+Dj9R5/nvwAl3yiSVxMyQgAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAADlElEQVR42s1XS08TURTuUhf+Cv0B/gR/gXFDWvAZ1BgTfEdj1MSFUQOiJpqomGrcIFHcuFDcICkFWxGUiryFlpaWoaXvF+20PZ5vmDa003ZmCjGc5Esmd+4959zzvgaDRjr4cWZPk8VrNFp9L0wWr81kXQ6YrN7MBvib1/APe7DXsF3UNODZZ7J4XrGgJIM0IokzONuw4AMDzl3GQU+7yerL6hBcAV8WPMBLl/BD/e69zMChJqB5yEcnbSt0/NuKmjIO8NRo8uX9fEBQEz7oT5FYIIqLeUrkCrSeL9CRYV+9MwJ4a7m5qnBHeJ2qUZw1Ujkr1LRE03vPbi1mB1rtAmX4xtFsnjJshUSOKJTJ07WfAS2x4YAshQLNQ54OvUF2ZcxPc7EsdU6F6Ox3QfM5yFKmWoPRPrKWpvM/VnVnR1mK8qL53p8gPZ8LK3B5bJVabbWj3M4KXBxdbSRNzaUKh8IxGqweWCAod6GGEG9KpB5XrBEFklLFNA66TVgYq6OAKyFSgiP8zsRaGZOX8xFaSoqU5UBsq+EGpObhISXwD7INJstyVz0FkOstfODRdIhSnO8POeCKzM9w4PX5EvRkNiTt2Sy4YzJIbSOCVKRAnCRlSDIv83ykD/634wAY9AtJBT57EyWmd9kCONjtjEoChgNpysuKPmYFH7ByXWwV/ANFOE2fzUWkQlVJSOOnM+FZKODX47sP7jiFmfE8p18ouyEe7BfiIrnYHbCYM5GV1vD7L6/XUoCtGTXILVWT8GPszzW2X+dUsBQDVi7JX9lSp+wbmfKJLYa4+B3O0FUuTLfGAxRjrSBwM9KsVPtkUNSlAKJ9OppRbVA2ds0EK4D0LKYwgrESkF3mgm5njHqXyvFWTrHTfEP4/+a4erk9ysyhKNzSyy6rs9dfCsIbzNjNpqukEOdYjytOXzjacTOt1oILEA8wN4pZjX32UhrCd/UIcXRBR8U7x3UBWZTiRvV6IUq3HQEJZftYdqkQvVmMSvlZjWaiWTqhPnSUcIkV7V2KS8ULbisC3fMdrxcHGKkQFUsxggIuWEnnyMfwpnJSmV2M6xMOXP/l58ivfhsUM7lqJkvDKxpD43OfEvB5oYYrYQnZAuZtacfVgBve5wYGl6JPAKKsEWqHoh03OpCoAU2nRUazDASnYiDRO5JtEdVHMj1D6RYgqI7nWsfyRoSrjuV6HyZ6zK75YbIjnmY74nH6v57n/wCMonlOj4UmzAAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage3 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACnUlEQVR42s1XXUsUURgeEGcVkaC7qCCw7roo+gsR/YGoi6AlKGp3FYqgpCiQIuoiqKtWZ2Z3E7QWRLoIyyvN1I2k8kbaPnYJCmpmA1tdddett/NM7Tijzvemc+CFYfad53n2/TrncJzTdVJsbYj1HG2MSvcbY9JkKCopfFQqw/CMd/gNPvDl6rYiwm5GIjIrMSOHBl8R33onDl9r4iPCLQZUcUG82irAAJY78tPxNj4qTvsgXmXiNDAdhrx7H8vlt/qR/zVgAtv2n/8PcoMI00gcudOsD3solqBtF/toZ2e/LwMGsAzpYFxr+Pmzids1p0N3hyhfmKN6LWABUxPBuNZrNbXad1zqp/nyMmVyMp3qHaNwatTU0q/zJE28t/QBBrCACexadxhaNBRJCDV1J5Kjquq2K2nLfG4930uzC2UqMeCWjqSlL7CwgK2lmHFqE04/ZCJ946rzlnMPLEGnPhdUv1+/iYZnvlr6AgsL2PphpU7MhjPCMb2zEwH7bwySMrek5RjPe66m3QogcHNsjsfdCGhuT1Iq84HiY+8Mhne8odrtBYCbY72ZsRMQTj2n7SsFZGsotuPSiK0AcCMCspmAAzcfUyYvU3Gxoq9gRwJm2TeTue9quiwiIHP/ttQ1Aq4PvSVlfiXPbgVo9cEwup68WVcAuE0FCONZ+lEq+xZQYAK6WY2YCrBKweF7T+mTUqSFStW1gBL75qNcpINsAlqmwEkRdg6+ci3gwsBLx0Vo24ZovZaOlKENe15kDaZvQ/g2tSectaGXQbS3a8AwiJDnXZcfehtE3kexovpV2Sh+NvPF+yj2uhkB9Cfr9cXlqiHc7jcjH9vxo6kciRNZ/9vx5h9IgnAk2/xDaSCO5YG4mATiahaIy+kGXc//AAHD1ggFumpLAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage4 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACFElEQVR42s1XWW7CMBDNZ/vRU7QH6G3KldoTFImehR82sQcUJEBiEQhQg0BIRCJsrl+EI0Nqx4a0ZKSRkjDz5uFZbBuGomQymadisfhWqVQ+y+VynqpN1T0pnvP4DTawNaKSXC73QoG/aACHKlFUBz7wvTpwOp1+oCDvFGyrEfhSt8AAllbwbDb7TJ3NGwJfqglMpeA0h6/UYRZhcKYzYKv8878I7pMQrgQtmEd+2WnuSL1eJ6Zp3qTAABafDsQKECiVSh/MqNPpENd1SVQCLGAyfMQKtBqrdrA+HA5kvV6TwWBA+v2+UJfLJZnP51IbYAALmMBm3XHWopRRirGDE6TRaEjzWavVyH6/94Cr1arUFlgQYHOrkPInHD9khsOhZxwG6jiOv8Sr1UpqCywIsPlh5U1MyiTBG6sQaLVaZLfb+QSwEs1mU5cAViEBAkkdAviOvNu2fab4dlHtKgSSIFAII4DccQUUqrDt9XoqBAoGffgWEcBS/1LBSgRYJwFDRACxjdN2GiAwnU693DLRJcAEtTKZTEQEXCEB5DQKAsBAjcgICFPQ7XbJZrMhx+NRmwB84Ntut+UpUCnC8XisTWA0GqkVoUobor3Yu0obwoZvybA21B5ElmUFBpFsdEsH0d1H8bWbEUDR6yg20QRU2oxu2Y4Xi0U02/HdDySxOJLd/VAai2N5LC4msbiaxeJy+l/X8x+iu3rSC4UgewAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage5 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACm0lEQVR42s1Xz08TQRTeox68GS+yCwRj4slfiTGevBj/hBL/BRO5cOBmvJjUQzXCFoigxh9FPYncxCgmBA2y3V0toLGJtEatYIvUFFikPN9bbZmV7nR2t8pO8pLN7pvvfTvvvW9mJElw7I6O75K7tYiiJvvkbuOF0qMvoFl/bMF+Z3/TIuQrNWo0xbR9spoclFW9pKg6iBj50hya6z/y+Wc7ECyKtiYauIbR3ChheYq95+pUG/6FGSDw3ytiEqZQ8L2XJw/hpFyjgjOWI+y6f/6PgldJuK5EU2xiJ7vszXEDDg6+gaM3UoGMMAiLTQfF2kJAiWuXKk7tw2nIFi1o1CAswqyuBMba0mqVaj+CrEtrZdByJeh8moWO0YyrjbxfhKGZAteHMAiLMAm70h2OFpVVY6DC7hxOonH81jQ3nweuvYYlqwzLPzegrc/g+hIWDcLeTIUxsKlwjMh0jX20nff3m1xQc37Z9iujPc8Wub6ERYOwWbGyFVPuSbazziIETt17C/mV9WqO6fkEZ8VqEbBJYGwJdbzfC4HWXhMezObhTuqbw+idEjc8EaDYmH/9ZT0CHU8ycPh6SrjfqdjOPp6rvwIYW2pW9Xk3Aqfvv4MkVvAPZwULEShigU7hXEqXGwGKLdnbaQ0CV17lHHn2SoCtj9jkF7cUWK4EEjN5WFwNTqCAGLexRlwJ8FJw5lEa5pYsWFnf8EyA9OHDdwsiD9P8FIgU4cWJz54JXBj/JFaEIm3Y2mug2pmONrw77TS2Dcm3hWlJfhv6EKKTiVlHgRbw+djNlD8h8ivFxtffUozlAWOZAFLsdzMiUNKHVWTQEg+wGQXZjodxO05g/gNvx9t+IAnFkWzbD6WhOJaH4mISiqtZOC6n/+l6/gtidBAXlLHDWgAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage6 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACcklEQVR42s1XzW7TQBD2EQ48BTwAj8ATIC5R3KhCXAoIwQUJcQdxoMeKQ1HLBYkLiAPnKpQ0aWhBlAZ6KK2gUpy4blIIJEqauImH+Uydboi8WceGeqSRos3sN593/nY1TVEuvto8k8iUk8msOatnym/1bKmqZ8udP8q/eQ3/wQa2WlSSWDTO6RnjCTtqspKiNrEHe8d2fGFx51RyyXioZ007gOO/1LSBAaxAzi+li2cZoDC+4yEtAFPxyEvneYMVoXNPLWCrfPm/cN4n4XsSiefGafHYJ3ImTa3s0vVVK5QCA1hiOOBriMBEzpj2jO5/3qdKu0tRCbCAefxxxvRwqR1l+zVm3e45tFW3aXarRo+++Gu+ekCvrZbUBhjAAiawveoYKFFenPfYYRPk5jtLGs8r+V1qdh3qMPDksim1BRYE2ML6fL/DiU1mbvuna3w5Lwf92rBdO4e1UGtLbYEFAbbYrNyOmVwq6qKxCoE7a1WqHzr9GNcPe3Tr/V5QAgTfmp4pPQ5CIMXH/WavRQvWoGJNz5mBCMA34r8yigBid3XVUq53JNvMZm00AfYNAhU/Anc/VmmbM7g1kMFqBJCgyH6ES0Kgoh2N1CECL43GQJyDEjjOD4deFBt+BDq+BNIc10YEBICxYDWlBHxD8GDjO1kHXbKd4ATaPXL33vu0PzIEI5Pw2U49MIGn334pJqFCGaa4vLxu55Vhmo9VVLEMYZsSSlJahuM0otsfKm7zERPthqR1SxtR2FYMGus/QrTicYcRQFtc60jQVC7EMAozjpd5HKejGMcnfiGJxZXsxC+lsbiWx+JhEounWSwep//ref4b7aRo6AFM0FUAAAAASUVORK5CYII=";
  private static com.google.gwt.resources.client.ImageResource diagramClicked;
  private static com.google.gwt.resources.client.ImageResource diagramDisabled;
  private static com.google.gwt.resources.client.ImageResource diagramHovered;
  private static com.google.gwt.resources.client.ImageResource diagramNormal;
  private static com.google.gwt.resources.client.ImageResource fitallClicked;
  private static com.google.gwt.resources.client.ImageResource fitallDisabled;
  private static com.google.gwt.resources.client.ImageResource fitallHovered;
  private static com.google.gwt.resources.client.ImageResource fitallNormal;
  private static org.reactome.web.fireworks.controls.navigation.MainControlPanel.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      diagramClicked(), 
      diagramDisabled(), 
      diagramHovered(), 
      diagramNormal(), 
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
        resourceMap.put("diagramClicked", diagramClicked());
        resourceMap.put("diagramDisabled", diagramDisabled());
        resourceMap.put("diagramHovered", diagramHovered());
        resourceMap.put("diagramNormal", diagramNormal());
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
      case 'diagramClicked': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::diagramClicked()();
      case 'diagramDisabled': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::diagramDisabled()();
      case 'diagramHovered': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::diagramHovered()();
      case 'diagramNormal': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::diagramNormal()();
      case 'fitallClicked': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::fitallClicked()();
      case 'fitallDisabled': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::fitallDisabled()();
      case 'fitallHovered': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::fitallHovered()();
      case 'fitallNormal': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::fitallNormal()();
      case 'getCSS': return this.@org.reactome.web.fireworks.controls.navigation.MainControlPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
