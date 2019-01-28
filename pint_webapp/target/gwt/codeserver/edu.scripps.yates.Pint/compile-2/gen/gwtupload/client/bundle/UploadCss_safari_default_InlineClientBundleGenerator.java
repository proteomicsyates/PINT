package gwtupload.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class UploadCss_safari_default_InlineClientBundleGenerator implements gwtupload.client.bundle.UploadCss {
  private static UploadCss_safari_default_InlineClientBundleGenerator _instance0 = new UploadCss_safari_default_InlineClientBundleGenerator();
  private void cssInitializer() {
    css = new com.google.gwt.resources.client.CssResource() {
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
        return "css";
      }
      public String getText() {
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? (("/* @external GWTUpld; */\n/* @external gwt-*; */\n/* @external upld*; */\n/* @external DecoratedFileUpload*; */\n/* @external status*; */\n/* @external prgbar*; */\n/* @external cancel; */\n/* @external changed; */\n/* @external filename; */\n.GWTUpld, table.GWTUpld td {\n  font-family : " + ("Verdana"+ ","+ " " +"Arial")  + ";\n  font-size : " + ("12px")  + ";\n  padding : " + ("0")  + ";\n}\n.GWTUpld form, .GWTUpld .upld-form-elements {\n  padding : " + ("0")  + ";\n  vertical-align : " + ("top")  + ";\n}\n.GWTUpld .upld-status {\n  font-family : " + ("arial")  + ";\n  font-size : " + ("12px")  + ";\n  font-weight : " + ("bold")  + ";\n}\n.GWTUpld .upld-status div.cancel {\n  width : " + ("12px")  + ";\n  height : " + ("12px")  + ";\n  cursor : ") + (("pointer")  + ";\n  margin-top : " + ("1px")  + ";\n  height : " + ((UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getHeight() + "px")  + ";\n  width : " + ((UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getSafeUri().asString() + "\") -" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getLeft() + "px -" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getTop() + "px  no-repeat")  + ";\n}\n.GWTUpld .upld-status div.cancel:hover {\n  height : " + ((UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getHeight() + "px")  + ";\n  width : " + ((UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getSafeUri().asString() + "\") -" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getLeft() + "px -" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getTop() + "px  no-repeat")  + ";\n}\n.GWTUpld .upld-status .filename {\n  overflow : " + ("hidden") ) + (";\n  white-space : " + ("nowrap")  + ";\n  margin-right : " + ("8px")  + ";\n  margin-left : " + ("11px")  + ";\n  height : " + ("100%")  + ";\n  font-size : " + ("12px")  + ";\n  max-width : " + ("200px")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n}\n.GWTUpld .upld-status .status {\n  padding-right : " + ("8px")  + ";\n  white-space : " + ("nowrap")  + ";\n  height : " + ("100%")  + ";\n  font-size : ") + (("12px")  + ";\n}\n.GWTUpld .upld-status .status-success {\n  color : " + ("green")  + ";\n}\n.GWTUpld .upld-status .status-error, .GWTUpld .upld-status .status-canceled {\n  color : " + ("red")  + ";\n}\n.GWTUpld .prgbar {\n  height : " + ("12px")  + ";\n  float : " + ("right")  + ";\n  width : " + ("100px")  + ";\n  margin-right : " + ("2px")  + ";\n}\n.GWTUpld .prgbar-back {\n  background : " + ("#fff"+ " " +"none"+ " " +"repeat"+ " " +"scroll"+ " " +"0"+ " " +"0")  + ";\n  border : " + ("1px"+ " " +"solid"+ " " +"#999")  + ";\n  overflow : " + ("hidden")  + ";\n  padding : " + ("1px") ) + (";\n}\n.GWTUpld .prgbar-done {\n  background : " + ("#d4e4ff"+ " " +"none"+ " " +"repeat"+ " " +"scroll"+ " " +"0"+ " " +"0")  + ";\n  font-size : " + ("0")  + ";\n  height : " + ("100%")  + ";\n  float : " + ("right")  + ";\n}\n.GWTUpld .prgbar-msg {\n  position : " + ("absolute")  + ";\n  z-index : " + ("9")  + ";\n  font-size : " + ("9px")  + ";\n  font-weight : " + ("normal")  + ";\n  margin-right : " + ("3px")  + ";\n}\n.GWTUpld .changed {\n  color : " + ("red")  + ";\n  font-weight : ") + (("bold")  + ";\n  text-decoration : " + ("blink")  + ";\n}\n.upld-modal .GWTUpld {\n  border : " + ("2px"+ " " +"groove"+ " " +"#f6a828")  + ";\n  padding : " + ("10px")  + ";\n  background : " + ("#bf984c")  + ";\n  -moz-border-radius-bottomleft : " + ("6px")  + ";\n  -moz-border-radius-bottomright : " + ("6px")  + ";\n  -moz-border-radius-topleft : " + ("6px")  + ";\n  -moz-border-radius-topright : " + ("6px")  + ";\n}\n.upld-modal-glass {\n  background-color : " + ("#d4e4ff")  + ";\n  opacity : " + ("0.3") ) + (";\n}\n.GWTUpld .DecoratedFileUpload {\n  margin-left : " + ("5px")  + ";\n  display : " + ("inline-block")  + ";\n}\n.GWTUpld .DecoratedFileUpload-button {\n  white-space : " + ("nowrap")  + ";\n  font-size : " + ("10px")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.GWTUpld .gwt-Button, .GWTUpld .gwt-FileUpload {\n  font-size : " + ("10px")  + ";\n  min-height : " + ("15px")  + ";\n}\n.GWTUpld .DecoratedFileUpload .gwt-Anchor, .GWTUpld .DecoratedFileUpload .gwt-Label {\n  color : " + ("blue")  + ";\n  text-decoration : " + ("underline")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.GWTUpld .DecoratedFileUpload-button:HOVER, .GWTUpld .DecoratedFileUpload-button-over {\n  color : ") + (("#af6b29")  + ";\n}\n.GWTUpld .DecoratedFileUpload-disabled {\n  color : " + ("grey")  + ";\n}\n.GWTUpld input[type=\"file\"] {\n  cursor : " + ("pointer")  + ";\n}\n")) : (("/* @external GWTUpld; */\n/* @external gwt-*; */\n/* @external upld*; */\n/* @external DecoratedFileUpload*; */\n/* @external status*; */\n/* @external prgbar*; */\n/* @external cancel; */\n/* @external changed; */\n/* @external filename; */\n.GWTUpld, table.GWTUpld td {\n  font-family : " + ("Verdana"+ ","+ " " +"Arial")  + ";\n  font-size : " + ("12px")  + ";\n  padding : " + ("0")  + ";\n}\n.GWTUpld form, .GWTUpld .upld-form-elements {\n  padding : " + ("0")  + ";\n  vertical-align : " + ("top")  + ";\n}\n.GWTUpld .upld-status {\n  font-family : " + ("arial")  + ";\n  font-size : " + ("12px")  + ";\n  font-weight : " + ("bold")  + ";\n}\n.GWTUpld .upld-status div.cancel {\n  width : " + ("12px")  + ";\n  height : " + ("12px")  + ";\n  cursor : ") + (("pointer")  + ";\n  margin-top : " + ("1px")  + ";\n  height : " + ((UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getHeight() + "px")  + ";\n  width : " + ((UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getSafeUri().asString() + "\") -" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getLeft() + "px -" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUpload()).getTop() + "px  no-repeat")  + ";\n}\n.GWTUpld .upld-status div.cancel:hover {\n  height : " + ((UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getHeight() + "px")  + ";\n  width : " + ((UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getWidth() + "px")  + ";\n  overflow : " + ("hidden")  + ";\n  background : " + ("url(\"" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getSafeUri().asString() + "\") -" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getLeft() + "px -" + (UploadCss_safari_default_InlineClientBundleGenerator.this.imgCancelUploadHover()).getTop() + "px  no-repeat")  + ";\n}\n.GWTUpld .upld-status .filename {\n  overflow : " + ("hidden") ) + (";\n  white-space : " + ("nowrap")  + ";\n  margin-left : " + ("8px")  + ";\n  margin-right : " + ("11px")  + ";\n  height : " + ("100%")  + ";\n  font-size : " + ("12px")  + ";\n  max-width : " + ("200px")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n}\n.GWTUpld .upld-status .status {\n  padding-left : " + ("8px")  + ";\n  white-space : " + ("nowrap")  + ";\n  height : " + ("100%")  + ";\n  font-size : ") + (("12px")  + ";\n}\n.GWTUpld .upld-status .status-success {\n  color : " + ("green")  + ";\n}\n.GWTUpld .upld-status .status-error, .GWTUpld .upld-status .status-canceled {\n  color : " + ("red")  + ";\n}\n.GWTUpld .prgbar {\n  height : " + ("12px")  + ";\n  float : " + ("left")  + ";\n  width : " + ("100px")  + ";\n  margin-left : " + ("2px")  + ";\n}\n.GWTUpld .prgbar-back {\n  background : " + ("#fff"+ " " +"none"+ " " +"repeat"+ " " +"scroll"+ " " +"0"+ " " +"0")  + ";\n  border : " + ("1px"+ " " +"solid"+ " " +"#999")  + ";\n  overflow : " + ("hidden")  + ";\n  padding : " + ("1px") ) + (";\n}\n.GWTUpld .prgbar-done {\n  background : " + ("#d4e4ff"+ " " +"none"+ " " +"repeat"+ " " +"scroll"+ " " +"0"+ " " +"0")  + ";\n  font-size : " + ("0")  + ";\n  height : " + ("100%")  + ";\n  float : " + ("left")  + ";\n}\n.GWTUpld .prgbar-msg {\n  position : " + ("absolute")  + ";\n  z-index : " + ("9")  + ";\n  font-size : " + ("9px")  + ";\n  font-weight : " + ("normal")  + ";\n  margin-left : " + ("3px")  + ";\n}\n.GWTUpld .changed {\n  color : " + ("red")  + ";\n  font-weight : ") + (("bold")  + ";\n  text-decoration : " + ("blink")  + ";\n}\n.upld-modal .GWTUpld {\n  border : " + ("2px"+ " " +"groove"+ " " +"#f6a828")  + ";\n  padding : " + ("10px")  + ";\n  background : " + ("#bf984c")  + ";\n  -moz-border-radius-bottomleft : " + ("6px")  + ";\n  -moz-border-radius-bottomright : " + ("6px")  + ";\n  -moz-border-radius-topleft : " + ("6px")  + ";\n  -moz-border-radius-topright : " + ("6px")  + ";\n}\n.upld-modal-glass {\n  background-color : " + ("#d4e4ff")  + ";\n  opacity : " + ("0.3") ) + (";\n}\n.GWTUpld .DecoratedFileUpload {\n  margin-right : " + ("5px")  + ";\n  display : " + ("inline-block")  + ";\n}\n.GWTUpld .DecoratedFileUpload-button {\n  white-space : " + ("nowrap")  + ";\n  font-size : " + ("10px")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.GWTUpld .gwt-Button, .GWTUpld .gwt-FileUpload {\n  font-size : " + ("10px")  + ";\n  min-height : " + ("15px")  + ";\n}\n.GWTUpld .DecoratedFileUpload .gwt-Anchor, .GWTUpld .DecoratedFileUpload .gwt-Label {\n  color : " + ("blue")  + ";\n  text-decoration : " + ("underline")  + ";\n  cursor : " + ("pointer")  + ";\n}\n.GWTUpld .DecoratedFileUpload-button:HOVER, .GWTUpld .DecoratedFileUpload-button-over {\n  color : ") + (("#af6b29")  + ";\n}\n.GWTUpld .DecoratedFileUpload-disabled {\n  color : " + ("grey")  + ";\n}\n.GWTUpld input[type=\"file\"] {\n  cursor : " + ("pointer")  + ";\n}\n"));
      }
    }
    ;
  }
  private static class cssInitializer {
    static {
      _instance0.cssInitializer();
    }
    static com.google.gwt.resources.client.CssResource get() {
      return css;
    }
  }
  public com.google.gwt.resources.client.CssResource css() {
    return cssInitializer.get();
  }
  private void imgCancelUploadInitializer() {
    imgCancelUpload = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "imgCancelUpload",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 12, 12, false, false
    );
  }
  private static class imgCancelUploadInitializer {
    static {
      _instance0.imgCancelUploadInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return imgCancelUpload;
    }
  }
  public com.google.gwt.resources.client.ImageResource imgCancelUpload() {
    return imgCancelUploadInitializer.get();
  }
  private void imgCancelUploadHoverInitializer() {
    imgCancelUploadHover = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "imgCancelUploadHover",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 12, 12, false, false
    );
  }
  private static class imgCancelUploadHoverInitializer {
    static {
      _instance0.imgCancelUploadHoverInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return imgCancelUploadHover;
    }
  }
  public com.google.gwt.resources.client.ImageResource imgCancelUploadHover() {
    return imgCancelUploadHoverInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.resources.client.CssResource css;
  private static final java.lang.String externalImage = "data:image/gif;base64,R0lGODlhDAAMAKU9ANk/P9lBQdpHR9tJSdxRUdtSUtxXV91cXN5eXt5hYeJubuN3d+V5eeN6euN8fOV+fuaDg+eFheOHh+eIiOeKiueOjumPj+WSkemVleuZmeudneafn+uenuujo+2kpO+vr/C2tuu6uum7uvK+vvDBwO7DwvTHx/XLy+zOzu3OzvXOzuzR0OzS0fHS0vbS0vbU1PbV1e/d2u/e3vjc3Pji4u7l5O/l4/rm5u7s6u7t6+7u7Pzu7vvv7////////////yH5BAEKAD8ALAAAAAAMAAwAAAZ/wJfH0yl2OEUPqLO70WZQKO2GnMFcKtXppHLBMBqVqWUjgWQxksmSGYFKuZosl/qAKJgPMZTLoTIcHhAWHRkWLH02GBUYDhYWEyE6Ihs4Kw8RCxMRDREbCQkSFwoNmg0KCQcEBAUGCKAVqAYCAQABAwOgLxMPDwwMDQwODxAgQQA7";
  private static final java.lang.String externalImage0 = "data:image/gif;base64,R0lGODlhDAAMAKUvAKhKuatQu6xTvK9Yv7Bav7JewbRiw7VlxLdoxbx0ycF+zcJ/zsKBzsSEz8aI0ceL0siM08mO08uS1cuU1cyV1s2Y18+c2dKh2tSl3NSm3dmw4d255N675OC/5uHC5+LD6OTI6ebM6+jP7OjQ7enS7erT7urV7u3Z8O3a8fDg8/Lk9fPm9fPn9vTp9vbt+P///////////////////////////////////////////////////////////////////yH+EUNyZWF0ZWQgd2l0aCBHSU1QACH5BAEKAD8ALAAAAAAMAAwAAAZ7QFMmgyliLsXMBuNSpVBQaEqFRJlKIlEoJCqZKhcRiJTybFAnD2hi6Ww+rCcrpNlEKhoihzW3XDIOExgWEyN8KRUSFQwTExAcLRwXKyINDwoQDwsPFwgIDxQJC5gLCQgGAwMEBQeeEqYFAQCzAgKeJhANDQoKowwNDhtBADs=";
  private static com.google.gwt.resources.client.ImageResource imgCancelUpload;
  private static com.google.gwt.resources.client.ImageResource imgCancelUploadHover;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      css(), 
      imgCancelUpload(), 
      imgCancelUploadHover(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("css", css());
        resourceMap.put("imgCancelUpload", imgCancelUpload());
        resourceMap.put("imgCancelUploadHover", imgCancelUploadHover());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'css': return this.@gwtupload.client.bundle.UploadCss::css()();
      case 'imgCancelUpload': return this.@gwtupload.client.bundle.UploadCss::imgCancelUpload()();
      case 'imgCancelUploadHover': return this.@gwtupload.client.bundle.UploadCss::imgCancelUploadHover()();
    }
    return null;
  }-*/;
}
