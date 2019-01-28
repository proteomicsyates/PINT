package org.reactome.web.diagram.profiles.interactors.model;

public class InteractorProfileNodeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode> {
  private final org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode shim = new org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode() {
    public java.lang.String getFill()  {
      java.lang.String toReturn = (java.lang.String) InteractorProfileNodeAutoBean.this.getWrapped().getFill();
      return toReturn;
    }
    public java.lang.String getLighterFill()  {
      java.lang.String toReturn = (java.lang.String) InteractorProfileNodeAutoBean.this.getWrapped().getLighterFill();
      return toReturn;
    }
    public java.lang.String getLighterStroke()  {
      java.lang.String toReturn = (java.lang.String) InteractorProfileNodeAutoBean.this.getWrapped().getLighterStroke();
      return toReturn;
    }
    public java.lang.String getLighterText()  {
      java.lang.String toReturn = (java.lang.String) InteractorProfileNodeAutoBean.this.getWrapped().getLighterText();
      return toReturn;
    }
    public java.lang.String getStroke()  {
      java.lang.String toReturn = (java.lang.String) InteractorProfileNodeAutoBean.this.getWrapped().getStroke();
      return toReturn;
    }
    public java.lang.String getText()  {
      java.lang.String toReturn = (java.lang.String) InteractorProfileNodeAutoBean.this.getWrapped().getText();
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
  public InteractorProfileNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public InteractorProfileNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode> getType() {return org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode.class;}
  @Override protected org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode createSimplePeer() {
    return new org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNodeAutoBean.this.data;
      public java.lang.String getFill()  {
        return (java.lang.String) InteractorProfileNodeAutoBean.this.getOrReify("fill");
      }
      public java.lang.String getLighterFill()  {
        return (java.lang.String) InteractorProfileNodeAutoBean.this.getOrReify("lighterFill");
      }
      public java.lang.String getLighterStroke()  {
        return (java.lang.String) InteractorProfileNodeAutoBean.this.getOrReify("lighterStroke");
      }
      public java.lang.String getLighterText()  {
        return (java.lang.String) InteractorProfileNodeAutoBean.this.getOrReify("lighterText");
      }
      public java.lang.String getStroke()  {
        return (java.lang.String) InteractorProfileNodeAutoBean.this.getOrReify("stroke");
      }
      public java.lang.String getText()  {
        return (java.lang.String) InteractorProfileNodeAutoBean.this.getOrReify("text");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode as = as();
    value = as.getFill();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileNodeAutoBean.this, "fill"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("fill", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fill", value, propertyContext);
    value = as.getLighterFill();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileNodeAutoBean.this, "lighterFill"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("lighterFill", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("lighterFill", value, propertyContext);
    value = as.getLighterStroke();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileNodeAutoBean.this, "lighterStroke"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("lighterStroke", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("lighterStroke", value, propertyContext);
    value = as.getLighterText();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileNodeAutoBean.this, "lighterText"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("lighterText", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("lighterText", value, propertyContext);
    value = as.getStroke();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileNodeAutoBean.this, "stroke"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stroke", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stroke", value, propertyContext);
    value = as.getText();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileNodeAutoBean.this, "text"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("text", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("text", value, propertyContext);
  }
}
