package org.reactome.web.diagram.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class IllustrationPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.client.IllustrationPanel.Resources {
  private static IllustrationPanel_Resources_default_InlineClientBundleGenerator _instance0 = new IllustrationPanel_Resources_default_InlineClientBundleGenerator();
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
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.client.IllustrationPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-panelHidden {\n  animation-direction : " + ("reverse")  + ";\n  -webkit-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -o-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  background-color : " + ("white")  + ";\n  width : " + ("0")  + ";\n  height : " + ("0")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-panelShown {\n  -webkit-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -o-transition : ") + (("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  background-color : " + ("white")  + ";\n  width : " + ("100%")  + ";\n  height : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-panelShown img {\n  width : " + ("99%")  + ";\n  padding : " + ("5px")  + ";\n  margin-bottom : " + ("100px")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-container {\n  height : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-close {\n  height : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px") ) + (";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("left")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("7px")  + ";\n  left : " + ("10px")  + ";\n  border-radius : ") + (("12px")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-close:hover {\n  height : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-close:active:hover {\n  height : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden") ) + (";\n  background : " + ("url(\"" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n")) : ((".org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-panelHidden {\n  animation-direction : " + ("reverse")  + ";\n  -webkit-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -o-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  background-color : " + ("white")  + ";\n  width : " + ("0")  + ";\n  height : " + ("0")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-panelShown {\n  -webkit-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -o-transition : ") + (("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  transition : " + ("width"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"height"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  background-color : " + ("white")  + ";\n  width : " + ("100%")  + ";\n  height : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-panelShown img {\n  width : " + ("99%")  + ";\n  padding : " + ("5px")  + ";\n  margin-bottom : " + ("100px")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-container {\n  height : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-close {\n  height : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px") ) + (";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("right")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("7px")  + ";\n  right : " + ("10px")  + ";\n  border-radius : ") + (("12px")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-close:hover {\n  height : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-close:active:hover {\n  height : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden") ) + (";\n  background : " + ("url(\"" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (IllustrationPanel_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n"));
      }
      public java.lang.String close() {
        return "org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-close";
      }
      public java.lang.String container() {
        return "org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-container";
      }
      public java.lang.String panelHidden() {
        return "org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-panelHidden";
      }
      public java.lang.String panelShown() {
        return "org-reactome-web-diagram-client-IllustrationPanel-ResourceCSS-panelShown";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.client.IllustrationPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.client.IllustrationPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACdklEQVR42r1W3UsUURS/uc3uUz30VOAfUBL5B/R/SBAZZWs6jUhkQUFPFQa9RcWus3cGlW0hKEUlKPJNQeshMChMe+njTUQC2WaxTud3Z2e786UrG3vgsJd7z/mdu+fjd0eIXcQYkt05yx3OXnGms6bzmX83lfrraZzBRuxXskNOFwM4rNSkOvBpCtywZB87bOwDPNAN+O4KnjPlNd3poCnJMNNBcQYbfQ8YyTc3ZW/DyHJJ9NskLhRI5McoEwVhzQxKEn1FEhcLyjZn6YFlbzjnlnucD7YDAwAfu1Gm1x+/U395gcT5J9QxUFLA0AOXSwxeoDsv39PTt+t0eHhcBdIusQ1MLYAzHhwCyBgs0eTSGkG8nd/UMzZP4uwjMviWUKx77HkKRC6u0pGrE+xr/wvCmD64aZ/gDa9x+3yROm9VSJetao1OP5glceahUqy3ql7I5tTdKRKXivq/8IAtOHcjocLy7Q3O+8jz5RDAz181Onn7mVKsdbn+YlmlCL6hWjG24MVMtIAorDj3mEZfrYSAqpwuqC732Qa28MnFO21G1KcyGtnvnrxNE0vrlCY462AbBW4ltDJji/r4UzyI67chd8zcytcY+CzvCdVNRWWbMiubewfgrpr78K2FAC2mKNNEiloq8uheRU5qU0wpWq/ZNkVLo7WT2zQ6aDws3femIoPmJQxaOEjnzYoa0tigxanCVmMvF1YbzqCFGFUwfdR2/tRrsUZZvj1oJkYVaWR3iAms8u6LIjRMKQguIDsAgQAHyov05tMPOsrECJ9UsovTtePTNaiY2xDUHC0gKBzpVJSu6NpNp+u2PDhteTLb8uj/78+Wv8QQdIlXHbpRAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACbklEQVR42r1WS2tTQRS+bgRR0GqjxqRJBPG1sIuu3Ovaf9CNtNsurP4AuxBUjOZFXVRpF7pQpHTTRUFFcGGTO9c39S6sRIQK9UIjXaQ0Hs83zo0zN5OYgs2BA8PMOd+Zex7fXMfpIEdzYjBV9MZYZ1l91kCpr/bGYONsVVKT4nS64N1jAOpGpS37dAWeLoqL7LTaLbimq/D9F/gl3WmgwNoBdEDZGF/DGPa0FMVw06jk0cE7Lu29VaH+2y4lCnDUQTy515d1aV+2QodyrvT5G0gMG+CZfOUkH6yHBgAenHpHz6s1uvy0SntululIXkhgaJzXfQycXVyhJ58COj75Wvpol1gHpnZ7bzoEhzNu92gpIEi98YtG5pdp5/VFeUso1qO8F8qDjz/oxN03dDgn9HRNq7y7p1IFrx4exDglQ/ffky61eoMuPPZpx7VXUrGu1TcNm3MPl+gA+zYDMCawOYA3rhcpwV8Q4/xfffnNAPi50aCzMx+kYq3LBNsiZcm8iBTcG0dx5yKbMsCuG2Uqiu8G0AanC6oLbGALn3RLp4k5R02lGbn0J1XopLAWNsEZbGBrdlFTfUeNfsthhh3282fHuXALX9ZawBeW1+QZbDKltrMS9CTAdqdom4scbdOkmtKJLbQpWhoBErY2jQ4ahuU8D405aJuWQTODYDhjtkGLUgXGHWOP8Q9l1EIVI/Ofm+lCLUAv8byFKqJkl1Zkd4wJbNYPKFtekSmLa2QH4tvNBHjlWZVefK3Rmam30ifVjuxsdA0KBjAo2UbXSd7rV5SOTupI1+0eHIBEH5Tog5Ts9sHpyZPZk0f/f/+2/AaX3iLghaFcpQAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACMklEQVR42r1WTUsbYRDen6O9+CMEkUqiaxOTHMylPXgQPFRv/Qv1roeC9aogXks0u5uKIhbsoUJaesh+5KPtkqJZDSkZ51l35d2vNAHNwMDyzswz7zvvzPOuJA2QnGZM5TRrLV8xD/KaWWW1Pa1iDTb4SKNKVq2/KGjmBwaiYRS+iBkKnHf2Oq+av4cFf1TEcOxA8IJmvBWDcp4mgcbZgZFU76JwZFpUDEod67RQNmhJjZTEXZtnW5p9XrFvIZDYKAZ3fmJNsKHjOwD4zWmdLu072vreprkjnbJCkgx/p48N2qtdk9ZyaPmz5cYIm+gAUyiNtSMGZ1WDlKZDkF6f6P03m2ZKurtLKL43r2zypdToUJGTZBTxlNbOQ2kUa5IXur5B5uOunDVIlM6/Pr27/EXTn2qu4htroqxftNxyCqfoAhtdsy7WF7WV2fHjz78BAIcBV8+brjoh8F32xV0shRuBsflyzcNwdyDBLJfhQL8JAPX6fVdFgQ985eDu/S47lLwJjXQJSoUgpeVQksAGHznURYJWJW/046aT0mWdL86gL3/uIuBYgw0+heRZsceS4HlL9PyXHG5TlIZbbneENkVLyzGU8tCmoUHDsGzw0Iw6aBhOlCoyaBGq4HHH2GP8fQEthKkC9NHzcoBWQC8ZNYYqksgOBFZp3dI+ExqITQwG8b1kAtz+0aav7a5LjAPJLo6uQcEABiUDMNwlPqWk2Gfxf3Q9lgdnLE/mWB79p/5tuQeTqeRBz8pWpQAAAABJRU5ErkJggg==";
  private static com.google.gwt.resources.client.ImageResource closeClicked;
  private static com.google.gwt.resources.client.ImageResource closeHovered;
  private static com.google.gwt.resources.client.ImageResource closeNormal;
  private static org.reactome.web.diagram.client.IllustrationPanel.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      closeClicked(), 
      closeHovered(), 
      closeNormal(), 
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
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'closeClicked': return this.@org.reactome.web.diagram.client.IllustrationPanel.Resources::closeClicked()();
      case 'closeHovered': return this.@org.reactome.web.diagram.client.IllustrationPanel.Resources::closeHovered()();
      case 'closeNormal': return this.@org.reactome.web.diagram.client.IllustrationPanel.Resources::closeNormal()();
      case 'getCSS': return this.@org.reactome.web.diagram.client.IllustrationPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
