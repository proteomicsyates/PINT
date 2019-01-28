package org.reactome.web.analysis.client.model;

public class InteractorEvidenceAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.InteractorEvidence> {
  private final org.reactome.web.analysis.client.model.InteractorEvidence shim = new org.reactome.web.analysis.client.model.InteractorEvidence() {
    public java.lang.String getId()  {
      java.lang.String toReturn = (java.lang.String) InteractorEvidenceAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.String getMapsTo()  {
      java.lang.String toReturn = (java.lang.String) InteractorEvidenceAutoBean.this.getWrapped().getMapsTo();
      return toReturn;
    }
    public java.util.List getExp()  {
      java.util.List toReturn = (java.util.List) InteractorEvidenceAutoBean.this.getWrapped().getExp();
      if (toReturn != null) {
        if (InteractorEvidenceAutoBean.this.isWrapped(toReturn)) {
          toReturn = InteractorEvidenceAutoBean.this.getFromWrapper(toReturn);
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
  public InteractorEvidenceAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public InteractorEvidenceAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.InteractorEvidence wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.InteractorEvidence as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.InteractorEvidence> getType() {return org.reactome.web.analysis.client.model.InteractorEvidence.class;}
  @Override protected org.reactome.web.analysis.client.model.InteractorEvidence createSimplePeer() {
    return new org.reactome.web.analysis.client.model.InteractorEvidence() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.InteractorEvidenceAutoBean.this.data;
      public java.lang.String getId()  {
        return (java.lang.String) InteractorEvidenceAutoBean.this.getOrReify("id");
      }
      public java.lang.String getMapsTo()  {
        return (java.lang.String) InteractorEvidenceAutoBean.this.getOrReify("mapsTo");
      }
      public java.util.List getExp()  {
        return (java.util.List) InteractorEvidenceAutoBean.this.getOrReify("exp");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.InteractorEvidence as = as();
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorEvidenceAutoBean.this, "id"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getMapsTo();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorEvidenceAutoBean.this, "mapsTo"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("mapsTo", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("mapsTo", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(InteractorEvidenceAutoBean.this, "exp"),
      new Class<?>[] {java.util.List.class, java.lang.Double.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("exp", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("exp", bean, propertyContext);
  }
}
