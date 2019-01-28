package org.reactome.web.diagram.data.layout;

public class LinkAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Link> {
  private final org.reactome.web.diagram.data.layout.Link shim = new org.reactome.web.diagram.data.layout.Link() {
    public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
      org.reactome.web.diagram.data.graph.model.GraphObject toReturn = (org.reactome.web.diagram.data.graph.model.GraphObject) LinkAutoBean.this.getWrapped().getGraphObject();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      return toReturn;
    }
    public double getMaxX()  {
      double toReturn = (double) LinkAutoBean.this.getWrapped().getMaxX();
      return toReturn;
    }
    public double getMaxY()  {
      double toReturn = (double) LinkAutoBean.this.getWrapped().getMaxY();
      return toReturn;
    }
    public double getMinX()  {
      double toReturn = (double) LinkAutoBean.this.getWrapped().getMinX();
      return toReturn;
    }
    public double getMinY()  {
      double toReturn = (double) LinkAutoBean.this.getWrapped().getMinY();
      return toReturn;
    }
    public java.lang.Boolean getIsDisease()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) LinkAutoBean.this.getWrapped().getIsDisease();
      return toReturn;
    }
    public java.lang.Boolean getIsFadeOut()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) LinkAutoBean.this.getWrapped().getIsFadeOut();
      return toReturn;
    }
    public java.lang.Long getId()  {
      java.lang.Long toReturn = (java.lang.Long) LinkAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.Long getReactomeId()  {
      java.lang.Long toReturn = (java.lang.Long) LinkAutoBean.this.getWrapped().getReactomeId();
      return toReturn;
    }
    public java.lang.String getDisplayName()  {
      java.lang.String toReturn = (java.lang.String) LinkAutoBean.this.getWrapped().getDisplayName();
      return toReturn;
    }
    public java.lang.String getInteractionType()  {
      java.lang.String toReturn = (java.lang.String) LinkAutoBean.this.getWrapped().getInteractionType();
      return toReturn;
    }
    public java.lang.String getReactionType()  {
      java.lang.String toReturn = (java.lang.String) LinkAutoBean.this.getWrapped().getReactionType();
      return toReturn;
    }
    public java.lang.String getRenderableClass()  {
      java.lang.String toReturn = (java.lang.String) LinkAutoBean.this.getWrapped().getRenderableClass();
      return toReturn;
    }
    public java.lang.String getSchemaClass()  {
      java.lang.String toReturn = (java.lang.String) LinkAutoBean.this.getWrapped().getSchemaClass();
      return toReturn;
    }
    public java.util.List getFollowingEvents()  {
      java.util.List toReturn = (java.util.List) LinkAutoBean.this.getWrapped().getFollowingEvents();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getPrecedingEvents()  {
      java.util.List toReturn = (java.util.List) LinkAutoBean.this.getWrapped().getPrecedingEvents();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getActivators()  {
      java.util.List toReturn = (java.util.List) LinkAutoBean.this.getWrapped().getActivators();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getCatalysts()  {
      java.util.List toReturn = (java.util.List) LinkAutoBean.this.getWrapped().getCatalysts();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getInhibitors()  {
      java.util.List toReturn = (java.util.List) LinkAutoBean.this.getWrapped().getInhibitors();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getInputs()  {
      java.util.List toReturn = (java.util.List) LinkAutoBean.this.getWrapped().getInputs();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getOutputs()  {
      java.util.List toReturn = (java.util.List) LinkAutoBean.this.getWrapped().getOutputs();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getSegments()  {
      java.util.List toReturn = (java.util.List) LinkAutoBean.this.getWrapped().getSegments();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) LinkAutoBean.this.getWrapped().getPosition();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Shape getEndShape()  {
      org.reactome.web.diagram.data.layout.Shape toReturn = (org.reactome.web.diagram.data.layout.Shape) LinkAutoBean.this.getWrapped().getEndShape();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ShapeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Shape getReactionShape()  {
      org.reactome.web.diagram.data.layout.Shape toReturn = (org.reactome.web.diagram.data.layout.Shape) LinkAutoBean.this.getWrapped().getReactionShape();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ShapeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
      LinkAutoBean.this.getWrapped().setGraphObject(obj);
      LinkAutoBean.this.set("setGraphObject", obj);
    }
    public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
      org.reactome.web.diagram.data.layout.ContextMenuTrigger toReturn = (org.reactome.web.diagram.data.layout.ContextMenuTrigger) LinkAutoBean.this.getWrapped().contextMenuTrigger();
      if (toReturn != null) {
        if (LinkAutoBean.this.isWrapped(toReturn)) {
          toReturn = LinkAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      LinkAutoBean.this.call("contextMenuTrigger", toReturn );
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
  public LinkAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public LinkAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Link wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Link as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Link> getType() {return org.reactome.web.diagram.data.layout.Link.class;}
  @Override protected org.reactome.web.diagram.data.layout.Link createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Link() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.LinkAutoBean.this.data;
      public org.reactome.web.diagram.data.graph.model.GraphObject getGraphObject()  {
        return (org.reactome.web.diagram.data.graph.model.GraphObject) LinkAutoBean.this.getOrReify("graphObject");
      }
      public double getMaxX()  {
        java.lang.Double toReturn = LinkAutoBean.this.getOrReify("maxX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMaxY()  {
        java.lang.Double toReturn = LinkAutoBean.this.getOrReify("maxY");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinX()  {
        java.lang.Double toReturn = LinkAutoBean.this.getOrReify("minX");
        return toReturn == null ? 0d : toReturn;
      }
      public double getMinY()  {
        java.lang.Double toReturn = LinkAutoBean.this.getOrReify("minY");
        return toReturn == null ? 0d : toReturn;
      }
      public java.lang.Boolean getIsDisease()  {
        return (java.lang.Boolean) LinkAutoBean.this.getOrReify("isDisease");
      }
      public java.lang.Boolean getIsFadeOut()  {
        return (java.lang.Boolean) LinkAutoBean.this.getOrReify("isFadeOut");
      }
      public java.lang.Long getId()  {
        return (java.lang.Long) LinkAutoBean.this.getOrReify("id");
      }
      public java.lang.Long getReactomeId()  {
        return (java.lang.Long) LinkAutoBean.this.getOrReify("reactomeId");
      }
      public java.lang.String getDisplayName()  {
        return (java.lang.String) LinkAutoBean.this.getOrReify("displayName");
      }
      public java.lang.String getInteractionType()  {
        return (java.lang.String) LinkAutoBean.this.getOrReify("interactionType");
      }
      public java.lang.String getReactionType()  {
        return (java.lang.String) LinkAutoBean.this.getOrReify("reactionType");
      }
      public java.lang.String getRenderableClass()  {
        return (java.lang.String) LinkAutoBean.this.getOrReify("renderableClass");
      }
      public java.lang.String getSchemaClass()  {
        return (java.lang.String) LinkAutoBean.this.getOrReify("schemaClass");
      }
      public java.util.List getFollowingEvents()  {
        return (java.util.List) LinkAutoBean.this.getOrReify("followingEvents");
      }
      public java.util.List getPrecedingEvents()  {
        return (java.util.List) LinkAutoBean.this.getOrReify("precedingEvents");
      }
      public java.util.List getActivators()  {
        return (java.util.List) LinkAutoBean.this.getOrReify("activators");
      }
      public java.util.List getCatalysts()  {
        return (java.util.List) LinkAutoBean.this.getOrReify("catalysts");
      }
      public java.util.List getInhibitors()  {
        return (java.util.List) LinkAutoBean.this.getOrReify("inhibitors");
      }
      public java.util.List getInputs()  {
        return (java.util.List) LinkAutoBean.this.getOrReify("inputs");
      }
      public java.util.List getOutputs()  {
        return (java.util.List) LinkAutoBean.this.getOrReify("outputs");
      }
      public java.util.List getSegments()  {
        return (java.util.List) LinkAutoBean.this.getOrReify("segments");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getPosition()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) LinkAutoBean.this.getOrReify("position");
      }
      public org.reactome.web.diagram.data.layout.Shape getEndShape()  {
        return (org.reactome.web.diagram.data.layout.Shape) LinkAutoBean.this.getOrReify("endShape");
      }
      public org.reactome.web.diagram.data.layout.Shape getReactionShape()  {
        return (org.reactome.web.diagram.data.layout.Shape) LinkAutoBean.this.getOrReify("reactionShape");
      }
      public void setGraphObject(org.reactome.web.diagram.data.graph.model.GraphObject obj)  {
        LinkAutoBean.this.setProperty("graphObject", obj);
      }
      public org.reactome.web.diagram.data.layout.ContextMenuTrigger contextMenuTrigger()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.contextMenuTrigger(LinkAutoBean.this);
      }
      public java.lang.String toString()  {
        return org.reactome.web.diagram.data.layout.category.DiagramObjectCategory.toString(LinkAutoBean.this);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Link as = as();
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getGraphObject());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "graphObject"),
      org.reactome.web.diagram.data.graph.model.GraphObject.class
    );
    if (visitor.visitReferenceProperty("graphObject", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("graphObject", bean, propertyContext);
    value = as.getMaxX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "maxX"),
      double.class
    );
    if (visitor.visitValueProperty("maxX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxX", value, propertyContext);
    value = as.getMaxY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "maxY"),
      double.class
    );
    if (visitor.visitValueProperty("maxY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("maxY", value, propertyContext);
    value = as.getMinX();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "minX"),
      double.class
    );
    if (visitor.visitValueProperty("minX", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minX", value, propertyContext);
    value = as.getMinY();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "minY"),
      double.class
    );
    if (visitor.visitValueProperty("minY", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("minY", value, propertyContext);
    value = as.getIsDisease();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "isDisease"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isDisease", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isDisease", value, propertyContext);
    value = as.getIsFadeOut();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "isFadeOut"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isFadeOut", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isFadeOut", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "id"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getReactomeId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "reactomeId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("reactomeId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("reactomeId", value, propertyContext);
    value = as.getDisplayName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "displayName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("displayName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("displayName", value, propertyContext);
    value = as.getInteractionType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "interactionType"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("interactionType", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("interactionType", value, propertyContext);
    value = as.getReactionType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "reactionType"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("reactionType", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("reactionType", value, propertyContext);
    value = as.getRenderableClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "renderableClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("renderableClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("renderableClass", value, propertyContext);
    value = as.getSchemaClass();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "schemaClass"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("schemaClass", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("schemaClass", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getFollowingEvents());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "followingEvents"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("followingEvents", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("followingEvents", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPrecedingEvents());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "precedingEvents"),
      new Class<?>[] {java.util.List.class, java.lang.Long.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("precedingEvents", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("precedingEvents", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getActivators());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "activators"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.ReactionPart.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("activators", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("activators", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getCatalysts());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "catalysts"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.ReactionPart.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("catalysts", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("catalysts", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInhibitors());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "inhibitors"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.ReactionPart.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("inhibitors", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("inhibitors", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInputs());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "inputs"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.ReactionPart.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("inputs", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("inputs", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getOutputs());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "outputs"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.ReactionPart.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("outputs", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("outputs", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getSegments());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "segments"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Segment.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("segments", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("segments", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPosition());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "position"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("position", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("position", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEndShape());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "endShape"),
      org.reactome.web.diagram.data.layout.Shape.class
    );
    if (visitor.visitReferenceProperty("endShape", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("endShape", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getReactionShape());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(LinkAutoBean.this, "reactionShape"),
      org.reactome.web.diagram.data.layout.Shape.class
    );
    if (visitor.visitReferenceProperty("reactionShape", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("reactionShape", bean, propertyContext);
  }
}
