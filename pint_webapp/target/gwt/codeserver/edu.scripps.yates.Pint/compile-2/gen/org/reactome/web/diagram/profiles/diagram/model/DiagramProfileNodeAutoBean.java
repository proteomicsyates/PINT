package org.reactome.web.diagram.profiles.diagram.model;

public class DiagramProfileNodeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode> {
  private final org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode shim = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode() {
    public java.lang.String getFadeOutFill()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getFadeOutFill();
      return toReturn;
    }
    public java.lang.String getFadeOutStroke()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getFadeOutStroke();
      return toReturn;
    }
    public java.lang.String getFadeOutText()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getFadeOutText();
      return toReturn;
    }
    public java.lang.String getFill()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getFill();
      return toReturn;
    }
    public java.lang.String getLighterFill()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getLighterFill();
      return toReturn;
    }
    public java.lang.String getLighterStroke()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getLighterStroke();
      return toReturn;
    }
    public java.lang.String getLighterText()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getLighterText();
      return toReturn;
    }
    public java.lang.String getLineWidth()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getLineWidth();
      return toReturn;
    }
    public java.lang.String getStroke()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getStroke();
      return toReturn;
    }
    public java.lang.String getText()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileNodeAutoBean.this.getWrapped().getText();
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
  public DiagramProfileNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public DiagramProfileNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode> getType() {return org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class;}
  @Override protected org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode createSimplePeer() {
    return new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean.this.data;
      public java.lang.String getFadeOutFill()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("fadeOutFill");
      }
      public java.lang.String getFadeOutStroke()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("fadeOutStroke");
      }
      public java.lang.String getFadeOutText()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("fadeOutText");
      }
      public java.lang.String getFill()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("fill");
      }
      public java.lang.String getLighterFill()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("lighterFill");
      }
      public java.lang.String getLighterStroke()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("lighterStroke");
      }
      public java.lang.String getLighterText()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("lighterText");
      }
      public java.lang.String getLineWidth()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("lineWidth");
      }
      public java.lang.String getStroke()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("stroke");
      }
      public java.lang.String getText()  {
        return (java.lang.String) DiagramProfileNodeAutoBean.this.getOrReify("text");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode as = as();
    value = as.getFadeOutFill();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "fadeOutFill"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("fadeOutFill", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fadeOutFill", value, propertyContext);
    value = as.getFadeOutStroke();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "fadeOutStroke"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("fadeOutStroke", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fadeOutStroke", value, propertyContext);
    value = as.getFadeOutText();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "fadeOutText"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("fadeOutText", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fadeOutText", value, propertyContext);
    value = as.getFill();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "fill"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("fill", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fill", value, propertyContext);
    value = as.getLighterFill();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "lighterFill"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("lighterFill", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("lighterFill", value, propertyContext);
    value = as.getLighterStroke();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "lighterStroke"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("lighterStroke", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("lighterStroke", value, propertyContext);
    value = as.getLighterText();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "lighterText"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("lighterText", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("lighterText", value, propertyContext);
    value = as.getLineWidth();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "lineWidth"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("lineWidth", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("lineWidth", value, propertyContext);
    value = as.getStroke();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "stroke"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stroke", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stroke", value, propertyContext);
    value = as.getText();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileNodeAutoBean.this, "text"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("text", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("text", value, propertyContext);
  }
}
