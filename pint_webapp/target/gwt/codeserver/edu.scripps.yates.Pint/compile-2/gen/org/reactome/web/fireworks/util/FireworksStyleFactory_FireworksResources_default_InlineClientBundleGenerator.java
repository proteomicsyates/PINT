package org.reactome.web.fireworks.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class FireworksStyleFactory_FireworksResources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.util.FireworksStyleFactory.FireworksResources {
  private static FireworksStyleFactory_FireworksResources_default_InlineClientBundleGenerator _instance0 = new FireworksStyleFactory_FireworksResources_default_InlineClientBundleGenerator();
  private void fireworksStyleInitializer() {
    fireworksStyle = new org.reactome.web.fireworks.util.FireworksStyleFactory.FireworksStyle() {
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
        return "fireworksStyle";
      }
      public String getText() {
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubble {\n  display : " + ("inline-block")  + ";\n  color : " + ("#4b4b4b")  + ";\n  position : " + ("relative")  + ";\n  padding : " + ("5px")  + ";\n  background : " + ("rgba(" + "200"+ ","+ " " +"255"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  border : " + ("rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")"+ " " +"solid"+ " " +"1px")  + ";\n  -webkit-border-radius : " + ("5px")  + ";\n  -moz-border-radius : " + ("5px")  + ";\n  border-radius : " + ("5px")  + ";\n  -webkit-box-shadow : " + ("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  -moz-box-shadow : ") + (("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  box-shadow : " + ("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  page-break-inside : " + ("avoid")  + ";\n  overflow : " + ("visible")  + ";\n}\n.org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleTopLeft:before {\n  content : " + ("\"\"")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("-6px")  + ";\n  right : " + ("6px")  + ";\n  border-right : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-left : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-bottom : " + ("6px"+ " " +"solid"+ " " +"rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")") ) + (";\n  display : " + ("block")  + ";\n  width : " + ("0")  + ";\n  z-index : " + ("1")  + ";\n  opacity : " + ("0.75")  + ";\n}\n.org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleTopRight:before {\n  content : " + ("\"\"")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("-6px")  + ";\n  left : " + ("6px")  + ";\n  border-right : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-left : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-bottom : ") + (("6px"+ " " +"solid"+ " " +"rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  display : " + ("block")  + ";\n  width : " + ("0")  + ";\n  z-index : " + ("1")  + ";\n  opacity : " + ("0.75")  + ";\n}\n.org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleBottomLeft:before {\n  content : " + ("\"\"")  + ";\n  position : " + ("absolute")  + ";\n  bottom : " + ("-6px")  + ";\n  right : " + ("6px")  + ";\n  border-right : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-left : " + ("6px"+ " " +"solid"+ " " +"transparent") ) + (";\n  border-top : " + ("6px"+ " " +"solid"+ " " +"rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  display : " + ("block")  + ";\n  width : " + ("0")  + ";\n  z-index : " + ("1")  + ";\n  opacity : " + ("0.75")  + ";\n}\n.org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleBottomRight:before {\n  content : " + ("\"\"")  + ";\n  position : " + ("absolute")  + ";\n  bottom : " + ("-6px")  + ";\n  left : " + ("6px")  + ";\n  border-right : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-left : ") + (("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-top : " + ("6px"+ " " +"solid"+ " " +"rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  display : " + ("block")  + ";\n  width : " + ("0")  + ";\n  z-index : " + ("1")  + ";\n  opacity : " + ("0.75")  + ";\n}\n")) : ((".org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubble {\n  display : " + ("inline-block")  + ";\n  color : " + ("#4b4b4b")  + ";\n  position : " + ("relative")  + ";\n  padding : " + ("5px")  + ";\n  background : " + ("rgba(" + "200"+ ","+ " " +"255"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  border : " + ("rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")"+ " " +"solid"+ " " +"1px")  + ";\n  -webkit-border-radius : " + ("5px")  + ";\n  -moz-border-radius : " + ("5px")  + ";\n  border-radius : " + ("5px")  + ";\n  -webkit-box-shadow : " + ("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  -moz-box-shadow : ") + (("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  box-shadow : " + ("2px"+ " " +"2px"+ " " +"10px"+ " " +"0"+ " " +"rgba(" + "97"+ ","+ " " +"97"+ ","+ " " +"97"+ ","+ " " +"0.78" + ")")  + ";\n  page-break-inside : " + ("avoid")  + ";\n  overflow : " + ("visible")  + ";\n}\n.org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleTopLeft:before {\n  content : " + ("\"\"")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("-6px")  + ";\n  left : " + ("6px")  + ";\n  border-left : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-right : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-bottom : " + ("6px"+ " " +"solid"+ " " +"rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")") ) + (";\n  display : " + ("block")  + ";\n  width : " + ("0")  + ";\n  z-index : " + ("1")  + ";\n  opacity : " + ("0.75")  + ";\n}\n.org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleTopRight:before {\n  content : " + ("\"\"")  + ";\n  position : " + ("absolute")  + ";\n  top : " + ("-6px")  + ";\n  right : " + ("6px")  + ";\n  border-left : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-right : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-bottom : ") + (("6px"+ " " +"solid"+ " " +"rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  display : " + ("block")  + ";\n  width : " + ("0")  + ";\n  z-index : " + ("1")  + ";\n  opacity : " + ("0.75")  + ";\n}\n.org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleBottomLeft:before {\n  content : " + ("\"\"")  + ";\n  position : " + ("absolute")  + ";\n  bottom : " + ("-6px")  + ";\n  left : " + ("6px")  + ";\n  border-left : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-right : " + ("6px"+ " " +"solid"+ " " +"transparent") ) + (";\n  border-top : " + ("6px"+ " " +"solid"+ " " +"rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  display : " + ("block")  + ";\n  width : " + ("0")  + ";\n  z-index : " + ("1")  + ";\n  opacity : " + ("0.75")  + ";\n}\n.org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleBottomRight:before {\n  content : " + ("\"\"")  + ";\n  position : " + ("absolute")  + ";\n  bottom : " + ("-6px")  + ";\n  right : " + ("6px")  + ";\n  border-left : " + ("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-right : ") + (("6px"+ " " +"solid"+ " " +"transparent")  + ";\n  border-top : " + ("6px"+ " " +"solid"+ " " +"rgba(" + "0"+ ","+ " " +"0"+ ","+ " " +"255"+ ","+ " " +"0.5" + ")")  + ";\n  display : " + ("block")  + ";\n  width : " + ("0")  + ";\n  z-index : " + ("1")  + ";\n  opacity : " + ("0.75")  + ";\n}\n"));
      }
      public java.lang.String bubble() {
        return "org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubble";
      }
      public java.lang.String bubbleBottomLeft() {
        return "org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleBottomLeft";
      }
      public java.lang.String bubbleBottomRight() {
        return "org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleBottomRight";
      }
      public java.lang.String bubbleTopLeft() {
        return "org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleTopLeft";
      }
      public java.lang.String bubbleTopRight() {
        return "org-reactome-web-fireworks-util-FireworksStyleFactory-FireworksStyle-bubbleTopRight";
      }
    }
    ;
  }
  private static class fireworksStyleInitializer {
    static {
      _instance0.fireworksStyleInitializer();
    }
    static org.reactome.web.fireworks.util.FireworksStyleFactory.FireworksStyle get() {
      return fireworksStyle;
    }
  }
  public org.reactome.web.fireworks.util.FireworksStyleFactory.FireworksStyle fireworksStyle() {
    return fireworksStyleInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.util.FireworksStyleFactory.FireworksStyle fireworksStyle;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      fireworksStyle(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("fireworksStyle", fireworksStyle());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'fireworksStyle': return this.@org.reactome.web.fireworks.util.FireworksStyleFactory.FireworksResources::fireworksStyle()();
    }
    return null;
  }-*/;
}
