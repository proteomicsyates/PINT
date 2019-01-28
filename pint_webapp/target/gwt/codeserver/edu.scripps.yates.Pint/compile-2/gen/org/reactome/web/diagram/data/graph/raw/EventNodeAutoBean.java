package org.reactome.web.diagram.data.graph.raw;

public class EventNodeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.graph.raw.EventNode> {
  private final org.reactome.web.diagram.data.graph.raw.EventNode shim = new org.reactome.web.diagram.data.graph.raw.EventNode() {
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) EventNodeAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.Long getSpeciesID()  {
      java.lang.Long toReturn = (java.lang.Long) EventNodeAutoBean.this.getWrapped().getSpeciesID();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) EventNodeAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getSchemaClass()  {
      java.lang.String toReturn = (java.lang.String) EventNodeAutoBean.this.getWrapped().getSchemaClass();
      return toReturn;
    }
    public java.lang.String getStId()  {
      java.lang.String toReturn = (java.lang.String) EventNodeAutoBean.this.getWrapped().getStId();
      return toReturn;
    }
    public java.util.List getActivators()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getActivators();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getCatalysts()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getCatalysts();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getDiagramIds()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getDiagramIds();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getFollowing()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getFollowing();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getInhibitors()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getInhibitors();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getInputs()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getInputs();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getOutputs()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getOutputs();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getPreceding()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getPreceding();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getRequirements()  {
      java.util.List toReturn = (java.util.List) EventNodeAutoBean.this.getWrapped().getRequirements();
      if (toReturn != null) {
        if (EventNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EventNodeAutoBean.this.getFromWrapper(toReturn);
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
  public EventNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public EventNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.graph.raw.EventNode wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.graph.raw.EventNode as() {return shim;}
  public Class<org.reactome.web.diagram.data.graph.raw.EventNode> getType() {return org.reactome.web.diagram.data.graph.raw.EventNode.class;}
  @Override protected org.reactome.web.diagram.data.graph.raw.EventNode createSimplePeer() {
    return new org.reactome.web.diagram.data.graph.raw.EventNode() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.graph.raw.EventNodeAutoBean.this.data;
      public java.lang.Long getDbId()  {
        return (java.lang.Long) EventNodeAutoBean.this.getOrReify("dbId");
      }
      public java.lang.Long getSpeciesID()  {
        return (java.lang.Long) EventNodeAutoBean.this.getOrReify("speciesID");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) EventNodeAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getSchemaClass()  {
        return (java.lang.String) EventNodeAutoBean.this.getOrReify("schemaClass");
      }
      public java.lang.String getStId()  {
        return (java.lang.String) EventNodeAutoBean.this.getOrReify("stId");
      }
      public java.util.List getActivators()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("activators");
      }
      public java.util.List getCatalysts()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("catalysts");
      }
      public java.util.List getDiagramIds()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("diagramIds");
      }
      public java.util.List getFollowing()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("following");
      }
      public java.util.List getInhibitors()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("inhibitors");
      }
      public java.util.List getInputs()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("inputs");
      }
      public java.util.List getOutputs()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("outputs");
      }
      public java.util.List getPreceding()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("preceding");
      }
      public java.util.List getRequirements()  {
        return (java.util.List) EventNodeAutoBean.this.getOrReify("requirements");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.graph.raw.EventNode as = as();
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getSpeciesID();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "speciesID"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("speciesID", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("speciesID", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getSchemaClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "schemaClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("schemaClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("schemaClass", value, propertyContext);
    value = as.getStId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "stId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stId", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getActivators());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "activators"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("activators", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("activators", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getCatalysts());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "catalysts"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("catalysts", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("catalysts", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getDiagramIds());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "diagramIds"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("diagramIds", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("diagramIds", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getFollowing());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "following"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("following", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("following", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInhibitors());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "inhibitors"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("inhibitors", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("inhibitors", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInputs());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "inputs"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("inputs", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("inputs", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getOutputs());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "outputs"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("outputs", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("outputs", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPreceding());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "preceding"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("preceding", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("preceding", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getRequirements());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventNodeAutoBean.this, "requirements"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("requirements", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("requirements", bean, propertyContext);
  }
}
