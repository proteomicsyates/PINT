package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PSMBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.util.Set getAmounts(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::amounts;
  }-*/;
  
  private static native void setAmounts(edu.scripps.yates.shared.model.PSMBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::amounts = value;
  }-*/;
  
  private static native java.util.HashMap getAmountsByExperimentalCondition(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::amountsByExperimentalCondition;
  }-*/;
  
  private static native void setAmountsByExperimentalCondition(edu.scripps.yates.shared.model.PSMBean instance, java.util.HashMap value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::amountsByExperimentalCondition = value;
  }-*/;
  
  private static native java.lang.Double getCalculatedMH(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::calculatedMH;
  }-*/;
  
  private static native void setCalculatedMH(edu.scripps.yates.shared.model.PSMBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::calculatedMH = value;
  }-*/;
  
  private static native java.lang.String getChargeState(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::chargeState;
  }-*/;
  
  private static native void setChargeState(edu.scripps.yates.shared.model.PSMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::chargeState = value;
  }-*/;
  
  private static native java.util.Set getConditions(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::conditions;
  }-*/;
  
  private static native void setConditions(edu.scripps.yates.shared.model.PSMBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::conditions = value;
  }-*/;
  
  private static native java.lang.Integer getDbID(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::dbID;
  }-*/;
  
  private static native void setDbID(edu.scripps.yates.shared.model.PSMBean instance, java.lang.Integer value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::dbID = value;
  }-*/;
  
  private static native java.lang.Double getExperimentalMH(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::experimentalMH;
  }-*/;
  
  private static native void setExperimentalMH(edu.scripps.yates.shared.model.PSMBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::experimentalMH = value;
  }-*/;
  
  private static native java.lang.String getFullSequence(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::fullSequence;
  }-*/;
  
  private static native void setFullSequence(edu.scripps.yates.shared.model.PSMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::fullSequence = value;
  }-*/;
  
  private static native java.lang.Double getIonProportion(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::ionProportion;
  }-*/;
  
  private static native void setIonProportion(edu.scripps.yates.shared.model.PSMBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::ionProportion = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.PSMBean getLightVersion(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::lightVersion;
  }-*/;
  
  private static native void setLightVersion(edu.scripps.yates.shared.model.PSMBean instance, edu.scripps.yates.shared.model.PSMBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::lightVersion = value;
  }-*/;
  
  private static native java.lang.Double getMassErrorPPM(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::massErrorPPM;
  }-*/;
  
  private static native void setMassErrorPPM(edu.scripps.yates.shared.model.PSMBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::massErrorPPM = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.MSRunBean getMsRun(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::msRun;
  }-*/;
  
  private static native void setMsRun(edu.scripps.yates.shared.model.PSMBean instance, edu.scripps.yates.shared.model.MSRunBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::msRun = value;
  }-*/;
  
  private static native int getNumProteins(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::numProteins;
  }-*/;
  
  private static native void setNumProteins(edu.scripps.yates.shared.model.PSMBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::numProteins = value;
  }-*/;
  
  private static native java.util.Set getOrganisms(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::organisms;
  }-*/;
  
  private static native void setOrganisms(edu.scripps.yates.shared.model.PSMBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::organisms = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.PeptideBean getPeptideBean(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::peptideBean;
  }-*/;
  
  private static native void setPeptideBean(edu.scripps.yates.shared.model.PSMBean instance, edu.scripps.yates.shared.model.PeptideBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::peptideBean = value;
  }-*/;
  
  private static native java.lang.Double getPi(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::pi;
  }-*/;
  
  private static native void setPi(edu.scripps.yates.shared.model.PSMBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::pi = value;
  }-*/;
  
  private static native java.lang.String getProteinAccessionString(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::proteinAccessionString;
  }-*/;
  
  private static native void setProteinAccessionString(edu.scripps.yates.shared.model.PSMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::proteinAccessionString = value;
  }-*/;
  
  private static native java.util.HashSet getProteinDBIds(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::proteinDBIds;
  }-*/;
  
  private static native void setProteinDBIds(edu.scripps.yates.shared.model.PSMBean instance, java.util.HashSet value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::proteinDBIds = value;
  }-*/;
  
  private static native java.lang.String getProteinDescriptionString(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::proteinDescriptionString;
  }-*/;
  
  private static native void setProteinDescriptionString(edu.scripps.yates.shared.model.PSMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::proteinDescriptionString = value;
  }-*/;
  
  private static native java.util.Set getProteins(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::proteins;
  }-*/;
  
  private static native void setProteins(edu.scripps.yates.shared.model.PSMBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::proteins = value;
  }-*/;
  
  private static native java.util.List getProteinsPrimaryAccessions(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::proteinsPrimaryAccessions;
  }-*/;
  
  private static native void setProteinsPrimaryAccessions(edu.scripps.yates.shared.model.PSMBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::proteinsPrimaryAccessions = value;
  }-*/;
  
  private static native java.lang.String getPsmID(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::psmID;
  }-*/;
  
  private static native void setPsmID(edu.scripps.yates.shared.model.PSMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::psmID = value;
  }-*/;
  
  private static native java.lang.String getPtmScoreString(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::ptmScoreString;
  }-*/;
  
  private static native void setPtmScoreString(edu.scripps.yates.shared.model.PSMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::ptmScoreString = value;
  }-*/;
  
  private static native java.lang.String getPtmString(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::ptmString;
  }-*/;
  
  private static native void setPtmString(edu.scripps.yates.shared.model.PSMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::ptmString = value;
  }-*/;
  
  private static native java.util.List getPtms(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::ptms;
  }-*/;
  
  private static native void setPtms(edu.scripps.yates.shared.model.PSMBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::ptms = value;
  }-*/;
  
  private static native java.util.Map getRatioDistributions(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::ratioDistributions;
  }-*/;
  
  private static native void setRatioDistributions(edu.scripps.yates.shared.model.PSMBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::ratioDistributions = value;
  }-*/;
  
  private static native java.util.Set getRatios(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::ratios;
  }-*/;
  
  private static native void setRatios(edu.scripps.yates.shared.model.PSMBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::ratios = value;
  }-*/;
  
  private static native java.util.HashMap getRatiosByExperimentalcondition(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::ratiosByExperimentalcondition;
  }-*/;
  
  private static native void setRatiosByExperimentalcondition(edu.scripps.yates.shared.model.PSMBean instance, java.util.HashMap value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::ratiosByExperimentalcondition = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.PeptideRelation getRelation(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::relation;
  }-*/;
  
  private static native void setRelation(edu.scripps.yates.shared.model.PSMBean instance, edu.scripps.yates.shared.model.PeptideRelation value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::relation = value;
  }-*/;
  
  private static native java.util.Map getScores(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::scores;
  }-*/;
  
  private static native void setScores(edu.scripps.yates.shared.model.PSMBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::scores = value;
  }-*/;
  
  private static native java.lang.String getSequence(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::sequence;
  }-*/;
  
  private static native void setSequence(edu.scripps.yates.shared.model.PSMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::sequence = value;
  }-*/;
  
  private static native java.lang.Integer getSpr(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::spr;
  }-*/;
  
  private static native void setSpr(edu.scripps.yates.shared.model.PSMBean instance, java.lang.Integer value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::spr = value;
  }-*/;
  
  private static native java.util.Map getStartingPositions(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::startingPositions;
  }-*/;
  
  private static native void setStartingPositions(edu.scripps.yates.shared.model.PSMBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::startingPositions = value;
  }-*/;
  
  private static native java.lang.Double getTotalIntensity(edu.scripps.yates.shared.model.PSMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PSMBean::totalIntensity;
  }-*/;
  
  private static native void setTotalIntensity(edu.scripps.yates.shared.model.PSMBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PSMBean::totalIntensity = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.PSMBean instance) throws SerializationException {
    setAmounts(instance, (java.util.Set) streamReader.readObject());
    setAmountsByExperimentalCondition(instance, (java.util.HashMap) streamReader.readObject());
    setCalculatedMH(instance, (java.lang.Double) streamReader.readObject());
    setChargeState(instance, streamReader.readString());
    setConditions(instance, (java.util.Set) streamReader.readObject());
    setDbID(instance, (java.lang.Integer) streamReader.readObject());
    setExperimentalMH(instance, (java.lang.Double) streamReader.readObject());
    setFullSequence(instance, streamReader.readString());
    setIonProportion(instance, (java.lang.Double) streamReader.readObject());
    setLightVersion(instance, (edu.scripps.yates.shared.model.PSMBean) streamReader.readObject());
    setMassErrorPPM(instance, (java.lang.Double) streamReader.readObject());
    setMsRun(instance, (edu.scripps.yates.shared.model.MSRunBean) streamReader.readObject());
    setNumProteins(instance, streamReader.readInt());
    setOrganisms(instance, (java.util.Set) streamReader.readObject());
    setPeptideBean(instance, (edu.scripps.yates.shared.model.PeptideBean) streamReader.readObject());
    setPi(instance, (java.lang.Double) streamReader.readObject());
    setProteinAccessionString(instance, streamReader.readString());
    setProteinDBIds(instance, (java.util.HashSet) streamReader.readObject());
    setProteinDescriptionString(instance, streamReader.readString());
    setProteins(instance, (java.util.Set) streamReader.readObject());
    setProteinsPrimaryAccessions(instance, (java.util.List) streamReader.readObject());
    setPsmID(instance, streamReader.readString());
    setPtmScoreString(instance, streamReader.readString());
    setPtmString(instance, streamReader.readString());
    setPtms(instance, (java.util.List) streamReader.readObject());
    setRatioDistributions(instance, (java.util.Map) streamReader.readObject());
    setRatios(instance, (java.util.Set) streamReader.readObject());
    setRatiosByExperimentalcondition(instance, (java.util.HashMap) streamReader.readObject());
    setRelation(instance, (edu.scripps.yates.shared.model.PeptideRelation) streamReader.readObject());
    setScores(instance, (java.util.Map) streamReader.readObject());
    setSequence(instance, streamReader.readString());
    setSpr(instance, (java.lang.Integer) streamReader.readObject());
    setStartingPositions(instance, (java.util.Map) streamReader.readObject());
    setTotalIntensity(instance, (java.lang.Double) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.model.PSMBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.PSMBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.PSMBean instance) throws SerializationException {
    streamWriter.writeObject(getAmounts(instance));
    streamWriter.writeObject(getAmountsByExperimentalCondition(instance));
    streamWriter.writeObject(getCalculatedMH(instance));
    streamWriter.writeString(getChargeState(instance));
    streamWriter.writeObject(getConditions(instance));
    streamWriter.writeObject(getDbID(instance));
    streamWriter.writeObject(getExperimentalMH(instance));
    streamWriter.writeString(getFullSequence(instance));
    streamWriter.writeObject(getIonProportion(instance));
    streamWriter.writeObject(getLightVersion(instance));
    streamWriter.writeObject(getMassErrorPPM(instance));
    streamWriter.writeObject(getMsRun(instance));
    streamWriter.writeInt(getNumProteins(instance));
    streamWriter.writeObject(getOrganisms(instance));
    streamWriter.writeObject(getPeptideBean(instance));
    streamWriter.writeObject(getPi(instance));
    streamWriter.writeString(getProteinAccessionString(instance));
    streamWriter.writeObject(getProteinDBIds(instance));
    streamWriter.writeString(getProteinDescriptionString(instance));
    streamWriter.writeObject(getProteins(instance));
    streamWriter.writeObject(getProteinsPrimaryAccessions(instance));
    streamWriter.writeString(getPsmID(instance));
    streamWriter.writeString(getPtmScoreString(instance));
    streamWriter.writeString(getPtmString(instance));
    streamWriter.writeObject(getPtms(instance));
    streamWriter.writeObject(getRatioDistributions(instance));
    streamWriter.writeObject(getRatios(instance));
    streamWriter.writeObject(getRatiosByExperimentalcondition(instance));
    streamWriter.writeObject(getRelation(instance));
    streamWriter.writeObject(getScores(instance));
    streamWriter.writeString(getSequence(instance));
    streamWriter.writeObject(getSpr(instance));
    streamWriter.writeObject(getStartingPositions(instance));
    streamWriter.writeObject(getTotalIntensity(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.PSMBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.PSMBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.PSMBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.PSMBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.PSMBean)object);
  }
  
}
