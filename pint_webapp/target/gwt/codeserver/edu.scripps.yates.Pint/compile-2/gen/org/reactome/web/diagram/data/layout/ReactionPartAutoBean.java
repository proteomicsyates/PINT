package org.reactome.web.diagram.data.layout;

public class ReactionPartAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.ReactionPart> {
  private final org.reactome.web.diagram.data.layout.ReactionPart shim = new org.reactome.web.diagram.data.layout.ReactionPart() {
    public java.lang.Integer getStoichiometry()  {
      java.lang.Integer toReturn = (java.lang.Integer) ReactionPartAutoBean.this.getWrapped().getStoichiometry();
      return toReturn;
    }
    public java.lang.Long getId()  {
      java.lang.Long toReturn = (java.lang.Long) ReactionPartAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.util.List getPoints()  {
      java.util.List toReturn = (java.util.List) ReactionPartAutoBean.this.getWrapped().getPoints();
      if (toReturn != null) {
        if (ReactionPartAutoBean.this.isWrapped(toReturn)) {
          toReturn = ReactionPartAutoBean.this.getFromWrapper(toReturn);
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
  public ReactionPartAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public ReactionPartAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.ReactionPart wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.ReactionPart as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.ReactionPart> getType() {return org.reactome.web.diagram.data.layout.ReactionPart.class;}
  @Override protected org.reactome.web.diagram.data.layout.ReactionPart createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.ReactionPart() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.ReactionPartAutoBean.this.data;
      public java.lang.Integer getStoichiometry()  {
        return (java.lang.Integer) ReactionPartAutoBean.this.getOrReify("stoichiometry");
      }
      public java.lang.Long getId()  {
        return (java.lang.Long) ReactionPartAutoBean.this.getOrReify("id");
      }
      public java.util.List getPoints()  {
        return (java.util.List) ReactionPartAutoBean.this.getOrReify("points");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.ReactionPart as = as();
    value = as.getStoichiometry();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ReactionPartAutoBean.this, "stoichiometry"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("stoichiometry", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("stoichiometry", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ReactionPartAutoBean.this, "id"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getPoints());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ReactionPartAutoBean.this, "points"),
      new Class<?>[] {java.util.List.class, org.reactome.web.diagram.data.layout.Coordinate.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("points", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("points", bean, propertyContext);
  }
}
