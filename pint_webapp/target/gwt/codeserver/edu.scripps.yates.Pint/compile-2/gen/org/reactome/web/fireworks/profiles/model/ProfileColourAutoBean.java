package org.reactome.web.fireworks.profiles.model;

public class ProfileColourAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.fireworks.profiles.model.ProfileColour> {
  private final org.reactome.web.fireworks.profiles.model.ProfileColour shim = new org.reactome.web.fireworks.profiles.model.ProfileColour() {
    public java.lang.String getFadeout()  {
      java.lang.String toReturn = (java.lang.String) ProfileColourAutoBean.this.getWrapped().getFadeout();
      return toReturn;
    }
    public java.lang.String getFlag()  {
      java.lang.String toReturn = (java.lang.String) ProfileColourAutoBean.this.getWrapped().getFlag();
      return toReturn;
    }
    public java.lang.String getHighlight()  {
      java.lang.String toReturn = (java.lang.String) ProfileColourAutoBean.this.getWrapped().getHighlight();
      return toReturn;
    }
    public java.lang.String getHit()  {
      java.lang.String toReturn = (java.lang.String) ProfileColourAutoBean.this.getWrapped().getHit();
      return toReturn;
    }
    public java.lang.String getInitial()  {
      java.lang.String toReturn = (java.lang.String) ProfileColourAutoBean.this.getWrapped().getInitial();
      return toReturn;
    }
    public java.lang.String getSelection()  {
      java.lang.String toReturn = (java.lang.String) ProfileColourAutoBean.this.getWrapped().getSelection();
      return toReturn;
    }
    public org.reactome.web.fireworks.profiles.model.ProfileGradient getEnrichment()  {
      org.reactome.web.fireworks.profiles.model.ProfileGradient toReturn = (org.reactome.web.fireworks.profiles.model.ProfileGradient) ProfileColourAutoBean.this.getWrapped().getEnrichment();
      if (toReturn != null) {
        if (ProfileColourAutoBean.this.isWrapped(toReturn)) {
          toReturn = ProfileColourAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.fireworks.profiles.model.ProfileGradientAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.fireworks.profiles.model.ProfileGradient getExpression()  {
      org.reactome.web.fireworks.profiles.model.ProfileGradient toReturn = (org.reactome.web.fireworks.profiles.model.ProfileGradient) ProfileColourAutoBean.this.getWrapped().getExpression();
      if (toReturn != null) {
        if (ProfileColourAutoBean.this.isWrapped(toReturn)) {
          toReturn = ProfileColourAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.fireworks.profiles.model.ProfileGradientAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    @Override public boolean equals(Object o) {
      return this == o || getWrapped().equals(o);
    }
    @Override public int hashCode() {
      return getWrapped().hashCode();
    }
    @Override public String toString() {
      return getWrapped().toString();
    }
  };
  { com.google.gwt.core.client.impl.WeakMapping.set(shim, com.google.web.bindery.autobean.shared.AutoBean.class.getName(), this); }
  public ProfileColourAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ProfileColourAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.fireworks.profiles.model.ProfileColour wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.fireworks.profiles.model.ProfileColour as() {return shim;}
  public Class<org.reactome.web.fireworks.profiles.model.ProfileColour> getType() {return org.reactome.web.fireworks.profiles.model.ProfileColour.class;}
  @Override protected org.reactome.web.fireworks.profiles.model.ProfileColour createSimplePeer() {
    return new org.reactome.web.fireworks.profiles.model.ProfileColour() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.fireworks.profiles.model.ProfileColourAutoBean.this.data;
      public java.lang.String getFadeout()  {
        return (java.lang.String) ProfileColourAutoBean.this.getOrReify("fadeout");
      }
      public java.lang.String getFlag()  {
        return (java.lang.String) ProfileColourAutoBean.this.getOrReify("flag");
      }
      public java.lang.String getHighlight()  {
        return (java.lang.String) ProfileColourAutoBean.this.getOrReify("highlight");
      }
      public java.lang.String getHit()  {
        return (java.lang.String) ProfileColourAutoBean.this.getOrReify("hit");
      }
      public java.lang.String getInitial()  {
        return (java.lang.String) ProfileColourAutoBean.this.getOrReify("initial");
      }
      public java.lang.String getSelection()  {
        return (java.lang.String) ProfileColourAutoBean.this.getOrReify("selection");
      }
      public org.reactome.web.fireworks.profiles.model.ProfileGradient getEnrichment()  {
        return (org.reactome.web.fireworks.profiles.model.ProfileGradient) ProfileColourAutoBean.this.getOrReify("enrichment");
      }
      public org.reactome.web.fireworks.profiles.model.ProfileGradient getExpression()  {
        return (org.reactome.web.fireworks.profiles.model.ProfileGradient) ProfileColourAutoBean.this.getOrReify("expression");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.fireworks.profiles.model.ProfileColour as = as();
    value = as.getFadeout();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileColourAutoBean.this, "fadeout"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("fadeout", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fadeout", value, propertyContext);
    value = as.getFlag();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileColourAutoBean.this, "flag"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("flag", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("flag", value, propertyContext);
    value = as.getHighlight();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileColourAutoBean.this, "highlight"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("highlight", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("highlight", value, propertyContext);
    value = as.getHit();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileColourAutoBean.this, "hit"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("hit", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("hit", value, propertyContext);
    value = as.getInitial();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileColourAutoBean.this, "initial"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("initial", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("initial", value, propertyContext);
    value = as.getSelection();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileColourAutoBean.this, "selection"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("selection", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("selection", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEnrichment());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileColourAutoBean.this, "enrichment"),
      org.reactome.web.fireworks.profiles.model.ProfileGradient.class
    );
    if (visitor.visitReferenceProperty("enrichment", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("enrichment", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExpression());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileColourAutoBean.this, "expression"),
      org.reactome.web.fireworks.profiles.model.ProfileGradient.class
    );
    if (visitor.visitReferenceProperty("expression", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("expression", bean, propertyContext);
  }
}
