package org.reactome.web.fireworks.controls.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class ExpandibleContainer_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.controls.common.ExpandibleContainer.Resources {
  private static ExpandibleContainer_Resources_default_InlineClientBundleGenerator _instance0 = new ExpandibleContainer_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.controls.common.ExpandibleContainer.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-container {\n  position : " + ("relative")  + ";\n  width : " + ("40px")  + ";\n  float : " + ("right")  + ";\n  margin-left : " + ("2px")  + ";\n  padding-bottom : " + ("7px")  + ";\n  height : " + ("35px")  + ";\n  -webkit-transition : " + ("all"+ " " +"0.3s"+ " " +"linear")  + ";\n  -moz-transition : " + ("all"+ " " +"0.3s"+ " " +"linear")  + ";\n  -o-transition : " + ("all"+ " " +"0.3s"+ " " +"linear")  + ";\n  transition : " + ("all"+ " " +"0.3s"+ " " +"linear")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-container:hover {\n  height : ") + (("80px")  + ";\n  -webkit-transition : " + ("all"+ " " +"0.1s"+ " " +"linear")  + ";\n  -moz-transition : " + ("all"+ " " +"0.1s"+ " " +"linear")  + ";\n  -o-transition : " + ("all"+ " " +"0.1s"+ " " +"linear")  + ";\n  transition : " + ("all"+ " " +"0.1s"+ " " +"linear")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-baseButtons {\n  display : " + ("block")  + ";\n  margin : " + ("10px"+ " " +"auto"+ " " +"0")  + ";\n  position : " + ("relative")  + ";\n  -webkit-transition : " + ("all"+ " " +"0.2s"+ " " +"ease-out")  + ";\n  -moz-transition : " + ("all"+ " " +"0.2s"+ " " +"ease-out")  + ";\n  -o-transition : " + ("all"+ " " +"0.2s"+ " " +"ease-out") ) + (";\n  transition : " + ("all"+ " " +"0.2s"+ " " +"ease-out")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-optionButtons {\n  margin : " + ("5px"+ " " +"auto"+ " " +"0")  + ";\n  opacity : " + ("0")  + ";\n  -webkit-transform : " + ("translateY(" + "-50px" + ")")  + ";\n  -ms-transform : " + ("translateY(" + "-50px" + ")")  + ";\n  transform : " + ("translateY(" + "-50px" + ")")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-container:hover .org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-optionButtons {\n  opacity : " + ("1")  + ";\n  -webkit-transform : " + ("none")  + ";\n  -ms-transform : " + ("none")  + ";\n  transform : " + ("none")  + ";\n  margin : ") + (("8px"+ " " +"auto"+ " " +"0")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-primaryButton {\n  margin : " + ("0"+ " " +"auto"+ " " +"0")  + ";\n  position : " + ("relative")  + ";\n  z-index : " + ("1")  + ";\n}\n")) : ((".org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-container {\n  position : " + ("relative")  + ";\n  width : " + ("40px")  + ";\n  float : " + ("left")  + ";\n  margin-right : " + ("2px")  + ";\n  padding-bottom : " + ("7px")  + ";\n  height : " + ("35px")  + ";\n  -webkit-transition : " + ("all"+ " " +"0.3s"+ " " +"linear")  + ";\n  -moz-transition : " + ("all"+ " " +"0.3s"+ " " +"linear")  + ";\n  -o-transition : " + ("all"+ " " +"0.3s"+ " " +"linear")  + ";\n  transition : " + ("all"+ " " +"0.3s"+ " " +"linear")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-container:hover {\n  height : ") + (("80px")  + ";\n  -webkit-transition : " + ("all"+ " " +"0.1s"+ " " +"linear")  + ";\n  -moz-transition : " + ("all"+ " " +"0.1s"+ " " +"linear")  + ";\n  -o-transition : " + ("all"+ " " +"0.1s"+ " " +"linear")  + ";\n  transition : " + ("all"+ " " +"0.1s"+ " " +"linear")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-baseButtons {\n  display : " + ("block")  + ";\n  margin : " + ("10px"+ " " +"auto"+ " " +"0")  + ";\n  position : " + ("relative")  + ";\n  -webkit-transition : " + ("all"+ " " +"0.2s"+ " " +"ease-out")  + ";\n  -moz-transition : " + ("all"+ " " +"0.2s"+ " " +"ease-out")  + ";\n  -o-transition : " + ("all"+ " " +"0.2s"+ " " +"ease-out") ) + (";\n  transition : " + ("all"+ " " +"0.2s"+ " " +"ease-out")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-optionButtons {\n  margin : " + ("5px"+ " " +"auto"+ " " +"0")  + ";\n  opacity : " + ("0")  + ";\n  -webkit-transform : " + ("translateY(" + "-50px" + ")")  + ";\n  -ms-transform : " + ("translateY(" + "-50px" + ")")  + ";\n  transform : " + ("translateY(" + "-50px" + ")")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-container:hover .org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-optionButtons {\n  opacity : " + ("1")  + ";\n  -webkit-transform : " + ("none")  + ";\n  -ms-transform : " + ("none")  + ";\n  transform : " + ("none")  + ";\n  margin : ") + (("8px"+ " " +"auto"+ " " +"0")  + ";\n}\n.org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-primaryButton {\n  margin : " + ("0"+ " " +"auto"+ " " +"0")  + ";\n  position : " + ("relative")  + ";\n  z-index : " + ("1")  + ";\n}\n"));
      }
      public java.lang.String baseButtons() {
        return "org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-baseButtons";
      }
      public java.lang.String container() {
        return "org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-container";
      }
      public java.lang.String optionButtons() {
        return "org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-optionButtons";
      }
      public java.lang.String primaryButton() {
        return "org-reactome-web-fireworks-controls-common-ExpandibleContainer-ResourceCSS-primaryButton";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.controls.common.ExpandibleContainer.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.controls.common.ExpandibleContainer.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.controls.common.ExpandibleContainer.ResourceCSS getCSS;
  
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
      case 'getCSS': return this.@org.reactome.web.fireworks.controls.common.ExpandibleContainer.Resources::getCSS()();
    }
    return null;
  }-*/;
}
