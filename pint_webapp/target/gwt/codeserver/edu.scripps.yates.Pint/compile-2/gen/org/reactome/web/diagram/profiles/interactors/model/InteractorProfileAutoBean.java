package org.reactome.web.diagram.profiles.interactors.model;

public class InteractorProfileAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.interactors.model.InteractorProfile> {
  private final org.reactome.web.diagram.profiles.interactors.model.InteractorProfile shim = new org.reactome.web.diagram.profiles.interactors.model.InteractorProfile() {
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) InteractorProfileAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode getChemical()  {
      org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode toReturn = (org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode) InteractorProfileAutoBean.this.getWrapped().getChemical();
      if (toReturn != null) {
        if (InteractorProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = InteractorProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode getProtein()  {
      org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode toReturn = (org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode) InteractorProfileAutoBean.this.getWrapped().getProtein();
      if (toReturn != null) {
        if (InteractorProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = InteractorProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNodeAutoBean(getFactory(), toReturn).as();
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
  public InteractorProfileAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public InteractorProfileAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.interactors.model.InteractorProfile wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.interactors.model.InteractorProfile as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.interactors.model.InteractorProfile> getType() {return org.reactome.web.diagram.profiles.interactors.model.InteractorProfile.class;}
  @Override protected org.reactome.web.diagram.profiles.interactors.model.InteractorProfile createSimplePeer() {
    return new org.reactome.web.diagram.profiles.interactors.model.InteractorProfile() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.interactors.model.InteractorProfileAutoBean.this.data;
      public java.lang.String getName()  {
        return (java.lang.String) InteractorProfileAutoBean.this.getOrReify("name");
      }
      public org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode getChemical()  {
        return (org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode) InteractorProfileAutoBean.this.getOrReify("chemical");
      }
      public org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode getProtein()  {
        return (org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode) InteractorProfileAutoBean.this.getOrReify("protein");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.interactors.model.InteractorProfile as = as();
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getChemical());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileAutoBean.this, "chemical"),
      org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode.class
    );
    if (visitor.visitReferenceProperty("chemical", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("chemical", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getProtein());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorProfileAutoBean.this, "protein"),
      org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode.class
    );
    if (visitor.visitReferenceProperty("protein", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("protein", bean, propertyContext);
  }
}
