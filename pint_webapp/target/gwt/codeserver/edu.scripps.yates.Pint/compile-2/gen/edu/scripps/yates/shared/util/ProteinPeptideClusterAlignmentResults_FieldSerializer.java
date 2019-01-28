package edu.scripps.yates.shared.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProteinPeptideClusterAlignmentResults_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.model.ProteinPeptideCluster getCluster(edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults::cluster;
  }-*/;
  
  private static native void setCluster(edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults instance, edu.scripps.yates.shared.model.ProteinPeptideCluster value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults::cluster = value;
  }-*/;
  
  private static native java.util.Map getResultsByPeptideSequence(edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults::resultsByPeptideSequence;
  }-*/;
  
  private static native void setResultsByPeptideSequence(edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults::resultsByPeptideSequence = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults instance) throws SerializationException {
    setCluster(instance, (edu.scripps.yates.shared.model.ProteinPeptideCluster) streamReader.readObject());
    setResultsByPeptideSequence(instance, (java.util.Map) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults instance) throws SerializationException {
    streamWriter.writeObject(getCluster(instance));
    streamWriter.writeObject(getResultsByPeptideSequence(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults)object);
  }
  
}
