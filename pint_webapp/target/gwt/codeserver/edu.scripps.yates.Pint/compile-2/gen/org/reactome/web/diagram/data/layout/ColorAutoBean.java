package org.reactome.web.diagram.data.layout;

public class ColorAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Color> {
  private final org.reactome.web.diagram.data.layout.Color shim = new org.reactome.web.diagram.data.layout.Color() {
    public java.lang.Integer getB()  {
      java.lang.Integer toReturn = (java.lang.Integer) ColorAutoBean.this.getWrapped().getB();
      return toReturn;
    }
    public java.lang.Integer getG()  {
      java.lang.Integer toReturn = (java.lang.Integer) ColorAutoBean.this.getWrapped().getG();
      return toReturn;
    }
    public java.lang.Integer getR()  {
      java.lang.Integer toReturn = (java.lang.Integer) ColorAutoBean.this.getWrapped().getR();
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
  public ColorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ColorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Color wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Color as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Color> getType() {return org.reactome.web.diagram.data.layout.Color.class;}
  @Override protected org.reactome.web.diagram.data.layout.Color createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Color() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.ColorAutoBean.this.data;
      public java.lang.Integer getB()  {
        return (java.lang.Integer) ColorAutoBean.this.getOrReify("b");
      }
      public java.lang.Integer getG()  {
        return (java.lang.Integer) ColorAutoBean.this.getOrReify("g");
      }
      public java.lang.Integer getR()  {
        return (java.lang.Integer) ColorAutoBean.this.getOrReify("r");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Color as = as();
    value = as.getB();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ColorAutoBean.this, "b"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("b", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("b", value, propertyContext);
    value = as.getG();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ColorAutoBean.this, "g"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("g", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("g", value, propertyContext);
    value = as.getR();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ColorAutoBean.this, "r"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("r", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("r", value, propertyContext);
  }
}
