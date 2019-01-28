package org.reactome.web.fireworks.data;

public class RawEdgeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.fireworks.data.RawEdge> {
  private final org.reactome.web.fireworks.data.RawEdge shim = new org.reactome.web.fireworks.data.RawEdge() {
    public java.lang.Long getFrom()  {
      java.lang.Long toReturn = (java.lang.Long) RawEdgeAutoBean.this.getWrapped().getFrom();
      return toReturn;
    }
    public java.lang.Long getTo()  {
      java.lang.Long toReturn = (java.lang.Long) RawEdgeAutoBean.this.getWrapped().getTo();
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
  public RawEdgeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawEdgeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.fireworks.data.RawEdge wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.fireworks.data.RawEdge as() {return shim;}
  public Class<org.reactome.web.fireworks.data.RawEdge> getType() {return org.reactome.web.fireworks.data.RawEdge.class;}
  @Override protected org.reactome.web.fireworks.data.RawEdge createSimplePeer() {
    return new org.reactome.web.fireworks.data.RawEdge() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.fireworks.data.RawEdgeAutoBean.this.data;
      public java.lang.Long getFrom()  {
        return (java.lang.Long) RawEdgeAutoBean.this.getOrReify("from");
      }
      public java.lang.Long getTo()  {
        return (java.lang.Long) RawEdgeAutoBean.this.getOrReify("to");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.fireworks.data.RawEdge as = as();
    value = as.getFrom();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawEdgeAutoBean.this, "from"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("from", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("from", value, propertyContext);
    value = as.getTo();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawEdgeAutoBean.this, "to"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("to", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("to", value, propertyContext);
  }
}
