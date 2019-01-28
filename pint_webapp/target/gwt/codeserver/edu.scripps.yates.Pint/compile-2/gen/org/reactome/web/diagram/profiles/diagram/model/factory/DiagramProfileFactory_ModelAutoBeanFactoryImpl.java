package org.reactome.web.diagram.profiles.diagram.model.factory;

public class DiagramProfileFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.diagram.profiles.diagram.model.factory.DiagramProfileFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.diagram.profiles.diagram.model.DiagramProfile.class, getConstructors_org_reactome_web_diagram_profiles_diagram_model_DiagramProfile());
    map.add(org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNode.class, getConstructors_org_reactome_web_diagram_profiles_diagram_model_DiagramProfileNode());
    map.add(org.reactome.web.diagram.profiles.diagram.model.DiagramProfileProperties.class, getConstructors_org_reactome_web_diagram_profiles_diagram_model_DiagramProfileProperties());
    map.add(org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnail.class, getConstructors_org_reactome_web_diagram_profiles_diagram_model_DiagramProfileThumbnail());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_diagram_model_DiagramProfile() /*-{
    return [
      @org.reactome.web.diagram.profiles.diagram.model.DiagramProfileAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.diagram.model.DiagramProfileAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/diagram/model/DiagramProfile;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_diagram_model_DiagramProfileNode() /*-{
    return [
      @org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/diagram/model/DiagramProfileNode;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_diagram_model_DiagramProfileProperties() /*-{
    return [
      @org.reactome.web.diagram.profiles.diagram.model.DiagramProfilePropertiesAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.diagram.model.DiagramProfilePropertiesAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/diagram/model/DiagramProfileProperties;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_diagram_model_DiagramProfileThumbnail() /*-{
    return [
      @org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnailAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnailAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/diagram/model/DiagramProfileThumbnail;)
    ];
  }-*/;
  @Override protected void initializeEnumMap() {
  }
  public com.google.web.bindery.autobean.shared.AutoBean profile() {
    return new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileAutoBean(DiagramProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean profileNode() {
    return new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileNodeAutoBean(DiagramProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean profileProperties() {
    return new org.reactome.web.diagram.profiles.diagram.model.DiagramProfilePropertiesAutoBean(DiagramProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean profileThumbnail() {
    return new org.reactome.web.diagram.profiles.diagram.model.DiagramProfileThumbnailAutoBean(DiagramProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
}
