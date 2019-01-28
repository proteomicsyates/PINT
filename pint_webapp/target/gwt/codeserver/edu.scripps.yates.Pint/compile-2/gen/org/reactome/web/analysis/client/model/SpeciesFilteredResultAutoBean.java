package org.reactome.web.analysis.client.model;

public class SpeciesFilteredResultAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.SpeciesFilteredResult> {
  private final org.reactome.web.analysis.client.model.SpeciesFilteredResult shim = new org.reactome.web.analysis.client.model.SpeciesFilteredResult() {
    public java.lang.String getType()  {
      java.lang.String toReturn = (java.lang.String) SpeciesFilteredResultAutoBean.this.getWrapped().getType();
      return toReturn;
    }
    public java.util.List getPathways()  {
      java.util.List toReturn = (java.util.List) SpeciesFilteredResultAutoBean.this.getWrapped().getPathways();
      if (toReturn != null) {
        if (SpeciesFilteredResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = SpeciesFilteredResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.AnalysisType getAnalysisType()  {
      org.reactome.web.analysis.client.model.AnalysisType toReturn = (org.reactome.web.analysis.client.model.AnalysisType) SpeciesFilteredResultAutoBean.this.getWrapped().getAnalysisType();
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.ExpressionSummary getExpressionSummary()  {
      org.reactome.web.analysis.client.model.ExpressionSummary toReturn = (org.reactome.web.analysis.client.model.ExpressionSummary) SpeciesFilteredResultAutoBean.this.getWrapped().getExpressionSummary();
      if (toReturn != null) {
        if (SpeciesFilteredResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = SpeciesFilteredResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.analysis.client.model.ExpressionSummaryAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setAnalysisType(org.reactome.web.analysis.client.model.AnalysisType analysisType)  {
      SpeciesFilteredResultAutoBean.this.getWrapped().setAnalysisType(analysisType);
      SpeciesFilteredResultAutoBean.this.set("setAnalysisType", analysisType);
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
  public SpeciesFilteredResultAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public SpeciesFilteredResultAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.SpeciesFilteredResult wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.SpeciesFilteredResult as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.SpeciesFilteredResult> getType() {return org.reactome.web.analysis.client.model.SpeciesFilteredResult.class;}
  @Override protected org.reactome.web.analysis.client.model.SpeciesFilteredResult createSimplePeer() {
    return new org.reactome.web.analysis.client.model.SpeciesFilteredResult() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.SpeciesFilteredResultAutoBean.this.data;
      public java.lang.String getType()  {
        return (java.lang.String) SpeciesFilteredResultAutoBean.this.getOrReify("type");
      }
      public java.util.List getPathways()  {
        return (java.util.List) SpeciesFilteredResultAutoBean.this.getOrReify("pathways");
      }
      public org.reactome.web.analysis.client.model.AnalysisType getAnalysisType()  {
        return (org.reactome.web.analysis.client.model.AnalysisType) SpeciesFilteredResultAutoBean.this.getOrReify("analysisType");
      }
      public org.reactome.web.analysis.client.model.ExpressionSummary getExpressionSummary()  {
        return (org.reactome.web.analysis.client.model.ExpressionSummary) SpeciesFilteredResultAutoBean.this.getOrReify("expressionSummary");
      }
      public void setAnalysisType(org.reactome.web.analysis.client.model.AnalysisType analysisType)  {
        SpeciesFilteredResultAutoBean.this.setProperty("analysisType", analysisType);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.SpeciesFilteredResult as = as();
    value = as.getType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SpeciesFilteredResultAutoBean.this, "type"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("type", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("type", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPathways());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SpeciesFilteredResultAutoBean.this, "pathways"),
      new Class<?>[] {java.util.List.class, org.reactome.web.analysis.client.model.PathwayBase.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("pathways", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("pathways", bean, propertyContext);
    value = as.getAnalysisType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SpeciesFilteredResultAutoBean.this, "analysisType"),
      org.reactome.web.analysis.client.model.AnalysisType.class
    );
    if (visitor.visitValueProperty("analysisType", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("analysisType", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExpressionSummary());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SpeciesFilteredResultAutoBean.this, "expressionSummary"),
      org.reactome.web.analysis.client.model.ExpressionSummary.class
    );
    if (visitor.visitReferenceProperty("expressionSummary", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("expressionSummary", bean, propertyContext);
  }
}
