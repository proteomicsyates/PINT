package org.reactome.web.diagram.data.interactors.custom.model;

public class CustomResourcesAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.custom.model.CustomResources> {
  private final org.reactome.web.diagram.data.interactors.custom.model.CustomResources shim = new org.reactome.web.diagram.data.interactors.custom.model.CustomResources() {
    public java.util.List getCustomResources()  {
      java.util.List toReturn = (java.util.List) CustomResourcesAutoBean.this.getWrapped().getCustomResources();
      if (toReturn != null) {
        if (CustomResourcesAutoBean.this.isWrapped(toReturn)) {
          toReturn = CustomResourcesAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setCustomResources(java.util.List list)  {
      CustomResourcesAutoBean.this.getWrapped().setCustomResources(list);
      CustomResourcesAutoBean.this.set("setCustomResources", list);
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
  public CustomResourcesAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public CustomResourcesAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.custom.model.CustomResources wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.custom.model.CustomResources as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.custom.model.CustomResources> getType() {return org.reactome.web.diagram.data.interactors.custom.model.CustomResources.class;}
  @Override protected org.reactome.web.diagram.data.interactors.custom.model.CustomResources createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.custom.model.CustomResources() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.custom.model.CustomResourcesAutoBean.this.data;
      public java.util.List getCustomResources()  {
        return (java.util.List) CustomResourcesAutoBean.this.getOrReify("customResources");
      }
      public void setCustomResources(java.util.List list)  {
        CustomResourcesAutoBean.this.setProperty("customResources", list);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.custom.model.CustomResources as = as();
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getCustomResources());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CustomResourcesAutoBean.this, "customResources"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.interactors.custom.model.CustomResource.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("customResources", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("customResources", bean, propertyContext);
  }
}
