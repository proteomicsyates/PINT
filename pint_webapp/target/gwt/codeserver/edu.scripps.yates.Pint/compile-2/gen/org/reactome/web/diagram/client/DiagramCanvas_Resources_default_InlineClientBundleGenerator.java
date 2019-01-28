package org.reactome.web.diagram.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class DiagramCanvas_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.client.DiagramCanvas.Resources {
  private static DiagramCanvas_Resources_default_InlineClientBundleGenerator _instance0 = new DiagramCanvas_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.client.DiagramCanvas.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-client-DiagramCanvas-ResourceCSS-watermark {\n  position : " + ("absolute")  + ";\n  bottom : " + ("5px")  + ";\n  left : " + ("80px")  + ";\n}\n.org-reactome-web-diagram-client-DiagramCanvas-ResourceCSS-watermark img {\n  width : " + ("auto")  + ";\n  height : " + ("25px")  + ";\n  opacity : " + ("0.5")  + ";\n  -webkit-transition : " + ("height"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("height"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  transition : " + ("height"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n}\n.org-reactome-web-diagram-client-DiagramCanvas-ResourceCSS-watermark:hover img {\n  height : " + ("32px")  + ";\n  opacity : ") + (("0.9")  + ";\n}\n")) : ((".org-reactome-web-diagram-client-DiagramCanvas-ResourceCSS-watermark {\n  position : " + ("absolute")  + ";\n  bottom : " + ("5px")  + ";\n  right : " + ("80px")  + ";\n}\n.org-reactome-web-diagram-client-DiagramCanvas-ResourceCSS-watermark img {\n  width : " + ("auto")  + ";\n  height : " + ("25px")  + ";\n  opacity : " + ("0.5")  + ";\n  -webkit-transition : " + ("height"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  -moz-transition : " + ("height"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n  transition : " + ("height"+ " " +"0.3s"+ " " +"ease-in-out"+ ","+ " " +"opacity"+ " " +"0.3s"+ " " +"ease-in-out")  + ";\n}\n.org-reactome-web-diagram-client-DiagramCanvas-ResourceCSS-watermark:hover img {\n  height : " + ("32px")  + ";\n  opacity : ") + (("0.9")  + ";\n}\n"));
      }
      public java.lang.String watermark() {
        return "org-reactome-web-diagram-client-DiagramCanvas-ResourceCSS-watermark";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.client.DiagramCanvas.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.client.DiagramCanvas.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void logoInitializer() {
    logo = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "logo",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 391, 150, false, false
    );
  }
  private static class logoInitializer {
    static {
      _instance0.logoInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return logo;
    }
  }
  public com.google.gwt.resources.client.ImageResource logo() {
    return logoInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.client.DiagramCanvas.ResourceCSS getCSS;
  private static final java.lang.String externalImage = "data:image/png;base64,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";
  private static com.google.gwt.resources.client.ImageResource logo;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      logo(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("logo", logo());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.client.DiagramCanvas.Resources::getCSS()();
      case 'logo': return this.@org.reactome.web.diagram.client.DiagramCanvas.Resources::logo()();
    }
    return null;
  }-*/;
}
