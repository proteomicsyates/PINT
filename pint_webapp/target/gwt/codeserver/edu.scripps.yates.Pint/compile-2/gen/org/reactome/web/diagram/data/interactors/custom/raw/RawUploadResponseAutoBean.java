package org.reactome.web.diagram.data.interactors.custom.raw;

public class RawUploadResponseAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse> {
  private final org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse shim = new org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse() {
    public java.util.List getWarningMessages()  {
      java.util.List toReturn = (java.util.List) RawUploadResponseAutoBean.this.getWrapped().getWarningMessages();
      if (toReturn != null) {
        if (RawUploadResponseAutoBean.this.isWrapped(toReturn)) {
          toReturn = RawUploadResponseAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.interactors.custom.raw.RawSummary getSummary()  {
      org.reactome.web.diagram.data.interactors.custom.raw.RawSummary toReturn = (org.reactome.web.diagram.data.interactors.custom.raw.RawSummary) RawUploadResponseAutoBean.this.getWrapped().getSummary();
      if (toReturn != null) {
        if (RawUploadResponseAutoBean.this.isWrapped(toReturn)) {
          toReturn = RawUploadResponseAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.interactors.custom.raw.RawSummaryAutoBean(getFactory(), toReturn).as();
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
  public RawUploadResponseAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawUploadResponseAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse> getType() {return org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse.class;}
  @Override protected org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponseAutoBean.this.data;
      public java.util.List getWarningMessages()  {
        return (java.util.List) RawUploadResponseAutoBean.this.getOrReify("warningMessages");
      }
      public org.reactome.web.diagram.data.interactors.custom.raw.RawSummary getSummary()  {
        return (org.reactome.web.diagram.data.interactors.custom.raw.RawSummary) RawUploadResponseAutoBean.this.getOrReify("summary");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse as = as();
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getWarningMessages());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawUploadResponseAutoBean.this, "warningMessages"),
      new Class<?>[] {java.util.List.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("warningMessages", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("warningMessages", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getSummary());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawUploadResponseAutoBean.this, "summary"),
      org.reactome.web.diagram.data.interactors.custom.raw.RawSummary.class
    );
    if (visitor.visitReferenceProperty("summary", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("summary", bean, propertyContext);
  }
}
