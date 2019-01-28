package org.reactome.web.diagram.data.graph.raw.factory;

public class GraphFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.diagram.data.graph.raw.factory.GraphFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.diagram.data.graph.raw.Graph.class, getConstructors_org_reactome_web_diagram_data_graph_raw_Graph());
    map.add(org.reactome.web.diagram.data.graph.raw.EntityNode.class, getConstructors_org_reactome_web_diagram_data_graph_raw_EntityNode());
    map.add(org.reactome.web.diagram.data.graph.raw.EventNode.class, getConstructors_org_reactome_web_diagram_data_graph_raw_EventNode());
    map.add(java.util.List.class, getConstructors_java_util_List());
    map.add(org.reactome.web.diagram.data.graph.raw.SubpathwayNode.class, getConstructors_org_reactome_web_diagram_data_graph_raw_SubpathwayNode());
    map.add(java.util.Iterator.class, getConstructors_java_util_Iterator());
    map.add(java.util.ListIterator.class, getConstructors_java_util_ListIterator());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_graph_raw_Graph() /*-{
    return [
      @org.reactome.web.diagram.data.graph.raw.GraphAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.graph.raw.GraphAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/graph/raw/Graph;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_graph_raw_EntityNode() /*-{
    return [
      @org.reactome.web.diagram.data.graph.raw.EntityNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.graph.raw.EntityNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/graph/raw/EntityNode;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_graph_raw_EventNode() /*-{
    return [
      @org.reactome.web.diagram.data.graph.raw.EventNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.graph.raw.EventNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/graph/raw/EventNode;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_java_util_List() /*-{
    return [
      ,
      @emul.java.util.ListAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Ljava/util/List;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_graph_raw_SubpathwayNode() /*-{
    return [
      @org.reactome.web.diagram.data.graph.raw.SubpathwayNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.graph.raw.SubpathwayNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/graph/raw/SubpathwayNode;)
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
  public com.google.web.bindery.autobean.shared.AutoBean graph() {
    return new org.reactome.web.diagram.data.graph.raw.GraphAutoBean(GraphFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean graphEntityNode() {
    return new org.reactome.web.diagram.data.graph.raw.EntityNodeAutoBean(GraphFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean graphEventNode() {
    return new org.reactome.web.diagram.data.graph.raw.EventNodeAutoBean(GraphFactory_ModelAutoBeanFactoryImpl.this);
  }
}
