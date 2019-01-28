package emul.java.util;

public class ListAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<java.util.List> {
  private final java.util.List shim = new java.util.List() {
    public boolean isEmpty()  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().isEmpty();
      return toReturn;
    }
    public java.lang.Object get(int index)  {
      java.lang.Object toReturn = (java.lang.Object) ListAutoBean.this.getWrapped().get(index);
      if (toReturn != null) {
        if (ListAutoBean.this.isWrapped(toReturn)) {
          toReturn = ListAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      ListAutoBean.this.call("get", toReturn, index);
      return toReturn;
    }
    public java.lang.Object remove(int index)  {
      java.lang.Object toReturn = (java.lang.Object) ListAutoBean.this.getWrapped().remove(index);
      if (toReturn != null) {
        if (ListAutoBean.this.isWrapped(toReturn)) {
          toReturn = ListAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      ListAutoBean.this.call("remove", toReturn, index);
      return toReturn;
    }
    public java.lang.Object set(int index,java.lang.Object element)  {
      java.lang.Object toReturn = (java.lang.Object) ListAutoBean.this.getWrapped().set(index,element);
      if (toReturn != null) {
        if (ListAutoBean.this.isWrapped(toReturn)) {
          toReturn = ListAutoBean.this.getFromWrapper(toReturn);
        } else {
        }
      }
      ListAutoBean.this.call("set", toReturn, index,element);
      return toReturn;
    }
    public java.lang.Object[] toArray(java.lang.Object[] a)  {
      java.lang.Object[] toReturn = (java.lang.Object[]) ListAutoBean.this.getWrapped().toArray(a);
      ListAutoBean.this.call("toArray", toReturn, a);
      return toReturn;
    }
    public boolean add(java.lang.Object o)  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().add(o);
      ListAutoBean.this.call("add", toReturn, o);
      return toReturn;
    }
    public boolean addAll(int index,java.util.Collection c)  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().addAll(index,c);
      ListAutoBean.this.call("addAll", toReturn, index,c);
      return toReturn;
    }
    public boolean addAll(java.util.Collection c)  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().addAll(c);
      ListAutoBean.this.call("addAll", toReturn, c);
      return toReturn;
    }
    public boolean contains(java.lang.Object o)  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().contains(o);
      ListAutoBean.this.call("contains", toReturn, o);
      return toReturn;
    }
    public boolean containsAll(java.util.Collection c)  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().containsAll(c);
      ListAutoBean.this.call("containsAll", toReturn, c);
      return toReturn;
    }
    public boolean remove(java.lang.Object o)  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().remove(o);
      ListAutoBean.this.call("remove", toReturn, o);
      return toReturn;
    }
    public boolean removeAll(java.util.Collection c)  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().removeAll(c);
      ListAutoBean.this.call("removeAll", toReturn, c);
      return toReturn;
    }
    public boolean retainAll(java.util.Collection c)  {
      boolean toReturn = (boolean) ListAutoBean.this.getWrapped().retainAll(c);
      ListAutoBean.this.call("retainAll", toReturn, c);
      return toReturn;
    }
    public int indexOf(java.lang.Object o)  {
      int toReturn = (int) ListAutoBean.this.getWrapped().indexOf(o);
      ListAutoBean.this.call("indexOf", toReturn, o);
      return toReturn;
    }
    public int lastIndexOf(java.lang.Object o)  {
      int toReturn = (int) ListAutoBean.this.getWrapped().lastIndexOf(o);
      ListAutoBean.this.call("lastIndexOf", toReturn, o);
      return toReturn;
    }
    public int size()  {
      int toReturn = (int) ListAutoBean.this.getWrapped().size();
      ListAutoBean.this.call("size", toReturn );
      return toReturn;
    }
    public java.lang.Object[] toArray()  {
      java.lang.Object[] toReturn = (java.lang.Object[]) ListAutoBean.this.getWrapped().toArray();
      ListAutoBean.this.call("toArray", toReturn );
      return toReturn;
    }
    public java.util.Iterator iterator()  {
      java.util.Iterator toReturn = (java.util.Iterator) ListAutoBean.this.getWrapped().iterator();
      if (toReturn != null) {
        if (ListAutoBean.this.isWrapped(toReturn)) {
          toReturn = ListAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.IteratorAutoBean(getFactory(), toReturn).as();
        }
      }
      ListAutoBean.this.call("iterator", toReturn );
      return toReturn;
    }
    public java.util.List subList(int fromIndex,int toIndex)  {
      java.util.List toReturn = (java.util.List) ListAutoBean.this.getWrapped().subList(fromIndex,toIndex);
      if (toReturn != null) {
        if (ListAutoBean.this.isWrapped(toReturn)) {
          toReturn = ListAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      ListAutoBean.this.call("subList", toReturn, fromIndex,toIndex);
      return toReturn;
    }
    public java.util.ListIterator listIterator()  {
      java.util.ListIterator toReturn = (java.util.ListIterator) ListAutoBean.this.getWrapped().listIterator();
      if (toReturn != null) {
        if (ListAutoBean.this.isWrapped(toReturn)) {
          toReturn = ListAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListIteratorAutoBean(getFactory(), toReturn).as();
        }
      }
      ListAutoBean.this.call("listIterator", toReturn );
      return toReturn;
    }
    public java.util.ListIterator listIterator(int from)  {
      java.util.ListIterator toReturn = (java.util.ListIterator) ListAutoBean.this.getWrapped().listIterator(from);
      if (toReturn != null) {
        if (ListAutoBean.this.isWrapped(toReturn)) {
          toReturn = ListAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListIteratorAutoBean(getFactory(), toReturn).as();
        }
      }
      ListAutoBean.this.call("listIterator", toReturn, from);
      return toReturn;
    }
    public void add(int index,java.lang.Object element)  {
      ListAutoBean.this.getWrapped().add(index,element);
      ListAutoBean.this.call("add", null, index,element);
    }
    public void clear()  {
      ListAutoBean.this.getWrapped().clear();
      ListAutoBean.this.call("clear", null );
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
  public ListAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, java.util.List wrapped) {
    super(wrapped, factory);
  }
  public java.util.List as() {return shim;}
  public Class<java.util.List> getType() {return java.util.List.class;}
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    java.util.List as = as();
    value = as.isEmpty();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(ListAutoBean.this, "empty"),
      boolean.class
    );
    if (visitor.visitValueProperty("empty", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("empty", value, propertyContext);
  }
}
