package org.reactome.web.diagram.data.interactors.custom.raw.factory;

public class UploadResponseFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.diagram.data.interactors.custom.raw.factory.UploadResponseFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorError.class, getConstructors_org_reactome_web_diagram_data_interactors_custom_raw_RawInteractorError());
    map.add(org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponse.class, getConstructors_org_reactome_web_diagram_data_interactors_custom_raw_RawUploadResponse());
    map.add(org.reactome.web.diagram.data.interactors.custom.raw.RawSummary.class, getConstructors_org_reactome_web_diagram_data_interactors_custom_raw_RawSummary());
    map.add(java.util.List.class, getConstructors_java_util_List());
    map.add(java.util.Iterator.class, getConstructors_java_util_Iterator());
    map.add(java.util.ListIterator.class, getConstructors_java_util_ListIterator());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_custom_raw_RawInteractorError() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorErrorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorErrorAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/custom/raw/RawInteractorError;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_custom_raw_RawUploadResponse() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponseAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponseAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/custom/raw/RawUploadResponse;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_data_interactors_custom_raw_RawSummary() /*-{
    return [
      @org.reactome.web.diagram.data.interactors.custom.raw.RawSummaryAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.data.interactors.custom.raw.RawSummaryAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/data/interactors/custom/raw/RawSummary;)
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
  public com.google.web.bindery.autobean.shared.AutoBean error() {
    return new org.reactome.web.diagram.data.interactors.custom.raw.RawInteractorErrorAutoBean(UploadResponseFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean response() {
    return new org.reactome.web.diagram.data.interactors.custom.raw.RawUploadResponseAutoBean(UploadResponseFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean token() {
    return new org.reactome.web.diagram.data.interactors.custom.raw.RawSummaryAutoBean(UploadResponseFactory_ModelAutoBeanFactoryImpl.this);
  }
}
