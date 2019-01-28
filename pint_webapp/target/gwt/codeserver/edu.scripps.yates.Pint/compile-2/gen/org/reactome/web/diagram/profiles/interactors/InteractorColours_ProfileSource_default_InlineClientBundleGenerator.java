package org.reactome.web.diagram.profiles.interactors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class InteractorColours_ProfileSource_default_InlineClientBundleGenerator implements org.reactome.web.diagram.profiles.interactors.InteractorColours.ProfileSource {
  private static InteractorColours_ProfileSource_default_InlineClientBundleGenerator _instance0 = new InteractorColours_ProfileSource_default_InlineClientBundleGenerator();
  private void profile01Initializer() {
    profile01 = new com.google.gwt.resources.client.TextResource() {
      // jar:file:/C:/Users/salvador/.m2/repository/org/reactome/web/diagram/3.2.1/diagram-3.2.1.jar!/org/reactome/web/diagram/profiles/interactors/profile_01.json
      public String getText() {
        return "{\n  \"name\": \"Cyan\",\n  \"chemical\": {\n    \"fill\": \"#B2EBF2\",\n    \"stroke\": \"#000000\",\n    \"lighterFill\" : \"#E0F7FA\",\n    \"lighterStroke\" : \"#006064\",\n    \"text\": \"#000000\",\n    \"lighterText\": \"#424242\"\n  },\n  \"protein\": {\n    \"fill\": \"#26C6DA\",\n    \"stroke\": \"#616161\",\n    \"lighterFill\" : \"#80DEEA\",\n    \"lighterStroke\" : \"#9E9E9E\",\n    \"text\": \"#000000\",\n    \"lighterText\": \"#424242\"\n  }\n}";
      }
      public String getName() {
        return "profile01";
      }
    }
    ;
  }
  private static class profile01Initializer {
    static {
      _instance0.profile01Initializer();
    }
    static com.google.gwt.resources.client.TextResource get() {
      return profile01;
    }
  }
  public com.google.gwt.resources.client.TextResource profile01() {
    return profile01Initializer.get();
  }
  private void profile02Initializer() {
    profile02 = new com.google.gwt.resources.client.TextResource() {
      // jar:file:/C:/Users/salvador/.m2/repository/org/reactome/web/diagram/3.2.1/diagram-3.2.1.jar!/org/reactome/web/diagram/profiles/interactors/profile_02.json
      public String getText() {
        return "{\n  \"name\": \"Teal\",\n  \"chemical\": {\n    \"fill\": \"#80CBC4\",\n    \"stroke\": \"#616161\",\n    \"lighterFill\" : \"#E0F2F1\",\n    \"lighterStroke\" : \"#9E9E9E\",\n    \"text\": \"#000000\",\n    \"lighterText\": \"#424242\"\n  },\n  \"protein\": {\n    \"fill\": \"#26A69A\",\n    \"stroke\": \"#616161\",\n    \"lighterFill\" : \"#80CBC4\",\n    \"lighterStroke\" : \"#9E9E9E\",\n    \"text\": \"#000000\",\n    \"lighterText\": \"#424242\"\n  }\n}";
      }
      public String getName() {
        return "profile02";
      }
    }
    ;
  }
  private static class profile02Initializer {
    static {
      _instance0.profile02Initializer();
    }
    static com.google.gwt.resources.client.TextResource get() {
      return profile02;
    }
  }
  public com.google.gwt.resources.client.TextResource profile02() {
    return profile02Initializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.resources.client.TextResource profile01;
  private static com.google.gwt.resources.client.TextResource profile02;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      profile01(), 
      profile02(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("profile01", profile01());
        resourceMap.put("profile02", profile02());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'profile01': return this.@org.reactome.web.diagram.profiles.interactors.InteractorColours.ProfileSource::profile01()();
      case 'profile02': return this.@org.reactome.web.diagram.profiles.interactors.InteractorColours.ProfileSource::profile02()();
    }
    return null;
  }-*/;
}
