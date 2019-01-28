package org.reactome.web.fireworks.controls.top.illustrations;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class Illustrations_Resources_default_InlineClientBundleGenerator implements org.reactome.web.fireworks.controls.top.illustrations.Illustrations.Resources {
  private static Illustrations_Resources_default_InlineClientBundleGenerator _instance0 = new Illustrations_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.fireworks.controls.top.illustrations.Illustrations.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustrations {\n  width : " + ("350px")  + ";\n  min-height : " + ("50px")  + ";\n  padding : " + ("0"+ " " +"5px"+ " " +"5px"+ " " +"0")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustration {\n  clear : " + ("both")  + ";\n  margin : " + ("10px"+ " " +"5px"+ " " +"5px"+ " " +"0")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustration img {\n  float : " + ("right")  + ";\n  height : " + ("24px")  + ";\n  width : " + ("24px")  + ";\n  margin-top : " + ("2px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustration div {\n  float : ") + (("right")  + ";\n  font-size : " + ("small")  + ";\n  margin : " + ("5px"+ " " +"5px"+ " " +"0"+ " " +"0")  + ";\n  overflow : " + ("hidden")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  width : " + ("300px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-error {\n  color : " + ("#ee665a")  + ";\n  font-size : " + ("small")  + ";\n  margin : " + ("10px"+ " " +"9px"+ " " +"0"+ " " +"0")  + ";\n  overflow : " + ("hidden") ) + (";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  width : " + ("300px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-warning {\n  color : " + ("#d06f2a")  + ";\n  font-size : " + ("small")  + ";\n  margin : " + ("10px"+ " " +"9px"+ " " +"0"+ " " +"0")  + ";\n  overflow : " + ("hidden")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  width : " + ("300px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-loading {\n  color : ") + (("#61c2d0")  + ";\n  font-size : " + ("small")  + ";\n  margin : " + ("10px"+ " " +"9px"+ " " +"0"+ " " +"0")  + ";\n  overflow : " + ("hidden")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  width : " + ("300px")  + ";\n}\n")) : ((".org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustrations {\n  width : " + ("350px")  + ";\n  min-height : " + ("50px")  + ";\n  padding : " + ("0"+ " " +"0"+ " " +"5px"+ " " +"5px")  + ";\n  font-family : " + ("Arial"+ " " +"Unicode"+ " " +"MS"+ ","+ " " +"Arial"+ ","+ " " +"sans-serif")  + " !important;\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustration {\n  clear : " + ("both")  + ";\n  margin : " + ("10px"+ " " +"0"+ " " +"5px"+ " " +"5px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustration img {\n  float : " + ("left")  + ";\n  height : " + ("24px")  + ";\n  width : " + ("24px")  + ";\n  margin-top : " + ("2px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustration div {\n  float : ") + (("left")  + ";\n  font-size : " + ("small")  + ";\n  margin : " + ("5px"+ " " +"0"+ " " +"0"+ " " +"5px")  + ";\n  overflow : " + ("hidden")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  width : " + ("300px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-error {\n  color : " + ("#ee665a")  + ";\n  font-size : " + ("small")  + ";\n  margin : " + ("10px"+ " " +"0"+ " " +"0"+ " " +"9px")  + ";\n  overflow : " + ("hidden") ) + (";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  width : " + ("300px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-warning {\n  color : " + ("#d06f2a")  + ";\n  font-size : " + ("small")  + ";\n  margin : " + ("10px"+ " " +"0"+ " " +"0"+ " " +"9px")  + ";\n  overflow : " + ("hidden")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  width : " + ("300px")  + ";\n}\n.org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-loading {\n  color : ") + (("#61c2d0")  + ";\n  font-size : " + ("small")  + ";\n  margin : " + ("10px"+ " " +"0"+ " " +"0"+ " " +"9px")  + ";\n  overflow : " + ("hidden")  + ";\n  text-overflow : " + ("ellipsis")  + ";\n  white-space : " + ("nowrap")  + ";\n  width : " + ("300px")  + ";\n}\n"));
      }
      public java.lang.String error() {
        return "org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-error";
      }
      public java.lang.String illustration() {
        return "org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustration";
      }
      public java.lang.String illustrations() {
        return "org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-illustrations";
      }
      public java.lang.String loading() {
        return "org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-loading";
      }
      public java.lang.String warning() {
        return "org-reactome-web-fireworks-controls-top-illustrations-Illustrations-ResourceCSS-warning";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.fireworks.controls.top.illustrations.Illustrations.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.fireworks.controls.top.illustrations.Illustrations.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void illustrationInitializer() {
    illustration = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "illustration",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 32, 32, false, false
    );
  }
  private static class illustrationInitializer {
    static {
      _instance0.illustrationInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return illustration;
    }
  }
  public com.google.gwt.resources.client.ImageResource illustration() {
    return illustrationInitializer.get();
  }
  private void illustrationDisabledInitializer() {
    illustrationDisabled = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "illustrationDisabled",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 24, 24, false, false
    );
  }
  private static class illustrationDisabledInitializer {
    static {
      _instance0.illustrationDisabledInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return illustrationDisabled;
    }
  }
  public com.google.gwt.resources.client.ImageResource illustrationDisabled() {
    return illustrationDisabledInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.fireworks.controls.top.illustrations.Illustrations.ResourceCSS getCSS;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACkUlEQVR42s1Xy27TQBT1EhZ8BXwAn8AXIDZR3LIAIcSCSogFLBELKFIrEBuEqFQ2sEOAVLFgA0RpCo0KRUTQF0WtiB3bcR60TdzWduLLvSYxtpPY4+IUX+lIlj2+58zcx8xwHKOdnlk9lsqW0umc9IjPlub5nFjhcyX9D/AZ39E3GkNjubgslRFO8FnhMRJpCGCERv/QvwcmPpXZPJKeFSb4nGREIPZBMsgH+YpEfuZt8Tg6KBycuAcF8sm45OJJ/EGJkbwLhXyzzHwY5I6IgSuReiYcjXnZB4aDuHoEjMwJkzRgbEEBZa8F+20rVjRNC+6v1G0RxNVbap1sv7tch2HZO0VzqsNTovhyurtE9zoCXktNOP9BjgXjX6u2z4wjwMa00+HcTaYr4JXY9MTuzrcaPC82bIdn56RIcb9ZqPQToNkdMz1b5N2D+wn40TDAsP4u5eh7OQ4BQNwcnxWnggQ8WPsFWsvyxHKjacQigLgp/vkgAVPrW57Zk6n7LY+j2xjjyaVadAHITQLUsBC4+at6GyZcZBfmZajhuwaW2WUs4YgCVK6zpUJYEs4IDfi+Y8CoLwHfyJojbnlbt79f+ViGpxvbLAJ0ZgH9cKtTXm57iZWyiTki7baYBYSGYARntVDdg6UtHR5iUlIpnkOUsWMG2fXPKlMI8mECaDndRq1V0MzQzvcCVyM8CUPK8MaXCviqkNnEXTO8DIMaEWU4zZT6AG0mO2Yb6pjxVIa0YckIIvmJYyju1LDWMVHXEKuYkCuIS3kluBGxtuJ/QWArHrQZDcP6bkb+7fjaogqmNRwBT5ze4NuO3QcSwsW8DFc/lWPFmKtL9hxIEnEk+++H0kQcyxNxMUnE1SwRl9PDup7/BuxprrsseKxpAAAAAElFTkSuQmCC";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAABp0lEQVR42r1W2W6CQBTlS5p+V/sXJo1/1F9o+uyDwR0SYjAaxDW4REGMISy3cyZAUBAptN7kRJiZew5n7iwKQk40m83Xdrv9wfDV6XTmDE6IOdrQhzHCb6PRaLyw5E9G5DPQA/gYi5xC5K1W650lnQoQ3+KE3FxyNqjGEJQgj4DcWia5KIpvFcljEXCl5px1WMPhkEzTrARN0yBiXdUkLCgtl0sKgoBs2y4Fz/O4CLjAGS9F1uBFAq7rxna73S7BVdHpORwOsQA4+RJmD/VowK0A3OAdvyUEgDqm5ztLwLIsigICs9nsylkRAXDDwSJLYL1exwKYW1VVeftgMKDz+UySJBVxsBDCrZ85RSgcvl6W5bhtt9txURDByWQyyRNwcgVAkCQfjUaUjP1+zz9AUZRcgcU9AcMwOBHqoes6OY5DWbFare5P0b0ij8djKhqXyyW3yKll2uv1uP3j8ciTALxHQB222y1tNhsOOO33+9nL9Haj+b7PLZcBnKQ2WvKomE6nVDXg7uqoSB526MD6roJwE1qpC+hfj+unXDhPuTKfcun/9d+WH+s2k3XsGAXjAAAAAElFTkSuQmCC";
  private static com.google.gwt.resources.client.ImageResource illustration;
  private static com.google.gwt.resources.client.ImageResource illustrationDisabled;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      illustration(), 
      illustrationDisabled(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("illustration", illustration());
        resourceMap.put("illustrationDisabled", illustrationDisabled());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.fireworks.controls.top.illustrations.Illustrations.Resources::getCSS()();
      case 'illustration': return this.@org.reactome.web.fireworks.controls.top.illustrations.Illustrations.Resources::illustration()();
      case 'illustrationDisabled': return this.@org.reactome.web.fireworks.controls.top.illustrations.Illustrations.Resources::illustrationDisabled()();
    }
    return null;
  }-*/;
}
