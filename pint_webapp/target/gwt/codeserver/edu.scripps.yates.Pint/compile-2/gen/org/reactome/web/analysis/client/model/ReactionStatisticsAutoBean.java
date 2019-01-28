package org.reactome.web.analysis.client.model;

public class ReactionStatisticsAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.ReactionStatistics> {
  private final org.reactome.web.analysis.client.model.ReactionStatistics shim = new org.reactome.web.analysis.client.model.ReactionStatistics() {
    public java.lang.Double getRatio()  {
      java.lang.Double toReturn = (java.lang.Double) ReactionStatisticsAutoBean.this.getWrapped().getRatio();
      return toReturn;
    }
    public java.lang.Integer getFound()  {
      java.lang.Integer toReturn = (java.lang.Integer) ReactionStatisticsAutoBean.this.getWrapped().getFound();
      return toReturn;
    }
    public java.lang.Integer getTotal()  {
      java.lang.Integer toReturn = (java.lang.Integer) ReactionStatisticsAutoBean.this.getWrapped().getTotal();
      return toReturn;
    }
    public java.lang.String getResource()  {
      java.lang.String toReturn = (java.lang.String) ReactionStatisticsAutoBean.this.getWrapped().getResource();
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
  public ReactionStatisticsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ReactionStatisticsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.ReactionStatistics wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.ReactionStatistics as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.ReactionStatistics> getType() {return org.reactome.web.analysis.client.model.ReactionStatistics.class;}
  @Override protected org.reactome.web.analysis.client.model.ReactionStatistics createSimplePeer() {
    return new org.reactome.web.analysis.client.model.ReactionStatistics() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.ReactionStatisticsAutoBean.this.data;
      public java.lang.Double getRatio()  {
        return (java.lang.Double) ReactionStatisticsAutoBean.this.getOrReify("ratio");
      }
      public java.lang.Integer getFound()  {
        return (java.lang.Integer) ReactionStatisticsAutoBean.this.getOrReify("found");
      }
      public java.lang.Integer getTotal()  {
        return (java.lang.Integer) ReactionStatisticsAutoBean.this.getOrReify("total");
      }
      public java.lang.String getResource()  {
        return (java.lang.String) ReactionStatisticsAutoBean.this.getOrReify("resource");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.ReactionStatistics as = as();
    value = as.getRatio();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ReactionStatisticsAutoBean.this, "ratio"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("ratio", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("ratio", value, propertyContext);
    value = as.getFound();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ReactionStatisticsAutoBean.this, "found"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("found", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("found", value, propertyContext);
    value = as.getTotal();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ReactionStatisticsAutoBean.this, "total"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("total", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("total", value, propertyContext);
    value = as.getResource();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ReactionStatisticsAutoBean.this, "resource"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("resource", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("resource", value, propertyContext);
  }
}
