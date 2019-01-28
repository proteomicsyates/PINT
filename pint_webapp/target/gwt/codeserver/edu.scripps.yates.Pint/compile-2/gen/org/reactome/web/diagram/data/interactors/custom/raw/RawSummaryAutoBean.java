package org.reactome.web.diagram.data.interactors.custom.raw;

public class RawSummaryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.custom.raw.RawSummary> {
  private final org.reactome.web.diagram.data.interactors.custom.raw.RawSummary shim = new org.reactome.web.diagram.data.interactors.custom.raw.RawSummary() {
    public java.lang.Integer getInteractions()  {
      java.lang.Integer toReturn = (java.lang.Integer) RawSummaryAutoBean.this.getWrapped().getInteractions();
      return toReturn;
    }
    public java.lang.Integer getInteractors()  {
      java.lang.Integer toReturn = (java.lang.Integer) RawSummaryAutoBean.this.getWrapped().getInteractors();
      return toReturn;
    }
    public java.lang.String getFileName()  {
      java.lang.String toReturn = (java.lang.String) RawSummaryAutoBean.this.getWrapped().getFileName();
      return toReturn;
    }
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) RawSummaryAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public java.lang.String getToken()  {
      java.lang.String toReturn = (java.lang.String) RawSummaryAutoBean.this.getWrapped().getToken();
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
  public RawSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.custom.raw.RawSummary wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.custom.raw.RawSummary as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.custom.raw.RawSummary> getType() {return org.reactome.web.diagram.data.interactors.custom.raw.RawSummary.class;}
  @Override protected org.reactome.web.diagram.data.interactors.custom.raw.RawSummary createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.custom.raw.RawSummary() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.custom.raw.RawSummaryAutoBean.this.data;
      public java.lang.Integer getInteractions()  {
        return (java.lang.Integer) RawSummaryAutoBean.this.getOrReify("interactions");
      }
      public java.lang.Integer getInteractors()  {
        return (java.lang.Integer) RawSummaryAutoBean.this.getOrReify("interactors");
      }
      public java.lang.String getFileName()  {
        return (java.lang.String) RawSummaryAutoBean.this.getOrReify("fileName");
      }
      public java.lang.String getName()  {
        return (java.lang.String) RawSummaryAutoBean.this.getOrReify("name");
      }
      public java.lang.String getToken()  {
        return (java.lang.String) RawSummaryAutoBean.this.getOrReify("token");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.custom.raw.RawSummary as = as();
    value = as.getInteractions();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawSummaryAutoBean.this, "interactions"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("interactions", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("interactions", value, propertyContext);
    value = as.getInteractors();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawSummaryAutoBean.this, "interactors"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("interactors", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("interactors", value, propertyContext);
    value = as.getFileName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawSummaryAutoBean.this, "fileName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("fileName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fileName", value, propertyContext);
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawSummaryAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    value = as.getToken();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawSummaryAutoBean.this, "token"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("token", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("token", value, propertyContext);
  }
}
