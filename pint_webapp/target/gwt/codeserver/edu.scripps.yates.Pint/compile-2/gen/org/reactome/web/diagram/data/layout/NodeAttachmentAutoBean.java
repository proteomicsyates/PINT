package org.reactome.web.diagram.data.layout;

public class NodeAttachmentAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.NodeAttachment> {
  private final org.reactome.web.diagram.data.layout.NodeAttachment shim = new org.reactome.web.diagram.data.layout.NodeAttachment() {
    public java.lang.Long getReactomeId()  {
      java.lang.Long toReturn = (java.lang.Long) NodeAttachmentAutoBean.this.getWrapped().getReactomeId();
      return toReturn;
    }
    public java.lang.String getDescription()  {
      java.lang.String toReturn = (java.lang.String) NodeAttachmentAutoBean.this.getWrapped().getDescription();
      return toReturn;
    }
    public java.lang.String getLabel()  {
      java.lang.String toReturn = (java.lang.String) NodeAttachmentAutoBean.this.getWrapped().getLabel();
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Shape getShape()  {
      org.reactome.web.diagram.data.layout.Shape toReturn = (org.reactome.web.diagram.data.layout.Shape) NodeAttachmentAutoBean.this.getWrapped().getShape();
      if (toReturn != null) {
        if (NodeAttachmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = NodeAttachmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ShapeAutoBean(getFactory(), toReturn).as();
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
  public NodeAttachmentAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public NodeAttachmentAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.NodeAttachment wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.NodeAttachment as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.NodeAttachment> getType() {return org.reactome.web.diagram.data.layout.NodeAttachment.class;}
  @Override protected org.reactome.web.diagram.data.layout.NodeAttachment createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.NodeAttachment() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.NodeAttachmentAutoBean.this.data;
      public java.lang.Long getReactomeId()  {
        return (java.lang.Long) NodeAttachmentAutoBean.this.getOrReify("reactomeId");
      }
      public java.lang.String getDescription()  {
        return (java.lang.String) NodeAttachmentAutoBean.this.getOrReify("description");
      }
      public java.lang.String getLabel()  {
        return (java.lang.String) NodeAttachmentAutoBean.this.getOrReify("label");
      }
      public org.reactome.web.diagram.data.layout.Shape getShape()  {
        return (org.reactome.web.diagram.data.layout.Shape) NodeAttachmentAutoBean.this.getOrReify("shape");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.NodeAttachment as = as();
    value = as.getReactomeId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAttachmentAutoBean.this, "reactomeId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("reactomeId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("reactomeId", value, propertyContext);
    value = as.getDescription();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAttachmentAutoBean.this, "description"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("description", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("description", value, propertyContext);
    value = as.getLabel();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAttachmentAutoBean.this, "label"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("label", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("label", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getShape());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(NodeAttachmentAutoBean.this, "shape"),
      org.reactome.web.diagram.data.layout.Shape.class
    );
    if (visitor.visitReferenceProperty("shape", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("shape", bean, propertyContext);
  }
}
