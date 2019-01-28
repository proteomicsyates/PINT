package org.reactome.web.diagram.data.layout;

public class SummaryItemAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.SummaryItem> {
  private final org.reactome.web.diagram.data.layout.SummaryItem shim = new org.reactome.web.diagram.data.layout.SummaryItem() {
    public java.lang.Boolean getHit()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) SummaryItemAutoBean.this.getWrapped().getHit();
      return toReturn;
    }
    public java.lang.Boolean getPressed()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) SummaryItemAutoBean.this.getWrapped().getPressed();
      return toReturn;
    }
    public java.lang.Integer getNumber()  {
      java.lang.Integer toReturn = (java.lang.Integer) SummaryItemAutoBean.this.getWrapped().getNumber();
      return toReturn;
    }
    public java.lang.String getType()  {
      java.lang.String toReturn = (java.lang.String) SummaryItemAutoBean.this.getWrapped().getType();
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Shape getShape()  {
      org.reactome.web.diagram.data.layout.Shape toReturn = (org.reactome.web.diagram.data.layout.Shape) SummaryItemAutoBean.this.getWrapped().getShape();
      if (toReturn != null) {
        if (SummaryItemAutoBean.this.isWrapped(toReturn)) {
          toReturn = SummaryItemAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ShapeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setHit(java.lang.Boolean hit)  {
      SummaryItemAutoBean.this.getWrapped().setHit(hit);
      SummaryItemAutoBean.this.set("setHit", hit);
    }
    public void setNumber(java.lang.Integer number)  {
      SummaryItemAutoBean.this.getWrapped().setNumber(number);
      SummaryItemAutoBean.this.set("setNumber", number);
    }
    public void setPressed(java.lang.Boolean pressed)  {
      SummaryItemAutoBean.this.getWrapped().setPressed(pressed);
      SummaryItemAutoBean.this.set("setPressed", pressed);
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
  public SummaryItemAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public SummaryItemAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.SummaryItem wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.SummaryItem as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.SummaryItem> getType() {return org.reactome.web.diagram.data.layout.SummaryItem.class;}
  @Override protected org.reactome.web.diagram.data.layout.SummaryItem createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.SummaryItem() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.SummaryItemAutoBean.this.data;
      public java.lang.Boolean getHit()  {
        return (java.lang.Boolean) SummaryItemAutoBean.this.getOrReify("hit");
      }
      public java.lang.Boolean getPressed()  {
        return (java.lang.Boolean) SummaryItemAutoBean.this.getOrReify("pressed");
      }
      public java.lang.Integer getNumber()  {
        return (java.lang.Integer) SummaryItemAutoBean.this.getOrReify("number");
      }
      public java.lang.String getType()  {
        return (java.lang.String) SummaryItemAutoBean.this.getOrReify("type");
      }
      public org.reactome.web.diagram.data.layout.Shape getShape()  {
        return (org.reactome.web.diagram.data.layout.Shape) SummaryItemAutoBean.this.getOrReify("shape");
      }
      public void setHit(java.lang.Boolean hit)  {
        SummaryItemAutoBean.this.setProperty("hit", hit);
      }
      public void setNumber(java.lang.Integer number)  {
        SummaryItemAutoBean.this.setProperty("number", number);
      }
      public void setPressed(java.lang.Boolean pressed)  {
        SummaryItemAutoBean.this.setProperty("pressed", pressed);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.SummaryItem as = as();
    value = as.getHit();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SummaryItemAutoBean.this, "hit"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("hit", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("hit", value, propertyContext);
    value = as.getPressed();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SummaryItemAutoBean.this, "pressed"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("pressed", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("pressed", value, propertyContext);
    value = as.getNumber();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SummaryItemAutoBean.this, "number"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("number", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("number", value, propertyContext);
    value = as.getType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SummaryItemAutoBean.this, "type"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("type", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("type", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getShape());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SummaryItemAutoBean.this, "shape"),
      org.reactome.web.diagram.data.layout.Shape.class
    );
    if (visitor.visitReferenceProperty("shape", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("shape", bean, propertyContext);
  }
}
