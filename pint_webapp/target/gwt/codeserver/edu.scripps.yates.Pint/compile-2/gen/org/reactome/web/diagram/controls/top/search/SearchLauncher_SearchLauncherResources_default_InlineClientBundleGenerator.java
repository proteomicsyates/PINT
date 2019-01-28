package org.reactome.web.diagram.controls.top.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherResources {
  private static SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator _instance0 = new SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launchPanel {\n  background-color : " + ("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : " + ("32px")  + ";\n  overflow : " + ("hidden")  + " !important;\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -webkit-transition : " + ("width"+ " " +"0.3s")  + ";\n  -moz-transition : ") + (("width"+ " " +"0.3s")  + ";\n  transition : " + ("width"+ " " +"0.3s")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launchPanelExpanded {\n  width : " + ("315px")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch {\n  height : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getHeight() + "px")  + ";\n  width : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getSafeUri().asString() + "\") -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getLeft() + "px -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getTop() + "px  no-repeat")  + ";\n  float : " + ("right")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0") ) + (";\n  cursor : " + ("pointer")  + ";\n  opacity : " + ("0.7")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:hover {\n  height : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getHeight() + "px")  + ";\n  width : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getSafeUri().asString() + "\") -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getLeft() + "px -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:active:hover {\n  height : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getHeight() + "px")  + ";\n  width : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getSafeUri().asString() + "\") -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getLeft() + "px -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:disabled, .org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:disabled:hover, .org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:disabled:active {\n  height : ") + (((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getHeight() + "px")  + ";\n  width : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getSafeUri().asString() + "\") -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getLeft() + "px -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getTop() + "px  no-repeat")  + ";\n  cursor : " + ("auto")  + ";\n  opacity : " + ("1")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-input {\n  border-radius : " + ("5px")  + ";\n  float : " + ("right")  + ";\n  margin : " + ("5px"+ " " +"4px"+ " " +"0"+ " " +"0")  + ";\n  height : " + ("20px")  + ";\n  width : " + ("260px") ) + (";\n  border : " + ("none")  + ";\n  background-color : " + ("#23688b")  + ";\n  font-size : " + ("14px")  + ";\n  opacity : " + ("0.2")  + ";\n  color : " + ("#23688b")  + ";\n  padding-right : " + ("5px")  + ";\n  outline : " + ("none")  + ";\n  transition : " + ("opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -webkit-transition : " + ("opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-inputActive {\n  opacity : ") + (("0.8")  + ";\n  color : " + ("#fff")  + ";\n}\n")) : ((".org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launchPanel {\n  background-color : " + ("rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("16px")  + ";\n  height : " + ("32px")  + ";\n  width : " + ("32px")  + ";\n  overflow : " + ("hidden")  + " !important;\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -webkit-transition : " + ("width"+ " " +"0.3s")  + ";\n  -moz-transition : ") + (("width"+ " " +"0.3s")  + ";\n  transition : " + ("width"+ " " +"0.3s")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launchPanelExpanded {\n  width : " + ("315px")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch {\n  height : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getHeight() + "px")  + ";\n  width : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getSafeUri().asString() + "\") -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getLeft() + "px -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchNormal()).getTop() + "px  no-repeat")  + ";\n  float : " + ("left")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0") ) + (";\n  cursor : " + ("pointer")  + ";\n  opacity : " + ("0.7")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:hover {\n  height : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getHeight() + "px")  + ";\n  width : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getSafeUri().asString() + "\") -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getLeft() + "px -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:active:hover {\n  height : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getHeight() + "px")  + ";\n  width : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getSafeUri().asString() + "\") -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getLeft() + "px -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchClicked()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:disabled, .org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:disabled:hover, .org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch:disabled:active {\n  height : ") + (((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getHeight() + "px")  + ";\n  width : " + ((SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getSafeUri().asString() + "\") -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getLeft() + "px -" + (SearchLauncher_SearchLauncherResources_default_InlineClientBundleGenerator.this.launchDisabled()).getTop() + "px  no-repeat")  + ";\n  cursor : " + ("auto")  + ";\n  opacity : " + ("1")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-input {\n  border-radius : " + ("5px")  + ";\n  float : " + ("left")  + ";\n  margin : " + ("5px"+ " " +"0"+ " " +"0"+ " " +"4px")  + ";\n  height : " + ("20px")  + ";\n  width : " + ("260px") ) + (";\n  border : " + ("none")  + ";\n  background-color : " + ("#23688b")  + ";\n  font-size : " + ("14px")  + ";\n  opacity : " + ("0.2")  + ";\n  color : " + ("#23688b")  + ";\n  padding-left : " + ("5px")  + ";\n  outline : " + ("none")  + ";\n  transition : " + ("opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -webkit-transition : " + ("opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n}\n.org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-inputActive {\n  opacity : ") + (("0.8")  + ";\n  color : " + ("#fff")  + ";\n}\n"));
      }
      public java.lang.String input() {
        return "org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-input";
      }
      public java.lang.String inputActive() {
        return "org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-inputActive";
      }
      public java.lang.String launch() {
        return "org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launch";
      }
      public java.lang.String launchPanel() {
        return "org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launchPanel";
      }
      public java.lang.String launchPanelExpanded() {
        return "org-reactome-web-diagram-controls-top-search-SearchLauncher-SearchLauncherCSS-launchPanelExpanded";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void launchClickedInitializer() {
    launchClicked = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "launchClicked",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 32, 32, false, false
    );
  }
  private static class launchClickedInitializer {
    static {
      _instance0.launchClickedInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return launchClicked;
    }
  }
  public com.google.gwt.resources.client.ImageResource launchClicked() {
    return launchClickedInitializer.get();
  }
  private void launchDisabledInitializer() {
    launchDisabled = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "launchDisabled",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 32, 32, false, false
    );
  }
  private static class launchDisabledInitializer {
    static {
      _instance0.launchDisabledInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return launchDisabled;
    }
  }
  public com.google.gwt.resources.client.ImageResource launchDisabled() {
    return launchDisabledInitializer.get();
  }
  private void launchHoveredInitializer() {
    launchHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "launchHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
      0, 0, 32, 32, false, false
    );
  }
  private static class launchHoveredInitializer {
    static {
      _instance0.launchHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return launchHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource launchHovered() {
    return launchHoveredInitializer.get();
  }
  private void launchNormalInitializer() {
    launchNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "launchNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
      0, 0, 32, 32, false, false
    );
  }
  private static class launchNormalInitializer {
    static {
      _instance0.launchNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return launchNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource launchNormal() {
    return launchNormalInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherCSS getCSS;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAADbUlEQVR42s1XWUhUYRQeEEcLKoiCgiiinlqljaAeo4ceeot6CaQgylGxiNaHxBZRI4oWsmYx20YxbRFEhWQQdIjQTLCUSjMbxxmt0VnvbKdzruNt7p175/5jEvPDB8P8/z3fuWf5zn81GtZ1xLAgI//hwUyd8X5mvrEzS2d0anVGjkC/6T/aozN0VjNnK0+/FkkMCC8CGEFnDfTs7IlzL2Vr8/RlaCiYArEUQbJBtlIjP1a5Rqsz9PwDsQSGHrLJGPIHOZhL+9yRT4Nskm3VN1cjX1hUDasv1sDS009m54RiJA7cmJcs7AXmDrBN+mDSH4SxKT/89nEQiQK0f7FDUa01tXQgVwK/9oSpXOkh66ADvFwYlJbT7QfLwCi7E8gl12oJ1Z77yAJv+20CkT8YAacnwP8mh355OZEj74acsL30JVN3iFo0K8+klzvY/WNCMD7gmITrrb2w4txzfo9qYF1xHdxu60PH/kZnFNPEEgXiFBROTmT23WmGECUZF73p5sv1isbKmz+CY2omMiHYdo0pCl5eMTOO6w8leJdvEsjdgRCsL6lXNfghLlpTgSBTFIhbgzpeKd3YcrUBHFhY/BtheFmMbcW39qCztOjZZWeeqqcBuTXYm1bpxt5bTeDhpo11fB1jcmD52WdCUY5joa66YGbRBStFwCHd2H+vRXCgsXeYyYHFpx7zGjHjwMrzZpYIODSxkSra2FDyghcaWjYXW1VvxGdcMQdIqCgiDM9xsg4QwtFozJgPdpS+UjVW+/6bUIQ2l5dVlDjZFBDuWvpEIqNkZNHJathV0YhtOF205HdhTSebFlAK5IqQML+wCnwxgQmj0Qha3nOzSXSGROlodTufpvhV1THAOpyssm04g5wrDSLDVFxcKAKf7S4YmnDzuQ6GI6Iz1Ip1XYNw2GRha0M5IYpHdoEJ7FgHnIRIbpF46cyd4A+FeWeb+0bUhUhJiqUobuziSag7aPqR2IxgsVHvt376CYPjbp74+4RHcMiFZylKSaU42TCSwxIcQptwLuwse823XvxeG47kqCQqfkxZCzqoPIySjONUQemKn4xCarCKu4bHlcex2oUkVYQj4jhQ/YjqIeFCwnAlSwW7K94I5ORLIBRWv5KxXkpZQQJFcejHlmW7lKbFtTwtPkzS4tMsLT5O/9Pn+R+AluR+Rck0egAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAChUlEQVR42s1XWY7iMBDN5/THnGL6AH2b4Uo9J5iRus/CD9CIPYhN8IHEBwoZxB6WsHj8orZljJ1UelotSioRZLvq1W47DpFyudz3Uqn0s1qt/q5UKm+c/3I+vDO+37CGPdjrfBYVCoVHLviVKwg4MyIHOIOzH1aczWa/cSHPXFiYQrHOIWRAVirl+Xz+Bz/s/odinV3IJCnnMXziB7xPVC7Yg2yK5bHKa7UaazabrNFofAiE1RM8YR7i3D4ajVgYhux8PrPj8chOpxMDrdfraC1NOKDrBkC5XP5lO7TZbNjlcmE2AiAAoYKArptSM2X7cDhkq9VKKgIIYTk8Ib4FBUHAOp0OqTquSpQjejFt3G63Uvh+v2ee5zHXdaM15ECr1WKTyeTKO/AG0QsvssOZmsxgMJBCYWm73bYKAzAoFp7pdrukZhV1TI4koy/y5iGVQyAsTRKoegtniF7IAMAffQHWqhZRhCH22CvCQClT6AaAor7Q7/dlXFEBFABQKJISv+gVBABFh3/4pvgLaxaLBQlAvV6XZwBAJGsC+877OL1aQMyFNWg+FAA4kzYE0G0EAFbLilLbs9lMnqGCFgB806Lv+1dNJm429Ho9mbSgFK3ZNyahKEW9/SI51T2IM7olLFZpOp1Sy7BoLEO1tFRCXgDUbrdjh8MhsloHiTyYz+cRMGoZZuI2wRMmRTaC+8XMWC6XyY3I1op1Ho/H0gsABIbr8R8DCx6BYvyq3oiZDYG8vNqGka3e0SnR7/UWrU5OdYKaPCGHUdw4TsumxFVHtXUcJ11I0rKJVC/cXEgoV7I0jL6ghyHxSka9lFIZDUpcZEiX0ru4lt/Fw+QunmZ38Tj9quf5P7Y6fu+SG6G3AAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAADRklEQVR42s1XS08TURTuUhfujBtpMWpMjEaMUZcGn8HEhJ2wMPEXGBOXanRdYzARBiS2QReooCY+2KkRUHmofVJswWKUViktWOgDOm2nx3Mu7Tgdpp1bJKYnOaHh3nu+757nHYOBUzab328yttqaTIL9jrHVOWxqc0RQxbxG2P/Ymq2J9hrWS2pabDuNgt1qFBxJk+AAHqW9dIbOrh35+tsNaMyMmuYF1lA6ayZbFWFvuf15B97C9Q/Aao+4yCYX+NZbH/fjodB6gSs0RLZ1b64HvqvTBYfvj8Nei3tNJEp6oqZlaGM5t18ZCMBsMgNxUYLIUgYWxSxIADD6KwHX3gUrCgdhrSJgarfdKHXIFkrCUiYHpWR+OQPDPxP8nkCsVaWmle0XX/2AD8G4DLSczSFYlv0mQgupbBER52wSGnp8XNVRVKJGwWnR2uiJLMnGvy2koMMehgNdHrZGOXCk+wtY3RFGrCBhDBNfKJyWvx1Oo8mcezkFGWnFKN30+ENvSWOCfRbmFJ5p6PVxNSvWMY1t9mb1Ym27EwohT6QlqH/g1TU4rvAWneHyAmIbsI93qhdOPvIV3YjH2Ck8k8yzpjzZZx3TP4fYGH/HiHqh+blfNvZphi+76xCwkJS/kcChex6eMIwYagVHWL1wvm8KCawkwOvvi1wEdt91QzwtyQQOchAgbAMbp6qF+m4vLOZvE+LM6qOYJzFxhQA1qjq+EIiaBEilfBKSsdO9E7rGXnyNyknIS5oR0AoBaRfWt1LKzYbGJ5Mwh0RJiPfVQb7WzEKglYSk2zuccoOhP+SRs8/8RXuoKV16M81mhFJ6vPO8c2FEswwLegJLSymUXCIy8UdTEIilWXgyUvGMoOrp80fhArZxvjLUaERK3YZNKYxA+QQvK1S5l/sDkEKXEdn+6Zh+IyrVitV6c3SGgdAYpkZDMZ9JpFntDyLQdExkwMF4WiZEVUH7yrbicsNIS/dgvR/DkjvzeIKVnnJtCCenemhTNQ9oeEIeRuXGcaVK4Uplc5qhGcNZUXIc6z1IKlU1B+pPRfmgfpDwPMkq0cankzK4xMKQ03+S8T5KeZUaFAmVLNejtCqe5VXxYVIVn2bV8XH6nz7P/wBUzhshe3IntQAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAADHklEQVR42s1XT28SURDnqAc/hX4AP4KfwHhpWDUxxtj452Q1WmP0oolRrxqjSav33upFL4ZQlKIRC7aNbVOtFtjCrhQoLLDLwjjzursuyy4MpDFMMtnNvn0z8+bPb+aFQkw6Ob92ZCKaC4dj8gspmluUYllViuX0fcZ3/EZr9A/9GzoomohkjknRzCwq0pCByRrtob0jKz4R2ToUXsg8lmKyMYRiD8sGySBZQyk/9X77KApIj664h9Mkk+ny7HHckD9A5TbnSTbn5H2Vn/sow9XPebiwuDOSEYGemJjLHO7n9tnNCpSMNtTNDlTwqeGzAwDfKzq8/lEZKhykq8eA0x8yT4I2bewZ0GxDIFVabVgt62wjSFdvqflk+7P1EqygYJvw4LDX6oh3vd2BmvVu02bVgOklhVUdXSWKH2f8ftyqtRzhOw0T3mRrcOlTXqxRDlz7UoC3uZowzCYKE9MTMw7C+YHMw5UimJZQOumNZPDJ5jM1kRdETfTM9JLKAiuBmOGFbak3RjKYlncbKHAqOditv1zeauBmjhdId0iKZl96F25+VfBE/2LNEXYL99DpRVKiNy4mGGWKuin+Ce/Cg+U/TtavYwVwDJhEhXZSUqJesXJlACfIAMW78Gi16JwmudtkGXA+viMwwjbgMs8AJWS11K6FKcxuzRK2q/Oy+nqy4BhQxhBMJlhIqfsaQGxXFgm7zajtuNpwkpBrtG2A4rf4Tta6QCawN8RluJtWBRoSkQ9e8aFZ8U1C4rNYirrlhrYl+P5ysesfAqXnGyUBPm6KFDSuAQnfMnSXo5uqmFyU6Lm6CWrTFOExu9FYJG8Cw/F0rcQrQz8gcvMZ9AQp8sC+L5Exs5tlIAihSkiVmoOBKAiKvTz3uyqUUHWQcAKbIsaIav9bSQcFPUKRUF1tUzP3QakvFPdrRn5MTYj6wp2UKkrPvbbq6pxOB0VvpP09MTOwHQ/LFC7DZ26gpvazZgS340EDybDstYHaVMqFqD0DCWckG4bvIS7Y1LHCMHAk4w6lXCaAIsrVW7yhdCzG8rG4mIzF1WwsLqf/63r+Fzn0b/yR6TYQAAAAAElFTkSuQmCC";
  private static com.google.gwt.resources.client.ImageResource launchClicked;
  private static com.google.gwt.resources.client.ImageResource launchDisabled;
  private static com.google.gwt.resources.client.ImageResource launchHovered;
  private static com.google.gwt.resources.client.ImageResource launchNormal;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      launchClicked(), 
      launchDisabled(), 
      launchHovered(), 
      launchNormal(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("launchClicked", launchClicked());
        resourceMap.put("launchDisabled", launchDisabled());
        resourceMap.put("launchHovered", launchHovered());
        resourceMap.put("launchNormal", launchNormal());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherResources::getCSS()();
      case 'launchClicked': return this.@org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherResources::launchClicked()();
      case 'launchDisabled': return this.@org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherResources::launchDisabled()();
      case 'launchHovered': return this.@org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherResources::launchHovered()();
      case 'launchNormal': return this.@org.reactome.web.diagram.controls.top.search.SearchLauncher.SearchLauncherResources::launchNormal()();
    }
    return null;
  }-*/;
}
