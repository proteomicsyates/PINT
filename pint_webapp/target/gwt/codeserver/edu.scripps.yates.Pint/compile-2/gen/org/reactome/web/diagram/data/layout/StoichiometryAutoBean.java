package org.reactome.web.diagram.data.layout;

public class StoichiometryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Stoichiometry> {
  private final org.reactome.web.diagram.data.layout.Stoichiometry shim = new org.reactome.web.diagram.data.layout.Stoichiometry() {
    public java.lang.Integer getValue()  {
      java.lang.Integer toReturn = (java.lang.Integer) StoichiometryAutoBean.this.getWrapped().getValue();
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Shape getShape()  {
      org.reactome.web.diagram.data.layout.Shape toReturn = (org.reactome.web.diagram.data.layout.Shape) StoichiometryAutoBean.this.getWrapped().getShape();
      if (toReturn != null) {
        if (StoichiometryAutoBean.this.isWrapped(toReturn)) {
          toReturn = StoichiometryAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.ShapeAutoBean(getFactory(), toReturn).as();
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
  public StoichiometryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public StoichiometryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Stoichiometry wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Stoichiometry as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Stoichiometry> getType() {return org.reactome.web.diagram.data.layout.Stoichiometry.class;}
  @Override protected org.reactome.web.diagram.data.layout.Stoichiometry createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Stoichiometry() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.StoichiometryAutoBean.this.data;
      public java.lang.Integer getValue()  {
        return (java.lang.Integer) StoichiometryAutoBean.this.getOrReify("value");
      }
      public org.reactome.web.diagram.data.layout.Shape getShape()  {
        return (org.reactome.web.diagram.data.layout.Shape) StoichiometryAutoBean.this.getOrReify("shape");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Stoichiometry as = as();
    value = as.getValue();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(StoichiometryAutoBean.this, "value"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("value", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("value", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getShape());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(StoichiometryAutoBean.this, "shape"),
      org.reactome.web.diagram.data.layout.Shape.class
    );
    if (visitor.visitReferenceProperty("shape", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("shape", bean, propertyContext);
  }
}
