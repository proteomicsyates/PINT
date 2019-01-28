package org.reactome.web.diagram.data.interactors.raw;

public class RawInteractorAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.interactors.raw.RawInteractor> {
  private final org.reactome.web.diagram.data.interactors.raw.RawInteractor shim = new org.reactome.web.diagram.data.interactors.raw.RawInteractor() {
    public double getScore()  {
      double toReturn = (double) RawInteractorAutoBean.this.getWrapped().getScore();
      return toReturn;
    }
    public java.lang.Boolean getIsHit()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) RawInteractorAutoBean.this.getWrapped().getIsHit();
      return toReturn;
    }
    public java.lang.Integer getEvidences()  {
      java.lang.Integer toReturn = (java.lang.Integer) RawInteractorAutoBean.this.getWrapped().getEvidences();
      return toReturn;
    }
    public java.lang.Long getId()  {
      java.lang.Long toReturn = (java.lang.Long) RawInteractorAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.String getAcc()  {
      java.lang.String toReturn = (java.lang.String) RawInteractorAutoBean.this.getWrapped().getAcc();
      return toReturn;
    }
    public java.lang.String getAccURL()  {
      java.lang.String toReturn = (java.lang.String) RawInteractorAutoBean.this.getWrapped().getAccURL();
      return toReturn;
    }
    public java.lang.String getAlias()  {
      java.lang.String toReturn = (java.lang.String) RawInteractorAutoBean.this.getWrapped().getAlias();
      return toReturn;
    }
    public java.lang.String getEvidencesURL()  {
      java.lang.String toReturn = (java.lang.String) RawInteractorAutoBean.this.getWrapped().getEvidencesURL();
      return toReturn;
    }
    public java.util.List getExp()  {
      java.util.List toReturn = (java.util.List) RawInteractorAutoBean.this.getWrapped().getExp();
      if (toReturn != null) {
        if (RawInteractorAutoBean.this.isWrapped(toReturn)) {
          toReturn = RawInteractorAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setExp(java.util.List exp)  {
      RawInteractorAutoBean.this.getWrapped().setExp(exp);
      RawInteractorAutoBean.this.set("setExp", exp);
    }
    public void setIsHit(java.lang.Boolean isHit)  {
      RawInteractorAutoBean.this.getWrapped().setIsHit(isHit);
      RawInteractorAutoBean.this.set("setIsHit", isHit);
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
  public RawInteractorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public RawInteractorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.interactors.raw.RawInteractor wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.interactors.raw.RawInteractor as() {return shim;}
  public Class<org.reactome.web.diagram.data.interactors.raw.RawInteractor> getType() {return org.reactome.web.diagram.data.interactors.raw.RawInteractor.class;}
  @Override protected org.reactome.web.diagram.data.interactors.raw.RawInteractor createSimplePeer() {
    return new org.reactome.web.diagram.data.interactors.raw.RawInteractor() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.interactors.raw.RawInteractorAutoBean.this.data;
      public double getScore()  {
        java.lang.Double toReturn = RawInteractorAutoBean.this.getOrReify("score");
        return toReturn == null ? 0d : toReturn;
      }
      public java.lang.Boolean getIsHit()  {
        return (java.lang.Boolean) RawInteractorAutoBean.this.getOrReify("isHit");
      }
      public java.lang.Integer getEvidences()  {
        return (java.lang.Integer) RawInteractorAutoBean.this.getOrReify("evidences");
      }
      public java.lang.Long getId()  {
        return (java.lang.Long) RawInteractorAutoBean.this.getOrReify("id");
      }
      public java.lang.String getAcc()  {
        return (java.lang.String) RawInteractorAutoBean.this.getOrReify("acc");
      }
      public java.lang.String getAccURL()  {
        return (java.lang.String) RawInteractorAutoBean.this.getOrReify("accURL");
      }
      public java.lang.String getAlias()  {
        return (java.lang.String) RawInteractorAutoBean.this.getOrReify("alias");
      }
      public java.lang.String getEvidencesURL()  {
        return (java.lang.String) RawInteractorAutoBean.this.getOrReify("evidencesURL");
      }
      public java.util.List getExp()  {
        return (java.util.List) RawInteractorAutoBean.this.getOrReify("exp");
      }
      public void setExp(java.util.List exp)  {
        RawInteractorAutoBean.this.setProperty("exp", exp);
      }
      public void setIsHit(java.lang.Boolean isHit)  {
        RawInteractorAutoBean.this.setProperty("isHit", isHit);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.interactors.raw.RawInteractor as = as();
    value = as.getScore();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "score"),
      double.class
    );
    if (visitor.visitValueProperty("score", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("score", value, propertyContext);
    value = as.getIsHit();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "isHit"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("isHit", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("isHit", value, propertyContext);
    value = as.getEvidences();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "evidences"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("evidences", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("evidences", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "id"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getAcc();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "acc"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("acc", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("acc", value, propertyContext);
    value = as.getAccURL();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "accURL"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("accURL", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("accURL", value, propertyContext);
    value = as.getAlias();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "alias"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("alias", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("alias", value, propertyContext);
    value = as.getEvidencesURL();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "evidencesURL"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("evidencesURL", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("evidencesURL", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(RawInteractorAutoBean.this, "exp"),
      new Class<?>[] {java.util.List.class, java.lang.Double.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("exp", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("exp", bean, propertyContext);
  }
}
