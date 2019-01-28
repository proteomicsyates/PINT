package org.reactome.web.diagram.data.interactors.custom.raw;

public class RawInteractorErrorAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError> {
  private final org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError shim = new org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError() {
    public java.lang.Integer getCode()  {
      java.lang.Integer toReturn = (java.lang.Integer) RawInteractorErrorAutoBean.this.getWrapped().getCode();
      return toReturn;
    }
    public java.lang.String getReason()  {
      java.lang.String toReturn = (java.lang.String) RawInteractorErrorAutoBean.this.getWrapped().getReason();
      return toReturn;
    }
    public java.util.List getMessages()  {
      java.util.List toReturn = (java.util.List) RawInteractorErrorAutoBean.this.getWrapped().getMessages();
      if (toReturn != null) {
        if (RawInteractorErrorAutoBean.this.isWrapped(toReturn)) {
          toReturn = RawInteractorErrorAutoBean.this.getFromWrapper(toReturn);
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
  public RawInteractorErrorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawInteractorErrorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError> getType() {return org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError.class;}
  @Override protected org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorErrorAutoBean.this.data;
      public java.lang.Integer getCode()  {
        return (java.lang.Integer) RawInteractorErrorAutoBean.this.getOrReify("code");
      }
      public java.lang.String getReason()  {
        return (java.lang.String) RawInteractorErrorAutoBean.this.getOrReify("reason");
      }
      public java.util.List getMessages()  {
        return (java.util.List) RawInteractorErrorAutoBean.this.getOrReify("messages");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError as = as();
    value = as.getCode();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorErrorAutoBean.this, "code"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("code", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("code", value, propertyContext);
    value = as.getReason();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorErrorAutoBean.this, "reason"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("reason", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("reason", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getMessages());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorErrorAutoBean.this, "messages"),
      new Class<?>[] {java.util.List.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("messages", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("messages", bean, propertyContext);
  }
}
