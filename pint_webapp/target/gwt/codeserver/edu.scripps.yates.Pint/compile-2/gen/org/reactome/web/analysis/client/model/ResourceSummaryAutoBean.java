package org.reactome.web.analysis.client.model;

public class ResourceSummaryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.ResourceSummary> {
  private final org.reactome.web.analysis.client.model.ResourceSummary shim = new org.reactome.web.analysis.client.model.ResourceSummary() {
    public java.lang.Integer getPathways()  {
      java.lang.Integer toReturn = (java.lang.Integer) ResourceSummaryAutoBean.this.getWrapped().getPathways();
      return toReturn;
    }
    public java.lang.String getResource()  {
      java.lang.String toReturn = (java.lang.String) ResourceSummaryAutoBean.this.getWrapped().getResource();
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
  public ResourceSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ResourceSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.ResourceSummary wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.ResourceSummary as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.ResourceSummary> getType() {return org.reactome.web.analysis.client.model.ResourceSummary.class;}
  @Override protected org.reactome.web.analysis.client.model.ResourceSummary createSimplePeer() {
    return new org.reactome.web.analysis.client.model.ResourceSummary() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.ResourceSummaryAutoBean.this.data;
      public java.lang.Integer getPathways()  {
        return (java.lang.Integer) ResourceSummaryAutoBean.this.getOrReify("pathways");
      }
      public java.lang.String getResource()  {
        return (java.lang.String) ResourceSummaryAutoBean.this.getOrReify("resource");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.ResourceSummary as = as();
    value = as.getPathways();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ResourceSummaryAutoBean.this, "pathways"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("pathways", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("pathways", value, propertyContext);
    value = as.getResource();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ResourceSummaryAutoBean.this, "resource"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("resource", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("resource", value, propertyContext);
  }
}
