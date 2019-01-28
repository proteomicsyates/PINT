package org.reactome.web.diagram.data.layout;

public class DiagramAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Diagram> {
  private final org.reactome.web.diagram.data.layout.Diagram shim = new org.reactome.web.diagram.data.layout.Diagram() {
    public java.lang.Boolean getForNormalDraw()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) DiagramAutoBean.this.getWrapped().getForNormalDraw();
      return toReturn;
    }
    public java.lang.Boolean getIsDisease()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) DiagramAutoBean.this.getWrapped().getIsDisease();
      return toReturn;
    }
    public java.lang.Integer getMaxX()  {
      java.lang.Integer toReturn = (java.lang.Integer) DiagramAutoBean.this.getWrapped().getMaxX();
      return toReturn;
    }
    public java.lang.Integer getMaxY()  {
      java.lang.Integer toReturn = (java.lang.Integer) DiagramAutoBean.this.getWrapped().getMaxY();
      return toReturn;
    }
    public java.lang.Integer getMinX()  {
      java.lang.Integer toReturn = (java.lang.Integer) DiagramAutoBean.this.getWrapped().getMinX();
      return toReturn;
    }
    public java.lang.Integer getMinY()  {
      java.lang.Integer toReturn = (java.lang.Integer) DiagramAutoBean.this.getWrapped().getMinY();
      return toReturn;
    }
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) DiagramAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) DiagramAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getStableId()  {
      java.lang.String toReturn = (java.lang.String) DiagramAutoBean.this.getWrapped().getStableId();
      return toReturn;
    }
    public java.util.List getCompartments()  {
      java.util.List toReturn = (java.util.List) DiagramAutoBean.this.getWrapped().getCompartments();
      if (toReturn != null) {
        if (DiagramAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getEdges()  {
      java.util.List toReturn = (java.util.List) DiagramAutoBean.this.getWrapped().getEdges();
      if (toReturn != null) {
        if (DiagramAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getLinks()  {
      java.util.List toReturn = (java.util.List) DiagramAutoBean.this.getWrapped().getLinks();
      if (toReturn != null) {
        if (DiagramAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getNodes()  {
      java.util.List toReturn = (java.util.List) DiagramAutoBean.this.getWrapped().getNodes();
      if (toReturn != null) {
        if (DiagramAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getNotes()  {
      java.util.List toReturn = (java.util.List) DiagramAutoBean.this.getWrapped().getNotes();
      if (toReturn != null) {
        if (DiagramAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getShadows()  {
      java.util.List toReturn = (java.util.List) DiagramAutoBean.this.getWrapped().getShadows();
      if (toReturn != null) {
        if (DiagramAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramAutoBean.this.getFromWrapper(toReturn);
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
  public DiagramAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public DiagramAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Diagram wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Diagram as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Diagram> getType() {return org.reactome.web.diagram.data.layout.Diagram.class;}
  @Override protected org.reactome.web.diagram.data.layout.Diagram createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Diagram() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.DiagramAutoBean.this.data;
      public java.lang.Boolean getForNormalDraw()  {
        return (java.lang.Boolean) DiagramAutoBean.this.getOrReify("forNormalDraw");
      }
      public java.lang.Boolean getIsDisease()  {
        return (java.lang.Boolean) DiagramAutoBean.this.getOrReify("isDisease");
      }
      public java.lang.Integer getMaxX()  {
        return (java.lang.Integer) DiagramAutoBean.this.getOrReify("maxX");
      }
      public java.lang.Integer getMaxY()  {
        return (java.lang.Integer) DiagramAutoBean.this.getOrReify("maxY");
      }
      public java.lang.Integer getMinX()  {
        return (java.lang.Integer) DiagramAutoBean.this.getOrReify("minX");
      }
      public java.lang.Integer getMinY()  {
        return (java.lang.Integer) DiagramAutoBean.this.getOrReify("minY");
      }
      public java.lang.Long getDbId()  {
        return (java.lang.Long) DiagramAutoBean.this.getOrReify("dbId");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) DiagramAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getStableId()  {
        return (java.lang.String) DiagramAutoBean.this.getOrReify("stableId");
      }
      public java.util.List getCompartments()  {
        return (java.util.List) DiagramAutoBean.this.getOrReify("compartments");
      }
      public java.util.List getEdges()  {
        return (java.util.List) DiagramAutoBean.this.getOrReify("edges");
      }
      public java.util.List getLinks()  {
        return (java.util.List) DiagramAutoBean.this.getOrReify("links");
      }
      public java.util.List getNodes()  {
        return (java.util.List) DiagramAutoBean.this.getOrReify("nodes");
      }
      public java.util.List getNotes()  {
        return (java.util.List) DiagramAutoBean.this.getOrReify("notes");
      }
      public java.util.List getShadows()  {
        return (java.util.List) DiagramAutoBean.this.getOrReify("shadows");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Diagram as = as();
    value = as.getForNormalDraw();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "forNormalDraw"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("forNormalDraw", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("forNormalDraw", value, propertyContext);
    value = as.getIsDisease();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "isDisease"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isDisease", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isDisease", value, propertyContext);
    value = as.getMaxX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "maxX"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("maxX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxX", value, propertyContext);
    value = as.getMaxY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "maxY"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("maxY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxY", value, propertyContext);
    value = as.getMinX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "minX"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("minX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minX", value, propertyContext);
    value = as.getMinY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "minY"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("minY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minY", value, propertyContext);
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getStableId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "stableId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stableId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stableId", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getCompartments());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "compartments"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Compartment.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("compartments", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("compartments", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEdges());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "edges"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Edge.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("edges", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("edges", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getLinks());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "links"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Link.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("links", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("links", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getNodes());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "nodes"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Node.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("nodes", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("nodes", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getNotes());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "notes"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Note.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("notes", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("notes", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getShadows());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramAutoBean.this, "shadows"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Shadow.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("shadows", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("shadows", bean, propertyContext);
  }
}
