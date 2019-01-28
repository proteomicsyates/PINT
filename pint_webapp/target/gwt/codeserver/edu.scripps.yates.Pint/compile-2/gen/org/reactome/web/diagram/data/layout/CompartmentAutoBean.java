package org.reactome.web.diagram.data.layout;

public class CompartmentAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Compartment> {
  private final org.reactome.web.diagram.data.layout.Compartment shim = new org.reactome.web.diagram.data.layout.Compartment() {
    public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
      org.reactome.web.diagram.data.graph.model.GraphObject toReturn = (org.reactome.web.diagram.data.graph.model.GraphObject) CompartmentAutoBean.this.getWrapped().getGraphObject();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      return toReturn;
    }
    public double getMaxX()  {
      double toReturn = (double) CompartmentAutoBean.this.getWrapped().getMaxX();
      return toReturn;
    }
    public double getMaxY()  {
      double toReturn = (double) CompartmentAutoBean.this.getWrapped().getMaxY();
      return toReturn;
    }
    public double getMinX()  {
      double toReturn = (double) CompartmentAutoBean.this.getWrapped().getMinX();
      return toReturn;
    }
    public double getMinY()  {
      double toReturn = (double) CompartmentAutoBean.this.getWrapped().getMinY();
      return toReturn;
    }
    public java.lang.Boolean getIsCrossed()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) CompartmentAutoBean.this.getWrapped().getIsCrossed();
      return toReturn;
    }
    public java.lang.Boolean getIsDisease()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) CompartmentAutoBean.this.getWrapped().getIsDisease();
      return toReturn;
    }
    public java.lang.Boolean getIsFadeOut()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) CompartmentAutoBean.this.getWrapped().getIsFadeOut();
      return toReturn;
    }
    public java.lang.Boolean getNeedDashedBorder()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) CompartmentAutoBean.this.getWrapped().getNeedDashedBorder();
      return toReturn;
    }
    public java.lang.Long getId()  {
      java.lang.Long toReturn = (java.lang.Long) CompartmentAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.Long getReactomeId()  {
      java.lang.Long toReturn = (java.lang.Long) CompartmentAutoBean.this.getWrapped().getReactomeId();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) CompartmentAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getRenderableClass()  {
      java.lang.String toReturn = (java.lang.String) CompartmentAutoBean.this.getWrapped().getRenderableClass();
      return toReturn;
    }
    public java.lang.String getSchemaClass()  {
      java.lang.String toReturn = (java.lang.String) CompartmentAutoBean.this.getWrapped().getSchemaClass();
      return toReturn;
    }
    public java.util.List getComponentIds()  {
      java.util.List toReturn = (java.util.List) CompartmentAutoBean.this.getWrapped().getComponentIds();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Bound getInsets()  {
      org.reactome.web.diagram.data.layout.Bound toReturn = (org.reactome.web.diagram.data.layout.Bound) CompartmentAutoBean.this.getWrapped().getInsets();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.BoundAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Color getBgColor()  {
      org.reactome.web.diagram.data.layout.Color toReturn = (org.reactome.web.diagram.data.layout.Color) CompartmentAutoBean.this.getWrapped().getBgColor();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ColorAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Color getFgColor()  {
      org.reactome.web.diagram.data.layout.Color toReturn = (org.reactome.web.diagram.data.layout.Color) CompartmentAutoBean.this.getWrapped().getFgColor();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ColorAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) CompartmentAutoBean.this.getWrapped().getPosition();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getTextPosition()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) CompartmentAutoBean.this.getWrapped().getTextPosition();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Identifier getIdentifier()  {
      org.reactome.web.diagram.data.layout.Identifier toReturn = (org.reactome.web.diagram.data.layout.Identifier) CompartmentAutoBean.this.getWrapped().getIdentifier();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.IdentifierAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.NodeProperties getInnerProp()  {
      org.reactome.web.diagram.data.layout.NodeProperties toReturn = (org.reactome.web.diagram.data.layout.NodeProperties) CompartmentAutoBean.this.getWrapped().getInnerProp();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.NodePropertiesAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.NodeProperties getProp()  {
      org.reactome.web.diagram.data.layout.NodeProperties toReturn = (org.reactome.web.diagram.data.layout.NodeProperties) CompartmentAutoBean.this.getWrapped().getProp();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.NodePropertiesAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
      CompartmentAutoBean.this.getWrapped().setGraphObject(obj);
      CompartmentAutoBean.this.set("setGraphObject", obj);
    }
    public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
      org.reactome.web.diagram.data.layout.ContextMenuTrigger toReturn = (org.reactome.web.diagram.data.layout.ContextMenuTrigger) CompartmentAutoBean.this.getWrapped().contextMenuTrigger();
      if (toReturn != null) {
        if (CompartmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = CompartmentAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      CompartmentAutoBean.this.call("contextMenuTrigger", toReturn );
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
  public CompartmentAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public CompartmentAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Compartment wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Compartment as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Compartment> getType() {return org.reactome.web.diagram.data.layout.Compartment.class;}
  @Override protected org.reactome.web.diagram.data.layout.Compartment createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Compartment() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.CompartmentAutoBean.this.data;
      public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
        return (org.reactome.web.diagram.data.graph.model.GraphObject) CompartmentAutoBean.this.getOrReify("graphObject");
      }
      public double getMaxX()  {
        java.lang.Double toReturn = CompartmentAutoBean.this.getOrReify("maxX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMaxY()  {
        java.lang.Double toReturn = CompartmentAutoBean.this.getOrReify("maxY");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinX()  {
        java.lang.Double toReturn = CompartmentAutoBean.this.getOrReify("minX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinY()  {
        java.lang.Double toReturn = CompartmentAutoBean.this.getOrReify("minY");
        return toReturn == null ? 0d : toReturn;
      }
      public java.lang.Boolean getIsCrossed()  {
        return (java.lang.Boolean) CompartmentAutoBean.this.getOrReify("isCrossed");
      }
      public java.lang.Boolean getIsDisease()  {
        return (java.lang.Boolean) CompartmentAutoBean.this.getOrReify("isDisease");
      }
      public java.lang.Boolean getIsFadeOut()  {
        return (java.lang.Boolean) CompartmentAutoBean.this.getOrReify("isFadeOut");
      }
      public java.lang.Boolean getNeedDashedBorder()  {
        return (java.lang.Boolean) CompartmentAutoBean.this.getOrReify("needDashedBorder");
      }
      public java.lang.Long getId()  {
        return (java.lang.Long) CompartmentAutoBean.this.getOrReify("id");
      }
      public java.lang.Long getReactomeId()  {
        return (java.lang.Long) CompartmentAutoBean.this.getOrReify("reactomeId");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) CompartmentAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getRenderableClass()  {
        return (java.lang.String) CompartmentAutoBean.this.getOrReify("renderableClass");
      }
      public java.lang.String getSchemaClass()  {
        return (java.lang.String) CompartmentAutoBean.this.getOrReify("schemaClass");
      }
      public java.util.List getComponentIds()  {
        return (java.util.List) CompartmentAutoBean.this.getOrReify("componentIds");
      }
      public org.reactome.web.diagram.data.layout.Bound getInsets()  {
        return (org.reactome.web.diagram.data.layout.Bound) CompartmentAutoBean.this.getOrReify("insets");
      }
      public org.reactome.web.diagram.data.layout.Color getBgColor()  {
        return (org.reactome.web.diagram.data.layout.Color) CompartmentAutoBean.this.getOrReify("bgColor");
      }
      public org.reactome.web.diagram.data.layout.Color getFgColor()  {
        return (org.reactome.web.diagram.data.layout.Color) CompartmentAutoBean.this.getOrReify("fgColor");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) CompartmentAutoBean.this.getOrReify("position");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getTextPosition()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) CompartmentAutoBean.this.getOrReify("textPosition");
      }
      public org.reactome.web.diagram.data.layout.Identifier getIdentifier()  {
        return (org.reactome.web.diagram.data.layout.Identifier) CompartmentAutoBean.this.getOrReify("identifier");
      }
      public org.reactome.web.diagram.data.layout.NodeProperties getInnerProp()  {
        return (org.reactome.web.diagram.data.layout.NodeProperties) CompartmentAutoBean.this.getOrReify("innerProp");
      }
      public org.reactome.web.diagram.data.layout.NodeProperties getProp()  {
        return (org.reactome.web.diagram.data.layout.NodeProperties) CompartmentAutoBean.this.getOrReify("prop");
      }
      public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
        CompartmentAutoBean.this.setProperty("graphObject", obj);
      }
      public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.contextMenuTrigger(CompartmentAutoBean.this);
      }
      public java.lang.String toString()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.toString(CompartmentAutoBean.this);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Compartment as = as();
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getGraphObject());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "graphObject"),
      org.reactome.web.diagram.data.graph.model.GraphObject.class
    );
    if (visitor.visitReferenceProperty("graphObject", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("graphObject", bean, propertyContext);
    value = as.getMaxX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "maxX"),
      double.class
    );
    if (visitor.visitValueProperty("maxX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxX", value, propertyContext);
    value = as.getMaxY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "maxY"),
      double.class
    );
    if (visitor.visitValueProperty("maxY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxY", value, propertyContext);
    value = as.getMinX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "minX"),
      double.class
    );
    if (visitor.visitValueProperty("minX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minX", value, propertyContext);
    value = as.getMinY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "minY"),
      double.class
    );
    if (visitor.visitValueProperty("minY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minY", value, propertyContext);
    value = as.getIsCrossed();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "isCrossed"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isCrossed", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isCrossed", value, propertyContext);
    value = as.getIsDisease();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "isDisease"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isDisease", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isDisease", value, propertyContext);
    value = as.getIsFadeOut();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "isFadeOut"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isFadeOut", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isFadeOut", value, propertyContext);
    value = as.getNeedDashedBorder();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "needDashedBorder"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("needDashedBorder", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("needDashedBorder", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "id"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getReactomeId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "reactomeId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("reactomeId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("reactomeId", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getRenderableClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "renderableClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("renderableClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("renderableClass", value, propertyContext);
    value = as.getSchemaClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "schemaClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("schemaClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("schemaClass", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getComponentIds());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "componentIds"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("componentIds", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("componentIds", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInsets());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "insets"),
      org.reactome.web.diagram.data.layout.Bound.class
    );
    if (visitor.visitReferenceProperty("insets", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("insets", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getBgColor());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "bgColor"),
      org.reactome.web.diagram.data.layout.Color.class
    );
    if (visitor.visitReferenceProperty("bgColor", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("bgColor", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getFgColor());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "fgColor"),
      org.reactome.web.diagram.data.layout.Color.class
    );
    if (visitor.visitReferenceProperty("fgColor", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("fgColor", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPosition());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "position"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("position", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("position", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getTextPosition());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "textPosition"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("textPosition", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("textPosition", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getIdentifier());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "identifier"),
      org.reactome.web.diagram.data.layout.Identifier.class
    );
    if (visitor.visitReferenceProperty("identifier", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("identifier", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInnerProp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "innerProp"),
      org.reactome.web.diagram.data.layout.NodeProperties.class
    );
    if (visitor.visitReferenceProperty("innerProp", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("innerProp", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getProp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CompartmentAutoBean.this, "prop"),
      org.reactome.web.diagram.data.layout.NodeProperties.class
    );
    if (visitor.visitReferenceProperty("prop", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("prop", bean, propertyContext);
  }
}
