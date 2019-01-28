package org.reactome.web.diagram.data.layout;

public class NodeCommonAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.NodeCommon> {
  private final org.reactome.web.diagram.data.layout.NodeCommon shim = new org.reactome.web.diagram.data.layout.NodeCommon() {
    public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
      org.reactome.web.diagram.data.graph.model.GraphObject toReturn = (org.reactome.web.diagram.data.graph.model.GraphObject) NodeCommonAutoBean.this.getWrapped().getGraphObject();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      return toReturn;
    }
    public double getMaxX()  {
      double toReturn = (double) NodeCommonAutoBean.this.getWrapped().getMaxX();
      return toReturn;
    }
    public double getMaxY()  {
      double toReturn = (double) NodeCommonAutoBean.this.getWrapped().getMaxY();
      return toReturn;
    }
    public double getMinX()  {
      double toReturn = (double) NodeCommonAutoBean.this.getWrapped().getMinX();
      return toReturn;
    }
    public double getMinY()  {
      double toReturn = (double) NodeCommonAutoBean.this.getWrapped().getMinY();
      return toReturn;
    }
    public java.lang.Boolean getIsCrossed()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeCommonAutoBean.this.getWrapped().getIsCrossed();
      return toReturn;
    }
    public java.lang.Boolean getIsDisease()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeCommonAutoBean.this.getWrapped().getIsDisease();
      return toReturn;
    }
    public java.lang.Boolean getIsFadeOut()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeCommonAutoBean.this.getWrapped().getIsFadeOut();
      return toReturn;
    }
    public java.lang.Boolean getNeedDashedBorder()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeCommonAutoBean.this.getWrapped().getNeedDashedBorder();
      return toReturn;
    }
    public java.lang.Long getId()  {
      java.lang.Long toReturn = (java.lang.Long) NodeCommonAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.Long getReactomeId()  {
      java.lang.Long toReturn = (java.lang.Long) NodeCommonAutoBean.this.getWrapped().getReactomeId();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) NodeCommonAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getRenderableClass()  {
      java.lang.String toReturn = (java.lang.String) NodeCommonAutoBean.this.getWrapped().getRenderableClass();
      return toReturn;
    }
    public java.lang.String getSchemaClass()  {
      java.lang.String toReturn = (java.lang.String) NodeCommonAutoBean.this.getWrapped().getSchemaClass();
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Bound getInsets()  {
      org.reactome.web.diagram.data.layout.Bound toReturn = (org.reactome.web.diagram.data.layout.Bound) NodeCommonAutoBean.this.getWrapped().getInsets();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.BoundAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Color getBgColor()  {
      org.reactome.web.diagram.data.layout.Color toReturn = (org.reactome.web.diagram.data.layout.Color) NodeCommonAutoBean.this.getWrapped().getBgColor();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ColorAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Color getFgColor()  {
      org.reactome.web.diagram.data.layout.Color toReturn = (org.reactome.web.diagram.data.layout.Color) NodeCommonAutoBean.this.getWrapped().getFgColor();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ColorAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) NodeCommonAutoBean.this.getWrapped().getPosition();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getTextPosition()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) NodeCommonAutoBean.this.getWrapped().getTextPosition();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Identifier getIdentifier()  {
      org.reactome.web.diagram.data.layout.Identifier toReturn = (org.reactome.web.diagram.data.layout.Identifier) NodeCommonAutoBean.this.getWrapped().getIdentifier();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.IdentifierAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.NodeProperties getInnerProp()  {
      org.reactome.web.diagram.data.layout.NodeProperties toReturn = (org.reactome.web.diagram.data.layout.NodeProperties) NodeCommonAutoBean.this.getWrapped().getInnerProp();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.NodePropertiesAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.NodeProperties getProp()  {
      org.reactome.web.diagram.data.layout.NodeProperties toReturn = (org.reactome.web.diagram.data.layout.NodeProperties) NodeCommonAutoBean.this.getWrapped().getProp();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.NodePropertiesAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
      NodeCommonAutoBean.this.getWrapped().setGraphObject(obj);
      NodeCommonAutoBean.this.set("setGraphObject", obj);
    }
    public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
      org.reactome.web.diagram.data.layout.ContextMenuTrigger toReturn = (org.reactome.web.diagram.data.layout.ContextMenuTrigger) NodeCommonAutoBean.this.getWrapped().contextMenuTrigger();
      if (toReturn != null) {
        if (NodeCommonAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeCommonAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      NodeCommonAutoBean.this.call("contextMenuTrigger", toReturn );
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
  public NodeCommonAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public NodeCommonAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.NodeCommon wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.NodeCommon as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.NodeCommon> getType() {return org.reactome.web.diagram.data.layout.NodeCommon.class;}
  @Override protected org.reactome.web.diagram.data.layout.NodeCommon createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.NodeCommon() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.NodeCommonAutoBean.this.data;
      public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
        return (org.reactome.web.diagram.data.graph.model.GraphObject) NodeCommonAutoBean.this.getOrReify("graphObject");
      }
      public double getMaxX()  {
        java.lang.Double toReturn = NodeCommonAutoBean.this.getOrReify("maxX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMaxY()  {
        java.lang.Double toReturn = NodeCommonAutoBean.this.getOrReify("maxY");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinX()  {
        java.lang.Double toReturn = NodeCommonAutoBean.this.getOrReify("minX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinY()  {
        java.lang.Double toReturn = NodeCommonAutoBean.this.getOrReify("minY");
        return toReturn == null ? 0d : toReturn;
      }
      public java.lang.Boolean getIsCrossed()  {
        return (java.lang.Boolean) NodeCommonAutoBean.this.getOrReify("isCrossed");
      }
      public java.lang.Boolean getIsDisease()  {
        return (java.lang.Boolean) NodeCommonAutoBean.this.getOrReify("isDisease");
      }
      public java.lang.Boolean getIsFadeOut()  {
        return (java.lang.Boolean) NodeCommonAutoBean.this.getOrReify("isFadeOut");
      }
      public java.lang.Boolean getNeedDashedBorder()  {
        return (java.lang.Boolean) NodeCommonAutoBean.this.getOrReify("needDashedBorder");
      }
      public java.lang.Long getId()  {
        return (java.lang.Long) NodeCommonAutoBean.this.getOrReify("id");
      }
      public java.lang.Long getReactomeId()  {
        return (java.lang.Long) NodeCommonAutoBean.this.getOrReify("reactomeId");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) NodeCommonAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getRenderableClass()  {
        return (java.lang.String) NodeCommonAutoBean.this.getOrReify("renderableClass");
      }
      public java.lang.String getSchemaClass()  {
        return (java.lang.String) NodeCommonAutoBean.this.getOrReify("schemaClass");
      }
      public org.reactome.web.diagram.data.layout.Bound getInsets()  {
        return (org.reactome.web.diagram.data.layout.Bound) NodeCommonAutoBean.this.getOrReify("insets");
      }
      public org.reactome.web.diagram.data.layout.Color getBgColor()  {
        return (org.reactome.web.diagram.data.layout.Color) NodeCommonAutoBean.this.getOrReify("bgColor");
      }
      public org.reactome.web.diagram.data.layout.Color getFgColor()  {
        return (org.reactome.web.diagram.data.layout.Color) NodeCommonAutoBean.this.getOrReify("fgColor");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) NodeCommonAutoBean.this.getOrReify("position");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getTextPosition()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) NodeCommonAutoBean.this.getOrReify("textPosition");
      }
      public org.reactome.web.diagram.data.layout.Identifier getIdentifier()  {
        return (org.reactome.web.diagram.data.layout.Identifier) NodeCommonAutoBean.this.getOrReify("identifier");
      }
      public org.reactome.web.diagram.data.layout.NodeProperties getInnerProp()  {
        return (org.reactome.web.diagram.data.layout.NodeProperties) NodeCommonAutoBean.this.getOrReify("innerProp");
      }
      public org.reactome.web.diagram.data.layout.NodeProperties getProp()  {
        return (org.reactome.web.diagram.data.layout.NodeProperties) NodeCommonAutoBean.this.getOrReify("prop");
      }
      public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
        NodeCommonAutoBean.this.setProperty("graphObject", obj);
      }
      public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.contextMenuTrigger(NodeCommonAutoBean.this);
      }
      public java.lang.String toString()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.toString(NodeCommonAutoBean.this);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.NodeCommon as = as();
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getGraphObject());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "graphObject"),
      org.reactome.web.diagram.data.graph.model.GraphObject.class
    );
    if (visitor.visitReferenceProperty("graphObject", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("graphObject", bean, propertyContext);
    value = as.getMaxX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "maxX"),
      double.class
    );
    if (visitor.visitValueProperty("maxX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxX", value, propertyContext);
    value = as.getMaxY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "maxY"),
      double.class
    );
    if (visitor.visitValueProperty("maxY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxY", value, propertyContext);
    value = as.getMinX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "minX"),
      double.class
    );
    if (visitor.visitValueProperty("minX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minX", value, propertyContext);
    value = as.getMinY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "minY"),
      double.class
    );
    if (visitor.visitValueProperty("minY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minY", value, propertyContext);
    value = as.getIsCrossed();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "isCrossed"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isCrossed", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isCrossed", value, propertyContext);
    value = as.getIsDisease();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "isDisease"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isDisease", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isDisease", value, propertyContext);
    value = as.getIsFadeOut();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "isFadeOut"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isFadeOut", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isFadeOut", value, propertyContext);
    value = as.getNeedDashedBorder();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "needDashedBorder"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("needDashedBorder", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("needDashedBorder", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "id"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getReactomeId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "reactomeId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("reactomeId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("reactomeId", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getRenderableClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "renderableClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("renderableClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("renderableClass", value, propertyContext);
    value = as.getSchemaClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "schemaClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("schemaClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("schemaClass", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInsets());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "insets"),
      org.reactome.web.diagram.data.layout.Bound.class
    );
    if (visitor.visitReferenceProperty("insets", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("insets", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getBgColor());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "bgColor"),
      org.reactome.web.diagram.data.layout.Color.class
    );
    if (visitor.visitReferenceProperty("bgColor", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("bgColor", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getFgColor());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "fgColor"),
      org.reactome.web.diagram.data.layout.Color.class
    );
    if (visitor.visitReferenceProperty("fgColor", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("fgColor", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPosition());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "position"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("position", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("position", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getTextPosition());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "textPosition"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("textPosition", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("textPosition", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getIdentifier());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "identifier"),
      org.reactome.web.diagram.data.layout.Identifier.class
    );
    if (visitor.visitReferenceProperty("identifier", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("identifier", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInnerProp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "innerProp"),
      org.reactome.web.diagram.data.layout.NodeProperties.class
    );
    if (visitor.visitReferenceProperty("innerProp", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("innerProp", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getProp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeCommonAutoBean.this, "prop"),
      org.reactome.web.diagram.data.layout.NodeProperties.class
    );
    if (visitor.visitReferenceProperty("prop", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("prop", bean, propertyContext);
  }
}
