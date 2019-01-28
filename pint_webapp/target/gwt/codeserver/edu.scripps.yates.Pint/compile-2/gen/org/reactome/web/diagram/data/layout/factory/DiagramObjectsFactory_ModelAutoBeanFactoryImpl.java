package org.reactome.web.diagram.data.layout.factory;

public class DiagramObjectsFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.diagram.data.layout.factory.DiagramObjectsFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.diagram.data.layout.Color.class, getConstructors_org_reactome_web_diagram_data_layout_Color());
    map.add(org.reactome.web.diagram.data.layout.Compartment.class, getConstructors_org_reactome_web_diagram_data_layout_Compartment());
    map.add(org.reactome.web.diagram.data.layout.Connector.class, getConstructors_org_reactome_web_diagram_data_layout_Connector());
    map.add(org.reactome.web.diagram.data.layout.Diagram.class, getConstructors_org_reactome_web_diagram_data_layout_Diagram());
    map.add(org.reactome.web.diagram.data.layout.Edge.class, getConstructors_org_reactome_web_diagram_data_layout_Edge());
    map.add(org.reactome.web.diagram.data.layout.EdgeCommon.class, getConstructors_org_reactome_web_diagram_data_layout_EdgeCommon());
    map.add(org.reactome.web.diagram.data.layout.Link.class, getConstructors_org_reactome_web_diagram_data_layout_Link());
    map.add(org.reactome.web.diagram.data.layout.Identifier.class, getConstructors_org_reactome_web_diagram_data_layout_Identifier());
    map.add(org.reactome.web.diagram.data.layout.Node.class, getConstructors_org_reactome_web_diagram_data_layout_Node());
    map.add(org.reactome.web.diagram.data.layout.NodeAttachment.class, getConstructors_org_reactome_web_diagram_data_layout_NodeAttachment());
    map.add(org.reactome.web.diagram.data.layout.NodeCommon.class, getConstructors_org_reactome_web_diagram_data_layout_NodeCommon());
    map.add(org.reactome.web.diagram.data.layout.Note.class, getConstructors_org_reactome_web_diagram_data_layout_Note());
    map.add(org.reactome.web.diagram.data.layout.DiagramObject.class, getConstructors_org_reactome_web_diagram_data_layout_DiagramObject());
    map.add(org.reactome.web.diagram.data.layout.Coordinate.class, getConstructors_org_reactome_web_diagram_data_layout_Coordinate());
    map.add(org.reactome.web.diagram.data.layout.NodeProperties.class, getConstructors_org_reactome_web_diagram_data_layout_NodeProperties());
    map.add(org.reactome.web.diagram.data.layout.ReactionPart.class, getConstructors_org_reactome_web_diagram_data_layout_ReactionPart());
    map.add(org.reactome.web.diagram.data.layout.Segment.class, getConstructors_org_reactome_web_diagram_data_layout_Segment());
    map.add(org.reactome.web.diagram.data.layout.Shadow.class, getConstructors_org_reactome_web_diagram_data_layout_Shadow());
    map.add(org.reactome.web.diagram.data.layout.Shape.class, getConstructors_org_reactome_web_diagram_data_layout_Shape());
    map.add(org.reactome.web.diagram.data.layout.SummaryItem.class, getConstructors_org_reactome_web_diagram_data_layout_SummaryItem());
    map.add(java.util.List.class, getConstructors_java_util_List());
    map.add(org.reactome.web.diagram.data.layout.Bound.class, getConstructors_org_reactome_web_diagram_data_layout_Bound());
    map.add(org.reactome.web.diagram.data.layout.Stoichiometry.class, getConstructors_org_reactome_web_diagram_data_layout_Stoichiometry());
    map.add(java.util.Iterator.class, getConstructors_java_util_Iterator());
    map.add(java.util.ListIterator.class, getConstructors_java_util_ListIterator());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Color() /*-{
    return [
      @org.reactome.web.diagram.data.layout.ColorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.ColorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Color;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Compartment() /*-{
    return [
      @org.reactome.web.diagram.data.layout.CompartmentAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.CompartmentAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Compartment;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Connector() /*-{
    return [
      @org.reactome.web.diagram.data.layout.ConnectorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.ConnectorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Connector;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Diagram() /*-{
    return [
      @org.reactome.web.diagram.data.layout.DiagramAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.DiagramAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Diagram;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Edge() /*-{
    return [
      @org.reactome.web.diagram.data.layout.EdgeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.EdgeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Edge;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_EdgeCommon() /*-{
    return [
      @org.reactome.web.diagram.data.layout.EdgeCommonAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.EdgeCommonAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/EdgeCommon;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Link() /*-{
    return [
      @org.reactome.web.diagram.data.layout.LinkAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.LinkAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Link;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Identifier() /*-{
    return [
      @org.reactome.web.diagram.data.layout.IdentifierAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.IdentifierAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Identifier;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Node() /*-{
    return [
      @org.reactome.web.diagram.data.layout.NodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.NodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Node;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_NodeAttachment() /*-{
    return [
      @org.reactome.web.diagram.data.layout.NodeAttachmentAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.NodeAttachmentAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/NodeAttachment;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_NodeCommon() /*-{
    return [
      @org.reactome.web.diagram.data.layout.NodeCommonAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.NodeCommonAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/NodeCommon;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Note() /*-{
    return [
      @org.reactome.web.diagram.data.layout.NoteAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.NoteAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Note;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_DiagramObject() /*-{
    return [
      @org.reactome.web.diagram.data.layout.DiagramObjectAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.DiagramObjectAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/DiagramObject;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Coordinate() /*-{
    return [
      @org.reactome.web.diagram.data.layout.CoordinateAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.CoordinateAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Coordinate;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_NodeProperties() /*-{
    return [
      @org.reactome.web.diagram.data.layout.NodePropertiesAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.NodePropertiesAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/NodeProperties;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_ReactionPart() /*-{
    return [
      @org.reactome.web.diagram.data.layout.ReactionPartAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.ReactionPartAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/ReactionPart;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Segment() /*-{
    return [
      @org.reactome.web.diagram.data.layout.SegmentAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.SegmentAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Segment;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Shadow() /*-{
    return [
      @org.reactome.web.diagram.data.layout.ShadowAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.ShadowAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Shadow;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Shape() /*-{
    return [
      @org.reactome.web.diagram.data.layout.ShapeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.ShapeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Shape;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_SummaryItem() /*-{
    return [
      @org.reactome.web.diagram.data.layout.SummaryItemAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.SummaryItemAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/SummaryItem;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_java_util_List() /*-{
    return [
      ,
      @emul.java.util.ListAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Ljava/util/List;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Bound() /*-{
    return [
      @org.reactome.web.diagram.data.layout.BoundAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.BoundAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Bound;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_layout_Stoichiometry() /*-{
    return [
      @org.reactome.web.diagram.data.layout.StoichiometryAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.layout.StoichiometryAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/layout/Stoichiometry;)
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
  public com.google.web.bindery.autobean.shared.AutoBean colorRGB() {
    return new org.reactome.web.diagram.data.layout.ColorAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean compartment() {
    return new org.reactome.web.diagram.data.layout.CompartmentAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean connector() {
    return new org.reactome.web.diagram.data.layout.ConnectorAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean diagram() {
    return new org.reactome.web.diagram.data.layout.DiagramAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean edge() {
    return new org.reactome.web.diagram.data.layout.EdgeAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean edgeCommon() {
    return new org.reactome.web.diagram.data.layout.EdgeCommonAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean link() {
    return new org.reactome.web.diagram.data.layout.LinkAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean mainId() {
    return new org.reactome.web.diagram.data.layout.IdentifierAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean node() {
    return new org.reactome.web.diagram.data.layout.NodeAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean nodeAttachment() {
    return new org.reactome.web.diagram.data.layout.NodeAttachmentAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean nodeCommon() {
    return new org.reactome.web.diagram.data.layout.NodeCommonAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean note() {
    return new org.reactome.web.diagram.data.layout.NoteAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean object() {
    return new org.reactome.web.diagram.data.layout.DiagramObjectAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean position() {
    return new org.reactome.web.diagram.data.layout.CoordinateAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean prop() {
    return new org.reactome.web.diagram.data.layout.NodePropertiesAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean reactionPart() {
    return new org.reactome.web.diagram.data.layout.ReactionPartAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean segment() {
    return new org.reactome.web.diagram.data.layout.SegmentAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean shadow() {
    return new org.reactome.web.diagram.data.layout.ShadowAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean shape() {
    return new org.reactome.web.diagram.data.layout.ShapeAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean summaryItem() {
    return new org.reactome.web.diagram.data.layout.SummaryItemAutoBean(DiagramObjectsFactory_ModelAutoBeanFactoryImpl.this);
  }
}
