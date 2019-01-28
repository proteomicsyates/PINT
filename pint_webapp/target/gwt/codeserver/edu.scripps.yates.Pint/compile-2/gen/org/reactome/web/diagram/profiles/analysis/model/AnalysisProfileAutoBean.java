package org.reactome.web.diagram.profiles.analysis.model;

public class AnalysisProfileAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile> {
  private final org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile shim = new org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile() {
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) AnalysisProfileAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public java.lang.String getRibbon()  {
      java.lang.String toReturn = (java.lang.String) AnalysisProfileAutoBean.this.getWrapped().getRibbon();
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.analysis.model.OverlayNode getEnrichment()  {
      org.reactome.web.diagram.profiles.analysis.model.OverlayNode toReturn = (org.reactome.web.diagram.profiles.analysis.model.OverlayNode) AnalysisProfileAutoBean.this.getWrapped().getEnrichment();
      if (toReturn != null) {
        if (AnalysisProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = AnalysisProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.analysis.model.OverlayNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.analysis.model.OverlayNode getExpression()  {
      org.reactome.web.diagram.profiles.analysis.model.OverlayNode toReturn = (org.reactome.web.diagram.profiles.analysis.model.OverlayNode) AnalysisProfileAutoBean.this.getWrapped().getExpression();
      if (toReturn != null) {
        if (AnalysisProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = AnalysisProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.analysis.model.OverlayNodeAutoBean(getFactory(), toReturn).as();
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
  public AnalysisProfileAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public AnalysisProfileAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile> getType() {return org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile.class;}
  @Override protected org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile createSimplePeer() {
    return new org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.analysis.model.AnalysisProfileAutoBean.this.data;
      public java.lang.String getName()  {
        return (java.lang.String) AnalysisProfileAutoBean.this.getOrReify("name");
      }
      public java.lang.String getRibbon()  {
        return (java.lang.String) AnalysisProfileAutoBean.this.getOrReify("ribbon");
      }
      public org.reactome.web.diagram.profiles.analysis.model.OverlayNode getEnrichment()  {
        return (org.reactome.web.diagram.profiles.analysis.model.OverlayNode) AnalysisProfileAutoBean.this.getOrReify("enrichment");
      }
      public org.reactome.web.diagram.profiles.analysis.model.OverlayNode getExpression()  {
        return (org.reactome.web.diagram.profiles.analysis.model.OverlayNode) AnalysisProfileAutoBean.this.getOrReify("expression");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile as = as();
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisProfileAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    value = as.getRibbon();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisProfileAutoBean.this, "ribbon"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("ribbon", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("ribbon", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEnrichment());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisProfileAutoBean.this, "enrichment"),
      org.reactome.web.diagram.profiles.analysis.model.OverlayNode.class
    );
    if (visitor.visitReferenceProperty("enrichment", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("enrichment", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExpression());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisProfileAutoBean.this, "expression"),
      org.reactome.web.diagram.profiles.analysis.model.OverlayNode.class
    );
    if (visitor.visitReferenceProperty("expression", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("expression", bean, propertyContext);
  }
}
