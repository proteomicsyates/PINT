package emul.java.util;

public class IteratorAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<java.util.Iterator> {
  private final java.util.Iterator shim = new java.util.Iterator() {
    public boolean hasNext()  {
      boolean toReturn = (boolean) IteratorAutoBean.this.getWrapped().hasNext();
      return toReturn;
    }
    public java.lang.Object next()  {
      java.lang.Object toReturn = (java.lang.Object) IteratorAutoBean.this.getWrapped().next();
      if (toReturn != null) {
        if (IteratorAutoBean.this.isWrapped(toReturn)) {
          toReturn = IteratorAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      IteratorAutoBean.this.call("next", toReturn );
      return toReturn;
    }
    @Override public void remove() {
      IteratorAutoBean.this.getWrapped().remove();
      IteratorAutoBean.this.call("remove", null);
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
  public IteratorAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, java.util.Iterator wrapped) {
    super(wrapped, factory);
  }
  public java.util.Iterator as() {return shim;}
  public Class<java.util.Iterator> getType() {return java.util.Iterator.class;}
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    java.util.Iterator as = as();
    value = as.hasNext();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(IteratorAutoBean.this, "next"),
      boolean.class
    );
    if (visitor.visitValueProperty("next", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("next", value, propertyContext);
  }
}
