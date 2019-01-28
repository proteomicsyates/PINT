package org.reactome.web.diagram.profiles.analysis.model;

public class OverlayLegendAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.analysis.model.OverlayLegend> {
  private final org.reactome.web.diagram.profiles.analysis.model.OverlayLegend shim = new org.reactome.web.diagram.profiles.analysis.model.OverlayLegend() {
    public java.lang.String getHover()  {
      java.lang.String toReturn = (java.lang.String) OverlayLegendAutoBean.this.getWrapped().getHover();
      return toReturn;
    }
    public java.lang.String getMedian()  {
      java.lang.String toReturn = (java.lang.String) OverlayLegendAutoBean.this.getWrapped().getMedian();
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
  public OverlayLegendAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public OverlayLegendAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.analysis.model.OverlayLegend wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.analysis.model.OverlayLegend as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.analysis.model.OverlayLegend> getType() {return org.reactome.web.diagram.profiles.analysis.model.OverlayLegend.class;}
  @Override protected org.reactome.web.diagram.profiles.analysis.model.OverlayLegend createSimplePeer() {
    return new org.reactome.web.diagram.profiles.analysis.model.OverlayLegend() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.analysis.model.OverlayLegendAutoBean.this.data;
      public java.lang.String getHover()  {
        return (java.lang.String) OverlayLegendAutoBean.this.getOrReify("hover");
      }
      public java.lang.String getMedian()  {
        return (java.lang.String) OverlayLegendAutoBean.this.getOrReify("median");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.analysis.model.OverlayLegend as = as();
    value = as.getHover();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(OverlayLegendAutoBean.this, "hover"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("hover", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("hover", value, propertyContext);
    value = as.getMedian();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(OverlayLegendAutoBean.this, "median"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("median", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("median", value, propertyContext);
  }
}
