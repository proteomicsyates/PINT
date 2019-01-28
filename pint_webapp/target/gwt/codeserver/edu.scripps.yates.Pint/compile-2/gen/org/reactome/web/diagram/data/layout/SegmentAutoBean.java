package org.reactome.web.diagram.data.layout;

public class SegmentAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Segment> {
  private final org.reactome.web.diagram.data.layout.Segment shim = new org.reactome.web.diagram.data.layout.Segment() {
    public org.reactome.web.diagram.data.layout.Coordinate getFrom()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) SegmentAutoBean.this.getWrapped().getFrom();
      if (toReturn != null) {
        if (SegmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = SegmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.data.layout.Coordinate getTo()  {
      org.reactome.web.diagram.data.layout.Coordinate toReturn = (org.reactome.web.diagram.data.layout.Coordinate) SegmentAutoBean.this.getWrapped().getTo();
      if (toReturn != null) {
        if (SegmentAutoBean.this.isWrapped(toReturn)) {
          toReturn = SegmentAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.data.layout.CoordinateAutoBean(getFactory(), toReturn).as();
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
  public SegmentAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public SegmentAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Segment wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Segment as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Segment> getType() {return org.reactome.web.diagram.data.layout.Segment.class;}
  @Override protected org.reactome.web.diagram.data.layout.Segment createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Segment() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.SegmentAutoBean.this.data;
      public org.reactome.web.diagram.data.layout.Coordinate getFrom()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) SegmentAutoBean.this.getOrReify("from");
      }
      public org.reactome.web.diagram.data.layout.Coordinate getTo()  {
        return (org.reactome.web.diagram.data.layout.Coordinate) SegmentAutoBean.this.getOrReify("to");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Segment as = as();
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getFrom());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SegmentAutoBean.this, "from"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("from", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("from", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getTo());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SegmentAutoBean.this, "to"),
      org.reactome.web.diagram.data.layout.Coordinate.class
    );
    if (visitor.visitReferenceProperty("to", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("to", bean, propertyContext);
  }
}
