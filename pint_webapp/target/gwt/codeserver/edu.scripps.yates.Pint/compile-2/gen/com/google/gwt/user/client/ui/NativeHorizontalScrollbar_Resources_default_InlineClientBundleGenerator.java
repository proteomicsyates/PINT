package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class NativeHorizontalScrollbar_Resources_default_InlineClientBundleGenerator implements com.google.gwt.user.client.ui.NativeHorizontalScrollbar.Resources {
  private static NativeHorizontalScrollbar_Resources_default_InlineClientBundleGenerator _instance0 = new NativeHorizontalScrollbar_Resources_default_InlineClientBundleGenerator();
  private void nativeHorizontalScrollbarStyleInitializer() {
    nativeHorizontalScrollbarStyle = new com.google.gwt.user.client.ui.NativeHorizontalScrollbar.Style() {
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
        return "nativeHorizontalScrollbarStyle";
      }
      public String getText() {
        return ("");
      }
      public java.lang.String nativeHorizontalScrollbar() {
        return "com-google-gwt-user-client-ui-NativeHorizontalScrollbar-Style-nativeHorizontalScrollbar";
      }
    }
    ;
  }
  private static class nativeHorizontalScrollbarStyleInitializer {
    static {
      _instance0.nativeHorizontalScrollbarStyleInitializer();
    }
    static com.google.gwt.user.client.ui.NativeHorizontalScrollbar.Style get() {
      return nativeHorizontalScrollbarStyle;
    }
  }
  public com.google.gwt.user.client.ui.NativeHorizontalScrollbar.Style nativeHorizontalScrollbarStyle() {
    return nativeHorizontalScrollbarStyleInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.user.client.ui.NativeHorizontalScrollbar.Style nativeHorizontalScrollbarStyle;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      nativeHorizontalScrollbarStyle(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("nativeHorizontalScrollbarStyle", nativeHorizontalScrollbarStyle());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'nativeHorizontalScrollbarStyle': return this.@com.google.gwt.user.client.ui.NativeHorizontalScrollbar.Resources::nativeHorizontalScrollbarStyle()();
    }
    return null;
  }-*/;
}
