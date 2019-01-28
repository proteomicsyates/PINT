package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class NativeVerticalScrollbar_UiBinderBundle_default_InlineClientBundleGenerator implements com.google.gwt.user.client.ui.NativeVerticalScrollbar.UiBinderBundle {
  private static NativeVerticalScrollbar_UiBinderBundle_default_InlineClientBundleGenerator _instance0 = new NativeVerticalScrollbar_UiBinderBundle_default_InlineClientBundleGenerator();
  private void nativeVerticalScrollbarUiInitializer() {
    nativeVerticalScrollbarUi = new com.google.gwt.user.client.ui.NativeVerticalScrollbar.UiStyle() {
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
        return "nativeVerticalScrollbarUi";
      }
      public String getText() {
        return (".com-google-gwt-user-client-ui-NativeVerticalScrollbar-UiStyle-viewport {\n  position : " + ("relative")  + ";\n  overflow : " + ("hidden")  + ";\n  direction : " + ("ltr")  + ";\n}\n/*@noflip { */\n.com-google-gwt-user-client-ui-NativeVerticalScrollbar-UiStyle-scrollable {\n  position : " + ("absolute")  + ";\n  top : " + ("0")  + ";\n  right : " + ("0")  + ";\n  height : " + ("100%")  + ";\n  width : " + ("100px")  + ";\n  overflow-y : " + ("scroll")  + ";\n  overflow-x : " + ("hidden")  + ";\n}\n/*} @noflip */\n");
      }
      public java.lang.String scrollable() {
        return "com-google-gwt-user-client-ui-NativeVerticalScrollbar-UiStyle-scrollable";
      }
      public java.lang.String viewport() {
        return "com-google-gwt-user-client-ui-NativeVerticalScrollbar-UiStyle-viewport";
      }
    }
    ;
  }
  private static class nativeVerticalScrollbarUiInitializer {
    static {
      _instance0.nativeVerticalScrollbarUiInitializer();
    }
    static com.google.gwt.user.client.ui.NativeVerticalScrollbar.UiStyle get() {
      return nativeVerticalScrollbarUi;
    }
  }
  public com.google.gwt.user.client.ui.NativeVerticalScrollbar.UiStyle nativeVerticalScrollbarUi() {
    return nativeVerticalScrollbarUiInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.user.client.ui.NativeVerticalScrollbar.UiStyle nativeVerticalScrollbarUi;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      nativeVerticalScrollbarUi(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("nativeVerticalScrollbarUi", nativeVerticalScrollbarUi());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'nativeVerticalScrollbarUi': return this.@com.google.gwt.user.client.ui.NativeVerticalScrollbar.UiBinderBundle::nativeVerticalScrollbarUi()();
    }
    return null;
  }-*/;
}
