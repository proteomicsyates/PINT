package org.reactome.web.diagram.data.layout;

public class ShapeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Shape> {
  private final org.reactome.web.diagram.data.layout.Shape shim = new org.reactome.web.diagram.data.layout.Shape() {
    public java.lang.Boolean getEmpty()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) ShapeAutoBean.this.getWrapped().getEmpty();
      return toReturn;
    }
    public java.lang.Double getR()  {
      java.lang.Double toReturn = (java.lang.Double) ShapeAutoBean.this.getWrapped().getR();
      return toReturn;
    }
    public java.lang.Double getR1()  {
      java.lang.Double toReturn = (java.lang.Double) ShapeAutoBean.this.getWrapped().getR1();
      return toReturn;
    }
    public java.lang.String getS()  {
      java.lang.String toReturn = (java.lang.String) ShapeAutoBean.this.getWrapped().getS();
      return toReturn;
    }
    public java.lang.String getType()  {
      java.lang.String toReturn = (java.lang.String) ShapeAutoBean.this.getWrapped().getType();
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getA()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) ShapeAutoBean.this.getWrapped().getA();
      if (toReturn != null) {
        if (ShapeAutoBean.this.isWrapped(toReturn)) {
          toReturn = ShapeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getB()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) ShapeAutoBean.this.getWrapped().getB();
      if (toReturn != null) {
        if (ShapeAutoBean.this.isWrapped(toReturn)) {
          toReturn = ShapeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getC()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) ShapeAutoBean.this.getWrapped().getC();
      if (toReturn != null) {
        if (ShapeAutoBean.this.isWrapped(toReturn)) {
          toReturn = ShapeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
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
  public ShapeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ShapeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Shape wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Shape as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Shape> getType() {return org.reactome.web.diagram.data.layout.Shape.class;}
  @Override protected org.reactome.web.diagram.data.layout.Shape createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Shape() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.ShapeAutoBean.this.data;
      public java.lang.Boolean getEmpty()  {
        return (java.lang.Boolean) ShapeAutoBean.this.getOrReify("empty");
      }
      public java.lang.Double getR()  {
        return (java.lang.Double) ShapeAutoBean.this.getOrReify("r");
      }
      public java.lang.Double getR1()  {
        return (java.lang.Double) ShapeAutoBean.this.getOrReify("r1");
      }
      public java.lang.String getS()  {
        return (java.lang.String) ShapeAutoBean.this.getOrReify("s");
      }
      public java.lang.String getType()  {
        return (java.lang.String) ShapeAutoBean.this.getOrReify("type");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getA()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) ShapeAutoBean.this.getOrReify("a");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getB()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) ShapeAutoBean.this.getOrReify("b");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getC()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) ShapeAutoBean.this.getOrReify("c");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Shape as = as();
    value = as.getEmpty();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ShapeAutoBean.this, "empty"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("empty", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("empty", value, propertyContext);
    value = as.getR();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ShapeAutoBean.this, "r"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("r", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("r", value, propertyContext);
    value = as.getR1();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ShapeAutoBean.this, "r1"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("r1", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("r1", value, propertyContext);
    value = as.getS();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ShapeAutoBean.this, "s"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("s", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("s", value, propertyContext);
    value = as.getType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ShapeAutoBean.this, "type"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("type", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("type", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getA());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ShapeAutoBean.this, "a"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("a", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("a", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getB());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ShapeAutoBean.this, "b"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("b", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("b", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getC());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ShapeAutoBean.this, "c"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("c", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("c", bean, propertyContext);
  }
}
