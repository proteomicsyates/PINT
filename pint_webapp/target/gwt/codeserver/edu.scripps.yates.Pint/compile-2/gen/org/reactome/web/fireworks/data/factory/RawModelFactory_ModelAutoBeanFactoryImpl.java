package org.reactome.web.fireworks.data.factory;

public class RawModelFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.fireworks.data.factory.RawModelFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.fireworks.data.RawEdge.class, getConstructors_org_reactome_web_fireworks_data_RawEdge());
    map.add(org.reactome.web.fireworks.data.RawGraph.class, getConstructors_org_reactome_web_fireworks_data_RawGraph());
    map.add(org.reactome.web.fireworks.data.RawNode.class, getConstructors_org_reactome_web_fireworks_data_RawNode());
    map.add(java.util.List.class, getConstructors_java_util_List());
    map.add(java.util.Iterator.class, getConstructors_java_util_Iterator());
    map.add(java.util.ListIterator.class, getConstructors_java_util_ListIterator());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_data_RawEdge() /*-{
    return [
      @org.reactome.web.fireworks.data.RawEdgeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.data.RawEdgeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/data/RawEdge;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_data_RawGraph() /*-{
    return [
      @org.reactome.web.fireworks.data.RawGraphAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.data.RawGraphAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/data/RawGraph;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_data_RawNode() /*-{
    return [
      @org.reactome.web.fireworks.data.RawNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.data.RawNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/data/RawNode;)
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
  public com.google.web.bindery.autobean.shared.AutoBean edge() {
    return new org.reactome.web.fireworks.data.RawEdgeAutoBean(RawModelFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean graph() {
    return new org.reactome.web.fireworks.data.RawGraphAutoBean(RawModelFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean node() {
    return new org.reactome.web.fireworks.data.RawNodeAutoBean(RawModelFactory_ModelAutoBeanFactoryImpl.this);
  }
}
