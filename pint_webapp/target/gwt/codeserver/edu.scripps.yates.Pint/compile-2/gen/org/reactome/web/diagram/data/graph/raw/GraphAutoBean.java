package org.reactome.web.diagram.data.graph.raw;

public class GraphAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.graph.raw.Graph> {
  private final org.reactome.web.diagram.data.graph.raw.Graph shim = new org.reactome.web.diagram.data.graph.raw.Graph() {
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) GraphAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.String getStId()  {
      java.lang.String toReturn = (java.lang.String) GraphAutoBean.this.getWrapped().getStId();
      return toReturn;
    }
    public java.util.List getNodes()  {
      java.util.List toReturn = (java.util.List) GraphAutoBean.this.getWrapped().getNodes();
      if (toReturn != null) {
        if (GraphAutoBean.this.isWrapped(toReturn)) {
          toReturn = GraphAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getEdges()  {
      java.util.List toReturn = (java.util.List) GraphAutoBean.this.getWrapped().getEdges();
      if (toReturn != null) {
        if (GraphAutoBean.this.isWrapped(toReturn)) {
          toReturn = GraphAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getSubpathways()  {
      java.util.List toReturn = (java.util.List) GraphAutoBean.this.getWrapped().getSubpathways();
      if (toReturn != null) {
        if (GraphAutoBean.this.isWrapped(toReturn)) {
          toReturn = GraphAutoBean.this.getFromWrapper(toReturn);
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
  public GraphAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public GraphAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.graph.raw.Graph wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.graph.raw.Graph as() {return shim;}
  public Class<org.reactome.web.diagram.data.graph.raw.Graph> getType() {return org.reactome.web.diagram.data.graph.raw.Graph.class;}
  @Override protected org.reactome.web.diagram.data.graph.raw.Graph createSimplePeer() {
    return new org.reactome.web.diagram.data.graph.raw.Graph() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.graph.raw.GraphAutoBean.this.data;
      public java.lang.Long getDbId()  {
        return (java.lang.Long) GraphAutoBean.this.getOrReify("dbId");
      }
      public java.lang.String getStId()  {
        return (java.lang.String) GraphAutoBean.this.getOrReify("stId");
      }
      public java.util.List getNodes()  {
        return (java.util.List) GraphAutoBean.this.getOrReify("nodes");
      }
      public java.util.List getEdges()  {
        return (java.util.List) GraphAutoBean.this.getOrReify("edges");
      }
      public java.util.List getSubpathways()  {
        return (java.util.List) GraphAutoBean.this.getOrReify("subpathways");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.graph.raw.Graph as = as();
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(GraphAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getStId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(GraphAutoBean.this, "stId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stId", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getNodes());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(GraphAutoBean.this, "nodes"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.graph.raw.EntityNode.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("nodes", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("nodes", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEdges());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(GraphAutoBean.this, "edges"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.graph.raw.EventNode.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("edges", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("edges", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getSubpathways());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(GraphAutoBean.this, "subpathways"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.graph.raw.SubpathwayNode.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("subpathways", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("subpathways", bean, propertyContext);
  }
}
