package org.reactome.web.fireworks.profiles.model;

public class ProfileAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.fireworks.profiles.model.Profile> {
  private final org.reactome.web.fireworks.profiles.model.Profile shim = new org.reactome.web.fireworks.profiles.model.Profile() {
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) ProfileAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public org.reactome.web.fireworks.profiles.model.ProfileColour getEdge()  {
      org.reactome.web.fireworks.profiles.model.ProfileColour toReturn = (org.reactome.web.fireworks.profiles.model.ProfileColour) ProfileAutoBean.this.getWrapped().getEdge();
      if (toReturn != null) {
        if (ProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = ProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.fireworks.profiles.model.ProfileColourAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.fireworks.profiles.model.ProfileColour getNode()  {
      org.reactome.web.fireworks.profiles.model.ProfileColour toReturn = (org.reactome.web.fireworks.profiles.model.ProfileColour) ProfileAutoBean.this.getWrapped().getNode();
      if (toReturn != null) {
        if (ProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = ProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.fireworks.profiles.model.ProfileColourAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.fireworks.profiles.model.ProfileColour getThumbnail()  {
      org.reactome.web.fireworks.profiles.model.ProfileColour toReturn = (org.reactome.web.fireworks.profiles.model.ProfileColour) ProfileAutoBean.this.getWrapped().getThumbnail();
      if (toReturn != null) {
        if (ProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = ProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.fireworks.profiles.model.ProfileColourAutoBean(getFactory(), toReturn).as();
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
  public ProfileAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ProfileAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.fireworks.profiles.model.Profile wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.fireworks.profiles.model.Profile as() {return shim;}
  public Class<org.reactome.web.fireworks.profiles.model.Profile> getType() {return org.reactome.web.fireworks.profiles.model.Profile.class;}
  @Override protected org.reactome.web.fireworks.profiles.model.Profile createSimplePeer() {
    return new org.reactome.web.fireworks.profiles.model.Profile() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.fireworks.profiles.model.ProfileAutoBean.this.data;
      public java.lang.String getName()  {
        return (java.lang.String) ProfileAutoBean.this.getOrReify("name");
      }
      public org.reactome.web.fireworks.profiles.model.ProfileColour getEdge()  {
        return (org.reactome.web.fireworks.profiles.model.ProfileColour) ProfileAutoBean.this.getOrReify("edge");
      }
      public org.reactome.web.fireworks.profiles.model.ProfileColour getNode()  {
        return (org.reactome.web.fireworks.profiles.model.ProfileColour) ProfileAutoBean.this.getOrReify("node");
      }
      public org.reactome.web.fireworks.profiles.model.ProfileColour getThumbnail()  {
        return (org.reactome.web.fireworks.profiles.model.ProfileColour) ProfileAutoBean.this.getOrReify("thumbnail");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.fireworks.profiles.model.Profile as = as();
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEdge());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileAutoBean.this, "edge"),
      org.reactome.web.fireworks.profiles.model.ProfileColour.class
    );
    if (visitor.visitReferenceProperty("edge", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("edge", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getNode());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileAutoBean.this, "node"),
      org.reactome.web.fireworks.profiles.model.ProfileColour.class
    );
    if (visitor.visitReferenceProperty("node", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("node", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getThumbnail());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ProfileAutoBean.this, "thumbnail"),
      org.reactome.web.fireworks.profiles.model.ProfileColour.class
    );
    if (visitor.visitReferenceProperty("thumbnail", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("thumbnail", bean, propertyContext);
  }
}
