package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PeptideBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.util.Set getAmounts(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::amounts;
  }-*/;
  
  private static native void setAmounts(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::amounts = value;
  }-*/;
  
  private static native java.util.HashMap getAmountsByExperimentalCondition(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::amountsByExperimentalCondition;
  }-*/;
  
  private static native void setAmountsByExperimentalCondition(edu.scripps.yates.shared.model.PeptideBean instance, java.util.HashMap value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::amountsByExperimentalCondition = value;
  }-*/;
  
  private static native java.lang.Double getCalculatedMH(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::calculatedMH;
  }-*/;
  
  private static native void setCalculatedMH(edu.scripps.yates.shared.model.PeptideBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::calculatedMH = value;
  }-*/;
  
  private static native java.util.Set getConditions(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::conditions;
  }-*/;
  
  private static native void setConditions(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::conditions = value;
  }-*/;
  
  private static native java.util.Set getDbIds(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::dbIds;
  }-*/;
  
  private static native void setDbIds(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::dbIds = value;
  }-*/;
  
  private static native java.lang.String getFullSequence(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::fullSequence;
  }-*/;
  
  private static native void setFullSequence(edu.scripps.yates.shared.model.PeptideBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::fullSequence = value;
  }-*/;
  
  private static native int getLength(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::length;
  }-*/;
  
  private static native void setLength(edu.scripps.yates.shared.model.PeptideBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::length = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.PeptideBean getLightVersion(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::lightVersion;
  }-*/;
  
  private static native void setLightVersion(edu.scripps.yates.shared.model.PeptideBean instance, edu.scripps.yates.shared.model.PeptideBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::lightVersion = value;
  }-*/;
  
  private static native java.util.Set getMsRuns(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::msRuns;
  }-*/;
  
  private static native void setMsRuns(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::msRuns = value;
  }-*/;
  
  private static native int getNumPSMs(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::numPSMs;
  }-*/;
  
  private static native void setNumPSMs(edu.scripps.yates.shared.model.PeptideBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::numPSMs = value;
  }-*/;
  
  private static native java.util.Map getNumPSMsByCondition(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::numPSMsByCondition;
  }-*/;
  
  private static native void setNumPSMsByCondition(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::numPSMsByCondition = value;
  }-*/;
  
  private static native java.util.Set getOrganisms(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::organisms;
  }-*/;
  
  private static native void setOrganisms(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::organisms = value;
  }-*/;
  
  private static native int getPeptideBeanUniqueIdentifier(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::peptideBeanUniqueIdentifier;
  }-*/;
  
  private static native void setPeptideBeanUniqueIdentifier(edu.scripps.yates.shared.model.PeptideBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::peptideBeanUniqueIdentifier = value;
  }-*/;
  
  private static native java.lang.String getProteinAccessionString(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::proteinAccessionString;
  }-*/;
  
  private static native void setProteinAccessionString(edu.scripps.yates.shared.model.PeptideBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::proteinAccessionString = value;
  }-*/;
  
  private static native java.util.Set getProteinDBIds(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::proteinDBIds;
  }-*/;
  
  private static native void setProteinDBIds(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::proteinDBIds = value;
  }-*/;
  
  private static native java.lang.String getProteinDescriptionString(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::proteinDescriptionString;
  }-*/;
  
  private static native void setProteinDescriptionString(edu.scripps.yates.shared.model.PeptideBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::proteinDescriptionString = value;
  }-*/;
  
  private static native java.util.Set getProteins(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::proteins;
  }-*/;
  
  private static native void setProteins(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::proteins = value;
  }-*/;
  
  private static native java.util.List getProteinsPrimaryAccessions(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::proteinsPrimaryAccessions;
  }-*/;
  
  private static native void setProteinsPrimaryAccessions(edu.scripps.yates.shared.model.PeptideBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::proteinsPrimaryAccessions = value;
  }-*/;
  
  private static native java.util.Set getPsmIds(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::psmIds;
  }-*/;
  
  private static native void setPsmIds(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::psmIds = value;
  }-*/;
  
  private static native java.util.Map getPsmIdsByCondition(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::psmIdsByCondition;
  }-*/;
  
  private static native void setPsmIdsByCondition(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::psmIdsByCondition = value;
  }-*/;
  
  private static native java.util.List getPsms(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::psms;
  }-*/;
  
  private static native void setPsms(edu.scripps.yates.shared.model.PeptideBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::psms = value;
  }-*/;
  
  private static native java.lang.String getPtmScoreString(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::ptmScoreString;
  }-*/;
  
  private static native void setPtmScoreString(edu.scripps.yates.shared.model.PeptideBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::ptmScoreString = value;
  }-*/;
  
  private static native java.lang.String getPtmString(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::ptmString;
  }-*/;
  
  private static native void setPtmString(edu.scripps.yates.shared.model.PeptideBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::ptmString = value;
  }-*/;
  
  private static native java.util.List getPtms(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::ptms;
  }-*/;
  
  private static native void setPtms(edu.scripps.yates.shared.model.PeptideBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::ptms = value;
  }-*/;
  
  private static native java.util.Map getRatioDistributions(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::ratioDistributions;
  }-*/;
  
  private static native void setRatioDistributions(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::ratioDistributions = value;
  }-*/;
  
  private static native java.util.Set getRatios(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::ratios;
  }-*/;
  
  private static native void setRatios(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::ratios = value;
  }-*/;
  
  private static native java.util.HashMap getRatiosByExperimentalcondition(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::ratiosByExperimentalcondition;
  }-*/;
  
  private static native void setRatiosByExperimentalcondition(edu.scripps.yates.shared.model.PeptideBean instance, java.util.HashMap value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::ratiosByExperimentalcondition = value;
  }-*/;
  
  private static native java.util.Set getRawSequences(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::rawSequences;
  }-*/;
  
  private static native void setRawSequences(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::rawSequences = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.PeptideRelation getRelation(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::relation;
  }-*/;
  
  private static native void setRelation(edu.scripps.yates.shared.model.PeptideBean instance, edu.scripps.yates.shared.model.PeptideRelation value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::relation = value;
  }-*/;
  
  private static native java.util.Map getScores(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::scores;
  }-*/;
  
  private static native void setScores(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::scores = value;
  }-*/;
  
  private static native java.lang.String getSequence(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::sequence;
  }-*/;
  
  private static native void setSequence(edu.scripps.yates.shared.model.PeptideBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::sequence = value;
  }-*/;
  
  private static native java.util.Map getStartingPositions(edu.scripps.yates.shared.model.PeptideBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PeptideBean::startingPositions;
  }-*/;
  
  private static native void setStartingPositions(edu.scripps.yates.shared.model.PeptideBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PeptideBean::startingPositions = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.PeptideBean instance) throws SerializationException {
    setAmounts(instance, (java.util.Set) streamReader.readObject());
    setAmountsByExperimentalCondition(instance, (java.util.HashMap) streamReader.readObject());
    setCalculatedMH(instance, (java.lang.Double) streamReader.readObject());
    setConditions(instance, (java.util.Set) streamReader.readObject());
    setDbIds(instance, (java.util.Set) streamReader.readObject());
    setFullSequence(instance, streamReader.readString());
    setLength(instance, streamReader.readInt());
    setLightVersion(instance, (edu.scripps.yates.shared.model.PeptideBean) streamReader.readObject());
    setMsRuns(instance, (java.util.Set) streamReader.readObject());
    setNumPSMs(instance, streamReader.readInt());
    setNumPSMsByCondition(instance, (java.util.Map) streamReader.readObject());
    setOrganisms(instance, (java.util.Set) streamReader.readObject());
    setPeptideBeanUniqueIdentifier(instance, streamReader.readInt());
    setProteinAccessionString(instance, streamReader.readString());
    setProteinDBIds(instance, (java.util.Set) streamReader.readObject());
    setProteinDescriptionString(instance, streamReader.readString());
    setProteins(instance, (java.util.Set) streamReader.readObject());
    setProteinsPrimaryAccessions(instance, (java.util.List) streamReader.readObject());
    setPsmIds(instance, (java.util.Set) streamReader.readObject());
    setPsmIdsByCondition(instance, (java.util.Map) streamReader.readObject());
    setPsms(instance, (java.util.List) streamReader.readObject());
    setPtmScoreString(instance, streamReader.readString());
    setPtmString(instance, streamReader.readString());
    setPtms(instance, (java.util.List) streamReader.readObject());
    setRatioDistributions(instance, (java.util.Map) streamReader.readObject());
    setRatios(instance, (java.util.Set) streamReader.readObject());
    setRatiosByExperimentalcondition(instance, (java.util.HashMap) streamReader.readObject());
    setRawSequences(instance, (java.util.Set) streamReader.readObject());
    setRelation(instance, (edu.scripps.yates.shared.model.PeptideRelation) streamReader.readObject());
    setScores(instance, (java.util.Map) streamReader.readObject());
    setSequence(instance, streamReader.readString());
    setStartingPositions(instance, (java.util.Map) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.model.PeptideBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.PeptideBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.PeptideBean instance) throws SerializationException {
    streamWriter.writeObject(getAmounts(instance));
    streamWriter.writeObject(getAmountsByExperimentalCondition(instance));
    streamWriter.writeObject(getCalculatedMH(instance));
    streamWriter.writeObject(getConditions(instance));
    streamWriter.writeObject(getDbIds(instance));
    streamWriter.writeString(getFullSequence(instance));
    streamWriter.writeInt(getLength(instance));
    streamWriter.writeObject(getLightVersion(instance));
    streamWriter.writeObject(getMsRuns(instance));
    streamWriter.writeInt(getNumPSMs(instance));
    streamWriter.writeObject(getNumPSMsByCondition(instance));
    streamWriter.writeObject(getOrganisms(instance));
    streamWriter.writeInt(getPeptideBeanUniqueIdentifier(instance));
    streamWriter.writeString(getProteinAccessionString(instance));
    streamWriter.writeObject(getProteinDBIds(instance));
    streamWriter.writeString(getProteinDescriptionString(instance));
    streamWriter.writeObject(getProteins(instance));
    streamWriter.writeObject(getProteinsPrimaryAccessions(instance));
    streamWriter.writeObject(getPsmIds(instance));
    streamWriter.writeObject(getPsmIdsByCondition(instance));
    streamWriter.writeObject(getPsms(instance));
    streamWriter.writeString(getPtmScoreString(instance));
    streamWriter.writeString(getPtmString(instance));
    streamWriter.writeObject(getPtms(instance));
    streamWriter.writeObject(getRatioDistributions(instance));
    streamWriter.writeObject(getRatios(instance));
    streamWriter.writeObject(getRatiosByExperimentalcondition(instance));
    streamWriter.writeObject(getRawSequences(instance));
    streamWriter.writeObject(getRelation(instance));
    streamWriter.writeObject(getScores(instance));
    streamWriter.writeString(getSequence(instance));
    streamWriter.writeObject(getStartingPositions(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.PeptideBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.PeptideBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.PeptideBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.PeptideBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.PeptideBean)object);
  }
  
}
