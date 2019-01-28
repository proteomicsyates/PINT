package org.reactome.web.diagram.data.layout;

public class NodeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Node> {
  private final org.reactome.web.diagram.data.layout.Node shim = new org.reactome.web.diagram.data.layout.Node() {
    public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
      org.reactome.web.diagram.data.graph.model.GraphObject toReturn = (org.reactome.web.diagram.data.graph.model.GraphObject) NodeAutoBean.this.getWrapped().getGraphObject();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      return toReturn;
    }
    public double getMaxX()  {
      double toReturn = (double) NodeAutoBean.this.getWrapped().getMaxX();
      return toReturn;
    }
    public double getMaxY()  {
      double toReturn = (double) NodeAutoBean.this.getWrapped().getMaxY();
      return toReturn;
    }
    public double getMinX()  {
      double toReturn = (double) NodeAutoBean.this.getWrapped().getMinX();
      return toReturn;
    }
    public double getMinY()  {
      double toReturn = (double) NodeAutoBean.this.getWrapped().getMinY();
      return toReturn;
    }
    public java.lang.Boolean getIsCrossed()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeAutoBean.this.getWrapped().getIsCrossed();
      return toReturn;
    }
    public java.lang.Boolean getIsDisease()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeAutoBean.this.getWrapped().getIsDisease();
      return toReturn;
    }
    public java.lang.Boolean getIsFadeOut()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeAutoBean.this.getWrapped().getIsFadeOut();
      return toReturn;
    }
    public java.lang.Boolean getNeedDashedBorder()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeAutoBean.this.getWrapped().getNeedDashedBorder();
      return toReturn;
    }
    public java.lang.Boolean getTrivial()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) NodeAutoBean.this.getWrapped().getTrivial();
      return toReturn;
    }
    public java.lang.Long getId()  {
      java.lang.Long toReturn = (java.lang.Long) NodeAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.Long getReactomeId()  {
      java.lang.Long toReturn = (java.lang.Long) NodeAutoBean.this.getWrapped().getReactomeId();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) NodeAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getRenderableClass()  {
      java.lang.String toReturn = (java.lang.String) NodeAutoBean.this.getWrapped().getRenderableClass();
      return toReturn;
    }
    public java.lang.String getSchemaClass()  {
      java.lang.String toReturn = (java.lang.String) NodeAutoBean.this.getWrapped().getSchemaClass();
      return toReturn;
    }
    public java.util.List getConnectors()  {
      java.util.List toReturn = (java.util.List) NodeAutoBean.this.getWrapped().getConnectors();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getNodeAttachments()  {
      java.util.List toReturn = (java.util.List) NodeAutoBean.this.getWrapped().getNodeAttachments();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.interactors.common.InteractorsSummary getDiagramEntityInteractorsSummary()  {
      org.reactome.web.diagram.data.interactors.common.InteractorsSummary toReturn = (org.reactome.web.diagram.data.interactors.common.InteractorsSummary) NodeAutoBean.this.getWrapped().getDiagramEntityInteractorsSummary();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Bound getInsets()  {
      org.reactome.web.diagram.data.layout.Bound toReturn = (org.reactome.web.diagram.data.layout.Bound) NodeAutoBean.this.getWrapped().getInsets();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.BoundAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Color getBgColor()  {
      org.reactome.web.diagram.data.layout.Color toReturn = (org.reactome.web.diagram.data.layout.Color) NodeAutoBean.this.getWrapped().getBgColor();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ColorAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Color getFgColor()  {
      org.reactome.web.diagram.data.layout.Color toReturn = (org.reactome.web.diagram.data.layout.Color) NodeAutoBean.this.getWrapped().getFgColor();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ColorAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) NodeAutoBean.this.getWrapped().getPosition();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getTextPosition()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) NodeAutoBean.this.getWrapped().getTextPosition();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Identifier getIdentifier()  {
      org.reactome.web.diagram.data.layout.Identifier toReturn = (org.reactome.web.diagram.data.layout.Identifier) NodeAutoBean.this.getWrapped().getIdentifier();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.IdentifierAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.NodeProperties getInnerProp()  {
      org.reactome.web.diagram.data.layout.NodeProperties toReturn = (org.reactome.web.diagram.data.layout.NodeProperties) NodeAutoBean.this.getWrapped().getInnerProp();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.NodePropertiesAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.NodeProperties getProp()  {
      org.reactome.web.diagram.data.layout.NodeProperties toReturn = (org.reactome.web.diagram.data.layout.NodeProperties) NodeAutoBean.this.getWrapped().getProp();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.NodePropertiesAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.SummaryItem getInteractorsSummary()  {
      org.reactome.web.diagram.data.layout.SummaryItem toReturn = (org.reactome.web.diagram.data.layout.SummaryItem) NodeAutoBean.this.getWrapped().getInteractorsSummary();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.SummaryItemAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setDiagramEntityInteractorsSummary(org.reactome.web.diagram.data.interactors.common.InteractorsSummary interactorsSummary)  {
      NodeAutoBean.this.getWrapped().setDiagramEntityInteractorsSummary(interactorsSummary);
      NodeAutoBean.this.set("setDiagramEntityInteractorsSummary", interactorsSummary);
    }
    public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
      NodeAutoBean.this.getWrapped().setGraphObject(obj);
      NodeAutoBean.this.set("setGraphObject", obj);
    }
    public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
      org.reactome.web.diagram.data.layout.ContextMenuTrigger toReturn = (org.reactome.web.diagram.data.layout.ContextMenuTrigger) NodeAutoBean.this.getWrapped().contextMenuTrigger();
      if (toReturn != null) {
        if (NodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      NodeAutoBean.this.call("contextMenuTrigger", toReturn );
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
  public NodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public NodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Node wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Node as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Node> getType() {return org.reactome.web.diagram.data.layout.Node.class;}
  @Override protected org.reactome.web.diagram.data.layout.Node createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Node() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.NodeAutoBean.this.data;
      public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
        return (org.reactome.web.diagram.data.graph.model.GraphObject) NodeAutoBean.this.getOrReify("graphObject");
      }
      public double getMaxX()  {
        java.lang.Double toReturn = NodeAutoBean.this.getOrReify("maxX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMaxY()  {
        java.lang.Double toReturn = NodeAutoBean.this.getOrReify("maxY");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinX()  {
        java.lang.Double toReturn = NodeAutoBean.this.getOrReify("minX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinY()  {
        java.lang.Double toReturn = NodeAutoBean.this.getOrReify("minY");
        return toReturn == null ? 0d : toReturn;
      }
      public java.lang.Boolean getIsCrossed()  {
        return (java.lang.Boolean) NodeAutoBean.this.getOrReify("isCrossed");
      }
      public java.lang.Boolean getIsDisease()  {
        return (java.lang.Boolean) NodeAutoBean.this.getOrReify("isDisease");
      }
      public java.lang.Boolean getIsFadeOut()  {
        return (java.lang.Boolean) NodeAutoBean.this.getOrReify("isFadeOut");
      }
      public java.lang.Boolean getNeedDashedBorder()  {
        return (java.lang.Boolean) NodeAutoBean.this.getOrReify("needDashedBorder");
      }
      public java.lang.Boolean getTrivial()  {
        return (java.lang.Boolean) NodeAutoBean.this.getOrReify("trivial");
      }
      public java.lang.Long getId()  {
        return (java.lang.Long) NodeAutoBean.this.getOrReify("id");
      }
      public java.lang.Long getReactomeId()  {
        return (java.lang.Long) NodeAutoBean.this.getOrReify("reactomeId");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) NodeAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getRenderableClass()  {
        return (java.lang.String) NodeAutoBean.this.getOrReify("renderableClass");
      }
      public java.lang.String getSchemaClass()  {
        return (java.lang.String) NodeAutoBean.this.getOrReify("schemaClass");
      }
      public java.util.List getConnectors()  {
        return (java.util.List) NodeAutoBean.this.getOrReify("connectors");
      }
      public java.util.List getNodeAttachments()  {
        return (java.util.List) NodeAutoBean.this.getOrReify("nodeAttachments");
      }
      public org.reactome.web.diagram.data.interactors.common.InteractorsSummary getDiagramEntityInteractorsSummary()  {
        return (org.reactome.web.diagram.data.interactors.common.InteractorsSummary) NodeAutoBean.this.getOrReify("diagramEntityInteractorsSummary");
      }
      public org.reactome.web.diagram.data.layout.Bound getInsets()  {
        return (org.reactome.web.diagram.data.layout.Bound) NodeAutoBean.this.getOrReify("insets");
      }
      public org.reactome.web.diagram.data.layout.Color getBgColor()  {
        return (org.reactome.web.diagram.data.layout.Color) NodeAutoBean.this.getOrReify("bgColor");
      }
      public org.reactome.web.diagram.data.layout.Color getFgColor()  {
        return (org.reactome.web.diagram.data.layout.Color) NodeAutoBean.this.getOrReify("fgColor");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) NodeAutoBean.this.getOrReify("position");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getTextPosition()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) NodeAutoBean.this.getOrReify("textPosition");
      }
      public org.reactome.web.diagram.data.layout.Identifier getIdentifier()  {
        return (org.reactome.web.diagram.data.layout.Identifier) NodeAutoBean.this.getOrReify("identifier");
      }
      public org.reactome.web.diagram.data.layout.NodeProperties getInnerProp()  {
        return (org.reactome.web.diagram.data.layout.NodeProperties) NodeAutoBean.this.getOrReify("innerProp");
      }
      public org.reactome.web.diagram.data.layout.NodeProperties getProp()  {
        return (org.reactome.web.diagram.data.layout.NodeProperties) NodeAutoBean.this.getOrReify("prop");
      }
      public org.reactome.web.diagram.data.layout.SummaryItem getInteractorsSummary()  {
        return (org.reactome.web.diagram.data.layout.SummaryItem) NodeAutoBean.this.getOrReify("interactorsSummary");
      }
      public void setDiagramEntityInteractorsSummary(org.reactome.web.diagram.data.interactors.common.InteractorsSummary interactorsSummary)  {
        NodeAutoBean.this.setProperty("diagramEntityInteractorsSummary", interactorsSummary);
      }
      public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
        NodeAutoBean.this.setProperty("graphObject", obj);
      }
      public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.contextMenuTrigger(NodeAutoBean.this);
      }
      public java.lang.String toString()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.toString(NodeAutoBean.this);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Node as = as();
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getGraphObject());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "graphObject"),
      org.reactome.web.diagram.data.graph.model.GraphObject.class
    );
    if (visitor.visitReferenceProperty("graphObject", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("graphObject", bean, propertyContext);
    value = as.getMaxX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "maxX"),
      double.class
    );
    if (visitor.visitValueProperty("maxX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxX", value, propertyContext);
    value = as.getMaxY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "maxY"),
      double.class
    );
    if (visitor.visitValueProperty("maxY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxY", value, propertyContext);
    value = as.getMinX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "minX"),
      double.class
    );
    if (visitor.visitValueProperty("minX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minX", value, propertyContext);
    value = as.getMinY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "minY"),
      double.class
    );
    if (visitor.visitValueProperty("minY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minY", value, propertyContext);
    value = as.getIsCrossed();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "isCrossed"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isCrossed", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isCrossed", value, propertyContext);
    value = as.getIsDisease();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "isDisease"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isDisease", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isDisease", value, propertyContext);
    value = as.getIsFadeOut();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "isFadeOut"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isFadeOut", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isFadeOut", value, propertyContext);
    value = as.getNeedDashedBorder();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "needDashedBorder"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("needDashedBorder", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("needDashedBorder", value, propertyContext);
    value = as.getTrivial();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "trivial"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("trivial", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("trivial", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "id"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getReactomeId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "reactomeId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("reactomeId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("reactomeId", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getRenderableClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "renderableClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("renderableClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("renderableClass", value, propertyContext);
    value = as.getSchemaClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "schemaClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("schemaClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("schemaClass", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getConnectors());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "connectors"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Connector.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("connectors", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("connectors", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getNodeAttachments());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "nodeAttachments"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.NodeAttachment.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("nodeAttachments", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("nodeAttachments", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getDiagramEntityInteractorsSummary());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "diagramEntityInteractorsSummary"),
      org.reactome.web.diagram.data.interactors.common.InteractorsSummary.class
    );
    if (visitor.visitReferenceProperty("diagramEntityInteractorsSummary", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("diagramEntityInteractorsSummary", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInsets());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "insets"),
      org.reactome.web.diagram.data.layout.Bound.class
    );
    if (visitor.visitReferenceProperty("insets", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("insets", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getBgColor());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "bgColor"),
      org.reactome.web.diagram.data.layout.Color.class
    );
    if (visitor.visitReferenceProperty("bgColor", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("bgColor", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getFgColor());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "fgColor"),
      org.reactome.web.diagram.data.layout.Color.class
    );
    if (visitor.visitReferenceProperty("fgColor", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("fgColor", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPosition());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "position"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("position", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("position", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getTextPosition());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "textPosition"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("textPosition", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("textPosition", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getIdentifier());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "identifier"),
      org.reactome.web.diagram.data.layout.Identifier.class
    );
    if (visitor.visitReferenceProperty("identifier", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("identifier", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInnerProp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "innerProp"),
      org.reactome.web.diagram.data.layout.NodeProperties.class
    );
    if (visitor.visitReferenceProperty("innerProp", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("innerProp", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getProp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "prop"),
      org.reactome.web.diagram.data.layout.NodeProperties.class
    );
    if (visitor.visitReferenceProperty("prop", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("prop", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInteractorsSummary());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAutoBean.this, "interactorsSummary"),
      org.reactome.web.diagram.data.layout.SummaryItem.class
    );
    if (visitor.visitReferenceProperty("interactorsSummary", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("interactorsSummary", bean, propertyContext);
  }
}
