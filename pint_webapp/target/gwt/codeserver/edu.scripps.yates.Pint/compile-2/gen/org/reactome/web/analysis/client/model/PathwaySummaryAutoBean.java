package org.reactome.web.analysis.client.model;

public class PathwaySummaryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.PathwaySummary> {
  private final org.reactome.web.analysis.client.model.PathwaySummary shim = new org.reactome.web.analysis.client.model.PathwaySummary() {
    public boolean getLlp()  {
      boolean toReturn = (boolean) PathwaySummaryAutoBean.this.getWrapped().getLlp();
      return toReturn;
    }
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) PathwaySummaryAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) PathwaySummaryAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public java.lang.String getStId()  {
      java.lang.String toReturn = (java.lang.String) PathwaySummaryAutoBean.this.getWrapped().getStId();
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.EntityStatistics getEntities()  {
      org.reactome.web.analysis.client.model.EntityStatistics toReturn = (org.reactome.web.analysis.client.model.EntityStatistics) PathwaySummaryAutoBean.this.getWrapped().getEntities();
      if (toReturn != null) {
        if (PathwaySummaryAutoBean.this.isWrapped(toReturn)) {
          toReturn = PathwaySummaryAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.analysis.client.model.EntityStatisticsAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.ReactionStatistics getReactions()  {
      org.reactome.web.analysis.client.model.ReactionStatistics toReturn = (org.reactome.web.analysis.client.model.ReactionStatistics) PathwaySummaryAutoBean.this.getWrapped().getReactions();
      if (toReturn != null) {
        if (PathwaySummaryAutoBean.this.isWrapped(toReturn)) {
          toReturn = PathwaySummaryAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.analysis.client.model.ReactionStatisticsAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.SpeciesSummary getSpecies()  {
      org.reactome.web.analysis.client.model.SpeciesSummary toReturn = (org.reactome.web.analysis.client.model.SpeciesSummary) PathwaySummaryAutoBean.this.getWrapped().getSpecies();
      if (toReturn != null) {
        if (PathwaySummaryAutoBean.this.isWrapped(toReturn)) {
          toReturn = PathwaySummaryAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.analysis.client.model.SpeciesSummaryAutoBean(getFactory(), toReturn).as();
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
  public PathwaySummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public PathwaySummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.PathwaySummary wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.PathwaySummary as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.PathwaySummary> getType() {return org.reactome.web.analysis.client.model.PathwaySummary.class;}
  @Override protected org.reactome.web.analysis.client.model.PathwaySummary createSimplePeer() {
    return new org.reactome.web.analysis.client.model.PathwaySummary() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.PathwaySummaryAutoBean.this.data;
      public boolean getLlp()  {
        java.lang.Boolean toReturn = PathwaySummaryAutoBean.this.getOrReify("llp");
        return toReturn == null ? false : toReturn;
      }
      public java.lang.Long getDbId()  {
        return (java.lang.Long) PathwaySummaryAutoBean.this.getOrReify("dbId");
      }
      public java.lang.String getName()  {
        return (java.lang.String) PathwaySummaryAutoBean.this.getOrReify("name");
      }
      public java.lang.String getStId()  {
        return (java.lang.String) PathwaySummaryAutoBean.this.getOrReify("stId");
      }
      public org.reactome.web.analysis.client.model.EntityStatistics getEntities()  {
        return (org.reactome.web.analysis.client.model.EntityStatistics) PathwaySummaryAutoBean.this.getOrReify("entities");
      }
      public org.reactome.web.analysis.client.model.ReactionStatistics getReactions()  {
        return (org.reactome.web.analysis.client.model.ReactionStatistics) PathwaySummaryAutoBean.this.getOrReify("reactions");
      }
      public org.reactome.web.analysis.client.model.SpeciesSummary getSpecies()  {
        return (org.reactome.web.analysis.client.model.SpeciesSummary) PathwaySummaryAutoBean.this.getOrReify("species");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.PathwaySummary as = as();
    value = as.getLlp();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwaySummaryAutoBean.this, "llp"),
      boolean.class
    );
    if (visitor.visitValueProperty("llp", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("llp", value, propertyContext);
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwaySummaryAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwaySummaryAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    value = as.getStId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwaySummaryAutoBean.this, "stId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stId", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEntities());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwaySummaryAutoBean.this, "entities"),
      org.reactome.web.analysis.client.model.EntityStatistics.class
    );
    if (visitor.visitReferenceProperty("entities", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("entities", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getReactions());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwaySummaryAutoBean.this, "reactions"),
      org.reactome.web.analysis.client.model.ReactionStatistics.class
    );
    if (visitor.visitReferenceProperty("reactions", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("reactions", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getSpecies());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwaySummaryAutoBean.this, "species"),
      org.reactome.web.analysis.client.model.SpeciesSummary.class
    );
    if (visitor.visitReferenceProperty("species", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("species", bean, propertyContext);
  }
}
