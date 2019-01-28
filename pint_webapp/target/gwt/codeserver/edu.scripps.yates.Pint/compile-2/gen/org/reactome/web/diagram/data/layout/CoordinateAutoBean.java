package org.reactome.web.diagram.data.layout;

public class CoordinateAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Coordinate> {
  private final org.reactome.web.diagram.data.layout.Coordinate shim = new org.reactome.web.diagram.data.layout.Coordinate() {
    public java.lang.Double getX()  {
      java.lang.Double toReturn = (java.lang.Double) CoordinateAutoBean.this.getWrapped().getX();
      return toReturn;
    }
    public java.lang.Double getY()  {
      java.lang.Double toReturn = (java.lang.Double) CoordinateAutoBean.this.getWrapped().getY();
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate add(org.reactome.web.diagram.data.layout.Coordinate value)  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) CoordinateAutoBean.this.getWrapped().add(value);
      if (toReturn != null) {
        if (CoordinateAutoBean.this.isWrapped(toReturn)) {
          toReturn = CoordinateAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      CoordinateAutoBean.this.call("add", toReturn, value);
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate divide(double factor)  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) CoordinateAutoBean.this.getWrapped().divide(factor);
      if (toReturn != null) {
        if (CoordinateAutoBean.this.isWrapped(toReturn)) {
          toReturn = CoordinateAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      CoordinateAutoBean.this.call("divide", toReturn, factor);
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate minus(org.reactome.web.diagram.data.layout.Coordinate value)  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) CoordinateAutoBean.this.getWrapped().minus(value);
      if (toReturn != null) {
        if (CoordinateAutoBean.this.isWrapped(toReturn)) {
          toReturn = CoordinateAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      CoordinateAutoBean.this.call("minus", toReturn, value);
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate multiply(double factor)  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) CoordinateAutoBean.this.getWrapped().multiply(factor);
      if (toReturn != null) {
        if (CoordinateAutoBean.this.isWrapped(toReturn)) {
          toReturn = CoordinateAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      CoordinateAutoBean.this.call("multiply", toReturn, factor);
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate transform(double factor,org.reactome.web.diagram.data.layout.Coordinate delta)  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) CoordinateAutoBean.this.getWrapped().transform(factor,delta);
      if (toReturn != null) {
        if (CoordinateAutoBean.this.isWrapped(toReturn)) {
          toReturn = CoordinateAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      CoordinateAutoBean.this.call("transform", toReturn, factor,delta);
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
  public CoordinateAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public CoordinateAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Coordinate wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Coordinate as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Coordinate> getType() {return org.reactome.web.diagram.data.layout.Coordinate.class;}
  @Override protected org.reactome.web.diagram.data.layout.Coordinate createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Coordinate() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.CoordinateAutoBean.this.data;
      public java.lang.Double getX()  {
        return (java.lang.Double) CoordinateAutoBean.this.getOrReify("x");
      }
      public java.lang.Double getY()  {
        return (java.lang.Double) CoordinateAutoBean.this.getOrReify("y");
      }
      public org.reactome.web.diagram.data.layout.Coordinate add(org.reactome.web.diagram.data.layout.Coordinate value)  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.add(CoordinateAutoBean.this, value);
      }
      public org.reactome.web.diagram.data.layout.Coordinate divide(double factor)  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.divide(CoordinateAutoBean.this, factor);
      }
      public org.reactome.web.diagram.data.layout.Coordinate minus(org.reactome.web.diagram.data.layout.Coordinate value)  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.minus(CoordinateAutoBean.this, value);
      }
      public org.reactome.web.diagram.data.layout.Coordinate multiply(double factor)  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.multiply(CoordinateAutoBean.this, factor);
      }
      public org.reactome.web.diagram.data.layout.Coordinate transform(double factor,org.reactome.web.diagram.data.layout.Coordinate delta)  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.transform(CoordinateAutoBean.this, factor, delta);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Coordinate as = as();
    value = as.getX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CoordinateAutoBean.this, "x"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("x", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("x", value, propertyContext);
    value = as.getY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CoordinateAutoBean.this, "y"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("y", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("y", value, propertyContext);
  }
}
