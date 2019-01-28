package org.reactome.web.analysis.client.model;

public class FoundEntityAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.FoundEntity> {
  private final org.reactome.web.analysis.client.model.FoundEntity shim = new org.reactome.web.analysis.client.model.FoundEntity() {
    public java.lang.String getId()  {
      java.lang.String toReturn = (java.lang.String) FoundEntityAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.util.List getExp()  {
      java.util.List toReturn = (java.util.List) FoundEntityAutoBean.this.getWrapped().getExp();
      if (toReturn != null) {
        if (FoundEntityAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundEntityAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.Set getMapsTo()  {
      java.util.Set toReturn = (java.util.Set) FoundEntityAutoBean.this.getWrapped().getMapsTo();
      if (toReturn != null) {
        if (FoundEntityAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundEntityAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.SetAutoBean(getFactory(), toReturn).as();
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
  public FoundEntityAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public FoundEntityAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.FoundEntity wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.FoundEntity as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.FoundEntity> getType() {return org.reactome.web.analysis.client.model.FoundEntity.class;}
  @Override protected org.reactome.web.analysis.client.model.FoundEntity createSimplePeer() {
    return new org.reactome.web.analysis.client.model.FoundEntity() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.FoundEntityAutoBean.this.data;
      public java.lang.String getId()  {
        return (java.lang.String) FoundEntityAutoBean.this.getOrReify("id");
      }
      public java.util.List getExp()  {
        return (java.util.List) FoundEntityAutoBean.this.getOrReify("exp");
      }
      public java.util.Set getMapsTo()  {
        return (java.util.Set) FoundEntityAutoBean.this.getOrReify("mapsTo");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.FoundEntity as = as();
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundEntityAutoBean.this, "id"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundEntityAutoBean.this, "exp"),
      new Class<?>[] {java.util.List.class, java.lang.Double.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("exp", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("exp", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getMapsTo());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundEntityAutoBean.this, "mapsTo"),
      new Class<?>[] {java.util.Set.class, org.reactome.web.analysis.client.model.IdentifierMap.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("mapsTo", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("mapsTo", bean, propertyContext);
  }
}
