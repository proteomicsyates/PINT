package org.reactome.web.fireworks.search.searchonfire.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class OptionsPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel.Resources {
  private static OptionsPanel_Resources_default_InlineClientBundleGenerator _instance0 = new OptionsPanel_Resources_default_InlineClientBundleGenerator();
  private void checkedIconInitializer() {
    checkedIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "checkedIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 12, 12, false, false
    );
  }
  private static class checkedIconInitializer {
    static {
      _instance0.checkedIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return checkedIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource checkedIcon() {
    return checkedIconInitializer.get();
  }
  private void uncheckedIconInitializer() {
    uncheckedIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "uncheckedIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 12, 12, false, false
    );
  }
  private static class uncheckedIconInitializer {
    static {
      _instance0.uncheckedIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return uncheckedIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource uncheckedIcon() {
    return uncheckedIconInitializer.get();
  }
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-mainPanel {\n  background-color : " + ("#1e94d0")  + ";\n  opacity : " + ("0.8")  + ";\n  padding : " + ("1px"+ " " +"5px"+ " " +"1px"+ " " +"1px")  + ";\n  border-radius : " + ("0"+ " " +"0"+ " " +"5px"+ " " +"5px")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn {\n  float : " + ("right")  + ";\n  clear : " + ("both")  + ";\n  background-color : " + ("#ccc")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("12px")  + ";\n  color : " + ("white")  + ";\n  margin-top : ") + (("3px")  + ";\n  padding : " + ("0"+ " " +"0"+ " " +"0"+ " " +"0")  + ";\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllEnabledBtn {\n  background-color : " + ("#58c3e5")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn span {\n  vertical-align : " + ("middle")  + " !important;\n  display : " + ("inline-block")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  height : " + ("14px")  + ";\n  width : " + ("auto")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer") ) + (" !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn:disabled, .org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn:disabled:active {\n  background-color : " + ("#a39da6")  + ";\n  cursor : " + ("default")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllLabel {\n  display : " + ("inline-block")  + ";\n  color : " + ("white")  + ";\n  margin-right : " + ("7px")  + ";\n  margin-top : " + ("2px")  + ";\n  font-size : " + ("0.95em")  + ";\n  font-weight : " + ("bold")  + ";\n  cursor : ") + (("pointer")  + " !important;\n}\n")) : ((".org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-mainPanel {\n  background-color : " + ("#1e94d0")  + ";\n  opacity : " + ("0.8")  + ";\n  padding : " + ("1px"+ " " +"1px"+ " " +"1px"+ " " +"5px")  + ";\n  border-radius : " + ("0"+ " " +"0"+ " " +"5px"+ " " +"5px")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn {\n  float : " + ("left")  + ";\n  clear : " + ("both")  + ";\n  background-color : " + ("#ccc")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("12px")  + ";\n  color : " + ("white")  + ";\n  margin-top : ") + (("3px")  + ";\n  padding : " + ("0"+ " " +"0"+ " " +"0"+ " " +"0")  + ";\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllEnabledBtn {\n  background-color : " + ("#58c3e5")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn span {\n  vertical-align : " + ("middle")  + " !important;\n  display : " + ("inline-block")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  height : " + ("14px")  + ";\n  width : " + ("auto")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer") ) + (" !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn:disabled, .org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn:disabled:active {\n  background-color : " + ("#a39da6")  + ";\n  cursor : " + ("default")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllLabel {\n  display : " + ("inline-block")  + ";\n  color : " + ("white")  + ";\n  margin-left : " + ("7px")  + ";\n  margin-top : " + ("2px")  + ";\n  font-size : " + ("0.95em")  + ";\n  font-weight : " + ("bold")  + ";\n  cursor : ") + (("pointer")  + " !important;\n}\n"));
      }
      public java.lang.String mainPanel() {
        return "org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-mainPanel";
      }
      public java.lang.String showAllBtn() {
        return "org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllBtn";
      }
      public java.lang.String showAllEnabledBtn() {
        return "org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllEnabledBtn";
      }
      public java.lang.String showAllLabel() {
        return "org-reactome-web-fireworks-search-searchonfire-options-OptionsPanel-ResourceCSS-showAllLabel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAYAAABWdVznAAAA0ElEQVR42mP49euX+b9//24C8Qcgfo8Dg+RugtQygBi/f/92YCAAQGpAakEaPuBT+Pfv30Ygnrxq1SpmsFqQlXhMtQfK/wXiB/fv3+cAq8Wl4dWrVzxAubtA/A+o0QkkhqLh9u3b7CCM5JQZ/4EA5ByYGFzDmTNnWIH0cSA+8vr1a16gie7/IODWs2fPuDA0QD20AWQiSBMQPwbiP0CNVsjORHHSlStX2IDsTf+hAOiUTnR/wTTAgxWqaRUQnwWFChYNH7BFHCPIiTgjjtSkAQAPxjz1URkQxAAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAYAAABWdVznAAAARElEQVR42mP49euX+b9//24C8Qcgfo8Dg+RugtQygBi/f/92YCAAQGpAakEaPjAQCcBqQVaSoOH9iNVAcrCSFnGkJg0AFYC1GYIvPyEAAAAASUVORK5CYII=";
  private static com.google.gwt.resources.client.ImageResource checkedIcon;
  private static com.google.gwt.resources.client.ImageResource uncheckedIcon;
  private static org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel.ResourceCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      checkedIcon(), 
      uncheckedIcon(), 
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("checkedIcon", checkedIcon());
        resourceMap.put("uncheckedIcon", uncheckedIcon());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'checkedIcon': return this.@org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel.Resources::checkedIcon()();
      case 'uncheckedIcon': return this.@org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel.Resources::uncheckedIcon()();
      case 'getCSS': return this.@org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel.Resources::getCSS()();
    }
    return null;
  }-*/;
}
