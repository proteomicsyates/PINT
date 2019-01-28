package org.reactome.web.diagram.data.interactors.raw.factory;

public class InteractorsFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.diagram.data.interactors.raw.factory.InteractorsFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.diagram.data.interactors.raw.RawInteractors.class, getConstructors_org_reactome_web_diagram_data_interactors_raw_RawInteractors());
    map.add(org.reactome.web.diagram.data.interactors.raw.RawInteractorEntity.class, getConstructors_org_reactome_web_diagram_data_interactors_raw_RawInteractorEntity());
    map.add(org.reactome.web.diagram.data.interactors.raw.RawInteractor.class, getConstructors_org_reactome_web_diagram_data_interactors_raw_RawInteractor());
    map.add(org.reactome.web.diagram.data.interactors.raw.RawResource.class, getConstructors_org_reactome_web_diagram_data_interactors_raw_RawResource());
    map.add(java.util.List.class, getConstructors_java_util_List());
    map.add(java.util.Iterator.class, getConstructors_java_util_Iterator());
    map.add(java.util.ListIterator.class, getConstructors_java_util_ListIterator());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_raw_RawInteractors() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.raw.RawInteractorsAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.raw.RawInteractorsAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/raw/RawInteractors;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_raw_RawInteractorEntity() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.raw.RawInteractorEntityAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.raw.RawInteractorEntityAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/raw/RawInteractorEntity;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_raw_RawInteractor() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.raw.RawInteractorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.raw.RawInteractorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/raw/RawInteractor;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_raw_RawResource() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.raw.RawResourceAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.raw.RawResourceAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/raw/RawResource;)
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
  public com.google.web.bindery.autobean.shared.AutoBean diagramInteractors() {
    return new org.reactome.web.diagram.data.interactors.raw.RawInteractorsAutoBean(InteractorsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean entityInteractors() {
    return new org.reactome.web.diagram.data.interactors.raw.RawInteractorEntityAutoBean(InteractorsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean interactor() {
    return new org.reactome.web.diagram.data.interactors.raw.RawInteractorAutoBean(InteractorsFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean resource() {
    return new org.reactome.web.diagram.data.interactors.raw.RawResourceAutoBean(InteractorsFactory_ModelAutoBeanFactoryImpl.this);
  }
}
