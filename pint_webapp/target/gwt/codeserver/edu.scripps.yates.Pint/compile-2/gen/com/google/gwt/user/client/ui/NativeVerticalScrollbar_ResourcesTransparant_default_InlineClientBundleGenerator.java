package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class NativeVerticalScrollbar_ResourcesTransparant_default_InlineClientBundleGenerator implements com.google.gwt.user.client.ui.NativeVerticalScrollbar.ResourcesTransparant {
  private static NativeVerticalScrollbar_ResourcesTransparant_default_InlineClientBundleGenerator _instance0 = new NativeVerticalScrollbar_ResourcesTransparant_default_InlineClientBundleGenerator();
  private void nativeVerticalScrollbarStyleInitializer() {
    nativeVerticalScrollbarStyle = new com.google.gwt.user.client.ui.NativeVerticalScrollbar.Style() {
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
        return "nativeVerticalScrollbarStyle";
      }
      public String getText() {
        return ("/* CssDef */\n.com-google-gwt-user-client-ui-NativeVerticalScrollbar-Style-nativeVerticalScrollbar {\n  opacity : " + ("0.7")  + ";\n  filter : " + ("alpha(opacity = 70)")  + ";\n  -webkit-transition : " + ("opacity"+ " " +"350ms")  + ";\n  -moz-transition : " + ("opacity"+ " " +"350ms")  + ";\n  -o-transition : " + ("opacity"+ " " +"350ms")  + ";\n  transition : " + ("opacity"+ " " +"350ms")  + ";\n}\n.com-google-gwt-user-client-ui-NativeVerticalScrollbar-Style-nativeVerticalScrollbar:hover {\n  opacity : " + ("1")  + ";\n  filter : " + ("alpha(opacity = 100)")  + ";\n}\n");
      }
      public java.lang.String nativeVerticalScrollbar() {
        return "com-google-gwt-user-client-ui-NativeVerticalScrollbar-Style-nativeVerticalScrollbar";
      }
    }
    ;
  }
  private static class nativeVerticalScrollbarStyleInitializer {
    static {
      _instance0.nativeVerticalScrollbarStyleInitializer();
    }
    static com.google.gwt.user.client.ui.NativeVerticalScrollbar.Style get() {
      return nativeVerticalScrollbarStyle;
    }
  }
  public com.google.gwt.user.client.ui.NativeVerticalScrollbar.Style nativeVerticalScrollbarStyle() {
    return nativeVerticalScrollbarStyleInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.user.client.ui.NativeVerticalScrollbar.Style nativeVerticalScrollbarStyle;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      nativeVerticalScrollbarStyle(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("nativeVerticalScrollbarStyle", nativeVerticalScrollbarStyle());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'nativeVerticalScrollbarStyle': return this.@com.google.gwt.user.client.ui.NativeVerticalScrollbar.ResourcesTransparant::nativeVerticalScrollbarStyle()();
    }
    return null;
  }-*/;
}
