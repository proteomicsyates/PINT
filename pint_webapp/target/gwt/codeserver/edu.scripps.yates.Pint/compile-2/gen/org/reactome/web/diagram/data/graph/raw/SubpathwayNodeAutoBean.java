package org.reactome.web.diagram.data.graph.raw;

public class SubpathwayNodeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.graph.raw.SubpathwayNode> {
  private final org.reactome.web.diagram.data.graph.raw.SubpathwayNode shim = new org.reactome.web.diagram.data.graph.raw.SubpathwayNode() {
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) SubpathwayNodeAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) SubpathwayNodeAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getStId()  {
      java.lang.String toReturn = (java.lang.String) SubpathwayNodeAutoBean.this.getWrapped().getStId();
      return toReturn;
    }
    public java.util.List getEvents()  {
      java.util.List toReturn = (java.util.List) SubpathwayNodeAutoBean.this.getWrapped().getEvents();
      if (toReturn != null) {
        if (SubpathwayNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = SubpathwayNodeAutoBean.this.getFromWrapper(toReturn);
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
  public SubpathwayNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public SubpathwayNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.graph.raw.SubpathwayNode wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.graph.raw.SubpathwayNode as() {return shim;}
  public Class<org.reactome.web.diagram.data.graph.raw.SubpathwayNode> getType() {return org.reactome.web.diagram.data.graph.raw.SubpathwayNode.class;}
  @Override protected org.reactome.web.diagram.data.graph.raw.SubpathwayNode createSimplePeer() {
    return new org.reactome.web.diagram.data.graph.raw.SubpathwayNode() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.graph.raw.SubpathwayNodeAutoBean.this.data;
      public java.lang.Long getDbId()  {
        return (java.lang.Long) SubpathwayNodeAutoBean.this.getOrReify("dbId");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) SubpathwayNodeAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getStId()  {
        return (java.lang.String) SubpathwayNodeAutoBean.this.getOrReify("stId");
      }
      public java.util.List getEvents()  {
        return (java.util.List) SubpathwayNodeAutoBean.this.getOrReify("events");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.graph.raw.SubpathwayNode as = as();
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SubpathwayNodeAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SubpathwayNodeAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getStId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SubpathwayNodeAutoBean.this, "stId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stId", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEvents());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SubpathwayNodeAutoBean.this, "events"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("events", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("events", bean, propertyContext);
  }
}
