package org.reactome.web.diagram.common.validation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class AbstractValidator_Resources_default_InlineClientBundleGenerator implements org.reactome.web.diagram.common.validation.AbstractValidator.Resources {
  private static AbstractValidator_Resources_default_InlineClientBundleGenerator _instance0 = new AbstractValidator_Resources_default_InlineClientBundleGenerator();
  private void getCSSInitializer() {
    getCSS = new org.reactome.web.diagram.common.validation.AbstractValidator.ResourceCSS() {
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
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".org-reactome-web-diagram-common-validation-AbstractValidator-ResourceCSS-container {\n  width : " + ("20px")  + ";\n  height : " + ("20px")  + ";\n  float : " + ("right")  + ";\n  margin-top : " + ("8px")  + ";\n  margin-right : " + ("4px")  + ";\n}\n.org-reactome-web-diagram-common-validation-AbstractValidator-ResourceCSS-icon {\n  cursor : " + ("help")  + ";\n  width : " + ("100%")  + ";\n  height : " + ("auto")  + ";\n}\n")) : ((".org-reactome-web-diagram-common-validation-AbstractValidator-ResourceCSS-container {\n  width : " + ("20px")  + ";\n  height : " + ("20px")  + ";\n  float : " + ("left")  + ";\n  margin-top : " + ("8px")  + ";\n  margin-left : " + ("4px")  + ";\n}\n.org-reactome-web-diagram-common-validation-AbstractValidator-ResourceCSS-icon {\n  cursor : " + ("help")  + ";\n  width : " + ("100%")  + ";\n  height : " + ("auto")  + ";\n}\n"));
      }
      public java.lang.String container() {
        return "org-reactome-web-diagram-common-validation-AbstractValidator-ResourceCSS-container";
      }
      public java.lang.String icon() {
        return "org-reactome-web-diagram-common-validation-AbstractValidator-ResourceCSS-icon";
      }
    }
    ;
  }
  private static class getCSSInitializer {
    static {
      _instance0.getCSSInitializer();
    }
    static org.reactome.web.diagram.common.validation.AbstractValidator.ResourceCSS get() {
      return getCSS;
    }
  }
  public org.reactome.web.diagram.common.validation.AbstractValidator.ResourceCSS getCSS() {
    return getCSSInitializer.get();
  }
  private void inValidInitializer() {
    inValid = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "inValid",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage),
      0, 0, 24, 24, false, false
    );
  }
  private static class inValidInitializer {
    static {
      _instance0.inValidInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return inValid;
    }
  }
  public com.google.gwt.resources.client.ImageResource inValid() {
    return inValidInitializer.get();
  }
  private void validInitializer() {
    valid = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
      "valid",
      com.google.gwt.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
      0, 0, 24, 24, false, false
    );
  }
  private static class validInitializer {
    static {
      _instance0.validInitializer();
    }
    static com.google.gwt.resources.client.ImageResource get() {
      return valid;
    }
  }
  public com.google.gwt.resources.client.ImageResource valid() {
    return validInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.reactome.web.diagram.common.validation.AbstractValidator.ResourceCSS getCSS;
  private static final java.lang.String externalImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAB6ElEQVR42r1WTUtCQRR9P0S0hYJrF64U3OsvKHHhTwgj0JWuNILcSC50pbTQlS7UTLIPKIJoI31gklKK1ULQSsl8b3rnQYOi780I4cCFYebcc+bduffOEwSNMRqN1kRR3JIkqSiJYvtnMBjDMMca9oARlh3D4VAvOx98NZtSfWeHXDidpGw0kqJOpxjmWMMeMMDCh4t8MplsfPd6nzWfj5T0ekqqZsAACx/4apLLJ9ns12rkxGqdI6rKazder2LHFsvcPnzgCw61k68DcGQ2Lzzpe6VC/sZrobAQA19wgGsu5vInflQXnPzP3kolKtDN51Vx4ADXzJ3gkhBHrVh3czkq0MlmNbHgAidNxa9WSywZDJpO7UyGCryk09oXL3OBU0lhWWn7cXeXmS3PqRQVaCWTTDw4wS3IBXN46XIxHVqJBBV42t9n4sEJbkGuks50EanZUyxGBRrRKBMPTnAL435/zALDGnt7VKAeiRAeH3BzC9TDYSrwEArxC/CG6D4YpAJ3gQB/iHgv+dbvpwKsmpm5ZN40PXc4lFSFndnt/GnKW2iwU5tNMWaHnS403lbRjMe562CmVfA2u97VFRXAfKlmx9Ou0eBYzU61XfM8OGWTiVx7POTa7SaL0pr54KzkyVzJo//fvy2/ZQKvEN1VOpYAAAAASUVORK5CYII=";
  private static final java.lang.String externalImage0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAB4klEQVR42r2Wz0sCURDH9y+J/qXSNPTkIYLsFP4AwUMXT+KtvyBdk7DwokhhYAc7VnQKJIxS0DQ1XS12d3rfB25tur+iHBl2mTfzebPz5r2nIJjIZDJZVRQlrKpqSVGVp/H7+AOKd25jY/ARnIokSSssWGwNW6p4LVK4GCav6KW1wzWueIcNY/CBL2JswWVZ9g+mg9FB7YBcaZcGNVL4wBcxiDWFs0z26t06bZ1sWYJ/KmIQC4ZR5j44+I58juEzRSwYYM3VnH3i228yX/QlYOnWBIuEOjqFrafXKVaO8ed3O1hgaq3IOkFxZ9yOJ8jd5ghSfajq7GCByVuYzRTN3mQdw0PFELH9wCeoNWpz42CCLbANU46UIo7gG5kNarw2OLw/7VPgODDnAybYAsvi+fsmsqP5uzzNJHGRWOgDJtjC6H30YQRCLbdPt3W2aCmqlaZSr5gmArbhBOiMq8crklWZktUkt21mN6k5bHL4y/iF/Dm/9QRGJfKIHuqMOxyGjFOXKZ7xTPbP903hWonMFnm3sEs9qUc/pXxftlynr0W2aNOdwg51pa4Gb4/ato4TrU3tbLRgIcjBKvvFz+KWcN1Gs3tUYEFxB9hpY91RsZTD7t+P66VcOEu5Mpdy6f/135ZPzGAPVYhFhxcAAAAASUVORK5CYII=";
  private static com.google.gwt.resources.client.ImageResource inValid;
  private static com.google.gwt.resources.client.ImageResource valid;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      getCSS(), 
      inValid(), 
      valid(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("getCSS", getCSS());
        resourceMap.put("inValid", inValid());
        resourceMap.put("valid", valid());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'getCSS': return this.@org.reactome.web.diagram.common.validation.AbstractValidator.Resources::getCSS()();
      case 'inValid': return this.@org.reactome.web.diagram.common.validation.AbstractValidator.Resources::inValid()();
      case 'valid': return this.@org.reactome.web.diagram.common.validation.AbstractValidator.Resources::valid()();
    }
    return null;
  }-*/;
}
