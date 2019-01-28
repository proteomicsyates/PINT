package org.reactome.web.diagram.data.layout;

public class DiagramObjectAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.DiagramObject> {
  private final org.reactome.web.diagram.data.layout.DiagramObject shim = new org.reactome.web.diagram.data.layout.DiagramObject() {
    public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
      org.reactome.web.diagram.data.graph.model.GraphObject toReturn = (org.reactome.web.diagram.data.graph.model.GraphObject) DiagramObjectAutoBean.this.getWrapped().getGraphObject();
      if (toReturn != null) {
        if (DiagramObjectAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramObjectAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      return toReturn;
    }
    public double getMaxX()  {
      double toReturn = (double) DiagramObjectAutoBean.this.getWrapped().getMaxX();
      return toReturn;
    }
    public double getMaxY()  {
      double toReturn = (double) DiagramObjectAutoBean.this.getWrapped().getMaxY();
      return toReturn;
    }
    public double getMinX()  {
      double toReturn = (double) DiagramObjectAutoBean.this.getWrapped().getMinX();
      return toReturn;
    }
    public double getMinY()  {
      double toReturn = (double) DiagramObjectAutoBean.this.getWrapped().getMinY();
      return toReturn;
    }
    public java.lang.Boolean getIsDisease()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) DiagramObjectAutoBean.this.getWrapped().getIsDisease();
      return toReturn;
    }
    public java.lang.Boolean getIsFadeOut()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) DiagramObjectAutoBean.this.getWrapped().getIsFadeOut();
      return toReturn;
    }
    public java.lang.Long getId()  {
      java.lang.Long toReturn = (java.lang.Long) DiagramObjectAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.Long getReactomeId()  {
      java.lang.Long toReturn = (java.lang.Long) DiagramObjectAutoBean.this.getWrapped().getReactomeId();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) DiagramObjectAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getRenderableClass()  {
      java.lang.String toReturn = (java.lang.String) DiagramObjectAutoBean.this.getWrapped().getRenderableClass();
      return toReturn;
    }
    public java.lang.String getSchemaClass()  {
      java.lang.String toReturn = (java.lang.String) DiagramObjectAutoBean.this.getWrapped().getSchemaClass();
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) DiagramObjectAutoBean.this.getWrapped().getPosition();
      if (toReturn != null) {
        if (DiagramObjectAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramObjectAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
      DiagramObjectAutoBean.this.getWrapped().setGraphObject(obj);
      DiagramObjectAutoBean.this.set("setGraphObject", obj);
    }
    public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
      org.reactome.web.diagram.data.layout.ContextMenuTrigger toReturn = (org.reactome.web.diagram.data.layout.ContextMenuTrigger) DiagramObjectAutoBean.this.getWrapped().contextMenuTrigger();
      if (toReturn != null) {
        if (DiagramObjectAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramObjectAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      DiagramObjectAutoBean.this.call("contextMenuTrigger", toReturn );
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
  public DiagramObjectAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public DiagramObjectAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.DiagramObject wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.DiagramObject as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.DiagramObject> getType() {return org.reactome.web.diagram.data.layout.DiagramObject.class;}
  @Override protected org.reactome.web.diagram.data.layout.DiagramObject createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.DiagramObject() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.DiagramObjectAutoBean.this.data;
      public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
        return (org.reactome.web.diagram.data.graph.model.GraphObject) DiagramObjectAutoBean.this.getOrReify("graphObject");
      }
      public double getMaxX()  {
        java.lang.Double toReturn = DiagramObjectAutoBean.this.getOrReify("maxX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMaxY()  {
        java.lang.Double toReturn = DiagramObjectAutoBean.this.getOrReify("maxY");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinX()  {
        java.lang.Double toReturn = DiagramObjectAutoBean.this.getOrReify("minX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinY()  {
        java.lang.Double toReturn = DiagramObjectAutoBean.this.getOrReify("minY");
        return toReturn == null ? 0d : toReturn;
      }
      public java.lang.Boolean getIsDisease()  {
        return (java.lang.Boolean) DiagramObjectAutoBean.this.getOrReify("isDisease");
      }
      public java.lang.Boolean getIsFadeOut()  {
        return (java.lang.Boolean) DiagramObjectAutoBean.this.getOrReify("isFadeOut");
      }
      public java.lang.Long getId()  {
        return (java.lang.Long) DiagramObjectAutoBean.this.getOrReify("id");
      }
      public java.lang.Long getReactomeId()  {
        return (java.lang.Long) DiagramObjectAutoBean.this.getOrReify("reactomeId");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) DiagramObjectAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getRenderableClass()  {
        return (java.lang.String) DiagramObjectAutoBean.this.getOrReify("renderableClass");
      }
      public java.lang.String getSchemaClass()  {
        return (java.lang.String) DiagramObjectAutoBean.this.getOrReify("schemaClass");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) DiagramObjectAutoBean.this.getOrReify("position");
      }
      public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
        DiagramObjectAutoBean.this.setProperty("graphObject", obj);
      }
      public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.contextMenuTrigger(DiagramObjectAutoBean.this);
      }
      public java.lang.String toString()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.toString(DiagramObjectAutoBean.this);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.DiagramObject as = as();
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getGraphObject());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "graphObject"),
      org.reactome.web.diagram.data.graph.model.GraphObject.class
    );
    if (visitor.visitReferenceProperty("graphObject", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("graphObject", bean, propertyContext);
    value = as.getMaxX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "maxX"),
      double.class
    );
    if (visitor.visitValueProperty("maxX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxX", value, propertyContext);
    value = as.getMaxY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "maxY"),
      double.class
    );
    if (visitor.visitValueProperty("maxY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxY", value, propertyContext);
    value = as.getMinX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "minX"),
      double.class
    );
    if (visitor.visitValueProperty("minX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minX", value, propertyContext);
    value = as.getMinY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "minY"),
      double.class
    );
    if (visitor.visitValueProperty("minY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minY", value, propertyContext);
    value = as.getIsDisease();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "isDisease"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isDisease", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isDisease", value, propertyContext);
    value = as.getIsFadeOut();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "isFadeOut"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isFadeOut", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isFadeOut", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "id"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getReactomeId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "reactomeId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("reactomeId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("reactomeId", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getRenderableClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "renderableClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("renderableClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("renderableClass", value, propertyContext);
    value = as.getSchemaClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "schemaClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("schemaClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("schemaClass", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPosition());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramObjectAutoBean.this, "position"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("position", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("position", bean, propertyContext);
  }
}
