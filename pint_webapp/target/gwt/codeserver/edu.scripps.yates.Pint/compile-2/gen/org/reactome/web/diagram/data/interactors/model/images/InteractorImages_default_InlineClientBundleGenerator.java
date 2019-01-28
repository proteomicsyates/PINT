package org.reactome.web.diagram.data.interactors.model.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class InteractorImages_default_InlineClientBundleGenerator implements org.reactome.web.diagram.data.interactors.model.images.InteractorImages {
  private static InteractorImages_default_InlineClientBundleGenerator _instance0 = new InteractorImages_default_InlineClientBundleGenerator();
  private void interactorInitializer() {
    interactor = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "interactor",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 16, 16, false, false
    );
  }
  private static class interactorInitializer {
    static {
      _instance0.interactorInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return interactor;
    }
  }
  public com.google.gwt.resources.client.ImageResource interactor() {
    return interactorInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABAUlEQVR42mNgIBKcPXRIb9LGjcdDZs16AsIgNkiMWP0MkzduPC3U2vqfoaICjEFskBjRBvjOmvWWobLyP0NDAwQD2WAxooCcnJbn9Ok/STdASkqdISxsCUNJyd/S5cv/Y/UCtsCpqa/3YQgKWgjU+BtsW0nJH9PCws09q1efxwhEbIFTtmTJP7DG0tK/DCEhy8AuISVwPKZN+8/g47MN6HdtskLXY8aM/wyFhT8ZvL17gEoESI7fuiVL3oH5IAMzMl4zODikAJUykZbCNDQcGOLizjHU1/8HYZ3i4usdK1ZcJTUlMjHY26eCXIEzGokEAl7Tp38nPyVSnJSpkZlwBTYAQRIcRtNV4lAAAAAASUVORK5CYII=";
  private static com.google.gwt.resources.client.ImageResource interactor;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      interactor(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("interactor", interactor());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'interactor': return this.@org.reactome.web.diagram.data.interactors.model.images.InteractorImages::interactor()();
    }
    return null;
  }-*/;
}
