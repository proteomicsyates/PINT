package org.reactome.web.analysis.client.model;

public class IdentifierMapAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.IdentifierMap> {
  private final org.reactome.web.analysis.client.model.IdentifierMap shim = new org.reactome.web.analysis.client.model.IdentifierMap() {
    public java.lang.String getResource()  {
      java.lang.String toReturn = (java.lang.String) IdentifierMapAutoBean.this.getWrapped().getResource();
      return toReturn;
    }
    public java.util.Set getIds()  {
      java.util.Set toReturn = (java.util.Set) IdentifierMapAutoBean.this.getWrapped().getIds();
      if (toReturn != null) {
        if (IdentifierMapAutoBean.this.isWrapped(toReturn)) {
          toReturn = IdentifierMapAutoBean.this.getFromWrapper(toReturn);
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
  public IdentifierMapAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public IdentifierMapAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.IdentifierMap wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.IdentifierMap as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.IdentifierMap> getType() {return org.reactome.web.analysis.client.model.IdentifierMap.class;}
  @Override protected org.reactome.web.analysis.client.model.IdentifierMap createSimplePeer() {
    return new org.reactome.web.analysis.client.model.IdentifierMap() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.IdentifierMapAutoBean.this.data;
      public java.lang.String getResource()  {
        return (java.lang.String) IdentifierMapAutoBean.this.getOrReify("resource");
      }
      public java.util.Set getIds()  {
        return (java.util.Set) IdentifierMapAutoBean.this.getOrReify("ids");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.IdentifierMap as = as();
    value = as.getResource();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(IdentifierMapAutoBean.this, "resource"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("resource", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("resource", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getIds());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(IdentifierMapAutoBean.this, "ids"),
      new Class<?>[] {java.util.Set.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("ids", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("ids", bean, propertyContext);
  }
}
