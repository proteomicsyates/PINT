package org.reactome.web.diagram.controls.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class HideableContainerPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.settings.HideableContainerPanel.Resources {
  private static HideableContainerPanel_Resources_default_InlineClientBundleGenerator _instance0 = new HideableContainerPanel_Resources_default_InlineClientBundleGenerator();
  private void aboutTabIconInitializer() {
    aboutTabIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "aboutTabIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 5, 17, false, false
    );
  }
  private static class aboutTabIconInitializer {
    static {
      _instance0.aboutTabIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return aboutTabIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource aboutTabIcon() {
    return aboutTabIconInitializer.get();
  }
  private void interactorsTabIconInitializer() {
    interactorsTabIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "interactorsTabIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 16, 15, false, false
    );
  }
  private static class interactorsTabIconInitializer {
    static {
      _instance0.interactorsTabIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return interactorsTabIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource interactorsTabIcon() {
    return interactorsTabIconInitializer.get();
  }
  private void profilesTabIconInitializer() {
    profilesTabIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "profilesTabIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
      0, 0, 16, 16, false, false
    );
  }
  private static class profilesTabIconInitializer {
    static {
      _instance0.profilesTabIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return profilesTabIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource profilesTabIcon() {
    return profilesTabIconInitializer.get();
  }
  private void showHideHoveredIconInitializer() {
    showHideHoveredIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "showHideHoveredIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
      0, 0, 16, 13, false, false
    );
  }
  private static class showHideHoveredIconInitializer {
    static {
      _instance0.showHideHoveredIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return showHideHoveredIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource showHideHoveredIcon() {
    return showHideHoveredIconInitializer.get();
  }
  private void showHideIconInitializer() {
    showHideIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "showHideIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage3),
      0, 0, 16, 13, false, false
    );
  }
  private static class showHideIconInitializer {
    static {
      _instance0.showHideIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return showHideIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource showHideIcon() {
    return showHideIconInitializer.get();
  }
  private void aboutThisInitializer() {
    aboutThis = new com.google.gwt.resources.client.TextResource() {
      // jar:file:/C:/Users/salvador/.m2/repository/org/reactome/web/diagram/3.2.1/diagram-3.2.1.jar!/org/reactome/web/diagram/controls/settings/tabs/aboutDiagram.html
      public String getText() {
        return "<div style=\"text-align: justify; max-width: 400px\">\n    <!--<dl>-->\n        <!--<dt><b>What is this?</b></dt>-->\n            <!--<dd>A genome-wide, hierarchical visualisation of Reactome pathways in a space-filling graph.</dd>-->\n        <!--<dt><b>What is the aim?</b></dt>-->\n            <!--<dd>Visualisation of genome-scale pathway analysis results, allowing both a quick “first glance”-->\n                <!--and rapid zooming in on interesting results.</dd>-->\n    <!--</dl>-->\n    <p style=\"text-align: justify;\">\n        Pathway diagrams represent the steps of a pathway as a series of interconnected molecular events, known in\n        Reactome as 'reactions'. Reactions are the core unit of Reactome's data model. They encapsulate 'changes of\n        state' in biology, such as the familiar biochemical reaction where substrates are converted into products\n        by the action of a catalyst, but also include processes such as transport of molecules from one cellular\n        compartment to another, binding, dissociation, phosphorylation, degradation and more. Cellular compartments\n        are represented as pink boxes - a typical diagram has a box representing the cytosol, bounded by a double-line\n        that represents the plasma membrane. The white background outside this represents the extracellular space.\n    </p>\n    <p style=\"text-align: justify;\">\n        Other organelles are represented as additional labelled boxes within the cytosol. Molecules are placed in the\n        physiologically-correct cellular compartment, or lie on the boundary of a compartment to indicate that they\n        are in the corresponding membrane, e.g. a molecule on the boundary of the cytosol is in the plasma membrane.\n    </p>\n\n    More information and documentation:\n    <ul style=\"list-style-type:square\">\n        <li>\n            <a href=\"http://wiki.reactome.org/index.php/Usersguide#Pathway_Diagrams\" target=\"_blank\">Pathway Diagrams</a>\n        </li>\n        <!--<li>-->\n            <!--<a href=\"http://wiki.reactome.org/index.php/Usersguide#Fireworks_and_Analysis_Results\">Pathways overview analysis result overlay</a>-->\n        <!--</li>-->\n    </ul>\n</div>\n\n\n\n\n\n";
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
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.settings.HideableContainerPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapper {\n  width : " + ("300px")  + ";\n  height : " + ("300px")  + ";\n  border : " + ("2px"+ " " +"solid"+ " " +"#1e95cf")  + ";\n  background-color : " + ("#58c3e5")  + ";\n  background : " + ("-webkit-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-o-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-moz-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  opacity : " + ("0.95")  + ";\n  color : " + ("white")  + ";\n  position : ") + (("relative")  + ";\n  border-radius : " + ("15px"+ " " +"0"+ " " +"0"+ " " +"15px")  + ";\n  float : " + ("right")  + ";\n  overflow : " + ("visible")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  line-height : " + ("normal")  + ";\n  -webkit-transition : " + ("width"+ " " +"0.3s"+ " " +"ease")  + ";\n  -moz-transition : " + ("width"+ " " +"0.3s"+ " " +"ease")  + ";\n  -o-transition : " + ("width"+ " " +"0.3s"+ " " +"ease")  + ";\n  transition : " + ("width"+ " " +"0.3s"+ " " +"ease")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapperInitial, .org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapper:hover {\n  width : " + ("310px") ) + (";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapperExpanded {\n  width : " + ("590px")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-outerPanel {\n  float : " + ("right")  + ";\n  width : " + ("100%")  + ";\n  height : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonsPanel {\n  float : " + ("right")  + ";\n  width : " + ("24px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonsPanel button {\n  color : " + ("white")  + ";\n  border-radius : " + ("10px"+ " " +"0"+ " " +"0"+ " " +"10px")  + ";\n  background : " + ("none"+ " " +"#58c3e5")  + ";\n  font-size : " + ("medium")  + ";\n  padding : ") + (("0"+ " " +"2px"+ " " +"0"+ " " +"2px")  + " !important;\n  outline : " + ("none")  + ";\n  width : " + ("100%")  + ";\n  height : " + ("80px")  + ";\n  float : " + ("left")  + ";\n  -webkit-transition : " + ("width"+ " " +"0.2s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("width"+ " " +"0.2s"+ " " +"ease-in-out")  + ";\n  -o-transition : " + ("width"+ " " +"0.2s"+ " " +"ease-in-out")  + ";\n  transition : " + ("width"+ " " +"0.2s"+ " " +"ease-in-out")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonsPanel button:hover {\n  color : " + ("white")  + ";\n  background : " + ("none"+ " " +"#58c3e5") ) + (";\n  font-size : " + ("medium")  + ";\n  padding : " + ("0"+ " " +"2px"+ " " +"0"+ " " +"2px")  + " !important;\n  outline : " + ("none")  + ";\n  width : " + ("140%")  + ";\n  background : " + ("none"+ " " +"#1e94d0")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-unselectable {\n  -webkit-touch-callout : " + ("none")  + ";\n  -webkit-user-select : " + ("none")  + ";\n  -khtml-user-select : " + ("none")  + ";\n  -moz-user-select : " + ("none")  + ";\n  -ms-user-select : " + ("none")  + ";\n  user-select : ") + (("none")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-undraggable {\n  -webkit-user-drag : " + ("none")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonSelected {\n  background : " + ("none"+ " " +"#066b9e")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-container {\n  float : " + ("right")  + ";\n  width : " + ("270px")  + ";\n  height : " + ("276px")  + ";\n  overflow : " + ("hidden")  + ";\n  padding : " + ("3px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-showHide {\n  height : " + ((HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getHeight() + "px")  + ";\n  width : " + ((HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getWidth() + "px")  + ";\n  overflow : " + ("hidden") ) + (";\n  background : " + ("url(\"" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getSafeUri().asString() + "\") -" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getLeft() + "px -" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("right")  + ";\n  margin : " + ("2px"+ " " +"8px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-showHide:hover {\n  height : " + ((HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getHeight() + "px")  + ";\n  width : " + ((HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : ") + (("url(\"" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getSafeUri().asString() + "\") -" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getLeft() + "px -" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-showHideRight {\n  -ms-transform : " + ("rotate(" + "180deg" + ")")  + ";\n  -webkit-transform : " + ("rotate(" + "180deg" + ")")  + ";\n  transform : " + ("rotate(" + "180deg" + ")")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-headerLabel {\n  font-size : " + ("medium")  + " !important;\n}\n")) : ((".org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapper {\n  width : " + ("300px")  + ";\n  height : " + ("300px")  + ";\n  border : " + ("2px"+ " " +"solid"+ " " +"#1e95cf")  + ";\n  background-color : " + ("#58c3e5")  + ";\n  background : " + ("-webkit-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-o-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-moz-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  opacity : " + ("0.95")  + ";\n  color : " + ("white")  + ";\n  position : ") + (("relative")  + ";\n  border-radius : " + ("15px"+ " " +"0"+ " " +"0"+ " " +"15px")  + ";\n  float : " + ("left")  + ";\n  overflow : " + ("visible")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  line-height : " + ("normal")  + ";\n  -webkit-transition : " + ("width"+ " " +"0.3s"+ " " +"ease")  + ";\n  -moz-transition : " + ("width"+ " " +"0.3s"+ " " +"ease")  + ";\n  -o-transition : " + ("width"+ " " +"0.3s"+ " " +"ease")  + ";\n  transition : " + ("width"+ " " +"0.3s"+ " " +"ease")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapperInitial, .org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapper:hover {\n  width : " + ("310px") ) + (";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapperExpanded {\n  width : " + ("590px")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-outerPanel {\n  float : " + ("left")  + ";\n  width : " + ("100%")  + ";\n  height : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonsPanel {\n  float : " + ("left")  + ";\n  width : " + ("24px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonsPanel button {\n  color : " + ("white")  + ";\n  border-radius : " + ("10px"+ " " +"0"+ " " +"0"+ " " +"10px")  + ";\n  background : " + ("none"+ " " +"#58c3e5")  + ";\n  font-size : " + ("medium")  + ";\n  padding : ") + (("0"+ " " +"2px"+ " " +"0"+ " " +"2px")  + " !important;\n  outline : " + ("none")  + ";\n  width : " + ("100%")  + ";\n  height : " + ("80px")  + ";\n  float : " + ("right")  + ";\n  -webkit-transition : " + ("width"+ " " +"0.2s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("width"+ " " +"0.2s"+ " " +"ease-in-out")  + ";\n  -o-transition : " + ("width"+ " " +"0.2s"+ " " +"ease-in-out")  + ";\n  transition : " + ("width"+ " " +"0.2s"+ " " +"ease-in-out")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonsPanel button:hover {\n  color : " + ("white")  + ";\n  background : " + ("none"+ " " +"#58c3e5") ) + (";\n  font-size : " + ("medium")  + ";\n  padding : " + ("0"+ " " +"2px"+ " " +"0"+ " " +"2px")  + " !important;\n  outline : " + ("none")  + ";\n  width : " + ("140%")  + ";\n  background : " + ("none"+ " " +"#1e94d0")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-unselectable {\n  -webkit-touch-callout : " + ("none")  + ";\n  -webkit-user-select : " + ("none")  + ";\n  -khtml-user-select : " + ("none")  + ";\n  -moz-user-select : " + ("none")  + ";\n  -ms-user-select : " + ("none")  + ";\n  user-select : ") + (("none")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-undraggable {\n  -webkit-user-drag : " + ("none")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonSelected {\n  background : " + ("none"+ " " +"#066b9e")  + " !important;\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-container {\n  float : " + ("left")  + ";\n  width : " + ("270px")  + ";\n  height : " + ("276px")  + ";\n  overflow : " + ("hidden")  + ";\n  padding : " + ("3px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-showHide {\n  height : " + ((HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getHeight() + "px")  + ";\n  width : " + ((HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getWidth() + "px")  + ";\n  overflow : " + ("hidden") ) + (";\n  background : " + ("url(\"" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getSafeUri().asString() + "\") -" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getLeft() + "px -" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideIcon()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("left")  + ";\n  margin : " + ("2px"+ " " +"8px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-showHide:hover {\n  height : " + ((HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getHeight() + "px")  + ";\n  width : " + ((HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : ") + (("url(\"" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getSafeUri().asString() + "\") -" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getLeft() + "px -" + (HideableContainerPanel_Resources_default_InlineClientBundleGenerator.this.showHideHoveredIcon()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-showHideRight {\n  -ms-transform : " + ("rotate(" + "180deg" + ")")  + ";\n  -webkit-transform : " + ("rotate(" + "180deg" + ")")  + ";\n  transform : " + ("rotate(" + "180deg" + ")")  + ";\n}\n.org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-headerLabel {\n  font-size : " + ("medium")  + " !important;\n}\n"));
      }
      public java.lang.String buttonSelected() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonSelected";
      }
      public java.lang.String buttonsPanel() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-buttonsPanel";
      }
      public java.lang.String container() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-container";
      }
      public java.lang.String headerLabel() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-headerLabel";
      }
      public java.lang.String outerPanel() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-outerPanel";
      }
      public java.lang.String showHide() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-showHide";
      }
      public java.lang.String showHideRight() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-showHideRight";
      }
      public java.lang.String undraggable() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-undraggable";
      }
      public java.lang.String unselectable() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-unselectable";
      }
      public java.lang.String wrapper() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapper";
      }
      public java.lang.String wrapperExpanded() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapperExpanded";
      }
      public java.lang.String wrapperInitial() {
        return "org-reactome-web-diagram-controls-settings-HideableContainerPanel-ResourceCSS-wrapperInitial";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.settings.HideableContainerPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.settings.HideableContainerPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAARCAYAAAAVKGZoAAAAdUlEQVR42mNgAIK/f/9O+AcBZ0F8BiDjJBD//Q8EIFGggi6G/2gAKH4HpLUNyPgFFfgNxMdgRmwD4k9ABWUMyODbt2+yQIoRLgBUdRGIX4K0wwQuoFl0Cpvt/0Aq/w2IIAiA/N4NkwAFEijUYB44Cw22iSA+AHdXHFaETI1jAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAPCAYAAADtc08vAAABTElEQVR42pWTPUvEQBCG9yy0sVA7Pwrt9A/YWNqIFioWwjWCnWJhLULAxlbs9T+cpWJrLRgsDJxoIwTPJKdJyLfPwAoxeJIMvOzszrzvzM4mStWwPM8fQe667rhqYo7jjMF7LrThd5MkWaotYBjGUFEyBN4RWKlF9n1/EkIHfIG+rGmabg4kENwB26ZpDrO2IfTAbRiGs1EULfxLJvGNhN0sy/bw78En/j6h1h/pLeJ3xM9+yB2pwppyeCxDwr+m6nwVcn9iL3oekn+hZINzwEECbDpZZ/0oaph0Kh3cMLApLXQoVVjPbdserSIIghm5rib3yTtRlmWNsHlgcyT3wn/VnbQHzYy8S3D661Aqx3G8KG8vnejnu6LqNLFlBDcafYUMdk6GCTz9pA4iW41EpBuIWWloGSJrTf+FbvlTRmBVNTWIT1Ld87yJauwbSdG142jBC9kAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABtElEQVR42p1TPS8EYRDei4+Ggkj8AqJUUuIUNGjkdIJCpRGVTiE5jRPxkYhCoVpBrnCJhGwjIXGFjzvuEh1isxF7uQ3Wfp1nNvPKZi0Jk0zemdl5nnnnnVlJ+kE8z0ubptkm/VVKpVKj4ziDIHBc113AeQU9gJ0EYcuvYAAHkLgGwDX0FnppWVaHLMtVsPeg+Oyua5pW/w1s23Y3EnZAMlyBIHEb/puqqnX0Hf4cxRHToRflcrnpC8wVCkjaoo9MkGLAGexVnK84l5EeQ5EE/EOyRfUuBiUrLLA3KiEBcIzy8Rat7A9JfL1pvto+V72jCmECxJ6Ru4Qzz35aPF6Cr//B1Vey2WwNfDmChF4yBZ2H+RicwIQgQEu9YioRBKeBXdn1DUVRqsG4CJ2ETpHPrW1GEOgYbTstGY3bJ9B1vQFOkfoGaIYftoeuGwLT29wE/GN/AjAeOGDg2uNilUPgYi6Xq+WRn/MURqnPEQTeA4n3THASIihwazHaDXoL3wdBPy2JGBP66xSTiWghT+tNYMMwmsUOzCLwAjXRTjy03nHEM9AnqAY9ohapjeBv6yKxT/qnfAJ5h5uexDL3WAAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAANCAYAAACgu+4kAAABR0lEQVR42oWSMUvDQBiGW2vuC/ZfuOvgT3BSRJzdnIzJXafO4i9wERyCubs6iIvgqIv+CWdFEAWLhQrFSivq+7UXiYkxB+GS97v3ufe7S61WMfxIbwlpPvN6I0zWSNmnf80k9QXMj3i+srpoWSuUuSdpxn8aRaR3URx52JnNKcBrJYGQ9gXfI9aKgO14wZP6CtG6zviORUMK7DqFnTPs+pACCwBS+hDibabYBWyf02DXu2yaHGBvxgv1KRa9OvGN+6ZQzzMAxue88RcAL8N8YZrIDsqMhRY8ZWIIfSdy3/1meLyI+OdINqgETCC4b+I2lP05A8yaWrrtK9OrBLhbmIN47SubGsZu3hCqc0MuZTnAjWaUrHARiT6y59IIzCafTXojpQA36mjnxLXCgPpEXT0gQC75h6oCTM9GxksA9PL67M7RMoO/AZ1nC7yBUUNXAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage3 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAANCAYAAACgu+4kAAABMklEQVR42mNgIABev37N++3bN2l08d+/f7v/BwK8mv/8+eP/79+/R0DaFyb29u1bPqDYISB+i9MAoI0yQAXr/0MB0AAvkPjfv3/zgOLv/yMBFI2rVq1ihir6BJIE0m+AmhN+/PihBmQ/BeIv/9EAXPOvX78MgApOwSSABi3+/PmzKJCeDhR/9R8HYHjx4gU3UFE3UNFvqK23gQHkAhQrgfJ//ccDGIAKTsA4oAADGfjz50+9/0QCUIDJAjVuQDJkP9A7piAXANkvYC7DaQBSlAUAFT+GGvIDaEADNAw6gfw7BA2AxTFQwxSghr9Qg64Bw8MWmh5A4CNeA5BixAyo+DzUkH9AQxyg6aAZ3Us4U+H+/ftZoOHwBTklggBQbC8ofAgmZRD4/v27PCghYUnmaSADAGRlFfJFHTADAAAAAElFTkSuQmCC";
  private static com.google.gwt.resources.client.ImageResource aboutTabIcon;
  private static com.google.gwt.resources.client.ImageResource interactorsTabIcon;
  private static com.google.gwt.resources.client.ImageResource profilesTabIcon;
  private static com.google.gwt.resources.client.ImageResource showHideHoveredIcon;
  private static com.google.gwt.resources.client.ImageResource showHideIcon;
  private static com.google.gwt.resources.client.TextResource aboutThis;
  private static org.reactome.web.diagram.controls.settings.HideableContainerPanel.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      aboutTabIcon(), 
      interactorsTabIcon(), 
      profilesTabIcon(), 
      showHideHoveredIcon(), 
      showHideIcon(), 
      aboutThis(), 
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("aboutTabIcon", aboutTabIcon());
        resourceMap.put("interactorsTabIcon", interactorsTabIcon());
        resourceMap.put("profilesTabIcon", profilesTabIcon());
        resourceMap.put("showHideHoveredIcon", showHideHoveredIcon());
        resourceMap.put("showHideIcon", showHideIcon());
        resourceMap.put("aboutThis", aboutThis());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'aboutTabIcon': return this.@org.reactome.web.diagram.controls.settings.HideableContainerPanel.Resources::aboutTabIcon()();
      case 'interactorsTabIcon': return this.@org.reactome.web.diagram.controls.settings.HideableContainerPanel.Resources::interactorsTabIcon()();
      case 'profilesTabIcon': return this.@org.reactome.web.diagram.controls.settings.HideableContainerPanel.Resources::profilesTabIcon()();
      case 'showHideHoveredIcon': return this.@org.reactome.web.diagram.controls.settings.HideableContainerPanel.Resources::showHideHoveredIcon()();
      case 'showHideIcon': return this.@org.reactome.web.diagram.controls.settings.HideableContainerPanel.Resources::showHideIcon()();
      case 'aboutThis': return this.@org.reactome.web.diagram.controls.settings.HideableContainerPanel.Resources::aboutThis()();
      case 'getCSS': return this.@org.reactome.web.diagram.controls.settings.HideableContainerPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
