package org.reactome.web.diagram.util.slider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class Slider_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.util.slider.Slider.Resources {
  private static Slider_Resources_default_InlineClientBundleGenerator _instance0 = new Slider_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.util.slider.Slider.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-util-slider-Slider-ResourceCSS-slider {\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-diagram-util-slider-Slider-ResourceCSS-sliderValueBox {\n  width : " + ("23px")  + ";\n  height : " + ("17px")  + ";\n  margin-right : " + ("5px")  + ";\n  margin-top : " + ("3px")  + ";\n  float : " + ("left")  + ";\n  background-color : " + ("#6e6e6e")  + ";\n  color : " + ("#fff")  + ";\n  border-radius : " + ("3px")  + ";\n  border : " + ("none")  + ";\n  outline : ") + (("none")  + ";\n}\n")) : ((".org-reactome-web-diagram-util-slider-Slider-ResourceCSS-slider {\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-diagram-util-slider-Slider-ResourceCSS-sliderValueBox {\n  width : " + ("23px")  + ";\n  height : " + ("17px")  + ";\n  margin-left : " + ("5px")  + ";\n  margin-top : " + ("3px")  + ";\n  float : " + ("right")  + ";\n  background-color : " + ("#6e6e6e")  + ";\n  color : " + ("#fff")  + ";\n  border-radius : " + ("3px")  + ";\n  border : " + ("none")  + ";\n  outline : ") + (("none")  + ";\n}\n"));
      }
      public java.lang.String slider() {
        return "org-reactome-web-diagram-util-slider-Slider-ResourceCSS-slider";
      }
      public java.lang.String sliderValueBox() {
        return "org-reactome-web-diagram-util-slider-Slider-ResourceCSS-sliderValueBox";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.util.slider.Slider.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.util.slider.Slider.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.util.slider.Slider.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.util.slider.Slider.Resources::getCSS()();
    }
    return null;
  }-*/;
}
