package org.reactome.web.diagram.controls.carousel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class CarouselPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.carousel.CarouselPanel.Resources {
  private static CarouselPanel_Resources_default_InlineClientBundleGenerator _instance0 = new CarouselPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.carousel.CarouselPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-carouselPanel {\n  overflow : " + ("hidden")  + ";\n  border-radius : " + ("4px")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-sliderOuterPanel {\n  display : " + ("block")  + ";\n  overflow : " + ("hidden")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-sliderPanel {\n  -webkit-transition : " + ("margin"+ " " +"0.5s"+ " " +"ease")  + ";\n  -moz-transition : " + ("margin"+ " " +"0.5s"+ " " +"ease")  + ";\n  -o-transition : " + ("margin"+ " " +"0.5s"+ " " +"ease")  + ";\n  transition : " + ("margin"+ " " +"0.5s"+ " " +"ease")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-buttonsPanel {\n  width : " + ("50%")  + ";\n  text-align : " + ("center")  + ";\n  margin-right : ") + (("auto")  + ";\n  margin-left : " + ("auto")  + ";\n  margin-top : " + ("5px")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn {\n  margin-top : " + ("5px")  + ";\n  margin-right : " + ("5px")  + ";\n  width : " + ("24px")  + ";\n  height : " + ("24px")  + ";\n  padding : " + ("0"+ " " +"0"+ " " +"0"+ " " +"3px")  + ";\n  float : " + ("right")  + ";\n  background-color : " + ("#177dc0")  + ";\n  border-radius : " + ("12px") ) + (";\n  border : " + ("none")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn:hover {\n  background-color : " + ("#07558b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn:active:hover {\n  background-color : " + ("#00468b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn:disabled, .org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn:active:disabled {\n  background-color : " + ("darkgray")  + ";\n  cursor : " + ("default")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn {\n  margin-top : " + ("5px")  + ";\n  margin-left : " + ("5px")  + ";\n  width : " + ("24px")  + ";\n  height : ") + (("24px")  + ";\n  padding : " + ("0"+ " " +"3px"+ " " +"0"+ " " +"0")  + ";\n  float : " + ("left")  + ";\n  background-color : " + ("#177dc0")  + ";\n  border-radius : " + ("12px")  + ";\n  border : " + ("none")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn:hover {\n  background-color : " + ("#07558b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn:active:hover {\n  background-color : " + ("#00468b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn:disabled, .org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn:active:disabled {\n  background-color : " + ("darkgray") ) + (";\n  cursor : " + ("default")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-circleBtn {\n  width : " + ("12px")  + ";\n  height : " + ("12px")  + ";\n  margin-right : " + ("2px")  + ";\n  background-color : " + ("#177dc0")  + ";\n  border-radius : " + ("6px")  + ";\n  border : " + ("none")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-circleBtn:hover {\n  background-color : " + ("#07558b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-circleBtnSelected {\n  background-color : ") + (("#86cee5")  + ";\n}\n")) : ((".org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-carouselPanel {\n  overflow : " + ("hidden")  + ";\n  border-radius : " + ("4px")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-sliderOuterPanel {\n  display : " + ("block")  + ";\n  overflow : " + ("hidden")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-sliderPanel {\n  -webkit-transition : " + ("margin"+ " " +"0.5s"+ " " +"ease")  + ";\n  -moz-transition : " + ("margin"+ " " +"0.5s"+ " " +"ease")  + ";\n  -o-transition : " + ("margin"+ " " +"0.5s"+ " " +"ease")  + ";\n  transition : " + ("margin"+ " " +"0.5s"+ " " +"ease")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-buttonsPanel {\n  width : " + ("50%")  + ";\n  text-align : " + ("center")  + ";\n  margin-left : ") + (("auto")  + ";\n  margin-right : " + ("auto")  + ";\n  margin-top : " + ("5px")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn {\n  margin-top : " + ("5px")  + ";\n  margin-left : " + ("5px")  + ";\n  width : " + ("24px")  + ";\n  height : " + ("24px")  + ";\n  padding : " + ("0"+ " " +"3px"+ " " +"0"+ " " +"0")  + ";\n  float : " + ("left")  + ";\n  background-color : " + ("#177dc0")  + ";\n  border-radius : " + ("12px") ) + (";\n  border : " + ("none")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn:hover {\n  background-color : " + ("#07558b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn:active:hover {\n  background-color : " + ("#00468b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn:disabled, .org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn:active:disabled {\n  background-color : " + ("darkgray")  + ";\n  cursor : " + ("default")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn {\n  margin-top : " + ("5px")  + ";\n  margin-right : " + ("5px")  + ";\n  width : " + ("24px")  + ";\n  height : ") + (("24px")  + ";\n  padding : " + ("0"+ " " +"0"+ " " +"0"+ " " +"3px")  + ";\n  float : " + ("right")  + ";\n  background-color : " + ("#177dc0")  + ";\n  border-radius : " + ("12px")  + ";\n  border : " + ("none")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn:hover {\n  background-color : " + ("#07558b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn:active:hover {\n  background-color : " + ("#00468b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn:disabled, .org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn:active:disabled {\n  background-color : " + ("darkgray") ) + (";\n  cursor : " + ("default")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-circleBtn {\n  width : " + ("12px")  + ";\n  height : " + ("12px")  + ";\n  margin-left : " + ("2px")  + ";\n  background-color : " + ("#177dc0")  + ";\n  border-radius : " + ("6px")  + ";\n  border : " + ("none")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-circleBtn:hover {\n  background-color : " + ("#07558b")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-circleBtnSelected {\n  background-color : ") + (("#86cee5")  + ";\n}\n"));
      }
      public java.lang.String buttonsPanel() {
        return "org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-buttonsPanel";
      }
      public java.lang.String carouselPanel() {
        return "org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-carouselPanel";
      }
      public java.lang.String circleBtn() {
        return "org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-circleBtn";
      }
      public java.lang.String circleBtnSelected() {
        return "org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-circleBtnSelected";
      }
      public java.lang.String leftBtn() {
        return "org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-leftBtn";
      }
      public java.lang.String rightBtn() {
        return "org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-rightBtn";
      }
      public java.lang.String sliderOuterPanel() {
        return "org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-sliderOuterPanel";
      }
      public java.lang.String sliderPanel() {
        return "org-reactome-web-diagram-controls-carousel-CarouselPanel-ResourceCSS-sliderPanel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.carousel.CarouselPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.carousel.CarouselPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void leftIconInitializer() {
    leftIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "leftIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 12, 16, false, false
    );
  }
  private static class leftIconInitializer {
    static {
      _instance0.leftIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return leftIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource leftIcon() {
    return leftIconInitializer.get();
  }
  private void rightIconInitializer() {
    rightIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "rightIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 12, 16, false, false
    );
  }
  private static class rightIconInitializer {
    static {
      _instance0.rightIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return rightIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource rightIcon() {
    return rightIconInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.controls.carousel.CarouselPanel.ResourceCSS getCSS;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAQCAYAAAAiYZ4HAAAAzklEQVR42mNgIBJ8/fpV8t+/f2uJUcv458+fJKDid/+BAK/KHz9+KAMV7v2PBLAqXLVqFfPfv39LgIq/whQC2R+BYlkYin/+/KkLlDyFbCqQv/Xbt29yKApv377NDjShESj5E0nhK6D7ozBM/fXrlwVQ8gqyqUDNSz9//iyKoRgo0QlU/BfJ1K9AU31whgRQQxuahu9ADb54g+/379/WQIXX0Jy0+NOnTyI4NUE93QzU+Iugp9GCVY+oYMUScUVAhV8IRhxZSYPsxIcteQMArS8/6dQaBBoAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAQCAYAAAAiYZ4HAAAAvUlEQVR42mP49+/f2q9fv0oyEAv+AwFQ07s/f/4kAbmMRGmAAaDGvT9+/FDGq+Hv379ZQIUfkTR9BYqVrFq1ihmnpm/fvskBFW5Fs+3Uz58/dfHaBvRHFFDhKyRNP4G2Nd6+fZsdp6bPnz+LAhUtRbPtyq9fvywI2eYD8g+SJqA5fzvxafAFKvqOpqENQ+GnT59EgBKL0Zx07ffv39bEePoXUHMzhqfxBKseMRH3BShWhDPiSE4aJCc+UpM3AGQqP+lqVCOSAAAAAElFTkSuQmCC";
  private static com.google.gwt.resources.client.ImageResource leftIcon;
  private static com.google.gwt.resources.client.ImageResource rightIcon;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      leftIcon(), 
      rightIcon(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("leftIcon", leftIcon());
        resourceMap.put("rightIcon", rightIcon());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.controls.carousel.CarouselPanel.Resources::getCSS()();
      case 'leftIcon': return this.@org.reactome.web.diagram.controls.carousel.CarouselPanel.Resources::leftIcon()();
      case 'rightIcon': return this.@org.reactome.web.diagram.controls.carousel.CarouselPanel.Resources::rightIcon()();
    }
    return null;
  }-*/;
}
