package org.reactome.web.diagram.profiles.analysis.model.factory;

public class AnalysisProfileFactory_ModelAutoBeanFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements org.reactome.web.diagram.profiles.analysis.model.factory.AnalysisProfileFactory.ModelAutoBeanFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(org.reactome.web.diagram.profiles.analysis.model.AnalysisProfile.class, getConstructors_org_reactome_web_diagram_profiles_analysis_model_AnalysisProfile());
    map.add(org.reactome.web.diagram.profiles.analysis.model.ProfileGradient.class, getConstructors_org_reactome_web_diagram_profiles_analysis_model_ProfileGradient());
    map.add(org.reactome.web.diagram.profiles.analysis.model.OverlayNode.class, getConstructors_org_reactome_web_diagram_profiles_analysis_model_OverlayNode());
    map.add(org.reactome.web.diagram.profiles.analysis.model.OverlayLegend.class, getConstructors_org_reactome_web_diagram_profiles_analysis_model_OverlayLegend());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_analysis_model_AnalysisProfile() /*-{
    return [
      @org.reactome.web.diagram.profiles.analysis.model.AnalysisProfileAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.analysis.model.AnalysisProfileAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/analysis/model/AnalysisProfile;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_analysis_model_ProfileGradient() /*-{
    return [
      @org.reactome.web.diagram.profiles.analysis.model.ProfileGradientAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.analysis.model.ProfileGradientAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/analysis/model/ProfileGradient;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_analysis_model_OverlayNode() /*-{
    return [
      @org.reactome.web.diagram.profiles.analysis.model.OverlayNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.analysis.model.OverlayNodeAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/analysis/model/OverlayNode;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_org_reactome_web_diagram_profiles_analysis_model_OverlayLegend() /*-{
    return [
      @org.reactome.web.diagram.profiles.analysis.model.OverlayLegendAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @org.reactome.web.diagram.profiles.analysis.model.OverlayLegendAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lorg/reactome/web/diagram/profiles/analysis/model/OverlayLegend;)
    ];
  }-*/;
  @Override protected void initializeEnumMap() {
  }
  public com.google.web.bindery.autobean.shared.AutoBean profile() {
    return new org.reactome.web.diagram.profiles.analysis.model.AnalysisProfileAutoBean(AnalysisProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean profileGradient() {
    return new org.reactome.web.diagram.profiles.analysis.model.ProfileGradientAutoBean(AnalysisProfileFactory_ModelAutoBeanFactoryImpl.this);
  }
}
