package org.reactome.web.analysis.client.model;

public class SpeciesSummaryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.SpeciesSummary> {
  private final org.reactome.web.analysis.client.model.SpeciesSummary shim = new org.reactome.web.analysis.client.model.SpeciesSummary() {
    public java.lang.Long getDbId()  {
      java.lang.Long toReturn = (java.lang.Long) SpeciesSummaryAutoBean.this.getWrapped().getDbId();
      return toReturn;
    }
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) SpeciesSummaryAutoBean.this.getWrapped().getName();
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
  public SpeciesSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public SpeciesSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.SpeciesSummary wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.SpeciesSummary as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.SpeciesSummary> getType() {return org.reactome.web.analysis.client.model.SpeciesSummary.class;}
  @Override protected org.reactome.web.analysis.client.model.SpeciesSummary createSimplePeer() {
    return new org.reactome.web.analysis.client.model.SpeciesSummary() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.SpeciesSummaryAutoBean.this.data;
      public java.lang.Long getDbId()  {
        return (java.lang.Long) SpeciesSummaryAutoBean.this.getOrReify("dbId");
      }
      public java.lang.String getName()  {
        return (java.lang.String) SpeciesSummaryAutoBean.this.getOrReify("name");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.SpeciesSummary as = as();
    value = as.getDbId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SpeciesSummaryAutoBean.this, "dbId"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("dbId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("dbId", value, propertyContext);
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SpeciesSummaryAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
  }
}
