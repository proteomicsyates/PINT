package org.reactome.web.diagram.profiles.analysis;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class AnalysisColours_ProfileSource_default_InlineClientBundleGenerator implements org.reactome.web.diagram.profiles.analysis.AnalysisColours.ProfileSource {
  private static AnalysisColours_ProfileSource_default_InlineClientBundleGenerator _instance0 = new AnalysisColours_ProfileSource_default_InlineClientBundleGenerator();
  private void profile01Initializer() {
    profile01 = new com.google.gwt.resources.client.TextResource() {
      // jar:file:/C:/Users/salvador/.m2/repository/org/reactome/web/diagram/3.2.1/diagram-3.2.1.jar!/org/reactome/web/diagram/profiles/analysis/profile_01.json
      public String getText() {
        return "{\n  \"name\": \"Standard\",\n  \"enrichment\": {\n    \"gradient\": {\n      \"min\": \"#F8DE36\",\n      \"stop\": null,\n      \"max\": \"#8F8A05\"\n    },\n    \"text\": \"#000000\"\n  },\n  \"expression\": {\n    \"gradient\": {\n      \"min\": \"#F4FA58\",\n      \"stop\": null,\n      \"max\": \"#2E64FE\"\n    },\n    \"legend\": {\n      \"median\": \"#000000\",\n      \"hover\": \"#FF0000\"\n    },\n    \"text\": \"#FFFFFF\"\n  },\n  \"ribbon\": \"#673AB7\"\n}";
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
      // jar:file:/C:/Users/salvador/.m2/repository/org/reactome/web/diagram/3.2.1/diagram-3.2.1.jar!/org/reactome/web/diagram/profiles/analysis/profile_02.json
      public String getText() {
        return "{\n  \"name\": \"Copper Plus\",\n  \"enrichment\": {\n    \"gradient\": {\n      \"min\": \"#FFFFCA\",\n      \"stop\": null,\n      \"max\": \"#FFFF50\"\n    },\n    \"text\": \"#000000\"\n  },\n  \"expression\": {\n    \"gradient\": {\n      \"min\": \"#FFFF00\",\n      \"stop\": \"#56D7EE\",\n      \"max\": \"#0000FF\"\n    },\n    \"legend\": {\n      \"median\": \"#000000\",\n      \"hover\": \"#FF0000\"\n    },\n    \"text\": \"#FFFFFF\"\n  },\n  \"ribbon\": \"#FB8C00\"\n}";
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
  private void profile03Initializer() {
    profile03 = new com.google.gwt.resources.client.TextResource() {
      // jar:file:/C:/Users/salvador/.m2/repository/org/reactome/web/diagram/3.2.1/diagram-3.2.1.jar!/org/reactome/web/diagram/profiles/analysis/profile_03.json
      public String getText() {
        return "{\n  \"name\": \"Strosobar\",\n  \"enrichment\": {\n    \"gradient\": {\n      \"min\": \"#FFFFCA\",\n      \"stop\": null,\n      \"max\": \"#FFFF50\"\n    },\n    \"text\": \"#000000\"\n  },\n  \"expression\": {\n    \"gradient\": {\n      \"min\": \"#D65C33\",\n      \"stop\": \"#FFFF4D\",\n      \"max\": \"#85AD33\"\n    },\n    \"legend\": {\n      \"median\": \"#000000\",\n      \"hover\": \"#FF0000\"\n    },\n    \"text\": \"#FFFFFF\"\n  },\n  \"ribbon\": \"#2196F3\"\n}";
      }
      public String getName() {
        return "profile03";
      }
    }
    ;
  }
  private static class profile03Initializer {
    static {
      _instance0.profile03Initializer();
    }
    static com.google.gwt.resources.client.TextResource get() {
      return profile03;
    }
  }
  public com.google.gwt.resources.client.TextResource profile03() {
    return profile03Initializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.resources.client.TextResource profile01;
  private static com.google.gwt.resources.client.TextResource profile02;
  private static com.google.gwt.resources.client.TextResource profile03;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      profile01(), 
      profile02(), 
      profile03(), 
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
        resourceMap.put("profile03", profile03());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'profile01': return this.@org.reactome.web.diagram.profiles.analysis.AnalysisColours.ProfileSource::profile01()();
      case 'profile02': return this.@org.reactome.web.diagram.profiles.analysis.AnalysisColours.ProfileSource::profile02()();
      case 'profile03': return this.@org.reactome.web.diagram.profiles.analysis.AnalysisColours.ProfileSource::profile03()();
    }
    return null;
  }-*/;
}
