package org.reactome.web.diagram.profiles.analysis.model;

public class ProfileGradientAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.analysis.model.ProfileGradient> {
  private final org.reactome.web.diagram.profiles.analysis.model.ProfileGradient shim = new org.reactome.web.diagram.profiles.analysis.model.ProfileGradient() {
    public java.lang.String getMax()  {
      java.lang.String toReturn = (java.lang.String) ProfileGradientAutoBean.this.getWrapped().getMax();
      return toReturn;
    }
    public java.lang.String getMin()  {
      java.lang.String toReturn = (java.lang.String) ProfileGradientAutoBean.this.getWrapped().getMin();
      return toReturn;
    }
    public java.lang.String getStop()  {
      java.lang.String toReturn = (java.lang.String) ProfileGradientAutoBean.this.getWrapped().getStop();
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
  public ProfileGradientAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ProfileGradientAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.analysis.model.ProfileGradient wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.analysis.model.ProfileGradient as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.analysis.model.ProfileGradient> getType() {return org.reactome.web.diagram.profiles.analysis.model.ProfileGradient.class;}
  @Override protected org.reactome.web.diagram.profiles.analysis.model.ProfileGradient createSimplePeer() {
    return new org.reactome.web.diagram.profiles.analysis.model.ProfileGradient() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.analysis.model.ProfileGradientAutoBean.this.data;
      public java.lang.String getMax()  {
        return (java.lang.String) ProfileGradientAutoBean.this.getOrReify("max");
      }
      public java.lang.String getMin()  {
        return (java.lang.String) ProfileGradientAutoBean.this.getOrReify("min");
      }
      public java.lang.String getStop()  {
        return (java.lang.String) ProfileGradientAutoBean.this.getOrReify("stop");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.analysis.model.ProfileGradient as = as();
    value = as.getMax();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileGradientAutoBean.this, "max"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("max", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("max", value, propertyContext);
    value = as.getMin();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileGradientAutoBean.this, "min"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("min", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("min", value, propertyContext);
    value = as.getStop();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileGradientAutoBean.this, "stop"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stop", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stop", value, propertyContext);
  }
}
