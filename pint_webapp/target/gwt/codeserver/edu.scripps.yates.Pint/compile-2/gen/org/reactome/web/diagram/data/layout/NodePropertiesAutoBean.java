package org.reactome.web.diagram.data.layout;

public class NodePropertiesAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.NodeProperties> {
  private final org.reactome.web.diagram.data.layout.NodeProperties shim = new org.reactome.web.diagram.data.layout.NodeProperties() {
    public java.lang.Double getHeight()  {
      java.lang.Double toReturn = (java.lang.Double) NodePropertiesAutoBean.this.getWrapped().getHeight();
      return toReturn;
    }
    public java.lang.Double getWidth()  {
      java.lang.Double toReturn = (java.lang.Double) NodePropertiesAutoBean.this.getWrapped().getWidth();
      return toReturn;
    }
    public java.lang.Double getX()  {
      java.lang.Double toReturn = (java.lang.Double) NodePropertiesAutoBean.this.getWrapped().getX();
      return toReturn;
    }
    public java.lang.Double getY()  {
      java.lang.Double toReturn = (java.lang.Double) NodePropertiesAutoBean.this.getWrapped().getY();
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
  public NodePropertiesAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public NodePropertiesAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.NodeProperties wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.NodeProperties as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.NodeProperties> getType() {return org.reactome.web.diagram.data.layout.NodeProperties.class;}
  @Override protected org.reactome.web.diagram.data.layout.NodeProperties createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.NodeProperties() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.NodePropertiesAutoBean.this.data;
      public java.lang.Double getHeight()  {
        return (java.lang.Double) NodePropertiesAutoBean.this.getOrReify("height");
      }
      public java.lang.Double getWidth()  {
        return (java.lang.Double) NodePropertiesAutoBean.this.getOrReify("width");
      }
      public java.lang.Double getX()  {
        return (java.lang.Double) NodePropertiesAutoBean.this.getOrReify("x");
      }
      public java.lang.Double getY()  {
        return (java.lang.Double) NodePropertiesAutoBean.this.getOrReify("y");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.NodeProperties as = as();
    value = as.getHeight();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodePropertiesAutoBean.this, "height"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("height", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("height", value, propertyContext);
    value = as.getWidth();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodePropertiesAutoBean.this, "width"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("width", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("width", value, propertyContext);
    value = as.getX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodePropertiesAutoBean.this, "x"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("x", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("x", value, propertyContext);
    value = as.getY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodePropertiesAutoBean.this, "y"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("y", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("y", value, propertyContext);
  }
}
