package org.reactome.web.diagram.data.interactors.raw;

public class RawInteractorEntityAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity> {
  private final org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity shim = new org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity() {
    public java.lang.Integer getCount()  {
      java.lang.Integer toReturn = (java.lang.Integer) RawInteractorEntityAutoBean.this.getWrapped().getCount();
      return toReturn;
    }
    public java.lang.String getAcc()  {
      java.lang.String toReturn = (java.lang.String) RawInteractorEntityAutoBean.this.getWrapped().getAcc();
      return toReturn;
    }
    public java.util.List getInteractors()  {
      java.util.List toReturn = (java.util.List) RawInteractorEntityAutoBean.this.getWrapped().getInteractors();
      if (toReturn != null) {
        if (RawInteractorEntityAutoBean.this.isWrapped(toReturn)) {
          toReturn = RawInteractorEntityAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
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
  public RawInteractorEntityAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawInteractorEntityAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity> getType() {return org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity.class;}
  @Override protected org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.raw.RawInteractorEntityAutoBean.this.data;
      public java.lang.Integer getCount()  {
        return (java.lang.Integer) RawInteractorEntityAutoBean.this.getOrReify("count");
      }
      public java.lang.String getAcc()  {
        return (java.lang.String) RawInteractorEntityAutoBean.this.getOrReify("acc");
      }
      public java.util.List getInteractors()  {
        return (java.util.List) RawInteractorEntityAutoBean.this.getOrReify("interactors");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity as = as();
    value = as.getCount();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorEntityAutoBean.this, "count"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("count", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("count", value, propertyContext);
    value = as.getAcc();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorEntityAutoBean.this, "acc"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("acc", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("acc", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInteractors());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorEntityAutoBean.this, "interactors"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.interactors.raw.RawInteractor.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("interactors", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("interactors", bean, propertyContext);
  }
}
