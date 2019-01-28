package org.reactome.web.analysis.client.model;

public class IdentifierSummaryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.IdentifierSummary> {
  private final org.reactome.web.analysis.client.model.IdentifierSummary shim = new org.reactome.web.analysis.client.model.IdentifierSummary() {
    public java.lang.String getId()  {
      java.lang.String toReturn = (java.lang.String) IdentifierSummaryAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.util.List getExp()  {
      java.util.List toReturn = (java.util.List) IdentifierSummaryAutoBean.this.getWrapped().getExp();
      if (toReturn != null) {
        if (IdentifierSummaryAutoBean.this.isWrapped(toReturn)) {
          toReturn = IdentifierSummaryAutoBean.this.getFromWrapper(toReturn);
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
  public IdentifierSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public IdentifierSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.IdentifierSummary wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.IdentifierSummary as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.IdentifierSummary> getType() {return org.reactome.web.analysis.client.model.IdentifierSummary.class;}
  @Override protected org.reactome.web.analysis.client.model.IdentifierSummary createSimplePeer() {
    return new org.reactome.web.analysis.client.model.IdentifierSummary() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.IdentifierSummaryAutoBean.this.data;
      public java.lang.String getId()  {
        return (java.lang.String) IdentifierSummaryAutoBean.this.getOrReify("id");
      }
      public java.util.List getExp()  {
        return (java.util.List) IdentifierSummaryAutoBean.this.getOrReify("exp");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.IdentifierSummary as = as();
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(IdentifierSummaryAutoBean.this, "id"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(IdentifierSummaryAutoBean.this, "exp"),
      new Class<?>[] {java.util.List.class, java.lang.Double.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("exp", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("exp", bean, propertyContext);
  }
}
