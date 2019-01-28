package org.reactome.web.diagram.data.interactors.raw;

public class RawResourceAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.raw.RawResource> {
  private final org.reactome.web.diagram.data.interactors.raw.RawResource shim = new org.reactome.web.diagram.data.interactors.raw.RawResource() {
    public java.lang.Boolean getActive()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) RawResourceAutoBean.this.getWrapped().getActive();
      return toReturn;
    }
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) RawResourceAutoBean.this.getWrapped().getName();
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
  public RawResourceAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawResourceAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.raw.RawResource wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.raw.RawResource as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.raw.RawResource> getType() {return org.reactome.web.diagram.data.interactors.raw.RawResource.class;}
  @Override protected org.reactome.web.diagram.data.interactors.raw.RawResource createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.raw.RawResource() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.raw.RawResourceAutoBean.this.data;
      public java.lang.Boolean getActive()  {
        return (java.lang.Boolean) RawResourceAutoBean.this.getOrReify("active");
      }
      public java.lang.String getName()  {
        return (java.lang.String) RawResourceAutoBean.this.getOrReify("name");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.raw.RawResource as = as();
    value = as.getActive();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawResourceAutoBean.this, "active"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("active", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("active", value, propertyContext);
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawResourceAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
  }
}
