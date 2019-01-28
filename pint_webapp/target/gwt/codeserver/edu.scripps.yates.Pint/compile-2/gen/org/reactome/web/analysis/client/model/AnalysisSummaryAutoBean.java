package org.reactome.web.analysis.client.model;

public class AnalysisSummaryAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.analysis.client.model.AnalysisSummary> {
  private final org.reactome.web.analysis.client.model.AnalysisSummary shim = new org.reactome.web.analysis.client.model.AnalysisSummary() {
    public boolean isText()  {
      boolean toReturn = (boolean) AnalysisSummaryAutoBean.this.getWrapped().isText();
      return toReturn;
    }
    public java.lang.Boolean getInteractors()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) AnalysisSummaryAutoBean.this.getWrapped().getInteractors();
      return toReturn;
    }
    public java.lang.Boolean getProjection()  {
      java.lang.Boolean toReturn = (java.lang.Boolean) AnalysisSummaryAutoBean.this.getWrapped().getProjection();
      return toReturn;
    }
    public java.lang.Long getSpecies()  {
      java.lang.Long toReturn = (java.lang.Long) AnalysisSummaryAutoBean.this.getWrapped().getSpecies();
      return toReturn;
    }
    public java.lang.String getFileName()  {
      java.lang.String toReturn = (java.lang.String) AnalysisSummaryAutoBean.this.getWrapped().getFileName();
      return toReturn;
    }
    public java.lang.String getSampleName()  {
      java.lang.String toReturn = (java.lang.String) AnalysisSummaryAutoBean.this.getWrapped().getSampleName();
      return toReturn;
    }
    public java.lang.String getSpeciesName()  {
      java.lang.String toReturn = (java.lang.String) AnalysisSummaryAutoBean.this.getWrapped().getSpeciesName();
      return toReturn;
    }
    public java.lang.String getToken()  {
      java.lang.String toReturn = (java.lang.String) AnalysisSummaryAutoBean.this.getWrapped().getToken();
      return toReturn;
    }
    public java.lang.String getType()  {
      java.lang.String toReturn = (java.lang.String) AnalysisSummaryAutoBean.this.getWrapped().getType();
      return toReturn;
    }
    public void setSpeciesName(java.lang.String name)  {
      AnalysisSummaryAutoBean.this.getWrapped().setSpeciesName(name);
      AnalysisSummaryAutoBean.this.set("setSpeciesName", name);
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
  public AnalysisSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public AnalysisSummaryAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.analysis.client.model.AnalysisSummary wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.analysis.client.model.AnalysisSummary as() {return shim;}
  public Class<org.reactome.web.analysis.client.model.AnalysisSummary> getType() {return org.reactome.web.analysis.client.model.AnalysisSummary.class;}
  @Override protected org.reactome.web.analysis.client.model.AnalysisSummary createSimplePeer() {
    return new org.reactome.web.analysis.client.model.AnalysisSummary() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.analysis.client.model.AnalysisSummaryAutoBean.this.data;
      public boolean isText()  {
        java.lang.Boolean toReturn = AnalysisSummaryAutoBean.this.getOrReify("text");
        return toReturn == null ? false : toReturn;
      }
      public java.lang.Boolean getInteractors()  {
        return (java.lang.Boolean) AnalysisSummaryAutoBean.this.getOrReify("interactors");
      }
      public java.lang.Boolean getProjection()  {
        return (java.lang.Boolean) AnalysisSummaryAutoBean.this.getOrReify("projection");
      }
      public java.lang.Long getSpecies()  {
        return (java.lang.Long) AnalysisSummaryAutoBean.this.getOrReify("species");
      }
      public java.lang.String getFileName()  {
        return (java.lang.String) AnalysisSummaryAutoBean.this.getOrReify("fileName");
      }
      public java.lang.String getSampleName()  {
        return (java.lang.String) AnalysisSummaryAutoBean.this.getOrReify("sampleName");
      }
      public java.lang.String getSpeciesName()  {
        return (java.lang.String) AnalysisSummaryAutoBean.this.getOrReify("speciesName");
      }
      public java.lang.String getToken()  {
        return (java.lang.String) AnalysisSummaryAutoBean.this.getOrReify("token");
      }
      public java.lang.String getType()  {
        return (java.lang.String) AnalysisSummaryAutoBean.this.getOrReify("type");
      }
      public void setSpeciesName(java.lang.String name)  {
        AnalysisSummaryAutoBean.this.setProperty("speciesName", name);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.analysis.client.model.AnalysisSummary as = as();
    value = as.isText();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "text"),
      boolean.class
    );
    if (visitor.visitValueProperty("text", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("text", value, propertyContext);
    value = as.getInteractors();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "interactors"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("interactors", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("interactors", value, propertyContext);
    value = as.getProjection();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "projection"),
      java.lang.Boolean.class
    );
    if (visitor.visitValueProperty("projection", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("projection", value, propertyContext);
    value = as.getSpecies();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "species"),
      java.lang.Long.class
    );
    if (visitor.visitValueProperty("species", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("species", value, propertyContext);
    value = as.getFileName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "fileName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("fileName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("fileName", value, propertyContext);
    value = as.getSampleName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "sampleName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("sampleName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("sampleName", value, propertyContext);
    value = as.getSpeciesName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "speciesName"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("speciesName", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("speciesName", value, propertyContext);
    value = as.getToken();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "token"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("token", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("token", value, propertyContext);
    value = as.getType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(AnalysisSummaryAutoBean.this, "type"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("type", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("type", value, propertyContext);
  }
}
