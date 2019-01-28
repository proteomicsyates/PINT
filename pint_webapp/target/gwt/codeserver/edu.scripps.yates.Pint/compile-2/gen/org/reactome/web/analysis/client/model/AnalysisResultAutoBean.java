package org.reactome.web.analysis.client.model;

public class AnalysisResultAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.AnalysisResult> {
  private final org.reactome.web.analysis.client.model.AnalysisResult shim = new org.reactome.web.analysis.client.model.AnalysisResult() {
    public java.lang.Integer getIdentifiersNotFound()  {
      java.lang.Integer toReturn = (java.lang.Integer) AnalysisResultAutoBean.this.getWrapped().getIdentifiersNotFound();
      return toReturn;
    }
    public java.lang.Integer getPathwaysFound()  {
      java.lang.Integer toReturn = (java.lang.Integer) AnalysisResultAutoBean.this.getWrapped().getPathwaysFound();
      return toReturn;
    }
    public java.util.List getWarnings()  {
      java.util.List toReturn = (java.util.List) AnalysisResultAutoBean.this.getWrapped().getWarnings();
      if (toReturn != null) {
        if (AnalysisResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = AnalysisResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getPathways()  {
      java.util.List toReturn = (java.util.List) AnalysisResultAutoBean.this.getWrapped().getPathways();
      if (toReturn != null) {
        if (AnalysisResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = AnalysisResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getResourceSummary()  {
      java.util.List toReturn = (java.util.List) AnalysisResultAutoBean.this.getWrapped().getResourceSummary();
      if (toReturn != null) {
        if (AnalysisResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = AnalysisResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.AnalysisSummary getSummary()  {
      org.reactome.web.analysis.client.model.AnalysisSummary toReturn = (org.reactome.web.analysis.client.model.AnalysisSummary) AnalysisResultAutoBean.this.getWrapped().getSummary();
      if (toReturn != null) {
        if (AnalysisResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = AnalysisResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.analysis.client.model.AnalysisSummaryAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.ExpressionSummary getExpression()  {
      org.reactome.web.analysis.client.model.ExpressionSummary toReturn = (org.reactome.web.analysis.client.model.ExpressionSummary) AnalysisResultAutoBean.this.getWrapped().getExpression();
      if (toReturn != null) {
        if (AnalysisResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = AnalysisResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.analysis.client.model.ExpressionSummaryAutoBean(getFactory(), toReturn).as();
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
  public AnalysisResultAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public AnalysisResultAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.AnalysisResult wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.AnalysisResult as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.AnalysisResult> getType() {return org.reactome.web.analysis.client.model.AnalysisResult.class;}
  @Override protected org.reactome.web.analysis.client.model.AnalysisResult createSimplePeer() {
    return new org.reactome.web.analysis.client.model.AnalysisResult() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.AnalysisResultAutoBean.this.data;
      public java.lang.Integer getIdentifiersNotFound()  {
        return (java.lang.Integer) AnalysisResultAutoBean.this.getOrReify("identifiersNotFound");
      }
      public java.lang.Integer getPathwaysFound()  {
        return (java.lang.Integer) AnalysisResultAutoBean.this.getOrReify("pathwaysFound");
      }
      public java.util.List getWarnings()  {
        return (java.util.List) AnalysisResultAutoBean.this.getOrReify("warnings");
      }
      public java.util.List getPathways()  {
        return (java.util.List) AnalysisResultAutoBean.this.getOrReify("pathways");
      }
      public java.util.List getResourceSummary()  {
        return (java.util.List) AnalysisResultAutoBean.this.getOrReify("resourceSummary");
      }
      public org.reactome.web.analysis.client.model.AnalysisSummary getSummary()  {
        return (org.reactome.web.analysis.client.model.AnalysisSummary) AnalysisResultAutoBean.this.getOrReify("summary");
      }
      public org.reactome.web.analysis.client.model.ExpressionSummary getExpression()  {
        return (org.reactome.web.analysis.client.model.ExpressionSummary) AnalysisResultAutoBean.this.getOrReify("expression");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.AnalysisResult as = as();
    value = as.getIdentifiersNotFound();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisResultAutoBean.this, "identifiersNotFound"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("identifiersNotFound", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("identifiersNotFound", value, propertyContext);
    value = as.getPathwaysFound();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisResultAutoBean.this, "pathwaysFound"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("pathwaysFound", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("pathwaysFound", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getWarnings());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisResultAutoBean.this, "warnings"),
      new Class<?>[] {java.util.List.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("warnings", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("warnings", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPathways());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisResultAutoBean.this, "pathways"),
      new Class<?>[] {java.util.List.class, org.reactome.web.analysis.client.model.PathwaySummary.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("pathways", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("pathways", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getResourceSummary());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisResultAutoBean.this, "resourceSummary"),
      new Class<?>[] {java.util.List.class, org.reactome.web.analysis.client.model.ResourceSummary.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("resourceSummary", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("resourceSummary", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getSummary());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisResultAutoBean.this, "summary"),
      org.reactome.web.analysis.client.model.AnalysisSummary.class
    );
    if (visitor.visitReferenceProperty("summary", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("summary", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExpression());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisResultAutoBean.this, "expression"),
      org.reactome.web.analysis.client.model.ExpressionSummary.class
    );
    if (visitor.visitReferenceProperty("expression", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("expression", bean, propertyContext);
  }
}
