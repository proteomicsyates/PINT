package org.reactome.web.diagram.context;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class ContextInfoPanel_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.context.ContextInfoPanel.Resources {
  private static ContextInfoPanel_Resources_default_InlineClientBundleGenerator _instance0 = new ContextInfoPanel_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.context.ContextInfoPanel.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-outerPanel {\n  float : " + ("right")  + ";\n  min-width : " + ("330px")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel {\n  float : " + ("right")  + ";\n  width : " + ("19%")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel button {\n  color : " + ("white")  + ";\n  border-radius : " + ("10px"+ " " +"0"+ " " +"0"+ " " +"10px")  + ";\n  background : " + ("none"+ " " +"#58c3e5")  + ";\n  font-size : " + ("x-small")  + ";\n  outline : " + ("none")  + ";\n  width : " + ("65px")  + ";\n  height : ") + (("50px")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel button:hover {\n  background : " + ("none"+ " " +"#1e94d0")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel button:disabled {\n  background : " + ("none"+ " " +"#58c3e5")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel div {\n  text-align : " + ("center")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonSelected {\n  background : " + ("none"+ " " +"#066b9e")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-container {\n  float : " + ("right")  + ";\n  width : " + ("78%")  + ";\n  height : " + ("150px")  + ";\n  overflow : " + ("hidden")  + ";\n  margin-right : " + ("5px")  + ";\n}\n")) : ((".org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-outerPanel {\n  float : " + ("left")  + ";\n  min-width : " + ("330px")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel {\n  float : " + ("left")  + ";\n  width : " + ("19%")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel button {\n  color : " + ("white")  + ";\n  border-radius : " + ("10px"+ " " +"0"+ " " +"0"+ " " +"10px")  + ";\n  background : " + ("none"+ " " +"#58c3e5")  + ";\n  font-size : " + ("x-small")  + ";\n  outline : " + ("none")  + ";\n  width : " + ("65px")  + ";\n  height : ") + (("50px")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel button:hover {\n  background : " + ("none"+ " " +"#1e94d0")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel button:disabled {\n  background : " + ("none"+ " " +"#58c3e5")  + ";\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel div {\n  text-align : " + ("center")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonSelected {\n  background : " + ("none"+ " " +"#066b9e")  + " !important;\n}\n.org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-container {\n  float : " + ("left")  + ";\n  width : " + ("78%")  + ";\n  height : " + ("150px")  + ";\n  overflow : " + ("hidden")  + ";\n  margin-left : " + ("5px")  + ";\n}\n"));
      }
      public java.lang.String buttonSelected() {
        return "org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonSelected";
      }
      public java.lang.String buttonsPanel() {
        return "org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-buttonsPanel";
      }
      public java.lang.String container() {
        return "org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-container";
      }
      public java.lang.String outerPanel() {
        return "org-reactome-web-diagram-context-ContextInfoPanel-ResourceCSS-outerPanel";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.context.ContextInfoPanel.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.context.ContextInfoPanel.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void interactorsInitializer() {
    interactors = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "interactors",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 26, 24, false, false
    );
  }
  private static class interactorsInitializer {
    static {
      _instance0.interactorsInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return interactors;
    }
  }
  public com.google.gwt.resources.client.ImageResource interactors() {
    return interactorsInitializer.get();
  }
  private void moleculesInitializer() {
    molecules = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "molecules",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 26, 24, false, false
    );
  }
  private static class moleculesInitializer {
    static {
      _instance0.moleculesInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return molecules;
    }
  }
  public com.google.gwt.resources.client.ImageResource molecules() {
    return moleculesInitializer.get();
  }
  private void pathwaysInitializer() {
    pathways = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "pathways",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
      0, 0, 24, 17, false, false
    );
  }
  private static class pathwaysInitializer {
    static {
      _instance0.pathwaysInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return pathways;
    }
  }
  public com.google.gwt.resources.client.ImageResource pathways() {
    return pathwaysInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.context.ContextInfoPanel.ResourceCSS getCSS;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAACRElEQVR42rWWMWgTYRTHr7VWW8SCdVGHioi7VCpY3RwcdJBCnQpaoSiC2qVDBTOJQrd2cqiLgxgnQazSId0ESanoFYUMGUKjnF5srk0uTS7R38OXEmIOruHrg5f7vnd3//977/t/38Wy2rRYLNZZq9U+1f7Z73K5PJjP5w9Zpu1PC0ulUvuMklSr1TmqCJqJiH01TfSyVUXSQ2MkxWLxGHgfQ4jeGiEJguA6YDkFdfFNHW/hvqkq3ihohdY9TqfT+yuVykXGz/AHO834Gi9Ncx0rFApHNTYO+LqS2Mh4qK1sRZKJRKILkO+Q3CbLS4DfZL6ILzRU8SiqfB3HOQDGFbCGt4O5XK5PwJLJ5F6Ze553OJPJ9GSz2V6RML5CFWeiJk4njvCOh+fxNRJ8QbjDYvLa9/3jKttZ5g5elblt291SqaxHFKeKUQFvUuIWuJOyBpeV5B7BDb0ZMH8q8eYX2zUB8uTcgvAG419K5EN0R4lExjb+JYK7IfurJJXch+SqVvVQF/55fb2Yf4i6PtLqFiTr4N21NOv3rNOAPt8hP67rHtQHF2QfRVUbOCekAvwH/hOSW9sPoKrTGpwRdvyJtmK5nhVVT9STiGJCKIr+74bsJXR/DsARrhckJkqSU0DaqYRLpVLplLVbphUvNwhlWvYdCZ1nPI/HjJFJxQBOQVRUQqf5UI3H43uMEdK6k4B+DpHxqtFWAvgqdGPuQDBRvrCzIZ/yTRGQMSJZi7oSGw3RnN0VNUL2Tv9ufZNPCCdJf9izfwH1a69ZNdz8HAAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAB0klEQVR42rWWv0vDUBDHowjqpKgoqKAOijgIbo7+BSIiOjm46eCksx0cXMXVQUTdHEQEJwnSQXBRKLgU7RDB34lpkzQ/jd/DVwkhTRP6enA0TV/uk7v7vnsVhDrM87yDnz+7yefzrZIktQu8DcEf/ZAVi8VurhDXdWcB+gqDkOEJb9AiQE4YhHtXjSjdaxhkGMaA0ABrYllo5OVyeUhRlM5GZHQK/wZgWBTFFu4AkrBpmqOAfMLvuANs255G4GfKgpXMgSiWuEIg2w0EfomQ8zHvfmh+hJHyHMeZSRVMVdUulGcKzR0J3rcsayIqm0BW24khuq73I9g7/A0u4eH1QqHQFpgCZpWMbKzdTLrLVyIC6PBrBpfhXrWM0tTfrBaEeoM33qHsqHyVXhEYrpIC6RpyH2MvPY/1u/BDXM/JstzxD4qBWJjGPaHslykIfFXTtD58bjEYZX4PV4IvSduBJkhFukd0oESAHhJKf9+PMcTJQVDjpKpJfDmjDNgPbpraA7RGoogBUZkvw6P/gmqcyWSaU/Q469cwrPngsZlzCUDnPMbTXpz8GUjkMqJqQJ7QloW6IbTHcLIO0lSJAtE24Dp4S6VSL2C3cIP9BcuSoiun7i/kBJGONvEyUAAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAARCAYAAADHeGwwAAABu0lEQVR42q2Vu0vDUBTGo4Oi+EDBwU1wEVREFEFwURwU9E8QXBxc3NWh4KLiKji6ObQuXQqCD8RJHA04tIIQqZCQmFhrX8mt38GbGkNu20gO/Mgl59zz+HJvK0lNWCwWa2WMPbIfs8GDFKUh4UfVZ3gXj6yArus9SKiBT86NLMttUtTmOM6ebduroTaVSqXxIAQFdiqVylJYfV/4+DowgAVShUJhKKDANiZYDlWgGmAo8AaS5XJ52ldgl2QSKDGC+El6epH40dO8IMkWT3gMSRZoXSwWh3nxpGEYvW5iTdO68WhB7CZ8qg8mUZdgysOMt3NsPEDcBbgEDi+SAtdUDNyDJ4oJUqOZbxTHFPNg0d2E0UfRxAR8V3SqyI91gn8/3YPS6ISNYYJTkR+J5+A/5OvZIFwZzlDtnJNIp9PtlmX1Y32Xy+UG6jWBCdaw/0hRlA6RBK/0MfzaYeM6Nu43kpCaoZstvIBwmgHH9AvJT0iiesmpa5oQ8RnwzMnQHaKfl5qOvotG3DZzh6hAVWCqqnbVAklv0zT76HzT2nvO61k2m+0UXNT3PwX+a/l8fhDar7Bfo/8KvLI33Jhv91ujqweVPWwAAAAASUVORK5CYII=";
  private static com.google.gwt.resources.client.ImageResource interactors;
  private static com.google.gwt.resources.client.ImageResource molecules;
  private static com.google.gwt.resources.client.ImageResource pathways;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      interactors(), 
      molecules(), 
      pathways(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("interactors", interactors());
        resourceMap.put("molecules", molecules());
        resourceMap.put("pathways", pathways());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.context.ContextInfoPanel.Resources::getCSS()();
      case 'interactors': return this.@org.reactome.web.diagram.context.ContextInfoPanel.Resources::interactors()();
      case 'molecules': return this.@org.reactome.web.diagram.context.ContextInfoPanel.Resources::molecules()();
      case 'pathways': return this.@org.reactome.web.diagram.context.ContextInfoPanel.Resources::pathways()();
    }
    return null;
  }-*/;
}
