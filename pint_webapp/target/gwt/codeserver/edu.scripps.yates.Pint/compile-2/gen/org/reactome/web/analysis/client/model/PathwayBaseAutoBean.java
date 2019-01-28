package org.reactome.web.analysis.client.model;

public class PathwayBaseAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.PathwayBase> {
  private final org.reactome.web.analysis.client.model.PathwayBase shim = new org.reactome.web.analysis.client.model.PathwayBase() {
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) PathwayBaseAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.String getStId()  {
      java.lang.String toReturn = (java.lang.String) PathwayBaseAutoBean.this.getWrapped().getStId();
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.EntityStatistics getEntities()  {
      org.reactome.web.analysis.client.model.EntityStatistics toReturn = (org.reactome.web.analysis.client.model.EntityStatistics) PathwayBaseAutoBean.this.getWrapped().getEntities();
      if (toReturn != null) {
        if (PathwayBaseAutoBean.this.isWrapped(toReturn)) {
          toReturn = PathwayBaseAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.analysis.client.model.EntityStatisticsAutoBean(getFactory(), toReturn).as();
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
  public PathwayBaseAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public PathwayBaseAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.PathwayBase wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.PathwayBase as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.PathwayBase> getType() {return org.reactome.web.analysis.client.model.PathwayBase.class;}
  @Override protected org.reactome.web.analysis.client.model.PathwayBase createSimplePeer() {
    return new org.reactome.web.analysis.client.model.PathwayBase() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.PathwayBaseAutoBean.this.data;
      public java.lang.Long getDbId()  {
        return (java.lang.Long) PathwayBaseAutoBean.this.getOrReify("dbId");
      }
      public java.lang.String getStId()  {
        return (java.lang.String) PathwayBaseAutoBean.this.getOrReify("stId");
      }
      public org.reactome.web.analysis.client.model.EntityStatistics getEntities()  {
        return (org.reactome.web.analysis.client.model.EntityStatistics) PathwayBaseAutoBean.this.getOrReify("entities");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.PathwayBase as = as();
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwayBaseAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getStId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwayBaseAutoBean.this, "stId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stId", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEntities());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(PathwayBaseAutoBean.this, "entities"),
      org.reactome.web.analysis.client.model.EntityStatistics.class
    );
    if (visitor.visitReferenceProperty("entities", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("entities", bean, propertyContext);
  }
}
