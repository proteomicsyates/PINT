package org.reactome.web.diagram.profiles.analysis.model;

public class OverlayNodeAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.analysis.model.OverlayNode> {
  private final org.reactome.web.diagram.profiles.analysis.model.OverlayNode shim = new org.reactome.web.diagram.profiles.analysis.model.OverlayNode() {
    public java.lang.String getText()  {
      java.lang.String toReturn = (java.lang.String) OverlayNodeAutoBean.this.getWrapped().getText();
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.analysis.model.OverlayLegend getLegend()  {
      org.reactome.web.diagram.profiles.analysis.model.OverlayLegend toReturn = (org.reactome.web.diagram.profiles.analysis.model.OverlayLegend) OverlayNodeAutoBean.this.getWrapped().getLegend();
      if (toReturn != null) {
        if (OverlayNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = OverlayNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.analysis.model.OverlayLegendAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.analysis.model.ProfileGradient getGradient()  {
      org.reactome.web.diagram.profiles.analysis.model.ProfileGradient toReturn = (org.reactome.web.diagram.profiles.analysis.model.ProfileGradient) OverlayNodeAutoBean.this.getWrapped().getGradient();
      if (toReturn != null) {
        if (OverlayNodeAutoBean.this.isWrapped(toReturn)) {
          toReturn = OverlayNodeAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.analysis.model.ProfileGradientAutoBean(getFactory(), toReturn).as();
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
  public OverlayNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public OverlayNodeAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.analysis.model.OverlayNode wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.analysis.model.OverlayNode as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.analysis.model.OverlayNode> getType() {return org.reactome.web.diagram.profiles.analysis.model.OverlayNode.class;}
  @Override protected org.reactome.web.diagram.profiles.analysis.model.OverlayNode createSimplePeer() {
    return new org.reactome.web.diagram.profiles.analysis.model.OverlayNode() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.analysis.model.OverlayNodeAutoBean.this.data;
      public java.lang.String getText()  {
        return (java.lang.String) OverlayNodeAutoBean.this.getOrReify("text");
      }
      public org.reactome.web.diagram.profiles.analysis.model.OverlayLegend getLegend()  {
        return (org.reactome.web.diagram.profiles.analysis.model.OverlayLegend) OverlayNodeAutoBean.this.getOrReify("legend");
      }
      public org.reactome.web.diagram.profiles.analysis.model.ProfileGradient getGradient()  {
        return (org.reactome.web.diagram.profiles.analysis.model.ProfileGradient) OverlayNodeAutoBean.this.getOrReify("gradient");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.analysis.model.OverlayNode as = as();
    value = as.getText();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(OverlayNodeAutoBean.this, "text"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("text", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("text", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getLegend());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(OverlayNodeAutoBean.this, "legend"),
      org.reactome.web.diagram.profiles.analysis.model.OverlayLegend.class
    );
    if (visitor.visitReferenceProperty("legend", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("legend", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getGradient());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(OverlayNodeAutoBean.this, "gradient"),
      org.reactome.web.diagram.profiles.analysis.model.ProfileGradient.class
    );
    if (visitor.visitReferenceProperty("gradient", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("gradient", bean, propertyContext);
  }
}
