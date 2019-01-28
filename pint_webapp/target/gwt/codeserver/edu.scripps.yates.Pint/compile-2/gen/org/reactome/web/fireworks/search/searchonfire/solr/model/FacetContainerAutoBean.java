package org.reactome.web.fireworks.search.searchonfire.solr.model;

public class FacetContainerAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer> {
  private final org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer shim = new org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer() {
    public java.lang.Integer getCount()  {
      java.lang.Integer toReturn = (java.lang.Integer) FacetContainerAutoBean.this.getWrapped().getCount();
      return toReturn;
    }
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) FacetContainerAutoBean.this.getWrapped().getName();
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
  public FacetContainerAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public FacetContainerAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer as() {return shim;}
  public Class<org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer> getType() {return org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer.class;}
  @Override protected org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer createSimplePeer() {
    return new org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainerAutoBean.this.data;
      public java.lang.Integer getCount()  {
        return (java.lang.Integer) FacetContainerAutoBean.this.getOrReify("count");
      }
      public java.lang.String getName()  {
        return (java.lang.String) FacetContainerAutoBean.this.getOrReify("name");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer as = as();
    value = as.getCount();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FacetContainerAutoBean.this, "count"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("count", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("count", value, propertyContext);
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FacetContainerAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
  }
}
