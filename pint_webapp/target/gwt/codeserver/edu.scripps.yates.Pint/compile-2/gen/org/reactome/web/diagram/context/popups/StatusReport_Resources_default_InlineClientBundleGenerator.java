package org.reactome.web.diagram.context.popups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class StatusReport_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.context.popups.StatusReport.Resources {
  private static StatusReport_Resources_default_InlineClientBundleGenerator _instance0 = new StatusReport_Resources_default_InlineClientBundleGenerator();
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
  private void failureInitializer() {
    failure = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "failure",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
      0, 0, 32, 32, false, false
    );
  }
  private static class failureInitializer {
    static {
      _instance0.failureInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return failure;
    }
  }
  public com.google.gwt.resources.client.ImageResource failure() {
    return failureInitializer.get();
  }
  private void iconInitializer() {
    icon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "icon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage3),
      0, 0, 27, 24, false, false
    );
  }
  private static class iconInitializer {
    static {
      _instance0.iconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return icon;
    }
  }
  public com.google.gwt.resources.client.ImageResource icon() {
    return iconInitializer.get();
  }
  private void successInitializer() {
    success = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "success",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage4),
      0, 0, 32, 32, false, false
    );
  }
  private static class successInitializer {
    static {
      _instance0.successInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return success;
    }
  }
  public com.google.gwt.resources.client.ImageResource success() {
    return successInitializer.get();
  }
  private void successWithWarningsInitializer() {
    successWithWarnings = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "successWithWarnings",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage5),
      0, 0, 34, 32, false, false
    );
  }
  private static class successWithWarningsInitializer {
    static {
      _instance0.successWithWarningsInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return successWithWarnings;
    }
  }
  public com.google.gwt.resources.client.ImageResource successWithWarnings() {
    return successWithWarningsInitializer.get();
  }
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.context.popups.StatusReport.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-popupPanel {\n  border : " + ("3px"+ " " +"solid"+ " " +"gray")  + " !important;\n  border-radius : " + ("15px")  + ";\n  padding : " + ("5px"+ " " +"10px"+ " " +"5px"+ " " +"10px")  + ";\n  background-color : " + ("#58c3e5")  + ";\n  background : " + ("-webkit-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-o-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-moz-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background-image : " + ("-ms-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : ") + (("medium")  + " !important;\n  color : " + ("white")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-containerPanel {\n  width : " + ("370px")  + ";\n  padding : " + ("2px"+ " " +"2px"+ " " +"0"+ " " +"0")  + ";\n  height : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-containerPanel img {\n  float : " + ("right")  + ";\n  margin-bottom : " + ("5px")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-title {\n  float : " + ("right")  + ";\n  margin-right : " + ("5px")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messagesOuter {\n  clear : " + ("right")  + ";\n  height : " + ("auto") ) + (";\n  width : " + ("360px")  + ";\n  max-height : " + ("70px")  + ";\n  min-height : " + ("20px")  + ";\n  overflow-x : " + ("hidden")  + ";\n  overflow-y : " + ("scroll")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messagesInner {\n  color : " + ("white")  + ";\n  height : " + ("auto")  + ";\n  width : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messageline {\n  white-space : " + ("pre")  + ";\n  overflow : " + ("scroll")  + ";\n  font-size : ") + (("smaller")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-close {\n  height : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("left")  + ";\n  margin-bottom : " + ("10px") ) + (";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-close:hover {\n  height : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-close:active:hover {\n  height : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n")) : ((".org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-popupPanel {\n  border : " + ("3px"+ " " +"solid"+ " " +"gray")  + " !important;\n  border-radius : " + ("15px")  + ";\n  padding : " + ("5px"+ " " +"10px"+ " " +"5px"+ " " +"10px")  + ";\n  background-color : " + ("#58c3e5")  + ";\n  background : " + ("-webkit-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-o-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("-moz-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background : " + ("linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  background-image : " + ("-ms-linear-gradient(" + "135deg"+ ","+ " " +"#58c3e5"+ ","+ " " +"#1e94d0" + ")")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : ") + (("medium")  + " !important;\n  color : " + ("white")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-containerPanel {\n  width : " + ("370px")  + ";\n  padding : " + ("2px"+ " " +"0"+ " " +"0"+ " " +"2px")  + ";\n  height : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-containerPanel img {\n  float : " + ("left")  + ";\n  margin-bottom : " + ("5px")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-title {\n  float : " + ("left")  + ";\n  margin-left : " + ("5px")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messagesOuter {\n  clear : " + ("left")  + ";\n  height : " + ("auto") ) + (";\n  width : " + ("360px")  + ";\n  max-height : " + ("70px")  + ";\n  min-height : " + ("20px")  + ";\n  overflow-x : " + ("hidden")  + ";\n  overflow-y : " + ("scroll")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messagesInner {\n  color : " + ("white")  + ";\n  height : " + ("auto")  + ";\n  width : " + ("100%")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messageline {\n  white-space : " + ("pre")  + ";\n  overflow : " + ("scroll")  + ";\n  font-size : ") + (("smaller")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-close {\n  height : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getHeight() + "px")  + ";\n  width : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getSafeUri().asString() + "\") -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getLeft() + "px -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeNormal()).getTop() + "px  no-repeat")  + ";\n  outline : " + ("none")  + ";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  cursor : " + ("pointer")  + ";\n  float : " + ("right")  + ";\n  margin-bottom : " + ("10px") ) + (";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-close:hover {\n  height : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getHeight() + "px")  + ";\n  width : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getSafeUri().asString() + "\") -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getLeft() + "px -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-close:active:hover {\n  height : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getHeight() + "px")  + ";\n  width : " + ((StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getSafeUri().asString() + "\") -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getLeft() + "px -" + (StatusReport_Resources_default_InlineClientBundleGenerator.this.closeClicked()).getTop() + "px  no-repeat")  + ";\n}\n"));
      }
      public java.lang.String close() {
        return "org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-close";
      }
      public java.lang.String containerPanel() {
        return "org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-containerPanel";
      }
      public java.lang.String messageline() {
        return "org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messageline";
      }
      public java.lang.String messagesInner() {
        return "org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messagesInner";
      }
      public java.lang.String messagesOuter() {
        return "org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-messagesOuter";
      }
      public java.lang.String popupPanel() {
        return "org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-popupPanel";
      }
      public java.lang.String title() {
        return "org-reactome-web-diagram-context-popups-StatusReport-ResourceCSS-title";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.context.popups.StatusReport.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.context.popups.StatusReport.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACdklEQVR42r1W3UsUURS/uc3uUz30VOAfUBL5B/R/SBAZZWs6jUhkQUFPFQa9RcWus3cGlW0hKEUlKPJNQeshMChMe+njTUQC2WaxTud3Z2e786UrG3vgsJd7z/mdu+fjd0eIXcQYkt05yx3OXnGms6bzmX83lfrraZzBRuxXskNOFwM4rNSkOvBpCtywZB87bOwDPNAN+O4KnjPlNd3poCnJMNNBcQYbfQ8YyTc3ZW/DyHJJ9NskLhRI5McoEwVhzQxKEn1FEhcLyjZn6YFlbzjnlnucD7YDAwAfu1Gm1x+/U395gcT5J9QxUFLA0AOXSwxeoDsv39PTt+t0eHhcBdIusQ1MLYAzHhwCyBgs0eTSGkG8nd/UMzZP4uwjMviWUKx77HkKRC6u0pGrE+xr/wvCmD64aZ/gDa9x+3yROm9VSJetao1OP5glceahUqy3ql7I5tTdKRKXivq/8IAtOHcjocLy7Q3O+8jz5RDAz181Onn7mVKsdbn+YlmlCL6hWjG24MVMtIAorDj3mEZfrYSAqpwuqC732Qa28MnFO21G1KcyGtnvnrxNE0vrlCY462AbBW4ltDJji/r4UzyI67chd8zcytcY+CzvCdVNRWWbMiubewfgrpr78K2FAC2mKNNEiloq8uheRU5qU0wpWq/ZNkVLo7WT2zQ6aDws3femIoPmJQxaOEjnzYoa0tigxanCVmMvF1YbzqCFGFUwfdR2/tRrsUZZvj1oJkYVaWR3iAms8u6LIjRMKQguIDsAgQAHyov05tMPOsrECJ9UsovTtePTNaiY2xDUHC0gKBzpVJSu6NpNp+u2PDhteTLb8uj/78+Wv8QQdIlXHbpRAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACbklEQVR42r1WS2tTQRS+bgRR0GqjxqRJBPG1sIuu3Ovaf9CNtNsurP4AuxBUjOZFXVRpF7pQpHTTRUFFcGGTO9c39S6sRIQK9UIjXaQ0Hs83zo0zN5OYgs2BA8PMOd+Zex7fXMfpIEdzYjBV9MZYZ1l91kCpr/bGYONsVVKT4nS64N1jAOpGpS37dAWeLoqL7LTaLbimq/D9F/gl3WmgwNoBdEDZGF/DGPa0FMVw06jk0cE7Lu29VaH+2y4lCnDUQTy515d1aV+2QodyrvT5G0gMG+CZfOUkH6yHBgAenHpHz6s1uvy0SntululIXkhgaJzXfQycXVyhJ58COj75Wvpol1gHpnZ7bzoEhzNu92gpIEi98YtG5pdp5/VFeUso1qO8F8qDjz/oxN03dDgn9HRNq7y7p1IFrx4exDglQ/ffky61eoMuPPZpx7VXUrGu1TcNm3MPl+gA+zYDMCawOYA3rhcpwV8Q4/xfffnNAPi50aCzMx+kYq3LBNsiZcm8iBTcG0dx5yKbMsCuG2Uqiu8G0AanC6oLbGALn3RLp4k5R02lGbn0J1XopLAWNsEZbGBrdlFTfUeNfsthhh3282fHuXALX9ZawBeW1+QZbDKltrMS9CTAdqdom4scbdOkmtKJLbQpWhoBErY2jQ4ahuU8D405aJuWQTODYDhjtkGLUgXGHWOP8Q9l1EIVI/Ofm+lCLUAv8byFKqJkl1Zkd4wJbNYPKFtekSmLa2QH4tvNBHjlWZVefK3Rmam30ifVjuxsdA0KBjAo2UbXSd7rV5SOTupI1+0eHIBEH5Tog5Ts9sHpyZPZk0f/f/+2/AaX3iLghaFcpQAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAACMklEQVR42r1WTUsbYRDen6O9+CMEkUqiaxOTHMylPXgQPFRv/Qv1roeC9aogXks0u5uKIhbsoUJaesh+5KPtkqJZDSkZ51l35d2vNAHNwMDyzswz7zvvzPOuJA2QnGZM5TRrLV8xD/KaWWW1Pa1iDTb4SKNKVq2/KGjmBwaiYRS+iBkKnHf2Oq+av4cFf1TEcOxA8IJmvBWDcp4mgcbZgZFU76JwZFpUDEod67RQNmhJjZTEXZtnW5p9XrFvIZDYKAZ3fmJNsKHjOwD4zWmdLu072vreprkjnbJCkgx/p48N2qtdk9ZyaPmz5cYIm+gAUyiNtSMGZ1WDlKZDkF6f6P03m2ZKurtLKL43r2zypdToUJGTZBTxlNbOQ2kUa5IXur5B5uOunDVIlM6/Pr27/EXTn2qu4htroqxftNxyCqfoAhtdsy7WF7WV2fHjz78BAIcBV8+brjoh8F32xV0shRuBsflyzcNwdyDBLJfhQL8JAPX6fVdFgQ985eDu/S47lLwJjXQJSoUgpeVQksAGHznURYJWJW/046aT0mWdL86gL3/uIuBYgw0+heRZsceS4HlL9PyXHG5TlIZbbneENkVLyzGU8tCmoUHDsGzw0Iw6aBhOlCoyaBGq4HHH2GP8fQEthKkC9NHzcoBWQC8ZNYYqksgOBFZp3dI+ExqITQwG8b1kAtz+0aav7a5LjAPJLo6uQcEABiUDMNwlPqWk2Gfxf3Q9lgdnLE/mWB79p/5tuQeTqeRBz8pWpQAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACsklEQVR42s1Xz0sbQRTOsT30P/AHmoLoQaMHvQmigqAnFaooil79Kzy24MlLA+3Zo3jqBmuQmjQG06LooSAhaNTaLDkktmZ3zWY63+IuMdPdzNsUyYMPltl53/d29s2bN4GApKmq+qpcLr8xTfN9pVJJcKgc+iPwnMA7zMHcwP8yTdNec+KPXOCPkc+zm+1t9mN9nX1fXWXJmRkLeMYY3mEO5sIHvr6FM5nMC07ylpMZ+XicpZaWmNLezj61tHgCczAXPvAFB7ioXx3kzsf3l5cstbhYV9QN8AUHuMApJW4YRog73P5SFLbb3e1KHunoYNGBARbt72dKW5vrPHCAC5zglvny2+zWlidpfGKCmaUSs618d8cORkbcfwvnAie4XVcim82+5BNOEK2XOHA0P89qLTk97Z0bnPNxJU6gJQTAk+Xd/cWF57LbQObX2tfJybp+4IYGtISthow9WliQSq7E1JQQQGxsTMoXGtB6skV5RB+wbWSzOzY+LgTwZXhY2h9a0HQqHApHanlZmgBitbY/OCi/PbkWNK2KyUvnHKqXTJGxsT80JASw19cn7Q8taEIbyx/+ubNDKjDRUEgIQCZ5qwFNaAf4UhyijlOcP/f0CAFEOjtJHNCENgLIfVtZITlHgkEhAKW1lcQBTWgjAD05O0tyhli1mZpGPiegCW1fAQAVXXcCeCgUGgqA/AuAh2LRCUDP5cj+1b+AnISArqpOAKWrK7K/k4R+tiFQur52AvidTpP9nW3opxAB6c1NVjw7s3C+sUFL4upC5KcUN4onpdjPYWS3W5lw2ILsKfrPw8jPcRwbHRWPYz7m+zimNiTHa2tCABjz3ZBQWzLcA2oNYw21ZJSmdLery8p+2wqnp9ZYQ00ptS0H9np7LdRbdum2vCkuJk1xNWuKy+lzXc//AmpUQ1hULl2IAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage3 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABsAAAAYCAYAAAALQIb7AAACdUlEQVR42rWVz2sTURDH4w9QRBGv/jp404sU1ItFDyI9CP0DFBQ86D+gXnNS8OhPFOtBxEtQDy2NVNSgB8WLhxaxmGo0ylYTXZa2SbrZXeNndBae8W2TJemDL2/yMjPfmdl58zKZlKter2/59XcFzWZzILNcKwzDU5DMtXQhfw6CYGhZyHC+2GpbnD3qO1G1Wt2AY99Cdr+vRJTqEE4/tSyrbySVSmV9FEXXtCEQoyu+7++U0iHfKpVKa3O53KrUHYbxdZy8A1PIF2mGI8gftFTvyW5/z9FDtB1nDngMwTFwEvkNCDWdS47jrOvQpcdRnQRPliTD2T2URrPZ7Er2CX5fLhaLayQr5LtdfM8hdGeNhglc192Y1M4uBofZn0kiogzJab1TP7S0ibA1DfZnksgWIDvA/kUjE8IJLc0cmF8CC2ozCzzwAKKrid8XhTwKIyp/l/IhrmB/zvmNLsp4UAjQn9EAvoEKqBHw8D/KzLfdEiEGN5H3YTzI74dygcE0Z3s6TJU8+GkrJ+dV/F6Q4E3CAf54qh24CMaEMP7gGJyXprEQTZpTBbmB7u02wjpnZ/+LslAorJauNFpa7tpXNZoiqL3sL6VLwTmzC1WnLHaWDJ1OFfqzPM/bhOM7bcaS/UeLUyuZduhImok/Yzh9K+VRWZritWJMdQvGma96XhqyokE2LvdT5XysY5uTMuri69E1mU6Vshq+MiKOFLKm4zIa51FcgbQvtczNMmiCF91+Mw1ktJc3bjDOtJtuZOBv7enF4G3b1eY0tHUpFTna8/PUaDR24OiEDOyECVKWq0NW2/r2otdqtc3aofPaFK685BKMqfcbBtKHLRzVKqkAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage4 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACoklEQVR42s2XS0sbURTHs2wX/RTtB+hX8UUWLiSIC8WF7gQFH/igIoJuVGhx0ShI2qK0i2Kjltj4opCYblwUYhioYwZsjUZnJpMc738wlxnymLnJUOfCgeHOOed37rn3nLkTCLgciqK8MAwjWCwWl0ul0iEThYn2KHg+xDvoQDfg1VBV9RVz/I4B8tfqNcXSMVr9uUoz32doeHvYFDxjDu+gA13YwLZhcDqdfsacvGHO9JScosndSWpfa6eW9y11BTrQhQ1s4QO+RFf9khknL28vaWJnwhFaS2ALH/AFn67guq6/ZgbysXRMnRudDcPLAh/wBZ/w7WblcvR3lNrCbU3DywJf8AnfNTMhSdJzpnCKaL2EW4N4zMQpWBUBsMMyK9/InqS93naAAVZFqeHEju+MewIKrgdrZhEMsGwlyiJ6i7LxAh6KhEjJK5T5m6GBzwNVdcACk3c4NA7UrhcBYJ/LI/MvU1UHLDDNjonWie7lpsk4yfyPebKOcCJcs1mBCTbSv7J/vt986j+EKKflOPxMOau7KDDBDrBUHKGPNxvAiXTC4feFe+rd7K2rDybYCCA7vTfdFHzxYNGW+qWjJUcbMMFGANrI9oijQd9WH/Vv9VfM93zqobye5/DEnwS1hlsd/YEJtqsABr8MmvurGRpN7U3xeYBSFykORyDdH7tdZc0agOMWJC+SHFIoFqhcsthH61iIL7jeNusWOB7Coa9D5uqtQUR+RcgoGXwO9S9ybvghdFuGo99GbUFYR07NmR1QJABehiKNCPumGmpFAHOxOSG4rRGJtuKx6JgtE/HzuHDZ2lpxIx8jXESv7q4oe5ulrkiXcAC2j1Gjn+OOtQ5TROFVP8dPfiHxxZXsyS+lvriW++LHxBe/Zr74Of1fv+cPFcTukyRFMwQAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage5 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACIAAAAgCAYAAAB3j6rJAAAFBUlEQVR42rWY+09bVRzAWYwmS9SocfqTMdlf4G/G+PjRROMvwIDBMIgiuImTjG2sEVKmDkF5hpGYYMiWQQctjzFYJwxRmUKi5V1algKl3BVmofS2tw/a296v3+8lrbdQ2o7WQ77pfZxz7ud8z/d1SEmJs3Ec95Lf7y8MBAIKQRBmUbZRfCgulHWU+/iuDvu8p9FonkxJdvP5fK/jR/pQeLPdDOpFNbRMtID8nhwuqC+A7GcZVP1aBdcmr8G4aRxcPhdgXwtCfeNwOF5MGAA1cAwnu+EX/DC6PAqld0oh9UZqTMlUZELd/TpYti4T0DbO8RlOd+RQEDzPv4GTmBcti3B24GxcAHslrT0Nmsebwel1EtCtzc3NZx4LgvaY9r1voQ9OdJw4FIRUCnsLYWV7hWA0drv9hXg18SYOcLdp2hIGkEpOZw7oLXqCmWAY5mhUCKfT+TJZf4+2J6kQQcntygXGzgDazE9RQbBD18I/C5Denv6/gJCUDJQAH+CBtv+gLXmLvKP4dnFSPnjx7kXReyK9U82raIv0SqXyiX0g+EI9sjSSFIirE1eBGsMyUNxfHNFeyJNQK5lhEG63+xUECZQMliQMUdRXBB7eA8FGnhep34B+gLQytNc2Pl9j1xKGoLgx92guBEGrLugpOHDrKFJbrdZnpduiHFwcTBik9a9WkLbasdoD+5JDUCoIM1oE0VLuSATiTP8Z8Pq9IQhKCbHG6Cw6cuVSKQgrH5EfGoJWR6kg2CxOi2iQscaNGccIpEEKwpfdLYu5/4oZBTT92bTvXft0ewhCwL+KexVxLWDYMEwgrVIQZ/lwedRBZP3B1jHTEXp+Xn0eKP4EG3lDvJqk7UOQZinIajTDItE+0oYZImmHQva6Yz30jGqVLEVW3CBT5ikCkUtBhjpnO6MOylflg9lhDoPZcm2FrgNCQHTJaHOcupkBOSjBe7Il9JqT0jhSSf4fawX53flgYk0QqVHYjjY2S5EO5qUa0Om+DpUG1Dwez6thpSAlojxlXkyYPFWeGLqljWqNWHVL2+9fArtyBWzG76FmqAiozMCdWIiUa/TXp67HtbeiZmy7mqEFxEoNuZ0ZYDXWAj//CXh1xcAYqnExJrKPskhV2aesh43L/0myb2ZD11yXWDTH6ts9XgrsUiUEJlMhMJUGdmMNeB0zHpZln98HotVqn0Kt6Pp1/UmtPwpUJ4E11QM/lweCGePNhgp8C0XgZJo8Fq386YNqkrfR+gOXf7mcNJDhvy+Bw1AuagO4eQDXsnjtMl7xcKa6ymhVmoyS0bk75xKG+KI3F+yoDf/sqV0Q+ySmZL14zc9/DNxavYczVB2LBtPMeTmQDckSApmYrQDXg7Jd2yCQrREA20To3rVcueN62NgSrXw9Qqc08gg6vR3mSHHp9keojTrwT2f9B7LRjRlRHbrn5z5ErTTwaEPHY51vPqCqftW2CtW/VYuJL14Qnf5b2FksDX2URGBaQVhXhD1zG77yupgGZcxzDlVQqJ3vEMhBIblX2wtUMuwNflQKnL51Ghr/aIR50yA41xohMJ0RDrLyAwirTWHP/DPZwJka/TZjw2txHbxsNttz9J8AKrJRODE8Y21qdVuB3WHFwCaWAYKw4nz4o2PnAWblmZwwAbsGjVW377nbUO7nTPWjj30upmMA5ojj6O7vINz7+PsupQk6+TPj9UfRGzY41Ei8QtrjTLXT/wI0WoAWySP9qgAAAABJRU5ErkJggg==";
  private static com.google.gwt.resources.client.ImageResource closeClicked;
  private static com.google.gwt.resources.client.ImageResource closeHovered;
  private static com.google.gwt.resources.client.ImageResource closeNormal;
  private static com.google.gwt.resources.client.ImageResource failure;
  private static com.google.gwt.resources.client.ImageResource icon;
  private static com.google.gwt.resources.client.ImageResource success;
  private static com.google.gwt.resources.client.ImageResource successWithWarnings;
  private static org.reactome.web.diagram.context.popups.StatusReport.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      closeClicked(), 
      closeHovered(), 
      closeNormal(), 
      failure(), 
      icon(), 
      success(), 
      successWithWarnings(), 
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
        resourceMap.put("failure", failure());
        resourceMap.put("icon", icon());
        resourceMap.put("success", success());
        resourceMap.put("successWithWarnings", successWithWarnings());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'closeClicked': return this.@org.reactome.web.diagram.context.popups.StatusReport.Resources::closeClicked()();
      case 'closeHovered': return this.@org.reactome.web.diagram.context.popups.StatusReport.Resources::closeHovered()();
      case 'closeNormal': return this.@org.reactome.web.diagram.context.popups.StatusReport.Resources::closeNormal()();
      case 'failure': return this.@org.reactome.web.diagram.context.popups.StatusReport.Resources::failure()();
      case 'icon': return this.@org.reactome.web.diagram.context.popups.StatusReport.Resources::icon()();
      case 'success': return this.@org.reactome.web.diagram.context.popups.StatusReport.Resources::success()();
      case 'successWithWarnings': return this.@org.reactome.web.diagram.context.popups.StatusReport.Resources::successWithWarnings()();
      case 'getCSS': return this.@org.reactome.web.diagram.context.popups.StatusReport.Resources::getCSS()();
    }
    return null;
  }-*/;
}
