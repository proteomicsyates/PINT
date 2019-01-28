package org.reactome.web.analysis.client.model;

public class FoundInteractorAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.FoundInteractor> {
  private final org.reactome.web.analysis.client.model.FoundInteractor shim = new org.reactome.web.analysis.client.model.FoundInteractor() {
    public java.lang.String getId()  {
      java.lang.String toReturn = (java.lang.String) FoundInteractorAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.util.List getExp()  {
      java.util.List toReturn = (java.util.List) FoundInteractorAutoBean.this.getWrapped().getExp();
      if (toReturn != null) {
        if (FoundInteractorAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundInteractorAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.Set getMapsTo()  {
      java.util.Set toReturn = (java.util.Set) FoundInteractorAutoBean.this.getWrapped().getMapsTo();
      if (toReturn != null) {
        if (FoundInteractorAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundInteractorAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.SetAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.analysis.client.model.IdentifierMap getInteractsWith()  {
      org.reactome.web.analysis.client.model.IdentifierMap toReturn = (org.reactome.web.analysis.client.model.IdentifierMap) FoundInteractorAutoBean.this.getWrapped().getInteractsWith();
      if (toReturn != null) {
        if (FoundInteractorAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundInteractorAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.analysis.client.model.IdentifierMapAutoBean(getFactory(), toReturn).as();
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
  public FoundInteractorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public FoundInteractorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.FoundInteractor wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.FoundInteractor as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.FoundInteractor> getType() {return org.reactome.web.analysis.client.model.FoundInteractor.class;}
  @Override protected org.reactome.web.analysis.client.model.FoundInteractor createSimplePeer() {
    return new org.reactome.web.analysis.client.model.FoundInteractor() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.FoundInteractorAutoBean.this.data;
      public java.lang.String getId()  {
        return (java.lang.String) FoundInteractorAutoBean.this.getOrReify("id");
      }
      public java.util.List getExp()  {
        return (java.util.List) FoundInteractorAutoBean.this.getOrReify("exp");
      }
      public java.util.Set getMapsTo()  {
        return (java.util.Set) FoundInteractorAutoBean.this.getOrReify("mapsTo");
      }
      public org.reactome.web.analysis.client.model.IdentifierMap getInteractsWith()  {
        return (org.reactome.web.analysis.client.model.IdentifierMap) FoundInteractorAutoBean.this.getOrReify("interactsWith");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.FoundInteractor as = as();
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundInteractorAutoBean.this, "id"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundInteractorAutoBean.this, "exp"),
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
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundInteractorAutoBean.this, "mapsTo"),
      new Class<?>[] {java.util.Set.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("mapsTo", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("mapsTo", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInteractsWith());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundInteractorAutoBean.this, "interactsWith"),
      org.reactome.web.analysis.client.model.IdentifierMap.class
    );
    if (visitor.visitReferenceProperty("interactsWith", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("interactsWith", bean, propertyContext);
  }
}
