package org.reactome.web.diagram.profiles.diagram.model;

public class DiagramProfilePropertiesAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties> {
  private final org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties shim = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties() {
    public java.lang.String getButton()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getButton();
      return toReturn;
    }
    public java.lang.String getDisease()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getDisease();
      return toReturn;
    }
    public java.lang.String getFlag()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getFlag();
      return toReturn;
    }
    public java.lang.String getHalo()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getHalo();
      return toReturn;
    }
    public java.lang.String getHighlight()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getHighlight();
      return toReturn;
    }
    public java.lang.String getHovering()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getHovering();
      return toReturn;
    }
    public java.lang.String getSelection()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getSelection();
      return toReturn;
    }
    public java.lang.String getText()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getText();
      return toReturn;
    }
    public java.lang.String getTrigger()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfilePropertiesAutoBean.this.getWrapped().getTrigger();
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
  public DiagramProfilePropertiesAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public DiagramProfilePropertiesAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties> getType() {return org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties.class;}
  @Override protected org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties createSimplePeer() {
    return new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.diagram.model.DiagramProfilePropertiesAutoBean.this.data;
      public java.lang.String getButton()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("button");
      }
      public java.lang.String getDisease()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("disease");
      }
      public java.lang.String getFlag()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("flag");
      }
      public java.lang.String getHalo()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("halo");
      }
      public java.lang.String getHighlight()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("highlight");
      }
      public java.lang.String getHovering()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("hovering");
      }
      public java.lang.String getSelection()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("selection");
      }
      public java.lang.String getText()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("text");
      }
      public java.lang.String getTrigger()  {
        return (java.lang.String) DiagramProfilePropertiesAutoBean.this.getOrReify("trigger");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties as = as();
    value = as.getButton();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "button"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("button", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("button", value, propertyContext);
    value = as.getDisease();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "disease"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("disease", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("disease", value, propertyContext);
    value = as.getFlag();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "flag"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("flag", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("flag", value, propertyContext);
    value = as.getHalo();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "halo"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("halo", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("halo", value, propertyContext);
    value = as.getHighlight();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "highlight"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("highlight", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("highlight", value, propertyContext);
    value = as.getHovering();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "hovering"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("hovering", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("hovering", value, propertyContext);
    value = as.getSelection();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "selection"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("selection", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("selection", value, propertyContext);
    value = as.getText();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "text"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("text", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("text", value, propertyContext);
    value = as.getTrigger();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfilePropertiesAutoBean.this, "trigger"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("trigger", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("trigger", value, propertyContext);
  }
}
