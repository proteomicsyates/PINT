package org.reactome.web.fireworks.search.searchonfire.pager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class Pager_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.search.searchonfire.pager.Pager.Resources {
  private static Pager_Resources_default_InlineClientBundleGenerator _instance0 = new Pager_Resources_default_InlineClientBundleGenerator();
  private void endIconInitializer() {
    endIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "endIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 10, 10, false, false
    );
  }
  private static class endIconInitializer {
    static {
      _instance0.endIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return endIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource endIcon() {
    return endIconInitializer.get();
  }
  private void nextIconInitializer() {
    nextIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "nextIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 10, 10, false, false
    );
  }
  private static class nextIconInitializer {
    static {
      _instance0.nextIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return nextIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource nextIcon() {
    return nextIconInitializer.get();
  }
  private void previousIconInitializer() {
    previousIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "previousIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage1),
      0, 0, 10, 10, false, false
    );
  }
  private static class previousIconInitializer {
    static {
      _instance0.previousIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return previousIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource previousIcon() {
    return previousIconInitializer.get();
  }
  private void startIconInitializer() {
    startIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "startIcon",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage2),
      0, 0, 10, 10, false, false
    );
  }
  private static class startIconInitializer {
    static {
      _instance0.startIconInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return startIcon;
    }
  }
  public com.google.gwt.resources.client.ImageResource startIcon() {
    return startIconInitializer.get();
  }
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.search.searchonfire.pager.Pager.SuggestionPanelCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-mainPanel {\n  background-color : " + ("#1e94d0")  + ";\n  opacity : " + ("0.8")  + ";\n  width : " + ("100%")  + ";\n  overflow : " + ("auto")  + " !important;\n  outline : " + ("none")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("small")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn {\n  background-color : " + ("#58c3e5")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("12px")  + ";\n  color : ") + (("white")  + ";\n  float : " + ("right")  + ";\n  margin-right : " + ("3px")  + ";\n  margin-top : " + ("5px")  + ";\n  padding : " + ("2px"+ " " +"4px"+ " " +"5px"+ " " +"5px")  + ";\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn span {\n  vertical-align : " + ("middle")  + " !important;\n  display : " + ("inline-block")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  height : " + ("12px")  + ";\n  width : " + ("auto") ) + (";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn:disabled, .org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn:disabled:active {\n  background-color : " + ("#a39da6")  + ";\n  cursor : " + ("default")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn {\n  background-color : " + ("#58c3e5")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("12px")  + ";\n  color : " + ("white")  + ";\n  float : ") + (("left")  + ";\n  margin-left : " + ("3px")  + ";\n  margin-top : " + ("5px")  + ";\n  padding : " + ("2px"+ " " +"5px"+ " " +"5px"+ " " +"4px")  + ";\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn span {\n  vertical-align : " + ("middle")  + " !important;\n  display : " + ("inline-block")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  height : " + ("12px")  + ";\n  width : " + ("auto")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn:hover {\n  background-color : " + ("#177dc0") ) + (";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn:disabled, .org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn:disabled:active {\n  background-color : " + ("#a39da6")  + ";\n  cursor : " + ("default")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-label {\n  color : " + ("white")  + ";\n  width : " + ("60%")  + ";\n  text-align : " + ("center")  + ";\n  margin-right : " + ("auto")  + ";\n  margin-left : " + ("auto")  + ";\n  margin-top : ") + (("5px")  + ";\n}\n")) : ((".org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-mainPanel {\n  background-color : " + ("#1e94d0")  + ";\n  opacity : " + ("0.8")  + ";\n  width : " + ("100%")  + ";\n  overflow : " + ("auto")  + " !important;\n  outline : " + ("none")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n  font-size : " + ("small")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn {\n  background-color : " + ("#58c3e5")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("12px")  + ";\n  color : ") + (("white")  + ";\n  float : " + ("left")  + ";\n  margin-left : " + ("3px")  + ";\n  margin-top : " + ("5px")  + ";\n  padding : " + ("2px"+ " " +"5px"+ " " +"5px"+ " " +"4px")  + ";\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn span {\n  vertical-align : " + ("middle")  + " !important;\n  display : " + ("inline-block")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  height : " + ("12px")  + ";\n  width : " + ("auto") ) + (";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn:hover {\n  background-color : " + ("#177dc0")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn:disabled, .org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn:disabled:active {\n  background-color : " + ("#a39da6")  + ";\n  cursor : " + ("default")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn {\n  background-color : " + ("#58c3e5")  + ";\n  border : " + ("none")  + ";\n  border-radius : " + ("12px")  + ";\n  color : " + ("white")  + ";\n  float : ") + (("right")  + ";\n  margin-right : " + ("3px")  + ";\n  margin-top : " + ("5px")  + ";\n  padding : " + ("2px"+ " " +"4px"+ " " +"5px"+ " " +"5px")  + ";\n  outline : " + ("none")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn span {\n  vertical-align : " + ("middle")  + " !important;\n  display : " + ("inline-block")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn img {\n  vertical-align : " + ("middle")  + " !important;\n  height : " + ("12px")  + ";\n  width : " + ("auto")  + ";\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn:hover {\n  background-color : " + ("#177dc0") ) + (";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn:active:hover {\n  background-color : " + ("#07558b")  + ";\n  cursor : " + ("pointer")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn:disabled, .org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn:disabled:active {\n  background-color : " + ("#a39da6")  + ";\n  cursor : " + ("default")  + " !important;\n}\n.org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-label {\n  color : " + ("white")  + ";\n  width : " + ("60%")  + ";\n  text-align : " + ("center")  + ";\n  margin-left : " + ("auto")  + ";\n  margin-right : " + ("auto")  + ";\n  margin-top : ") + (("5px")  + ";\n}\n"));
      }
      public java.lang.String label() {
        return "org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-label";
      }
      public java.lang.String leftBtn() {
        return "org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-leftBtn";
      }
      public java.lang.String mainPanel() {
        return "org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-mainPanel";
      }
      public java.lang.String rightBtn() {
        return "org-reactome-web-fireworks-search-searchonfire-pager-Pager-SuggestionPanelCSS-rightBtn";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.search.searchonfire.pager.Pager.SuggestionPanelCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.search.searchonfire.pager.Pager.SuggestionPanelCSS getCSS() {
    return getCSSInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAi0lEQVR42mP4DwR///6dcuXKFTYGLAAolw/ELQz//v37AcTfgfgLUCD327dv0sgKgeI3QIaBFf6HAiD7FxA/BGqo//79uzxOhUgafgPxfSDeiFchmoZ/lCuEuvUz0K3duDzzE4i/ARU03759mx3DM1D8E6igZv/+/SxYgwca4CX19fVMOAK8EognAAB6eDWgZKtNEAAAAABJRU5ErkJggg==";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAgElEQVR42mP4DwR///6dcuXKFTYGfODfv38/gfg7EH8Basj99u2bNE6F/6EAyP4NxI+BGhq+f/8uj1MhkoY/QPwIiDfjVYik4S8QfwPaUEaswmJ8Vn8AKpiOyzOgEPgMVND96tUrHmzB8wOEQb69ffs2O9bggQZ4yapVq5jxhTcAMyH46BZmvEkAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAj0lEQVR42mNgwAOuXLnC9vfv3yn/gQCrgm/fvkkDFeT++/fvCxB/B+KfKAq+f/8uD1TQAJR4DMS//0MBikIgZzMQPwLiP//RAFgh0IQyIOMbEP/9jwPAFBYTpRAGgBqmAwU+4LQaGbx69YoHqKEHKPEZJIlTIQzcvn2bHer7H1D8E194M6xatYoZqKEEZCoACg746fJ+7WsAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAmUlEQVR42mP4+/dvCxDnM2ABV65cYQPKTf0PBAwg4t+/fzeQFXz79k0aqCAXKP4FiL8D8Q8Uhd+/f5cHKqgH8h8C8a//UICiEIg3AvF9IP79Hw0gK/yHTQFFCm8A3dYNpD8juw2rQpBnbt++zQ7U0AzkfwPinzgVwsD+/ftZgBpqQIpBisAKgQITgLgSW4DX19czAeVKQIYBACBINaFUl/NCAAAAAElFTkSuQmCC";
  private static com.google.gwt.resources.client.ImageResource endIcon;
  private static com.google.gwt.resources.client.ImageResource nextIcon;
  private static com.google.gwt.resources.client.ImageResource previousIcon;
  private static com.google.gwt.resources.client.ImageResource startIcon;
  private static org.reactome.web.fireworks.search.searchonfire.pager.Pager.SuggestionPanelCSS getCSS;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      endIcon(), 
      nextIcon(), 
      previousIcon(), 
      startIcon(), 
      getCSS(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("endIcon", endIcon());
        resourceMap.put("nextIcon", nextIcon());
        resourceMap.put("previousIcon", previousIcon());
        resourceMap.put("startIcon", startIcon());
        resourceMap.put("getCSS", getCSS());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'endIcon': return this.@org.reactome.web.fireworks.search.searchonfire.pager.Pager.Resources::endIcon()();
      case 'nextIcon': return this.@org.reactome.web.fireworks.search.searchonfire.pager.Pager.Resources::nextIcon()();
      case 'previousIcon': return this.@org.reactome.web.fireworks.search.searchonfire.pager.Pager.Resources::previousIcon()();
      case 'startIcon': return this.@org.reactome.web.fireworks.search.searchonfire.pager.Pager.Resources::startIcon()();
      case 'getCSS': return this.@org.reactome.web.fireworks.search.searchonfire.pager.Pager.Resources::getCSS()();
    }
    return null;
  }-*/;
}
