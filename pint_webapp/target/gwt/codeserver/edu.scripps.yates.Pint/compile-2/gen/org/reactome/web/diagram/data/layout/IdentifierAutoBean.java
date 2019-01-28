package org.reactome.web.diagram.data.layout;

public class IdentifierAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.data.layout.Identifier> {
  private final org.reactome.web.diagram.data.layout.Identifier shim = new org.reactome.web.diagram.data.layout.Identifier() {
    public java.lang.String getId()  {
      java.lang.String toReturn = (java.lang.String) IdentifierAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.String getResource()  {
      java.lang.String toReturn = (java.lang.String) IdentifierAutoBean.this.getWrapped().getResource();
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
  public IdentifierAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public IdentifierAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.data.layout.Identifier wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.data.layout.Identifier as() {return shim;}
  public Class<org.reactome.web.diagram.data.layout.Identifier> getType() {return org.reactome.web.diagram.data.layout.Identifier.class;}
  @Override protected org.reactome.web.diagram.data.layout.Identifier createSimplePeer() {
    return new org.reactome.web.diagram.data.layout.Identifier() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.data.layout.IdentifierAutoBean.this.data;
      public java.lang.String getId()  {
        return (java.lang.String) IdentifierAutoBean.this.getOrReify("id");
      }
      public java.lang.String getResource()  {
        return (java.lang.String) IdentifierAutoBean.this.getOrReify("resource");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.data.layout.Identifier as = as();
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(IdentifierAutoBean.this, "id"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getResource();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(IdentifierAutoBean.this, "resource"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("resource", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("resource", value, propertyContext);
  }
}
