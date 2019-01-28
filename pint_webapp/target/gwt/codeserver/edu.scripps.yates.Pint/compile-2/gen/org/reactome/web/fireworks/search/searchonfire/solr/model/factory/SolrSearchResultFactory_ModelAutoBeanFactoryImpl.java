package org.reactome.web.fireworks.search.searchonfire.solr.model.factory;

public class SolrSearchResultFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.fireworks.search.searchonfire.solr.model.factory.SolrSearchResultFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.fireworks.search.searchonfire.solr.model.Entry.class, getConstructors_org_reactome_web_fireworks_search_searchonfire_solr_model_Entry());
    map.add(org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer.class, getConstructors_org_reactome_web_fireworks_search_searchonfire_solr_model_FacetContainer());
    map.add(org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult.class, getConstructors_org_reactome_web_fireworks_search_searchonfire_solr_model_SolrSearchResult());
    map.add(java.util.List.class, getConstructors_java_util_List());
    map.add(java.util.Iterator.class, getConstructors_java_util_Iterator());
    map.add(java.util.ListIterator.class, getConstructors_java_util_ListIterator());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_search_searchonfire_solr_model_Entry() /*-{
    return [
      @org.reactome.web.fireworks.search.searchonfire.solr.model.EntryAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.search.searchonfire.solr.model.EntryAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/search/searchonfire/solr/model/Entry;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_search_searchonfire_solr_model_FacetContainer() /*-{
    return [
      @org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainerAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainerAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/search/searchonfire/solr/model/FacetContainer;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_search_searchonfire_solr_model_SolrSearchResult() /*-{
    return [
      @org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResultAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResultAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/search/searchonfire/solr/model/SolrSearchResult;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_java_util_List() /*-{
    return [
      ,
      @emul.java.util.ListAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Ljava/util/List;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_java_util_Iterator() /*-{
    return [
      ,
      @emul.java.util.IteratorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Ljava/util/Iterator;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_java_util_ListIterator() /*-{
    return [
      ,
      @emul.java.util.ListIteratorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Ljava/util/ListIterator;)
    ];
  }-*/;
  @Override protected void initializeEnumMap() {
  }
  public com.google.web.bindery.autobean.shared.AutoBean entries() {
    return new org.reactome.web.fireworks.search.searchonfire.solr.model.EntryAutoBean(SolrSearchResultFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean facets() {
    return new org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainerAutoBean(SolrSearchResultFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean solrSearchResult() {
    return new org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResultAutoBean(SolrSearchResultFactory_ModelAutoBeanFactoryImpl.this);
  }
}
