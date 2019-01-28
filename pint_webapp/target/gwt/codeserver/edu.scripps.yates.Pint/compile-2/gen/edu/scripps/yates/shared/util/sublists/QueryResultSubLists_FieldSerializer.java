package edu.scripps.yates.shared.util.sublists;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class QueryResultSubLists_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native int getNumDifferentSequences(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::numDifferentSequences;
  }-*/;
  
  private static native void setNumDifferentSequences(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::numDifferentSequences = value;
  }-*/;
  
  private static native int getNumDifferentSequencesDistinguishingModifieds(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::numDifferentSequencesDistinguishingModifieds;
  }-*/;
  
  private static native void setNumDifferentSequencesDistinguishingModifieds(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::numDifferentSequencesDistinguishingModifieds = value;
  }-*/;
  
  private static native java.util.List getPeptideScores(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::peptideScores;
  }-*/;
  
  private static native void setPeptideScores(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::peptideScores = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.sublists.PeptideBeanSubList getPeptideSubList(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::peptideSubList;
  }-*/;
  
  private static native void setPeptideSubList(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, edu.scripps.yates.shared.util.sublists.PeptideBeanSubList value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::peptideSubList = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList getProteinGroupSubList(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::proteinGroupSubList;
  }-*/;
  
  private static native void setProteinGroupSubList(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::proteinGroupSubList = value;
  }-*/;
  
  private static native java.util.List getProteinScores(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::proteinScores;
  }-*/;
  
  private static native void setProteinScores(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::proteinScores = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.sublists.ProteinBeanSubList getProteinSubList(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::proteinSubList;
  }-*/;
  
  private static native void setProteinSubList(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, edu.scripps.yates.shared.util.sublists.ProteinBeanSubList value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::proteinSubList = value;
  }-*/;
  
  private static native java.util.List getPsmScores(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::psmScores;
  }-*/;
  
  private static native void setPsmScores(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::psmScores = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.sublists.PsmBeanSubList getPsmSubList(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::psmSubList;
  }-*/;
  
  private static native void setPsmSubList(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, edu.scripps.yates.shared.util.sublists.PsmBeanSubList value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::psmSubList = value;
  }-*/;
  
  private static native java.util.List getPtmScores(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::ptmScores;
  }-*/;
  
  private static native void setPtmScores(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::ptmScores = value;
  }-*/;
  
  private static native java.util.List getScoreTypes(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::scoreTypes;
  }-*/;
  
  private static native void setScoreTypes(edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::scoreTypes = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) throws SerializationException {
    setNumDifferentSequences(instance, streamReader.readInt());
    setNumDifferentSequencesDistinguishingModifieds(instance, streamReader.readInt());
    setPeptideScores(instance, (java.util.List) streamReader.readObject());
    setPeptideSubList(instance, (edu.scripps.yates.shared.util.sublists.PeptideBeanSubList) streamReader.readObject());
    setProteinGroupSubList(instance, (edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList) streamReader.readObject());
    setProteinScores(instance, (java.util.List) streamReader.readObject());
    setProteinSubList(instance, (edu.scripps.yates.shared.util.sublists.ProteinBeanSubList) streamReader.readObject());
    setPsmScores(instance, (java.util.List) streamReader.readObject());
    setPsmSubList(instance, (edu.scripps.yates.shared.util.sublists.PsmBeanSubList) streamReader.readObject());
    setPtmScores(instance, (java.util.List) streamReader.readObject());
    setScoreTypes(instance, (java.util.List) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.util.sublists.QueryResultSubLists instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.util.sublists.QueryResultSubLists();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.sublists.QueryResultSubLists instance) throws SerializationException {
    streamWriter.writeInt(getNumDifferentSequences(instance));
    streamWriter.writeInt(getNumDifferentSequencesDistinguishingModifieds(instance));
    streamWriter.writeObject(getPeptideScores(instance));
    streamWriter.writeObject(getPeptideSubList(instance));
    streamWriter.writeObject(getProteinGroupSubList(instance));
    streamWriter.writeObject(getProteinScores(instance));
    streamWriter.writeObject(getProteinSubList(instance));
    streamWriter.writeObject(getPsmScores(instance));
    streamWriter.writeObject(getPsmSubList(instance));
    streamWriter.writeObject(getPtmScores(instance));
    streamWriter.writeObject(getScoreTypes(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.util.sublists.QueryResultSubLists_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.sublists.QueryResultSubLists_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.util.sublists.QueryResultSubLists)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.sublists.QueryResultSubLists_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.util.sublists.QueryResultSubLists)object);
  }
  
}
