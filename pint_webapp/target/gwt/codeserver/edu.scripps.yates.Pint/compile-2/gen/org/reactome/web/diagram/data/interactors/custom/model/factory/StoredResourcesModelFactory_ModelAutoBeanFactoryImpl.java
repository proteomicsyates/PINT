package org.reactome.web.diagram.data.interactors.custom.model.factory;

public class StoredResourcesModelFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.diagram.data.interactors.custom.model.factory.StoredResourcesModelFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.diagram.data.interactors.custom.model.CustomResource.class, getConstructors_org_reactome_web_diagram_data_interactors_custom_model_CustomResource());
    map.add(org.reactome.web.diagram.data.interactors.custom.model.CustomResources.class, getConstructors_org_reactome_web_diagram_data_interactors_custom_model_CustomResources());
    map.add(java.util.List.class, getConstructors_java_util_List());
    map.add(java.util.Iterator.class, getConstructors_java_util_Iterator());
    map.add(java.util.ListIterator.class, getConstructors_java_util_ListIterator());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_custom_model_CustomResource() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.custom.model.CustomResourceAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.custom.model.CustomResourceAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/custom/model/CustomResource;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_custom_model_CustomResources() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.custom.model.CustomResourcesAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.custom.model.CustomResourcesAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/custom/model/CustomResources;)
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
  public com.google.web.bindery.autobean.shared.AutoBean customResource() {
    return new org.reactome.web.diagram.data.interactors.custom.model.CustomResourceAutoBean(StoredResourcesModelFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean customResources() {
    return new org.reactome.web.diagram.data.interactors.custom.model.CustomResourcesAutoBean(StoredResourcesModelFactory_ModelAutoBeanFactoryImpl.this);
  }
}
