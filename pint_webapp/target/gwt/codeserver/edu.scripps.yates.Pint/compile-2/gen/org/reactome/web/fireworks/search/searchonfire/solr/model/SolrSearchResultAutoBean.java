package org.reactome.web.fireworks.search.searchonfire.solr.model;

public class SolrSearchResultAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult> {
  private final org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult shim = new org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult() {
    public java.lang.Integer getFound()  {
      java.lang.Integer toReturn = (java.lang.Integer) SolrSearchResultAutoBean.this.getWrapped().getFound();
      return toReturn;
    }
    public java.lang.Integer getRows()  {
      java.lang.Integer toReturn = (java.lang.Integer) SolrSearchResultAutoBean.this.getWrapped().getRows();
      return toReturn;
    }
    public java.lang.Integer getStartRow()  {
      java.lang.Integer toReturn = (java.lang.Integer) SolrSearchResultAutoBean.this.getWrapped().getStartRow();
      return toReturn;
    }
    public java.lang.String getSelectedFacet()  {
      java.lang.String toReturn = (java.lang.String) SolrSearchResultAutoBean.this.getWrapped().getSelectedFacet();
      return toReturn;
    }
    public java.lang.String getSpecies()  {
      java.lang.String toReturn = (java.lang.String) SolrSearchResultAutoBean.this.getWrapped().getSpecies();
      return toReturn;
    }
    public java.lang.String getTerm()  {
      java.lang.String toReturn = (java.lang.String) SolrSearchResultAutoBean.this.getWrapped().getTerm();
      return toReturn;
    }
    public java.util.List getEntries()  {
      java.util.List toReturn = (java.util.List) SolrSearchResultAutoBean.this.getWrapped().getEntries();
      if (toReturn != null) {
        if (SolrSearchResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = SolrSearchResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public java.util.List getFacets()  {
      java.util.List toReturn = (java.util.List) SolrSearchResultAutoBean.this.getWrapped().getFacets();
      if (toReturn != null) {
        if (SolrSearchResultAutoBean.this.isWrapped(toReturn)) {
          toReturn = SolrSearchResultAutoBean.this.getFromWrapper(toReturn);
        } else {
          toReturn = new emul.java.util.ListAutoBean(getFactory(), toReturn).as();
        }
      }
      return toReturn;
    }
    public void setEntries(java.util.List entries)  {
      SolrSearchResultAutoBean.this.getWrapped().setEntries(entries);
      SolrSearchResultAutoBean.this.set("setEntries", entries);
    }
    public void setFacets(java.util.List facets)  {
      SolrSearchResultAutoBean.this.getWrapped().setFacets(facets);
      SolrSearchResultAutoBean.this.set("setFacets", facets);
    }
    public void setFound(java.lang.Integer found)  {
      SolrSearchResultAutoBean.this.getWrapped().setFound(found);
      SolrSearchResultAutoBean.this.set("setFound", found);
    }
    public void setRows(java.lang.Integer rows)  {
      SolrSearchResultAutoBean.this.getWrapped().setRows(rows);
      SolrSearchResultAutoBean.this.set("setRows", rows);
    }
    public void setSelectedFacet(java.lang.String facet)  {
      SolrSearchResultAutoBean.this.getWrapped().setSelectedFacet(facet);
      SolrSearchResultAutoBean.this.set("setSelectedFacet", facet);
    }
    public void setSpecies(java.lang.String species)  {
      SolrSearchResultAutoBean.this.getWrapped().setSpecies(species);
      SolrSearchResultAutoBean.this.set("setSpecies", species);
    }
    public void setStartRow(java.lang.Integer page)  {
      SolrSearchResultAutoBean.this.getWrapped().setStartRow(page);
      SolrSearchResultAutoBean.this.set("setStartRow", page);
    }
    public void setTerm(java.lang.String term)  {
      SolrSearchResultAutoBean.this.getWrapped().setTerm(term);
      SolrSearchResultAutoBean.this.set("setTerm", term);
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
  public SolrSearchResultAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public SolrSearchResultAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult wrapped) {
    super(wrapped, factory);
  }
  public org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult as() {return shim;}
  public Class<org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult> getType() {return org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult.class;}
  @Override protected org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult createSimplePeer() {
    return new org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult() {
      private final com.google.web.bindery.autobean.shared.Splittable data = org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResultAutoBean.this.data;
      public java.lang.Integer getFound()  {
        return (java.lang.Integer) SolrSearchResultAutoBean.this.getOrReify("found");
      }
      public java.lang.Integer getRows()  {
        return (java.lang.Integer) SolrSearchResultAutoBean.this.getOrReify("rows");
      }
      public java.lang.Integer getStartRow()  {
        return (java.lang.Integer) SolrSearchResultAutoBean.this.getOrReify("startRow");
      }
      public java.lang.String getSelectedFacet()  {
        return (java.lang.String) SolrSearchResultAutoBean.this.getOrReify("selectedFacet");
      }
      public java.lang.String getSpecies()  {
        return (java.lang.String) SolrSearchResultAutoBean.this.getOrReify("species");
      }
      public java.lang.String getTerm()  {
        return (java.lang.String) SolrSearchResultAutoBean.this.getOrReify("term");
      }
      public java.util.List getEntries()  {
        return (java.util.List) SolrSearchResultAutoBean.this.getOrReify("entries");
      }
      public java.util.List getFacets()  {
        return (java.util.List) SolrSearchResultAutoBean.this.getOrReify("facets");
      }
      public void setEntries(java.util.List entries)  {
        SolrSearchResultAutoBean.this.setProperty("entries", entries);
      }
      public void setFacets(java.util.List facets)  {
        SolrSearchResultAutoBean.this.setProperty("facets", facets);
      }
      public void setFound(java.lang.Integer found)  {
        SolrSearchResultAutoBean.this.setProperty("found", found);
      }
      public void setRows(java.lang.Integer rows)  {
        SolrSearchResultAutoBean.this.setProperty("rows", rows);
      }
      public void setSelectedFacet(java.lang.String facet)  {
        SolrSearchResultAutoBean.this.setProperty("selectedFacet", facet);
      }
      public void setSpecies(java.lang.String species)  {
        SolrSearchResultAutoBean.this.setProperty("species", species);
      }
      public void setStartRow(java.lang.Integer page)  {
        SolrSearchResultAutoBean.this.setProperty("startRow", page);
      }
      public void setTerm(java.lang.String term)  {
        SolrSearchResultAutoBean.this.setProperty("term", term);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult as = as();
    value = as.getFound();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SolrSearchResultAutoBean.this, "found"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("found", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("found", value, propertyContext);
    value = as.getRows();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SolrSearchResultAutoBean.this, "rows"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("rows", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("rows", value, propertyContext);
    value = as.getStartRow();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SolrSearchResultAutoBean.this, "startRow"),
      java.lang.Integer.class
    );
    if (visitor.visitValueProperty("startRow", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("startRow", value, propertyContext);
    value = as.getSelectedFacet();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SolrSearchResultAutoBean.this, "selectedFacet"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("selectedFacet", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("selectedFacet", value, propertyContext);
    value = as.getSpecies();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SolrSearchResultAutoBean.this, "species"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("species", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("species", value, propertyContext);
    value = as.getTerm();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SolrSearchResultAutoBean.this, "term"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("term", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("term", value, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getEntries());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SolrSearchResultAutoBean.this, "entries"),
      new Class<?>[] {java.util.List.class, org.reactome.web.fireworks.search.searchonfire.solr.model.Entry.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("entries", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("entries", bean, propertyContext);
    bean = (com.google.web.bindery.autobean.shared.impl.AbstractAutoBean) com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(as.getFacets());
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(SolrSearchResultAutoBean.this, "facets"),
      new Class<?>[] {java.util.List.class, org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer.class},
      new int[] {1, 0}
    );
    if (visitor.visitCollectionProperty("facets", bean, propertyContext)) {
      if (bean != null) { bean.traverse(visitor, ctx); }
    }
    visitor.endVisitCollectionProperty("facets", bean, propertyContext);
  }
}
