package org.reactome.web.analysis.client.model;

public class ExpressionSummaryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.ExpressionSummary> {
  private final org.reactome.web.analysis.client.model.ExpressionSummary shim = new org.reactome.web.analysis.client.model.ExpressionSummary() {
    public java.lang.Double getMax()  {
      java.lang.Double toReturn = (java.lang.Double) ExpressionSummaryAutoBean.this.getWrapped().getMax();
      return toReturn;
    }
    public java.lang.Double getMin()  {
      java.lang.Double toReturn = (java.lang.Double) ExpressionSummaryAutoBean.this.getWrapped().getMin();
      return toReturn;
    }
    public java.util.List getColumnNames()  {
      java.util.List toReturn = (java.util.List) ExpressionSummaryAutoBean.this.getWrapped().getColumnNames();
      if (toReturn != null) {
        if (ExpressionSummaryAutoBean.this.isWrapped(toReturn)) {
          toReturn = ExpressionSummaryAutoBean.this.getFromWrapper(toReturn);
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
  public ExpressionSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ExpressionSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.ExpressionSummary wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.ExpressionSummary as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.ExpressionSummary> getType() {return org.reactome.web.analysis.client.model.ExpressionSummary.class;}
  @Override protected org.reactome.web.analysis.client.model.ExpressionSummary createSimplePeer() {
    return new org.reactome.web.analysis.client.model.ExpressionSummary() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.ExpressionSummaryAutoBean.this.data;
      public java.lang.Double getMax()  {
        return (java.lang.Double) ExpressionSummaryAutoBean.this.getOrReify("max");
      }
      public java.lang.Double getMin()  {
        return (java.lang.Double) ExpressionSummaryAutoBean.this.getOrReify("min");
      }
      public java.util.List getColumnNames()  {
        return (java.util.List) ExpressionSummaryAutoBean.this.getOrReify("columnNames");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.ExpressionSummary as = as();
    value = as.getMax();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ExpressionSummaryAutoBean.this, "max"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("max", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("max", value, propertyContext);
    value = as.getMin();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ExpressionSummaryAutoBean.this, "min"),
      java.lang.Double.class
    );
    if (visitor.visitValueProperty("min", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("min", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getColumnNames());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ExpressionSummaryAutoBean.this, "columnNames"),
      new Class<?>[] {java.util.List.class, java.lang.String.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("columnNames", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("columnNames", bean, propertyContext);
  }
}
