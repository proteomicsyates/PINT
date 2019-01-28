package org.reactome.web.analysis.client.model;

public class FoundElementsAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.FoundElements> {
  private final org.reactome.web.analysis.client.model.FoundElements shim = new org.reactome.web.analysis.client.model.FoundElements() {
    public java.lang.Integer getFoundEntities()  {
      java.lang.Integer toReturn = (java.lang.Integer) FoundElementsAutoBean.this.getWrapped().getFoundEntities();
      return toReturn;
    }
    public java.lang.Integer getFoundInteractors()  {
      java.lang.Integer toReturn = (java.lang.Integer) FoundElementsAutoBean.this.getWrapped().getFoundInteractors();
      return toReturn;
    }
    public java.util.List getExpNames()  {
      java.util.List toReturn = (java.util.List) FoundElementsAutoBean.this.getWrapped().getExpNames();
      if (toReturn != null) {
        if (FoundElementsAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundElementsAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getEntities()  {
      java.util.List toReturn = (java.util.List) FoundElementsAutoBean.this.getWrapped().getEntities();
      if (toReturn != null) {
        if (FoundElementsAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundElementsAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getInteractors()  {
      java.util.List toReturn = (java.util.List) FoundElementsAutoBean.this.getWrapped().getInteractors();
      if (toReturn != null) {
        if (FoundElementsAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundElementsAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.Set getResources()  {
      java.util.Set toReturn = (java.util.Set) FoundElementsAutoBean.this.getWrapped().getResources();
      if (toReturn != null) {
        if (FoundElementsAutoBean.this.isWrapped(toReturn)) {
          toReturn = FoundElementsAutoBean.this.getFromWrapper(toReturn);
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
  public FoundElementsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public FoundElementsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.FoundElements wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.FoundElements as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.FoundElements> getType() {return org.reactome.web.analysis.client.model.FoundElements.class;}
  @Override protected org.reactome.web.analysis.client.model.FoundElements createSimplePeer() {
    return new org.reactome.web.analysis.client.model.FoundElements() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.FoundElementsAutoBean.this.data;
      public java.lang.Integer getFoundEntities()  {
        return (java.lang.Integer) FoundElementsAutoBean.this.getOrReify("foundEntities");
      }
      public java.lang.Integer getFoundInteractors()  {
        return (java.lang.Integer) FoundElementsAutoBean.this.getOrReify("foundInteractors");
      }
      public java.util.List getExpNames()  {
        return (java.util.List) FoundElementsAutoBean.this.getOrReify("expNames");
      }
      public java.util.List getEntities()  {
        return (java.util.List) FoundElementsAutoBean.this.getOrReify("entities");
      }
      public java.util.List getInteractors()  {
        return (java.util.List) FoundElementsAutoBean.this.getOrReify("interactors");
      }
      public java.util.Set getResources()  {
        return (java.util.Set) FoundElementsAutoBean.this.getOrReify("resources");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.FoundElements as = as();
    value = as.getFoundEntities();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundElementsAutoBean.this, "foundEntities"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("foundEntities", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("foundEntities", value, propertyContext);
    value = as.getFoundInteractors();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundElementsAutoBean.this, "foundInteractors"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("foundInteractors", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("foundInteractors", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExpNames());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundElementsAutoBean.this, "expNames"),
      new Class<?>[] {java.util.List.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("expNames", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("expNames", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEntities());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundElementsAutoBean.this, "entities"),
      new Class<?>[] {java.util.List.class, org.reactome.web.analysis.client.model.FoundEntity.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("entities", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("entities", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInteractors());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundElementsAutoBean.this, "interactors"),
      new Class<?>[] {java.util.List.class, org.reactome.web.analysis.client.model.FoundInteractor.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("interactors", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("interactors", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getResources());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(FoundElementsAutoBean.this, "resources"),
      new Class<?>[] {java.util.Set.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("resources", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("resources", bean, propertyContext);
  }
}
