package org.reactome.web.fireworks.search.searchonfire.solr.model;

public class EntryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.fireworks.search.searchonfire.solr.model.Entry> {
  private final org.reactome.web.fireworks.search.searchonfire.solr.model.Entry shim = new org.reactome.web.fireworks.search.searchonfire.solr.model.Entry() {
    public java.lang.String getExactType()  {
      java.lang.String toReturn = (java.lang.String) EntryAutoBean.this.getWrapped().getExactType();
      return toReturn;
    }
    public java.lang.String getId()  {
      java.lang.String toReturn = (java.lang.String) EntryAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) EntryAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public java.lang.String getStId()  {
      java.lang.String toReturn = (java.lang.String) EntryAutoBean.this.getWrapped().getStId();
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
  public EntryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public EntryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.fireworks.search.searchonfire.solr.model.Entry wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.fireworks.search.searchonfire.solr.model.Entry as() {return shim;}
  public Class<org.reactome.web.fireworks.search.searchonfire.solr.model.Entry> getType() {return org.reactome.web.fireworks.search.searchonfire.solr.model.Entry.class;}
  @Override protected org.reactome.web.fireworks.search.searchonfire.solr.model.Entry createSimplePeer() {
    return new org.reactome.web.fireworks.search.searchonfire.solr.model.Entry() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.fireworks.search.searchonfire.solr.model.EntryAutoBean.this.data;
      public java.lang.String getExactType()  {
        return (java.lang.String) EntryAutoBean.this.getOrReify("exactType");
      }
      public java.lang.String getId()  {
        return (java.lang.String) EntryAutoBean.this.getOrReify("id");
      }
      public java.lang.String getName()  {
        return (java.lang.String) EntryAutoBean.this.getOrReify("name");
      }
      public java.lang.String getStId()  {
        return (java.lang.String) EntryAutoBean.this.getOrReify("stId");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.fireworks.search.searchonfire.solr.model.Entry as = as();
    value = as.getExactType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntryAutoBean.this, "exactType"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("exactType", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("exactType", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntryAutoBean.this, "id"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntryAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    value = as.getStId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntryAutoBean.this, "stId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("stId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stId", value, propertyContext);
  }
}
