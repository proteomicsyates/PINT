package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProteinPeptideCluster_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults getAligmentResults(edu.scripps.yates.shared.model.ProteinPeptideCluster instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::aligmentResults;
  }-*/;
  
  private static native void setAligmentResults(edu.scripps.yates.shared.model.ProteinPeptideCluster instance, edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::aligmentResults = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.interfaces.ContainsPeptides getPeptideProvider(edu.scripps.yates.shared.model.ProteinPeptideCluster instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::peptideProvider;
  }-*/;
  
  private static native void setPeptideProvider(edu.scripps.yates.shared.model.ProteinPeptideCluster instance, edu.scripps.yates.shared.model.interfaces.ContainsPeptides value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::peptideProvider = value;
  }-*/;
  
  private static native java.util.Set getPeptideUniqueIdentifiers(edu.scripps.yates.shared.model.ProteinPeptideCluster instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::peptideUniqueIdentifiers;
  }-*/;
  
  private static native void setPeptideUniqueIdentifiers(edu.scripps.yates.shared.model.ProteinPeptideCluster instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::peptideUniqueIdentifiers = value;
  }-*/;
  
  private static native java.util.Set getPeptides(edu.scripps.yates.shared.model.ProteinPeptideCluster instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::peptides;
  }-*/;
  
  private static native void setPeptides(edu.scripps.yates.shared.model.ProteinPeptideCluster instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::peptides = value;
  }-*/;
  
  private static native java.util.Map getProteinMap(edu.scripps.yates.shared.model.ProteinPeptideCluster instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::proteinMap;
  }-*/;
  
  private static native void setProteinMap(edu.scripps.yates.shared.model.ProteinPeptideCluster instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::proteinMap = value;
  }-*/;
  
  private static native java.util.Set getProteinSet(edu.scripps.yates.shared.model.ProteinPeptideCluster instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::proteinSet;
  }-*/;
  
  private static native void setProteinSet(edu.scripps.yates.shared.model.ProteinPeptideCluster instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::proteinSet = value;
  }-*/;
  
  private static native java.util.Set getProteinUniqueIdentifier(edu.scripps.yates.shared.model.ProteinPeptideCluster instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::proteinUniqueIdentifier;
  }-*/;
  
  private static native void setProteinUniqueIdentifier(edu.scripps.yates.shared.model.ProteinPeptideCluster instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinPeptideCluster::proteinUniqueIdentifier = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ProteinPeptideCluster instance) throws SerializationException {
    setAligmentResults(instance, (edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults) streamReader.readObject());
    setPeptideProvider(instance, (edu.scripps.yates.shared.model.interfaces.ContainsPeptides) streamReader.readObject());
    setPeptideUniqueIdentifiers(instance, (java.util.Set) streamReader.readObject());
    setPeptides(instance, (java.util.Set) streamReader.readObject());
    setProteinMap(instance, (java.util.Map) streamReader.readObject());
    setProteinSet(instance, (java.util.Set) streamReader.readObject());
    setProteinUniqueIdentifier(instance, (java.util.Set) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.model.ProteinPeptideCluster instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ProteinPeptideCluster();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ProteinPeptideCluster instance) throws SerializationException {
    streamWriter.writeObject(getAligmentResults(instance));
    streamWriter.writeObject(getPeptideProvider(instance));
    streamWriter.writeObject(getPeptideUniqueIdentifiers(instance));
    streamWriter.writeObject(getPeptides(instance));
    streamWriter.writeObject(getProteinMap(instance));
    streamWriter.writeObject(getProteinSet(instance));
    streamWriter.writeObject(getProteinUniqueIdentifier(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ProteinPeptideCluster_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinPeptideCluster_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ProteinPeptideCluster)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinPeptideCluster_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ProteinPeptideCluster)object);
  }
  
}
