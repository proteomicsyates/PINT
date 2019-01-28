package org.reactome.web.diagram.data.layout;

public class ConnectorAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Connector> {
  private final org.reactome.web.diagram.data.layout.Connector shim = new org.reactome.web.diagram.data.layout.Connector() {
    public java.lang.Boolean getIsDisease()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) ConnectorAutoBean.this.getWrapped().getIsDisease();
      return toReturn;
    }
    public java.lang.Boolean getIsFadeOut()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) ConnectorAutoBean.this.getWrapped().getIsFadeOut();
      return toReturn;
    }
    public java.lang.Long getEdgeId()  {
      java.lang.Long toReturn = (java.lang.Long) ConnectorAutoBean.this.getWrapped().getEdgeId();
      return toReturn;
    }
    public java.lang.String getType()  {
      java.lang.String toReturn = (java.lang.String) ConnectorAutoBean.this.getWrapped().getType();
      return toReturn;
    }
    public java.util.List getSegments()  {
      java.util.List toReturn = (java.util.List) ConnectorAutoBean.this.getWrapped().getSegments();
      if (toReturn != null) {
        if (ConnectorAutoBean.this.isWrapped(toReturn)) {
          toReturn = ConnectorAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Shape getEndShape()  {
      org.reactome.web.diagram.data.layout.Shape toReturn = (org.reactome.web.diagram.data.layout.Shape) ConnectorAutoBean.this.getWrapped().getEndShape();
      if (toReturn != null) {
        if (ConnectorAutoBean.this.isWrapped(toReturn)) {
          toReturn = ConnectorAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ShapeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Stoichiometry getStoichiometry()  {
      org.reactome.web.diagram.data.layout.Stoichiometry toReturn = (org.reactome.web.diagram.data.layout.Stoichiometry) ConnectorAutoBean.this.getWrapped().getStoichiometry();
      if (toReturn != null) {
        if (ConnectorAutoBean.this.isWrapped(toReturn)) {
          toReturn = ConnectorAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.StoichiometryAutoBean(getFactory(), toReturn).as();
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
  public ConnectorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ConnectorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Connector wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Connector as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Connector> getType() {return org.reactome.web.diagram.data.layout.Connector.class;}
  @Override protected org.reactome.web.diagram.data.layout.Connector createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Connector() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.ConnectorAutoBean.this.data;
      public java.lang.Boolean getIsDisease()  {
        return (java.lang.Boolean) ConnectorAutoBean.this.getOrReify("isDisease");
      }
      public java.lang.Boolean getIsFadeOut()  {
        return (java.lang.Boolean) ConnectorAutoBean.this.getOrReify("isFadeOut");
      }
      public java.lang.Long getEdgeId()  {
        return (java.lang.Long) ConnectorAutoBean.this.getOrReify("edgeId");
      }
      public java.lang.String getType()  {
        return (java.lang.String) ConnectorAutoBean.this.getOrReify("type");
      }
      public java.util.List getSegments()  {
        return (java.util.List) ConnectorAutoBean.this.getOrReify("segments");
      }
      public org.reactome.web.diagram.data.layout.Shape getEndShape()  {
        return (org.reactome.web.diagram.data.layout.Shape) ConnectorAutoBean.this.getOrReify("endShape");
      }
      public org.reactome.web.diagram.data.layout.Stoichiometry getStoichiometry()  {
        return (org.reactome.web.diagram.data.layout.Stoichiometry) ConnectorAutoBean.this.getOrReify("stoichiometry");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Connector as = as();
    value = as.getIsDisease();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ConnectorAutoBean.this, "isDisease"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isDisease", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isDisease", value, propertyContext);
    value = as.getIsFadeOut();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ConnectorAutoBean.this, "isFadeOut"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isFadeOut", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isFadeOut", value, propertyContext);
    value = as.getEdgeId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ConnectorAutoBean.this, "edgeId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("edgeId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("edgeId", value, propertyContext);
    value = as.getType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ConnectorAutoBean.this, "type"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("type", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("type", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getSegments());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ConnectorAutoBean.this, "segments"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Segment.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("segments", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("segments", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEndShape());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ConnectorAutoBean.this, "endShape"),
      org.reactome.web.diagram.data.layout.Shape.class
    );
    if (visitor.visitReferenceProperty("endShape", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("endShape", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getStoichiometry());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ConnectorAutoBean.this, "stoichiometry"),
      org.reactome.web.diagram.data.layout.Stoichiometry.class
    );
    if (visitor.visitReferenceProperty("stoichiometry", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("stoichiometry", bean, propertyContext);
  }
}
