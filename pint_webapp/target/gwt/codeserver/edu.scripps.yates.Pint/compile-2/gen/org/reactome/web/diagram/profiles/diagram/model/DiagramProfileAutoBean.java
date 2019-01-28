package org.reactome.web.diagram.profiles.diagram.model;

public class DiagramProfileAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.diagram.profiles.diagram.model.DiagramProfile> {
  private final org.reactome.web.diagram.profiles.diagram.model.DiagramProfile shim = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfile() {
    public java.lang.String getName()  {
      java.lang.String toReturn = (java.lang.String) DiagramProfileAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getAttachment()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getAttachment();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getChemical()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getChemical();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getCompartment()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getCompartment();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getComplex()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getComplex();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getEntity()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getEntity();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getEntityset()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getEntityset();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getFlowline()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getFlowline();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getGene()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getGene();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getInteractor()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getInteractor();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getLink()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getLink();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getNote()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getNote();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getOtherentity()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getOtherentity();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getProcessnode()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getProcessnode();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getProtein()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getProtein();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getReaction()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getReaction();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getRna()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getRna();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getStoichiometry()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getWrapped().getStoichiometry();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties getProperties()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties) DiagramProfileAutoBean.this.getWrapped().getProperties();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfilePropertiesAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail getThumbnail()  {
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail toReturn = (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail) DiagramProfileAutoBean.this.getWrapped().getThumbnail();
      if (toReturn != null) {
        if (DiagramProfileAutoBean.this.isWrapped(toReturn)) {
          toReturn = DiagramProfileAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnailAutoBean(getFactory(), toReturn).as();
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
  public DiagramProfileAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public DiagramProfileAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.diagram.profiles.diagram.model.DiagramProfile wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.diagram.profiles.diagram.model.DiagramProfile as() {return shim;}
  public Class<org.reactome.web.diagram.profiles.diagram.model.DiagramProfile> getType() {return org.reactome.web.diagram.profiles.diagram.model.DiagramProfile.class;}
  @Override protected org.reactome.web.diagram.profiles.diagram.model.DiagramProfile createSimplePeer() {
    return new org.reactome.web.diagram.profiles.diagram.model.DiagramProfile() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.diagram.profiles.diagram.model.DiagramProfileAutoBean.this.data;
      public java.lang.String getName()  {
        return (java.lang.String) DiagramProfileAutoBean.this.getOrReify("name");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getAttachment()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("attachment");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getChemical()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("chemical");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getCompartment()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("compartment");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getComplex()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("complex");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getEntity()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("entity");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getEntityset()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("entityset");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getFlowline()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("flowline");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getGene()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("gene");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getInteractor()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("interactor");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getLink()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("link");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getNote()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("note");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getOtherentity()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("otherentity");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getProcessnode()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("processnode");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getProtein()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("protein");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getReaction()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("reaction");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getRna()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("rna");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode getStoichiometry()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode) DiagramProfileAutoBean.this.getOrReify("stoichiometry");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties getProperties()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties) DiagramProfileAutoBean.this.getOrReify("properties");
      }
      public org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail getThumbnail()  {
        return (org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail) DiagramProfileAutoBean.this.getOrReify("thumbnail");
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.diagram.profiles.diagram.model.DiagramProfile as = as();
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getAttachment());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "attachment"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("attachment", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("attachment", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getChemical());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "chemical"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("chemical", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("chemical", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getCompartment());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "compartment"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("compartment", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("compartment", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getComplex());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "complex"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("complex", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("complex", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEntity());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "entity"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("entity", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("entity", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEntityset());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "entityset"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("entityset", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("entityset", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getFlowline());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "flowline"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("flowline", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("flowline", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getGene());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "gene"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("gene", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("gene", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getInteractor());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "interactor"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("interactor", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("interactor", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getLink());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "link"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("link", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("link", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getNote());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "note"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("note", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("note", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getOtherentity());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "otherentity"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("otherentity", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("otherentity", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getProcessnode());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "processnode"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("processnode", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("processnode", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getProtein());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "protein"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("protein", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("protein", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getReaction());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "reaction"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("reaction", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("reaction", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getRna());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "rna"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("rna", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("rna", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getStoichiometry());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "stoichiometry"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class
    );
    if (visitor.visitReferenceProperty("stoichiometry", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("stoichiometry", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getProperties());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "properties"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties.class
    );
    if (visitor.visitReferenceProperty("properties", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("properties", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getThumbnail());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(DiagramProfileAutoBean.this, "thumbnail"),
      org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail.class
    );
    if (visitor.visitReferenceProperty("thumbnail", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitReferenceProperty("thumbnail", bean, propertyContext);
  }
}
