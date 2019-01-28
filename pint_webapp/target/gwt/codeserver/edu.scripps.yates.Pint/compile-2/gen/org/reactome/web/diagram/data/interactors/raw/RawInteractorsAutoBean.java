package org.reactome.web.diagram.data.interactors.raw;

public class RawInteractorsAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.raw.RawInteractors> {
  private final org.reactome.web.diagram.data.interactors.raw.RawInteractors shim = new org.reactome.web.diagram.data.interactors.raw.RawInteractors() {
    public java.lang.String getResource()  {
      java.lang.String toReturn = (java.lang.String) RawInteractorsAutoBean.this.getWrapped().getResource();
      return toReturn;
    }
    public java.util.List getEntities()  {
      java.util.List toReturn = (java.util.List) RawInteractorsAutoBean.this.getWrapped().getEntities();
      if (toReturn != null) {
        if (RawInteractorsAutoBean.this.isWrapped(toReturn)) {
          toReturn = RawInteractorsAutoBean.this.getFromWrapper(toReturn);
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
  public RawInteractorsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawInteractorsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.raw.RawInteractors wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.raw.RawInteractors as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.raw.RawInteractors> getType() {return org.reactome.web.diagram.data.interactors.raw.RawInteractors.class;}
  @Override protected org.reactome.web.diagram.data.interactors.raw.RawInteractors createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.raw.RawInteractors() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.raw.RawInteractorsAutoBean.this.data;
      public java.lang.String getResource()  {
        return (java.lang.String) RawInteractorsAutoBean.this.getOrReify("resource");
      }
      public java.util.List getEntities()  {
        return (java.util.List) RawInteractorsAutoBean.this.getOrReify("entities");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.raw.RawInteractors as = as();
    value = as.getResource();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorsAutoBean.this, "resource"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("resource", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("resource", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEntities());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorsAutoBean.this, "entities"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("entities", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("entities", bean, propertyContext);
  }
}
