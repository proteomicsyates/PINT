package org.reactome.web.diagram.data.graph.raw;

public class EntityNodeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.graph.raw.EntityNode> {
  private final org.reactome.web.diagram.data.graph.raw.EntityNode shim = new org.reactome.web.diagram.data.graph.raw.EntityNode() {
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) EntityNodeAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.Long getSpeciesID()  {
      java.lang.Long toReturn = (java.lang.Long) EntityNodeAutoBean.this.getWrapped().getSpeciesID();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) EntityNodeAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getIdentifier()  {
      java.lang.String toReturn = (java.lang.String) EntityNodeAutoBean.this.getWrapped().getIdentifier();
      return toReturn;
    }
    public java.lang.String getSchemaClass()  {
      java.lang.String toReturn = (java.lang.String) EntityNodeAutoBean.this.getWrapped().getSchemaClass();
      return toReturn;
    }
    public java.lang.String getStId()  {
      java.lang.String toReturn = (java.lang.String) EntityNodeAutoBean.this.getWrapped().getStId();
      return toReturn;
    }
    public java.util.List getChildren()  {
      java.util.List toReturn = (java.util.List) EntityNodeAutoBean.this.getWrapped().getChildren();
      if (toReturn != null) {
        if (EntityNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EntityNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getDiagramIds()  {
      java.util.List toReturn = (java.util.List) EntityNodeAutoBean.this.getWrapped().getDiagramIds();
      if (toReturn != null) {
        if (EntityNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EntityNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getParents()  {
      java.util.List toReturn = (java.util.List) EntityNodeAutoBean.this.getWrapped().getParents();
      if (toReturn != null) {
        if (EntityNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EntityNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getGeneNames()  {
      java.util.List toReturn = (java.util.List) EntityNodeAutoBean.this.getWrapped().getGeneNames();
      if (toReturn != null) {
        if (EntityNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = EntityNodeAutoBean.this.getFromWrapper(toReturn);
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
  public EntityNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public EntityNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.graph.raw.EntityNode wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.graph.raw.EntityNode as() {return shim;}
  public Class<org.reactome.web.diagram.data.graph.raw.EntityNode> getType() {return org.reactome.web.diagram.data.graph.raw.EntityNode.class;}
  @Override protected org.reactome.web.diagram.data.graph.raw.EntityNode createSimplePeer() {
    return new org.reactome.web.diagram.data.graph.raw.EntityNode() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.graph.raw.EntityNodeAutoBean.this.data;
      public java.lang.Long getDbId()  {
        return (java.lang.Long) EntityNodeAutoBean.this.getOrReify("dbId");
      }
      public java.lang.Long getSpeciesID()  {
        return (java.lang.Long) EntityNodeAutoBean.this.getOrReify("speciesID");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) EntityNodeAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getIdentifier()  {
        return (java.lang.String) EntityNodeAutoBean.this.getOrReify("identifier");
      }
      public java.lang.String getSchemaClass()  {
        return (java.lang.String) EntityNodeAutoBean.this.getOrReify("schemaClass");
      }
      public java.lang.String getStId()  {
        return (java.lang.String) EntityNodeAutoBean.this.getOrReify("stId");
      }
      public java.util.List getChildren()  {
        return (java.util.List) EntityNodeAutoBean.this.getOrReify("children");
      }
      public java.util.List getDiagramIds()  {
        return (java.util.List) EntityNodeAutoBean.this.getOrReify("diagramIds");
      }
      public java.util.List getParents()  {
        return (java.util.List) EntityNodeAutoBean.this.getOrReify("parents");
      }
      public java.util.List getGeneNames()  {
        return (java.util.List) EntityNodeAutoBean.this.getOrReify("geneNames");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.graph.raw.EntityNode as = as();
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getSpeciesID();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "speciesID"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("speciesID", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("speciesID", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getIdentifier();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "identifier"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("identifier", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("identifier", value, propertyContext);
    value = as.getSchemaClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "schemaClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("schemaClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("schemaClass", value, propertyContext);
    value = as.getStId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "stId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stId", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getChildren());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "children"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("children", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("children", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getDiagramIds());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "diagramIds"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("diagramIds", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("diagramIds", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getParents());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "parents"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("parents", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("parents", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getGeneNames());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityNodeAutoBean.this, "geneNames"),
      new Class<?>[] {java.util.List.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("geneNames", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("geneNames", bean, propertyContext);
  }
}
