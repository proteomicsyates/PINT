package org.reactome.web.diagram.controls.carousel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class Slide_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.carousel.Slide.Resources {
  private static Slide_Resources_default_InlineClientBundleGenerator _instance0 = new Slide_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.carousel.Slide.ResourceCSS() {
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
        return (".org-reactome-web-diagram-controls-carousel-Slide-ResourceCSS-caption {\n  position : " + ("absolute")  + ";\n  bottom : " + ("5px")  + ";\n  opacity : " + ("0.75")  + ";\n  -webkit-transition : " + ("opacity"+ " " +"0.5s"+ " " +"ease")  + ";\n  -moz-transition : " + ("opacity"+ " " +"0.5s"+ " " +"ease")  + ";\n  -o-transition : " + ("opacity"+ " " +"0.5s"+ " " +"ease")  + ";\n  transition : " + ("opacity"+ " " +"0.5s"+ " " +"ease")  + ";\n}\n.org-reactome-web-diagram-controls-carousel-Slide-ResourceCSS-caption:hover {\n  opacity : " + ("1")  + ";\n}\n");
      }
      public java.lang.String caption() {
        return "org-reactome-web-diagram-controls-carousel-Slide-ResourceCSS-caption";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.carousel.Slide.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.carousel.Slide.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.controls.carousel.Slide.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.diagram.controls.carousel.Slide.Resources::getCSS()();
    }
    return null;
  }-*/;
}
