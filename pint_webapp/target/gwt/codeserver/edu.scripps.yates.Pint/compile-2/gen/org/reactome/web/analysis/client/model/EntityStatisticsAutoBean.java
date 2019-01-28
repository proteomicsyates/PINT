package org.reactome.web.analysis.client.model;

public class EntityStatisticsAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.EntityStatistics> {
  private final org.reactome.web.analysis.client.model.EntityStatistics shim = new org.reactome.web.analysis.client.model.EntityStatistics() {
    public java.lang.Double getFdr()  {
      java.lang.Double toReturn = (java.lang.Double) EntityStatisticsAutoBean.this.getWrapped().getFdr();
      return toReturn;
    }
    public java.lang.Double getRatio()  {
      java.lang.Double toReturn = (java.lang.Double) EntityStatisticsAutoBean.this.getWrapped().getRatio();
      return toReturn;
    }
    public java.lang.Double getpValue()  {
      java.lang.Double toReturn = (java.lang.Double) EntityStatisticsAutoBean.this.getWrapped().getpValue();
      return toReturn;
    }
    public java.lang.Integer getCuratedFound()  {
      java.lang.Integer toReturn = (java.lang.Integer) EntityStatisticsAutoBean.this.getWrapped().getCuratedFound();
      return toReturn;
    }
    public java.lang.Integer getCuratedTotal()  {
      java.lang.Integer toReturn = (java.lang.Integer) EntityStatisticsAutoBean.this.getWrapped().getCuratedTotal();
      return toReturn;
    }
    public java.lang.Integer getFound()  {
      java.lang.Integer toReturn = (java.lang.Integer) EntityStatisticsAutoBean.this.getWrapped().getFound();
      return toReturn;
    }
    public java.lang.Integer getInteractorsFound()  {
      java.lang.Integer toReturn = (java.lang.Integer) EntityStatisticsAutoBean.this.getWrapped().getInteractorsFound();
      return toReturn;
    }
    public java.lang.Integer getInteractorsTotal()  {
      java.lang.Integer toReturn = (java.lang.Integer) EntityStatisticsAutoBean.this.getWrapped().getInteractorsTotal();
      return toReturn;
    }
    public java.lang.Integer getTotal()  {
      java.lang.Integer toReturn = (java.lang.Integer) EntityStatisticsAutoBean.this.getWrapped().getTotal();
      return toReturn;
    }
    public java.lang.String getResource()  {
      java.lang.String toReturn = (java.lang.String) EntityStatisticsAutoBean.this.getWrapped().getResource();
      return toReturn;
    }
    public java.util.List getExp()  {
      java.util.List toReturn = (java.util.List) EntityStatisticsAutoBean.this.getWrapped().getExp();
      if (toReturn != null) {
        if (EntityStatisticsAutoBean.this.isWrapped(toReturn)) {
          toReturn = EntityStatisticsAutoBean.this.getFromWrapper(toReturn);
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
  public EntityStatisticsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public EntityStatisticsAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.EntityStatistics wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.EntityStatistics as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.EntityStatistics> getType() {return org.reactome.web.analysis.client.model.EntityStatistics.class;}
  @Override protected org.reactome.web.analysis.client.model.EntityStatistics createSimplePeer() {
    return new org.reactome.web.analysis.client.model.EntityStatistics() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.EntityStatisticsAutoBean.this.data;
      public java.lang.Double getFdr()  {
        return (java.lang.Double) EntityStatisticsAutoBean.this.getOrReify("fdr");
      }
      public java.lang.Double getRatio()  {
        return (java.lang.Double) EntityStatisticsAutoBean.this.getOrReify("ratio");
      }
      public java.lang.Double getpValue()  {
        return (java.lang.Double) EntityStatisticsAutoBean.this.getOrReify("pValue");
      }
      public java.lang.Integer getCuratedFound()  {
        return (java.lang.Integer) EntityStatisticsAutoBean.this.getOrReify("curatedFound");
      }
      public java.lang.Integer getCuratedTotal()  {
        return (java.lang.Integer) EntityStatisticsAutoBean.this.getOrReify("curatedTotal");
      }
      public java.lang.Integer getFound()  {
        return (java.lang.Integer) EntityStatisticsAutoBean.this.getOrReify("found");
      }
      public java.lang.Integer getInteractorsFound()  {
        return (java.lang.Integer) EntityStatisticsAutoBean.this.getOrReify("interactorsFound");
      }
      public java.lang.Integer getInteractorsTotal()  {
        return (java.lang.Integer) EntityStatisticsAutoBean.this.getOrReify("interactorsTotal");
      }
      public java.lang.Integer getTotal()  {
        return (java.lang.Integer) EntityStatisticsAutoBean.this.getOrReify("total");
      }
      public java.lang.String getResource()  {
        return (java.lang.String) EntityStatisticsAutoBean.this.getOrReify("resource");
      }
      public java.util.List getExp()  {
        return (java.util.List) EntityStatisticsAutoBean.this.getOrReify("exp");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.EntityStatistics as = as();
    value = as.getFdr();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "fdr"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("fdr", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fdr", value, propertyContext);
    value = as.getRatio();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "ratio"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("ratio", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("ratio", value, propertyContext);
    value = as.getpValue();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "pValue"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("pValue", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("pValue", value, propertyContext);
    value = as.getCuratedFound();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "curatedFound"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("curatedFound", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("curatedFound", value, propertyContext);
    value = as.getCuratedTotal();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "curatedTotal"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("curatedTotal", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("curatedTotal", value, propertyContext);
    value = as.getFound();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "found"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("found", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("found", value, propertyContext);
    value = as.getInteractorsFound();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "interactorsFound"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("interactorsFound", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("interactorsFound", value, propertyContext);
    value = as.getInteractorsTotal();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "interactorsTotal"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("interactorsTotal", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("interactorsTotal", value, propertyContext);
    value = as.getTotal();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "total"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("total", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("total", value, propertyContext);
    value = as.getResource();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "resource"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("resource", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("resource", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getExp());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EntityStatisticsAutoBean.this, "exp"),
      new Class<?>[] {java.util.List.class, java.lang.Double.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("exp", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("exp", bean, propertyContext);
  }
}
