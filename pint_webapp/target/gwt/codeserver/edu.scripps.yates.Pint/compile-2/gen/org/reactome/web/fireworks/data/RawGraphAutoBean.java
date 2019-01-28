package org.reactome.web.fireworks.data;

public class RawGraphAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.fireworks.data.RawGraph> {
  private final org.reactome.web.fireworks.data.RawGraph shim = new org.reactome.web.fireworks.data.RawGraph() {
    public java.lang.Long getSpeciesId()  {
      java.lang.Long toReturn = (java.lang.Long) RawGraphAutoBean.this.getWrapped().getSpeciesId();
      return toReturn;
    }
    public java.lang.String getSpeciesName()  {
      java.lang.String toReturn = (java.lang.String) RawGraphAutoBean.this.getWrapped().getSpeciesName();
      return toReturn;
    }
    public java.util.List getEdges()  {
      java.util.List toReturn = (java.util.List) RawGraphAutoBean.this.getWrapped().getEdges();
      if (toReturn != null) {
        if (RawGraphAutoBean.this.isWrapped(toReturn)) {
          toReturn = RawGraphAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getNodes()  {
      java.util.List toReturn = (java.util.List) RawGraphAutoBean.this.getWrapped().getNodes();
      if (toReturn != null) {
        if (RawGraphAutoBean.this.isWrapped(toReturn)) {
          toReturn = RawGraphAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
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
  public RawGraphAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawGraphAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.fireworks.data.RawGraph wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.fireworks.data.RawGraph as() {return shim;}
  public Class<org.reactome.web.fireworks.data.RawGraph> getType() {return org.reactome.web.fireworks.data.RawGraph.class;}
  @Override protected org.reactome.web.fireworks.data.RawGraph createSimplePeer() {
    return new org.reactome.web.fireworks.data.RawGraph() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.fireworks.data.RawGraphAutoBean.this.data;
      public java.lang.Long getSpeciesId()  {
        return (java.lang.Long) RawGraphAutoBean.this.getOrReify("speciesId");
      }
      public java.lang.String getSpeciesName()  {
        return (java.lang.String) RawGraphAutoBean.this.getOrReify("speciesName");
      }
      public java.util.List getEdges()  {
        return (java.util.List) RawGraphAutoBean.this.getOrReify("edges");
      }
      public java.util.List getNodes()  {
        return (java.util.List) RawGraphAutoBean.this.getOrReify("nodes");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.fireworks.data.RawGraph as = as();
    value = as.getSpeciesId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawGraphAutoBean.this, "speciesId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("speciesId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("speciesId", value, propertyContext);
    value = as.getSpeciesName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawGraphAutoBean.this, "speciesName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("speciesName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("speciesName", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEdges());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawGraphAutoBean.this, "edges"),
      new Class<?>[] {java.util.List.class, org.reactome.web.fireworks.data.RawEdge.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("edges", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("edges", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getNodes());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawGraphAutoBean.this, "nodes"),
      new Class<?>[] {java.util.List.class, org.reactome.web.fireworks.data.RawNode.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("nodes", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("nodes", bean, propertyContext);
  }
}
