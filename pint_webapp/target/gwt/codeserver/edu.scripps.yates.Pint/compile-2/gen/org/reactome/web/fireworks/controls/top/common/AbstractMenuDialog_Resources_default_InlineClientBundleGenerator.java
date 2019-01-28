package org.reactome.web.fireworks.controls.top.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class AbstractMenuDialog_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.Resources {
  private static AbstractMenuDialog_Resources_default_InlineClientBundleGenerator _instance0 = new AbstractMenuDialog_Resources_default_InlineClientBundleGenerator();
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
  private void closeDisabledInitializer() {
    closeDisabled = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "closeDisabled",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 24, 24, false, false
    );
  }
  private static class closeDisabledInitializer {
    static {
      _instance0.closeDisabledInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return closeDisabled;
    }
  }
  public com.google.gwt.resources.client.ImageResource closeDisabled() {
    return closeDisabledInitializer.get();
  }
  private void closeHoveredInitializer() {
    closeHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "closeHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
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
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
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
    getCSS = new org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-menuDialog {\n  background-color : " + ("white")  + " !important;\n  opacity : " + ("0.85")  + ";\n  border : " + ("2px"+ " " +"solid"+ " " +"rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border-radius : " + ("16px")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  margin : " + ("5px"+ " " +"0")  + ";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header {\n  margin : " + ("0"+ " " +"4px"+ " " +"2px"+ " " +"0")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header:hover, .org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header:active:hover {\n  cursor : ") + (("move")  + " !important;\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header button {\n  float : " + ("left")  + ";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-headerText {\n  float : " + ("right")  + ";\n  color : " + ("#005a75")  + ";\n  font-weight : " + ("bolder")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  margin-right : " + ("5px")  + ";\n  min-width : " + ("150px")  + ";\n  max-width : " + ("160px") ) + (";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-close {\n  height : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("left")  + ";\n  position : " + ("absolute")  + ";\n  top : ") + (("2px")  + ";\n  left : " + ("5px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-close:hover {\n  height : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-close:active:hover {\n  height : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n")) : ((".org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-menuDialog {\n  background-color : " + ("white")  + " !important;\n  opacity : " + ("0.85")  + ";\n  border : " + ("2px"+ " " +"solid"+ " " +"rgba(" + "88"+ ","+ " " +"195"+ ","+ " " +"229"+ ","+ " " +"0.8" + ")")  + ";\n  border-radius : " + ("16px")  + ";\n  -webkit-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  -moz-box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  box-shadow : " + ("3px"+ " " +"3px"+ " " +"5px"+ " " +"0"+ " " +"rgba(" + "50"+ ","+ " " +"50"+ ","+ " " +"50"+ ","+ " " +"0.75" + ")")  + ";\n  margin : " + ("5px"+ " " +"0")  + ";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header {\n  margin : " + ("0"+ " " +"0"+ " " +"2px"+ " " +"4px")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header:hover, .org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header:active:hover {\n  cursor : ") + (("move")  + " !important;\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header button {\n  float : " + ("right")  + ";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-headerText {\n  float : " + ("left")  + ";\n  color : " + ("#005a75")  + ";\n  font-weight : " + ("bolder")  + ";\n  overflow : " + ("hidden")  + ";\n  white-space : " + ("nowrap")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  margin-left : " + ("5px")  + ";\n  min-width : " + ("150px")  + ";\n  max-width : " + ("160px") ) + (";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-close {\n  height : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("right")  + ";\n  position : " + ("absolute")  + ";\n  top : ") + (("2px")  + ";\n  right : " + ("5px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-close:hover {\n  height : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-close:active:hover {\n  height : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (AbstractMenuDialog_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n"));
      }
      public java.lang.String close() {
        return "org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-close";
      }
      public java.lang.String header() {
        return "org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-header";
      }
      public java.lang.String headerText() {
        return "org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-headerText";
      }
      public java.lang.String menuDialog() {
        return "org-reactome-web-fireworks-controls-top-common-AbstractMenuDialog-ResourceCSS-menuDialog";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACdklEQVR42r1W3UsUURS/uc3uUz30VOAfUBL5B/R/SBAZZWs6jUhkQUFPFQa9RcWus3cGlW0hKEUlKPJNQeshMChMe+njTUQC2WaxTud3Z2e786UrG3vgsJd7z/mdu+fjd0eIXcQYkt05yx3OXnGms6bzmX83lfrraZzBRuxXskNOFwM4rNSkOvBpCtywZB87bOwDPNAN+O4KnjPlNd3poCnJMNNBcQYbfQ8YyTc3ZW/DyHJJ9NskLhRI5McoEwVhzQxKEn1FEhcLyjZn6YFlbzjnlnucD7YDAwAfu1Gm1x+/U395gcT5J9QxUFLA0AOXSwxeoDsv39PTt+t0eHhcBdIusQ1MLYAzHhwCyBgs0eTSGkG8nd/UMzZP4uwjMviWUKx77HkKRC6u0pGrE+xr/wvCmD64aZ/gDa9x+3yROm9VSJetao1OP5glceahUqy3ql7I5tTdKRKXivq/8IAtOHcjocLy7Q3O+8jz5RDAz181Onn7mVKsdbn+YlmlCL6hWjG24MVMtIAorDj3mEZfrYSAqpwuqC732Qa28MnFO21G1KcyGtnvnrxNE0vrlCY462AbBW4ltDJji/r4UzyI67chd8zcytcY+CzvCdVNRWWbMiubewfgrpr78K2FAC2mKNNEiloq8uheRU5qU0wpWq/ZNkVLo7WT2zQ6aDws3femIoPmJQxaOEjnzYoa0tigxanCVmMvF1YbzqCFGFUwfdR2/tRrsUZZvj1oJkYVaWR3iAms8u6LIjRMKQguIDsAgQAHyov05tMPOsrECJ9UsovTtePTNaiY2xDUHC0gKBzpVJSu6NpNp+u2PDhteTLb8uj/78+Wv8QQdIlXHbpRAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAABs0lEQVR42r1W22rCQBDN59i+9I/60g/wQb+n0D8ofoTxFlFCIEjyEAwieEGFPgjqdM6SlclmE6K0GRiy2cyc3Z3L2ThOhUyn07fJZNJm/WZdsO4zXWRzbdg4jwo7vY7H408GoDoKW/jUAmfjD3ba1gUXuoVvJTgbdZ4ANrVTtvN3aTgajWgwGKhnGdhwOFRq2gDL3HmL9Uc6zmYzOh6PlCSJWoidJICyWa1WtNvtyPM89S4WAVZLLvAlnfGEI+R2u1EURdTv9+8AGMdxTFo2mw1xknObAKYC59Vf+OUsQzOfz0nK5XKhMAwVMBRjzEkJgsAM1RnY2H3XiJ8yXC6XOYDr9Uq+7yvFWEqapipExgmgXSS3Z0se4r5er3NACBdUCmxga+RAb7bnZF1Z+IhTYEc6FzbBN33ikkpbOFnrl5YhAA6HQwEcc7qaKnpi38gC/xuiJpJcKFMYo/TqlilKWp+4UKa2RkPTPNpoaE5ro9moAm2P9tcCWrBRhQ4XciFpJkcVZWQHAoMjCM3sUoxd11VEeDqdFDFWkl0ZXWs6tsT2Xj216LqRC6eRK7ORS/+vf1t+Ab3fff1qJw2VAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACbklEQVR42r1WS2tTQRS+bgRR0GqjxqRJBPG1sIuu3Ovaf9CNtNsurP4AuxBUjOZFXVRpF7pQpHTTRUFFcGGTO9c39S6sRIQK9UIjXaQ0Hs83zo0zN5OYgs2BA8PMOd+Zex7fXMfpIEdzYjBV9MZYZ1l91kCpr/bGYONsVVKT4nS64N1jAOpGpS37dAWeLoqL7LTaLbimq/D9F/gl3WmgwNoBdEDZGF/DGPa0FMVw06jk0cE7Lu29VaH+2y4lCnDUQTy515d1aV+2QodyrvT5G0gMG+CZfOUkH6yHBgAenHpHz6s1uvy0SntululIXkhgaJzXfQycXVyhJ58COj75Wvpol1gHpnZ7bzoEhzNu92gpIEi98YtG5pdp5/VFeUso1qO8F8qDjz/oxN03dDgn9HRNq7y7p1IFrx4exDglQ/ffky61eoMuPPZpx7VXUrGu1TcNm3MPl+gA+zYDMCawOYA3rhcpwV8Q4/xfffnNAPi50aCzMx+kYq3LBNsiZcm8iBTcG0dx5yKbMsCuG2Uqiu8G0AanC6oLbGALn3RLp4k5R02lGbn0J1XopLAWNsEZbGBrdlFTfUeNfsthhh3282fHuXALX9ZawBeW1+QZbDKltrMS9CTAdqdom4scbdOkmtKJLbQpWhoBErY2jQ4ahuU8D405aJuWQTODYDhjtkGLUgXGHWOP8Q9l1EIVI/Ofm+lCLUAv8byFKqJkl1Zkd4wJbNYPKFtekSmLa2QH4tvNBHjlWZVefK3Rmam30ifVjuxsdA0KBjAo2UbXSd7rV5SOTupI1+0eHIBEH5Tog5Ts9sHpyZPZk0f/f/+2/AaX3iLghaFcpQAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACMklEQVR42r1WTUsbYRDen6O9+CMEkUqiaxOTHMylPXgQPFRv/Qv1roeC9aogXks0u5uKIhbsoUJaesh+5KPtkqJZDSkZ51l35d2vNAHNwMDyzswz7zvvzPOuJA2QnGZM5TRrLV8xD/KaWWW1Pa1iDTb4SKNKVq2/KGjmBwaiYRS+iBkKnHf2Oq+av4cFf1TEcOxA8IJmvBWDcp4mgcbZgZFU76JwZFpUDEod67RQNmhJjZTEXZtnW5p9XrFvIZDYKAZ3fmJNsKHjOwD4zWmdLu072vreprkjnbJCkgx/p48N2qtdk9ZyaPmz5cYIm+gAUyiNtSMGZ1WDlKZDkF6f6P03m2ZKurtLKL43r2zypdToUJGTZBTxlNbOQ2kUa5IXur5B5uOunDVIlM6/Pr27/EXTn2qu4htroqxftNxyCqfoAhtdsy7WF7WV2fHjz78BAIcBV8+brjoh8F32xV0shRuBsflyzcNwdyDBLJfhQL8JAPX6fVdFgQ985eDu/S47lLwJjXQJSoUgpeVQksAGHznURYJWJW/046aT0mWdL86gL3/uIuBYgw0+heRZsceS4HlL9PyXHG5TlIZbbneENkVLyzGU8tCmoUHDsGzw0Iw6aBhOlCoyaBGq4HHH2GP8fQEthKkC9NHzcoBWQC8ZNYYqksgOBFZp3dI+ExqITQwG8b1kAtz+0aav7a5LjAPJLo6uQcEABiUDMNwlPqWk2Gfxf3Q9lgdnLE/mWB79p/5tuQeTqeRBz8pWpQAAAABJRU5ErkJggg==";
  private static com.google.gwt.resources.client.ImageResource closeClicked;
  private static com.google.gwt.resources.client.ImageResource closeDisabled;
  private static com.google.gwt.resources.client.ImageResource closeHovered;
  private static com.google.gwt.resources.client.ImageResource closeNormal;
  private static org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      closeClicked(), 
      closeDisabled(), 
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
        resourceMap.put("closeDisabled", closeDisabled());
        resourceMap.put("closeHovered", closeHovered());
        resourceMap.put("closeNormal", closeNormal());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'closeClicked': return this.@org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.Resources::closeClicked()();
      case 'closeDisabled': return this.@org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.Resources::closeDisabled()();
      case 'closeHovered': return this.@org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.Resources::closeHovered()();
      case 'closeNormal': return this.@org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.Resources::closeNormal()();
      case 'getCSS': return this.@org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog.Resources::getCSS()();
    }
    return null;
  }-*/;
}
