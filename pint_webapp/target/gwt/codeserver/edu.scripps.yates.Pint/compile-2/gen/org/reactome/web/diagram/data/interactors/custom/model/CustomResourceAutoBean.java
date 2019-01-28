package org.reactome.web.diagram.data.interactors.custom.model;

public class CustomResourceAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.custom.model.CustomResource> {
  private final org.reactome.web.diagram.data.interactors.custom.model.CustomResource shim = new org.reactome.web.diagram.data.interactors.custom.model.CustomResource() {
    public java.lang.String getFilename()  {
      java.lang.String toReturn = (java.lang.String) CustomResourceAutoBean.this.getWrapped().getFilename();
      return toReturn;
    }
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) CustomResourceAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public java.lang.String getToken()  {
      java.lang.String toReturn = (java.lang.String) CustomResourceAutoBean.this.getWrapped().getToken();
      return toReturn;
    }
    public void setFilename(java.lang.String filename)  {
      CustomResourceAutoBean.this.getWrapped().setFilename(filename);
      CustomResourceAutoBean.this.set("setFilename", filename);
    }
    public void setName(java.lang.String name)  {
      CustomResourceAutoBean.this.getWrapped().setName(name);
      CustomResourceAutoBean.this.set("setName", name);
    }
    public void setToken(java.lang.String token)  {
      CustomResourceAutoBean.this.getWrapped().setToken(token);
      CustomResourceAutoBean.this.set("setToken", token);
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
  public CustomResourceAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public CustomResourceAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.custom.model.CustomResource wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.custom.model.CustomResource as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.custom.model.CustomResource> getType() {return org.reactome.web.diagram.data.interactors.custom.model.CustomResource.class;}
  @Override protected org.reactome.web.diagram.data.interactors.custom.model.CustomResource createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.custom.model.CustomResource() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.custom.model.CustomResourceAutoBean.this.data;
      public java.lang.String getFilename()  {
        return (java.lang.String) CustomResourceAutoBean.this.getOrReify("filename");
      }
      public java.lang.String getName()  {
        return (java.lang.String) CustomResourceAutoBean.this.getOrReify("name");
      }
      public java.lang.String getToken()  {
        return (java.lang.String) CustomResourceAutoBean.this.getOrReify("token");
      }
      public void setFilename(java.lang.String filename)  {
        CustomResourceAutoBean.this.setProperty("filename", filename);
      }
      public void setName(java.lang.String name)  {
        CustomResourceAutoBean.this.setProperty("name", name);
      }
      public void setToken(java.lang.String token)  {
        CustomResourceAutoBean.this.setProperty("token", token);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.custom.model.CustomResource as = as();
    value = as.getFilename();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CustomResourceAutoBean.this, "filename"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("filename", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("filename", value, propertyContext);
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CustomResourceAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    value = as.getToken();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(CustomResourceAutoBean.this, "token"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("token", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("token", value, propertyContext);
  }
}
