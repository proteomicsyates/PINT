package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProteinBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getAlternativeNamesString(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::alternativeNamesString;
  }-*/;
  
  private static native void setAlternativeNamesString(edu.scripps.yates.shared.model.ProteinBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::alternativeNamesString = value;
  }-*/;
  
  private static native java.util.Set getAmounts(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::amounts;
  }-*/;
  
  private static native void setAmounts(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::amounts = value;
  }-*/;
  
  private static native java.util.HashMap getAmountsByExperimentalCondition(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::amountsByExperimentalCondition;
  }-*/;
  
  private static native void setAmountsByExperimentalCondition(edu.scripps.yates.shared.model.ProteinBean instance, java.util.HashMap value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::amountsByExperimentalCondition = value;
  }-*/;
  
  private static native java.util.HashMap getAmountsByMSRunID(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::amountsByMSRunID;
  }-*/;
  
  private static native void setAmountsByMSRunID(edu.scripps.yates.shared.model.ProteinBean instance, java.util.HashMap value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::amountsByMSRunID = value;
  }-*/;
  
  private static native java.util.Set getAnnotations(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::annotations;
  }-*/;
  
  private static native void setAnnotations(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::annotations = value;
  }-*/;
  
  private static native java.util.Set getConditions(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::conditions;
  }-*/;
  
  private static native void setConditions(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::conditions = value;
  }-*/;
  
  private static native java.lang.Double getCoverage(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::coverage;
  }-*/;
  
  private static native void setCoverage(edu.scripps.yates.shared.model.ProteinBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::coverage = value;
  }-*/;
  
  private static native char[] getCoverageArrayString(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::coverageArrayString;
  }-*/;
  
  private static native void setCoverageArrayString(edu.scripps.yates.shared.model.ProteinBean instance, char[] value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::coverageArrayString = value;
  }-*/;
  
  private static native java.util.Set getDbIds(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::dbIds;
  }-*/;
  
  private static native void setDbIds(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::dbIds = value;
  }-*/;
  
  private static native java.lang.String getDescriptionString(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::descriptionString;
  }-*/;
  
  private static native void setDescriptionString(edu.scripps.yates.shared.model.ProteinBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::descriptionString = value;
  }-*/;
  
  private static native java.util.Set getDifferentSequences(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::differentSequences;
  }-*/;
  
  private static native void setDifferentSequences(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::differentSequences = value;
  }-*/;
  
  private static native java.lang.String getEnsemblID(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::ensemblID;
  }-*/;
  
  private static native void setEnsemblID(edu.scripps.yates.shared.model.ProteinBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::ensemblID = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ProteinEvidence getEvidence(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::evidence;
  }-*/;
  
  private static native void setEvidence(edu.scripps.yates.shared.model.ProteinBean instance, edu.scripps.yates.shared.model.ProteinEvidence value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::evidence = value;
  }-*/;
  
  private static native java.util.Set getFunctions(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::functions;
  }-*/;
  
  private static native void setFunctions(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::functions = value;
  }-*/;
  
  private static native java.lang.String getGeneString(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::geneString;
  }-*/;
  
  private static native void setGeneString(edu.scripps.yates.shared.model.ProteinBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::geneString = value;
  }-*/;
  
  private static native java.util.Set getGenes(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::genes;
  }-*/;
  
  private static native void setGenes(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::genes = value;
  }-*/;
  
  private static native int getLength(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::length;
  }-*/;
  
  private static native void setLength(edu.scripps.yates.shared.model.ProteinBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::length = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ProteinBean getLightVersion(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::lightVersion;
  }-*/;
  
  private static native void setLightVersion(edu.scripps.yates.shared.model.ProteinBean instance, edu.scripps.yates.shared.model.ProteinBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::lightVersion = value;
  }-*/;
  
  private static native java.util.Set getMsruns(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::msruns;
  }-*/;
  
  private static native void setMsruns(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::msruns = value;
  }-*/;
  
  private static native double getMw(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::mw;
  }-*/;
  
  private static native void setMw(edu.scripps.yates.shared.model.ProteinBean instance, double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::mw = value;
  }-*/;
  
  private static native int getNumPSMs(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::numPSMs;
  }-*/;
  
  private static native void setNumPSMs(edu.scripps.yates.shared.model.ProteinBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::numPSMs = value;
  }-*/;
  
  private static native java.util.Map getNumPSMsByCondition(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::numPSMsByCondition;
  }-*/;
  
  private static native void setNumPSMsByCondition(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::numPSMsByCondition = value;
  }-*/;
  
  private static native int getNumPeptides(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::numPeptides;
  }-*/;
  
  private static native void setNumPeptides(edu.scripps.yates.shared.model.ProteinBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::numPeptides = value;
  }-*/;
  
  private static native java.util.Map getOmimEntries(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::omimEntries;
  }-*/;
  
  private static native void setOmimEntries(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::omimEntries = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.OrganismBean getOrganism(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::organism;
  }-*/;
  
  private static native void setOrganism(edu.scripps.yates.shared.model.ProteinBean instance, edu.scripps.yates.shared.model.OrganismBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::organism = value;
  }-*/;
  
  private static native java.util.Set getPeptideDBIds(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::peptideDBIds;
  }-*/;
  
  private static native void setPeptideDBIds(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::peptideDBIds = value;
  }-*/;
  
  private static native java.util.List getPeptides(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::peptides;
  }-*/;
  
  private static native void setPeptides(edu.scripps.yates.shared.model.ProteinBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::peptides = value;
  }-*/;
  
  private static native double getPi(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::pi;
  }-*/;
  
  private static native void setPi(edu.scripps.yates.shared.model.ProteinBean instance, double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::pi = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.AccessionBean getPrimaryAccession(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::primaryAccession;
  }-*/;
  
  private static native void setPrimaryAccession(edu.scripps.yates.shared.model.ProteinBean instance, edu.scripps.yates.shared.model.AccessionBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::primaryAccession = value;
  }-*/;
  
  private static native int getProteinBeanUniqueIdentifier(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::proteinBeanUniqueIdentifier;
  }-*/;
  
  private static native void setProteinBeanUniqueIdentifier(edu.scripps.yates.shared.model.ProteinBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::proteinBeanUniqueIdentifier = value;
  }-*/;
  
  private static native java.util.Set getPsmIds(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::psmIds;
  }-*/;
  
  private static native void setPsmIds(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::psmIds = value;
  }-*/;
  
  private static native java.util.Map getPsmIdsByCondition(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::psmIdsByCondition;
  }-*/;
  
  private static native void setPsmIdsByCondition(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::psmIdsByCondition = value;
  }-*/;
  
  private static native java.util.Map getPsmIdsbyMSRun(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::psmIdsbyMSRun;
  }-*/;
  
  private static native void setPsmIdsbyMSRun(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::psmIdsbyMSRun = value;
  }-*/;
  
  private static native java.util.List getPsms(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::psms;
  }-*/;
  
  private static native void setPsms(edu.scripps.yates.shared.model.ProteinBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::psms = value;
  }-*/;
  
  private static native java.util.Map getRatioDistributions(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::ratioDistributions;
  }-*/;
  
  private static native void setRatioDistributions(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::ratioDistributions = value;
  }-*/;
  
  private static native java.util.Set getRatios(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::ratios;
  }-*/;
  
  private static native void setRatios(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::ratios = value;
  }-*/;
  
  private static native java.util.HashMap getRatiosByExperimentalcondition(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::ratiosByExperimentalcondition;
  }-*/;
  
  private static native void setRatiosByExperimentalcondition(edu.scripps.yates.shared.model.ProteinBean instance, java.util.HashMap value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::ratiosByExperimentalcondition = value;
  }-*/;
  
  private static native java.util.List getReactomePathways(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::reactomePathways;
  }-*/;
  
  private static native void setReactomePathways(edu.scripps.yates.shared.model.ProteinBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::reactomePathways = value;
  }-*/;
  
  private static native java.util.Map getScores(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::scores;
  }-*/;
  
  private static native void setScores(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::scores = value;
  }-*/;
  
  private static native java.util.Set getSecondaryAccessions(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::secondaryAccessions;
  }-*/;
  
  private static native void setSecondaryAccessions(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::secondaryAccessions = value;
  }-*/;
  
  private static native java.util.Set getThresholds(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::thresholds;
  }-*/;
  
  private static native void setThresholds(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::thresholds = value;
  }-*/;
  
  private static native java.util.Map getUniprotFeatures(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::uniprotFeatures;
  }-*/;
  
  private static native void setUniprotFeatures(edu.scripps.yates.shared.model.ProteinBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::uniprotFeatures = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.UniprotProteinExistence getUniprotProteinExistence(edu.scripps.yates.shared.model.ProteinBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinBean::uniprotProteinExistence;
  }-*/;
  
  private static native void setUniprotProteinExistence(edu.scripps.yates.shared.model.ProteinBean instance, edu.scripps.yates.shared.model.UniprotProteinExistence value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinBean::uniprotProteinExistence = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ProteinBean instance) throws SerializationException {
    setAlternativeNamesString(instance, streamReader.readString());
    setAmounts(instance, (java.util.Set) streamReader.readObject());
    setAmountsByExperimentalCondition(instance, (java.util.HashMap) streamReader.readObject());
    setAmountsByMSRunID(instance, (java.util.HashMap) streamReader.readObject());
    setAnnotations(instance, (java.util.Set) streamReader.readObject());
    setConditions(instance, (java.util.Set) streamReader.readObject());
    setCoverage(instance, (java.lang.Double) streamReader.readObject());
    setCoverageArrayString(instance, (char[]) streamReader.readObject());
    setDbIds(instance, (java.util.Set) streamReader.readObject());
    setDescriptionString(instance, streamReader.readString());
    setDifferentSequences(instance, (java.util.Set) streamReader.readObject());
    setEnsemblID(instance, streamReader.readString());
    setEvidence(instance, (edu.scripps.yates.shared.model.ProteinEvidence) streamReader.readObject());
    setFunctions(instance, (java.util.Set) streamReader.readObject());
    setGeneString(instance, streamReader.readString());
    setGenes(instance, (java.util.Set) streamReader.readObject());
    setLength(instance, streamReader.readInt());
    setLightVersion(instance, (edu.scripps.yates.shared.model.ProteinBean) streamReader.readObject());
    setMsruns(instance, (java.util.Set) streamReader.readObject());
    setMw(instance, streamReader.readDouble());
    setNumPSMs(instance, streamReader.readInt());
    setNumPSMsByCondition(instance, (java.util.Map) streamReader.readObject());
    setNumPeptides(instance, streamReader.readInt());
    setOmimEntries(instance, (java.util.Map) streamReader.readObject());
    setOrganism(instance, (edu.scripps.yates.shared.model.OrganismBean) streamReader.readObject());
    setPeptideDBIds(instance, (java.util.Set) streamReader.readObject());
    setPeptides(instance, (java.util.List) streamReader.readObject());
    setPi(instance, streamReader.readDouble());
    setPrimaryAccession(instance, (edu.scripps.yates.shared.model.AccessionBean) streamReader.readObject());
    setProteinBeanUniqueIdentifier(instance, streamReader.readInt());
    setPsmIds(instance, (java.util.Set) streamReader.readObject());
    setPsmIdsByCondition(instance, (java.util.Map) streamReader.readObject());
    setPsmIdsbyMSRun(instance, (java.util.Map) streamReader.readObject());
    setPsms(instance, (java.util.List) streamReader.readObject());
    setRatioDistributions(instance, (java.util.Map) streamReader.readObject());
    setRatios(instance, (java.util.Set) streamReader.readObject());
    setRatiosByExperimentalcondition(instance, (java.util.HashMap) streamReader.readObject());
    setReactomePathways(instance, (java.util.List) streamReader.readObject());
    setScores(instance, (java.util.Map) streamReader.readObject());
    setSecondaryAccessions(instance, (java.util.Set) streamReader.readObject());
    setThresholds(instance, (java.util.Set) streamReader.readObject());
    setUniprotFeatures(instance, (java.util.Map) streamReader.readObject());
    setUniprotProteinExistence(instance, (edu.scripps.yates.shared.model.UniprotProteinExistence) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.model.ProteinBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ProteinBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ProteinBean instance) throws SerializationException {
    streamWriter.writeString(getAlternativeNamesString(instance));
    streamWriter.writeObject(getAmounts(instance));
    streamWriter.writeObject(getAmountsByExperimentalCondition(instance));
    streamWriter.writeObject(getAmountsByMSRunID(instance));
    streamWriter.writeObject(getAnnotations(instance));
    streamWriter.writeObject(getConditions(instance));
    streamWriter.writeObject(getCoverage(instance));
    streamWriter.writeObject(getCoverageArrayString(instance));
    streamWriter.writeObject(getDbIds(instance));
    streamWriter.writeString(getDescriptionString(instance));
    streamWriter.writeObject(getDifferentSequences(instance));
    streamWriter.writeString(getEnsemblID(instance));
    streamWriter.writeObject(getEvidence(instance));
    streamWriter.writeObject(getFunctions(instance));
    streamWriter.writeString(getGeneString(instance));
    streamWriter.writeObject(getGenes(instance));
    streamWriter.writeInt(getLength(instance));
    streamWriter.writeObject(getLightVersion(instance));
    streamWriter.writeObject(getMsruns(instance));
    streamWriter.writeDouble(getMw(instance));
    streamWriter.writeInt(getNumPSMs(instance));
    streamWriter.writeObject(getNumPSMsByCondition(instance));
    streamWriter.writeInt(getNumPeptides(instance));
    streamWriter.writeObject(getOmimEntries(instance));
    streamWriter.writeObject(getOrganism(instance));
    streamWriter.writeObject(getPeptideDBIds(instance));
    streamWriter.writeObject(getPeptides(instance));
    streamWriter.writeDouble(getPi(instance));
    streamWriter.writeObject(getPrimaryAccession(instance));
    streamWriter.writeInt(getProteinBeanUniqueIdentifier(instance));
    streamWriter.writeObject(getPsmIds(instance));
    streamWriter.writeObject(getPsmIdsByCondition(instance));
    streamWriter.writeObject(getPsmIdsbyMSRun(instance));
    streamWriter.writeObject(getPsms(instance));
    streamWriter.writeObject(getRatioDistributions(instance));
    streamWriter.writeObject(getRatios(instance));
    streamWriter.writeObject(getRatiosByExperimentalcondition(instance));
    streamWriter.writeObject(getReactomePathways(instance));
    streamWriter.writeObject(getScores(instance));
    streamWriter.writeObject(getSecondaryAccessions(instance));
    streamWriter.writeObject(getThresholds(instance));
    streamWriter.writeObject(getUniprotFeatures(instance));
    streamWriter.writeObject(getUniprotProteinExistence(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ProteinBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ProteinBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ProteinBean)object);
  }
  
}
