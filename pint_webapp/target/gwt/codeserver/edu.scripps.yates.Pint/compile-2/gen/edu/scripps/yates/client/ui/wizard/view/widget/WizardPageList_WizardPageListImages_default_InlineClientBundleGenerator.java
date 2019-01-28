package edu.scripps.yates.client.ui.wizard.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class WizardPageList_WizardPageListImages_default_InlineClientBundleGenerator implements edu.scripps.yates.client.ui.wizard.view.widget.WizardPageList.WizardPageListImages {
  private static WizardPageList_WizardPageListImages_default_InlineClientBundleGenerator _instance0 = new WizardPageList_WizardPageListImages_default_InlineClientBundleGenerator();
  private void indicatorInitializer() {
    indicator = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "indicator",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 16, 16, false, false
    );
  }
  private static class indicatorInitializer {
    static {
      _instance0.indicatorInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return indicator;
    }
  }
  public com.google.gwt.resources.client.ImageResource indicator() {
    return indicatorInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABIklEQVR42mNgGAV4QfZi86+p8w3/h66y5CTLgOT5hl8bN0f9D5+h8T+0T4awIflLbf/nLjb/m77Q+G/iXP2/NetD/686PfF/yWr//6794n8dporyEHLy/w3npv1fe3YKWOOK0xP+T9hT8n/h8a7/GUvd/pu2s/817mDgx+dksOa+Xfn/O3dk/2/dlva/YXPi/6at6f9nHGr+H7PA5r9qPcMfnAZEz9b8v/LUhP9LT/T+X3is6/+8ox1AQ7L+Tz/U9D9tqcd/xVqGV9JVDAY4DQicrvjPc6L0P+d+0X/W3fz/4uZb/Z92sOF/0mK3//K1DM8lKhm0SIoF7WbGr/ELnf/LVzM8kaphUCc5GoFO/ipXzfxfvIZBkezEJFPIwDm4kzsAOkiHUbRKUmoAAAAASUVORK5CYII=";
  private static com.google.gwt.resources.client.ImageResource indicator;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      indicator(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("indicator", indicator());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'indicator': return this.@edu.scripps.yates.client.ui.wizard.view.widget.WizardPageList.WizardPageListImages::indicator()();
    }
    return null;
  }-*/;
}
