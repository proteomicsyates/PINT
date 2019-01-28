package org.reactome.web.diagram.controls.settings.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class InfoLabel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.controls.settings.common.InfoLabel.Resources {
  private static InfoLabel_Resources_default_InlineClientBundleGenerator _instance0 = new InfoLabel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.controls.settings.common.InfoLabel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabel {\n  font-size : " + ("medium")  + " !important;\n  margin-top : " + ("5px")  + ";\n  font-weight : " + ("bold")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  background-color : " + ("#1e94d0")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  text-align : " + ("center")  + ";\n  padding-bottom : " + ("2px")  + ";\n  max-height : " + ("23px")  + ";\n  overflow : ") + (("hidden")  + ";\n  -webkit-transition : " + ("max-height"+ " " +"0.3s"+ " " +"ease")  + ";\n  -moz-transition : " + ("max-height"+ " " +"0.3s"+ " " +"ease")  + ";\n  -o-transition : " + ("max-height"+ " " +"0.3s"+ " " +"ease")  + ";\n  transition : " + ("max-height"+ " " +"0.3s"+ " " +"ease")  + ";\n}\n.org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabelExpanded {\n  height : " + ("auto")  + " !important;\n  max-height : " + ("500px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabelBtn {\n  height : " + ((InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getHeight() + "px")  + ";\n  width : " + ((InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getSafeUri().asString() + "\") -" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getLeft() + "px -" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getTop() + "px  no-repeat") ) + (";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  float : " + ("left")  + ";\n  margin-top : " + ("5px")  + ";\n  margin-left : " + ("8px")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabelBtn:hover {\n  height : " + ((InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getHeight() + "px")  + ";\n  width : " + ((InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : ") + (("url(\"" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getSafeUri().asString() + "\") -" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getLeft() + "px -" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoText {\n  margin-top : " + ("6px")  + ";\n  font-size : " + ("small")  + " !important;\n  padding : " + ("0"+ " " +"10px"+ " " +"0"+ " " +"10px")  + ";\n  height : " + ("auto")  + ";\n  text-align : " + ("justify")  + ";\n  font-style : " + ("italic")  + ";\n}\n")) : ((".org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabel {\n  font-size : " + ("medium")  + " !important;\n  margin-top : " + ("5px")  + ";\n  font-weight : " + ("bold")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  background-color : " + ("#1e94d0")  + ";\n  border-radius : " + ("15px")  + ";\n  color : " + ("white")  + ";\n  text-align : " + ("center")  + ";\n  padding-bottom : " + ("2px")  + ";\n  max-height : " + ("23px")  + ";\n  overflow : ") + (("hidden")  + ";\n  -webkit-transition : " + ("max-height"+ " " +"0.3s"+ " " +"ease")  + ";\n  -moz-transition : " + ("max-height"+ " " +"0.3s"+ " " +"ease")  + ";\n  -o-transition : " + ("max-height"+ " " +"0.3s"+ " " +"ease")  + ";\n  transition : " + ("max-height"+ " " +"0.3s"+ " " +"ease")  + ";\n}\n.org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabelExpanded {\n  height : " + ("auto")  + " !important;\n  max-height : " + ("500px")  + ";\n}\n.org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabelBtn {\n  height : " + ((InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getHeight() + "px")  + ";\n  width : " + ((InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getSafeUri().asString() + "\") -" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getLeft() + "px -" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoNormal()).getTop() + "px  no-repeat") ) + (";\n  background-repeat : " + ("no-repeat")  + ";\n  border : " + ("0")  + ";\n  float : " + ("right")  + ";\n  margin-top : " + ("5px")  + ";\n  margin-right : " + ("8px")  + ";\n  outline : " + ("none")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabelBtn:hover {\n  height : " + ((InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getHeight() + "px")  + ";\n  width : " + ((InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : ") + (("url(\"" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getSafeUri().asString() + "\") -" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getLeft() + "px -" + (InfoLabel_Resources_default_InlineClientBundleGenerator.this.infoHovered()).getTop() + "px  no-repeat")  + ";\n}\n.org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoText {\n  margin-top : " + ("6px")  + ";\n  font-size : " + ("small")  + " !important;\n  padding : " + ("0"+ " " +"10px"+ " " +"0"+ " " +"10px")  + ";\n  height : " + ("auto")  + ";\n  text-align : " + ("justify")  + ";\n  font-style : " + ("italic")  + ";\n}\n"));
      }
      public java.lang.String infoLabel() {
        return "org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabel";
      }
      public java.lang.String infoLabelBtn() {
        return "org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabelBtn";
      }
      public java.lang.String infoLabelExpanded() {
        return "org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoLabelExpanded";
      }
      public java.lang.String infoText() {
        return "org-reactome-web-diagram-controls-settings-common-InfoLabel-ResourceCSS-infoText";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.controls.settings.common.InfoLabel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.controls.settings.common.InfoLabel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void infoHoveredInitializer() {
    infoHovered = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "infoHovered",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 16, 16, false, false
    );
  }
  private static class infoHoveredInitializer {
    static {
      _instance0.infoHoveredInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return infoHovered;
    }
  }
  public com.google.gwt.resources.client.ImageResource infoHovered() {
    return infoHoveredInitializer.get();
  }
  private void infoNormalInitializer() {
    infoNormal = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "infoNormal",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 16, 16, false, false
    );
  }
  private static class infoNormalInitializer {
    static {
      _instance0.infoNormalInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return infoNormal;
    }
  }
  public com.google.gwt.resources.client.ImageResource infoNormal() {
    return infoNormalInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.controls.settings.common.InfoLabel.ResourceCSS getCSS;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABHklEQVR42qVTvU7DQAw+NvokrDwBT9OZF+ABOrGhAlNZy4JyVyYG+gQslVAlhkg93w1dKpDoT+yk+KI2SoLTRupJn+TLZ39nO7ZStfMCyWXk6d4ATo2jVQ62w7fAqabzbG1HAz1yQMbY7vBlIP0o3bPgE3z/BRugcclxO3IUD+L4nOkzzuCtzAXfisjI00PFgaEhnYTgnAca1vkQU9RcS7sENMbTE9upwGd5TzidvkBuwqsa8Caa4VVk6U58gGPVrtt1cmlscr0vMQK8lQVwqthYy+nT91EBjj0k8NtOQC6hnUBegtzElgLcxAO/8ZhAVoy2NEiMxV5AO+w1DpI0ytrRXNukW2Tg1hfa42vjKAvL9KM9vVfg8LNxmU5Z5z/ua7fiRY3DWgAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAA9klEQVR42qVTOQ7CMBA0HbyElhfwGmo+wAOo6BBHR0uNqCjIC2iQkCUKinQ0CCTA5LCZjUJkFjtEykir2J7Zsb1ZC8EQRVEnTdOp1loinnlIWiNO+BCGYQuiOcTa5MDwiNhZc00a0v4kgwuMBcxPQBN0A+MN44IvE7jODANEe0rO+SXnKae4s31sZrKCcEEnd3A6qwkEEwf5ol0RgziOu/iOXRtQrqAKOwweIPvWFUeeE0oyUB7yWsFAlRncqxrIGgbSWcSqBlkRfb/xn0HxG0sa6WIZDL2N5GpljM9JkvQ+vFKqjbW1t5X5YwJuiC2Lg/cx1XnOb7sNCQQ5kqTGAAAAAElFTkSuQmCC";
  private static com.google.gwt.resources.client.ImageResource infoHovered;
  private static com.google.gwt.resources.client.ImageResource infoNormal;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      infoHovered(), 
      infoNormal(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("infoHovered", infoHovered());
        resourceMap.put("infoNormal", infoNormal());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.controls.settings.common.InfoLabel.Resources::getCSS()();
      case 'infoHovered': return this.@org.reactome.web.diagram.controls.settings.common.InfoLabel.Resources::infoHovered()();
      case 'infoNormal': return this.@org.reactome.web.diagram.controls.settings.common.InfoLabel.Resources::infoNormal()();
    }
    return null;
  }-*/;
}
