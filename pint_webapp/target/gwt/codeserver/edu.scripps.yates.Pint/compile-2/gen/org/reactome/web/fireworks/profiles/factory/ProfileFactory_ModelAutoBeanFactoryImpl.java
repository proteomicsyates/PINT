package org.reactome.web.fireworks.profiles.factory;

public class ProfileFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.fireworks.profiles.factory.ProfileFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.fireworks.profiles.model.Profile.class, getConstructors_org_reactome_web_fireworks_profiles_model_Profile());
    map.add(org.reactome.web.fireworks.profiles.model.ProfileColour.class, getConstructors_org_reactome_web_fireworks_profiles_model_ProfileColour());
    map.add(org.reactome.web.fireworks.profiles.model.ProfileGradient.class, getConstructors_org_reactome_web_fireworks_profiles_model_ProfileGradient());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_profiles_model_Profile() /*-{
    return [
      @org.reactome.web.fireworks.profiles.model.ProfileAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.profiles.model.ProfileAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/profiles/model/Profile;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_profiles_model_ProfileColour() /*-{
    return [
      @org.reactome.web.fireworks.profiles.model.ProfileColourAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.profiles.model.ProfileColourAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/profiles/model/ProfileColour;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_fireworks_profiles_model_ProfileGradient() /*-{
    return [
      @org.reactome.web.fireworks.profiles.model.ProfileGradientAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.fireworks.profiles.model.ProfileGradientAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/fireworks/profiles/model/ProfileGradient;)
    ];
  }-*/;
  @Override protected void initializeEnumMap() {
  }
  public com.google.web.bindery.autobean.shared.AutoBean profile() {
    return new org.reactome.web.fireworks.profiles.model.ProfileAutoBean(ProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean profileColour() {
    return new org.reactome.web.fireworks.profiles.model.ProfileColourAutoBean(ProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean profileGradient() {
    return new org.reactome.web.fireworks.profiles.model.ProfileGradientAutoBean(ProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
}
