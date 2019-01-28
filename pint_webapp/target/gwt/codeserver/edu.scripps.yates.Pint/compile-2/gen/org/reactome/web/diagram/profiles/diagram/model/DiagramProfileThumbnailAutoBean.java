package org.reactome.web.diagram.profiles.diagram.model;

public class DiagramProfileThumbnailAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail> {
  private final org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail shim = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail() {
    public java.lang.String getEdge()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileThumbnailAutoBean.this.getWrapped().getEdge();
      return toReturn;
    }
    public java.lang.String getHighlight()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileThumbnailAutoBean.this.getWrapped().getHighlight();
      return toReturn;
    }
    public java.lang.String getHovering()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileThumbnailAutoBean.this.getWrapped().getHovering();
      return toReturn;
    }
    public java.lang.String getNode()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileThumbnailAutoBean.this.getWrapped().getNode();
      return toReturn;
    }
    public java.lang.String getSelection()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileThumbnailAutoBean.this.getWrapped().getSelection();
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
  public DiagramProfileThumbnailAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public DiagramProfileThumbnailAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail> getType() {return org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail.class;}
  @Override protected org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail createSimplePeer() {
    return new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnailAutoBean.this.data;
      public java.lang.String getEdge()  {
        return (java.lang.String) DiagramProfileThumbnailAutoBean.this.getOrReify("edge");
      }
      public java.lang.String getHighlight()  {
        return (java.lang.String) DiagramProfileThumbnailAutoBean.this.getOrReify("highlight");
      }
      public java.lang.String getHovering()  {
        return (java.lang.String) DiagramProfileThumbnailAutoBean.this.getOrReify("hovering");
      }
      public java.lang.String getNode()  {
        return (java.lang.String) DiagramProfileThumbnailAutoBean.this.getOrReify("node");
      }
      public java.lang.String getSelection()  {
        return (java.lang.String) DiagramProfileThumbnailAutoBean.this.getOrReify("selection");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail as = as();
    value = as.getEdge();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileThumbnailAutoBean.this, "edge"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("edge", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("edge", value, propertyContext);
    value = as.getHighlight();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileThumbnailAutoBean.this, "highlight"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("highlight", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("highlight", value, propertyContext);
    value = as.getHovering();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileThumbnailAutoBean.this, "hovering"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("hovering", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("hovering", value, propertyContext);
    value = as.getNode();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileThumbnailAutoBean.this, "node"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("node", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("node", value, propertyContext);
    value = as.getSelection();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileThumbnailAutoBean.this, "selection"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("selection", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("selection", value, propertyContext);
  }
}
