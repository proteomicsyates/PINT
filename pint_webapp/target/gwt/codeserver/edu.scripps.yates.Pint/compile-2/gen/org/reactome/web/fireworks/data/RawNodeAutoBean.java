package org.reactome.web.fireworks.data;

public class RawNodeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.fireworks.data.RawNode> {
  private final org.reactome.web.fireworks.data.RawNode shim = new org.reactome.web.fireworks.data.RawNode() {
    public java.lang.Double getAngle()  {
      java.lang.Double toReturn = (java.lang.Double) RawNodeAutoBean.this.getWrapped().getAngle();
      return toReturn;
    }
    public java.lang.Double getRatio()  {
      java.lang.Double toReturn = (java.lang.Double) RawNodeAutoBean.this.getWrapped().getRatio();
      return toReturn;
    }
    public java.lang.Double getX()  {
      java.lang.Double toReturn = (java.lang.Double) RawNodeAutoBean.this.getWrapped().getX();
      return toReturn;
    }
    public java.lang.Double getY()  {
      java.lang.Double toReturn = (java.lang.Double) RawNodeAutoBean.this.getWrapped().getY();
      return toReturn;
    }
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) RawNodeAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) RawNodeAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public java.lang.String getStId()  {
      java.lang.String toReturn = (java.lang.String) RawNodeAutoBean.this.getWrapped().getStId();
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
  public RawNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.fireworks.data.RawNode wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.fireworks.data.RawNode as() {return shim;}
  public Class<org.reactome.web.fireworks.data.RawNode> getType() {return org.reactome.web.fireworks.data.RawNode.class;}
  @Override protected org.reactome.web.fireworks.data.RawNode createSimplePeer() {
    return new org.reactome.web.fireworks.data.RawNode() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.fireworks.data.RawNodeAutoBean.this.data;
      public java.lang.Double getAngle()  {
        return (java.lang.Double) RawNodeAutoBean.this.getOrReify("angle");
      }
      public java.lang.Double getRatio()  {
        return (java.lang.Double) RawNodeAutoBean.this.getOrReify("ratio");
      }
      public java.lang.Double getX()  {
        return (java.lang.Double) RawNodeAutoBean.this.getOrReify("x");
      }
      public java.lang.Double getY()  {
        return (java.lang.Double) RawNodeAutoBean.this.getOrReify("y");
      }
      public java.lang.Long getDbId()  {
        return (java.lang.Long) RawNodeAutoBean.this.getOrReify("dbId");
      }
      public java.lang.String getName()  {
        return (java.lang.String) RawNodeAutoBean.this.getOrReify("name");
      }
      public java.lang.String getStId()  {
        return (java.lang.String) RawNodeAutoBean.this.getOrReify("stId");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.fireworks.data.RawNode as = as();
    value = as.getAngle();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawNodeAutoBean.this, "angle"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("angle", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("angle", value, propertyContext);
    value = as.getRatio();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawNodeAutoBean.this, "ratio"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("ratio", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("ratio", value, propertyContext);
    value = as.getX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawNodeAutoBean.this, "x"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("x", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("x", value, propertyContext);
    value = as.getY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawNodeAutoBean.this, "y"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("y", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("y", value, propertyContext);
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawNodeAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawNodeAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    value = as.getStId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawNodeAutoBean.this, "stId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stId", value, propertyContext);
  }
}
