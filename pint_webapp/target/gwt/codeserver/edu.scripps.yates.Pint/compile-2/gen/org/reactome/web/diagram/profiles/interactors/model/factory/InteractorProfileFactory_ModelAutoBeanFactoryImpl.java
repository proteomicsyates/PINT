package org.reactome.web.diagram.profiles.interactors.model.factory;

public class InteractorProfileFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.diagram.profiles.interactors.model.factory.InteractorProfileFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.diagram.profiles.interactors.model.InteractorProfile.class, getConstructors_org_reactome_web_diagram_profiles_interactors_model_InteractorProfile());
    map.add(org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNode.class, getConstructors_org_reactome_web_diagram_profiles_interactors_model_InteractorProfileNode());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_interactors_model_InteractorProfile() /*-{
    return [
      @org.reactome.web.diagram.profiles.interactors.model.InteractorProfileAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.interactors.model.InteractorProfileAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/interactors/model/InteractorProfile;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_interactors_model_InteractorProfileNode() /*-{
    return [
      @org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/interactors/model/InteractorProfileNode;)
    ];
  }-*/;
  @Override protected void initializeEnumMap() {
  }
  public com.google.web.bindery.autobean.shared.AutoBean profile() {
    return new org.reactome.web.diagram.profiles.interactors.model.InteractorProfileAutoBean(InteractorProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean profileNode() {
    return new org.reactome.web.diagram.profiles.interactors.model.InteractorProfileNodeAutoBean(InteractorProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
}
