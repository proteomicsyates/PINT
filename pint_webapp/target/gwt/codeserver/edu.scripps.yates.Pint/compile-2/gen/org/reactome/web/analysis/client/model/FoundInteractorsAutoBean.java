package org.reactome.web.analysis.client.model;

public class FoundInteractorsAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.FoundInteractors> {
  private final org.reactome.web.analysis.client.model.FoundInteractors shim = new org.reactome.web.analysis.client.model.FoundInteractors() {
    public java.lang.Integer getFound()  {
      java.lang.Integer toReturn = (java.lang.Integer) FoundInteractorsAutoBean.this.getWrapped().getFound();
      return toReturn;
    }
    public java.util.List getExpNames()  {
      java.util.List toReturn = (java.util.List) FoundInteractorsAutoBean.this.getWrapped().getExpNames();
      if (toReturn != null) {
        if (FoundInteractorsAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundInteractorsAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getIdentifiers()  {
      java.util.List toReturn = (java.util.List) FoundInteractorsAutoBean.this.getWrapped().getIdentifiers();
      if (toReturn != null) {
        if (FoundInteractorsAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundInteractorsAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.Set getResources()  {
      java.util.Set toReturn = (java.util.Set) FoundInteractorsAutoBean.this.getWrapped().getResources();
      if (toReturn != null) {
        if (FoundInteractorsAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundInteractorsAutoBean.this.getFromWrapper(toReturn);
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
  public FoundInteractorsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public FoundInteractorsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.FoundInteractors wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.FoundInteractors as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.FoundInteractors> getType() {return org.reactome.web.analysis.client.model.FoundInteractors.class;}
  @Override protected org.reactome.web.analysis.client.model.FoundInteractors createSimplePeer() {
    return new org.reactome.web.analysis.client.model.FoundInteractors() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.FoundInteractorsAutoBean.this.data;
      public java.lang.Integer getFound()  {
        return (java.lang.Integer) FoundInteractorsAutoBean.this.getOrReify("found");
      }
      public java.util.List getExpNames()  {
        return (java.util.List) FoundInteractorsAutoBean.this.getOrReify("expNames");
      }
      public java.util.List getIdentifiers()  {
        return (java.util.List) FoundInteractorsAutoBean.this.getOrReify("identifiers");
      }
      public java.util.Set getResources()  {
        return (java.util.Set) FoundInteractorsAutoBean.this.getOrReify("resources");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.FoundInteractors as = as();
    value = as.getFound();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundInteractorsAutoBean.this, "found"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("found", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("found", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExpNames());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundInteractorsAutoBean.this, "expNames"),
      new Class<?>[] {java.util.List.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("expNames", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("expNames", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getIdentifiers());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundInteractorsAutoBean.this, "identifiers"),
      new Class<?>[] {java.util.List.class, org.reactome.web.analysis.client.model.FoundInteractor.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("identifiers", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("identifiers", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getResources());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundInteractorsAutoBean.this, "resources"),
      new Class<?>[] {java.util.Set.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("resources", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("resources", bean, propertyContext);
  }
}
