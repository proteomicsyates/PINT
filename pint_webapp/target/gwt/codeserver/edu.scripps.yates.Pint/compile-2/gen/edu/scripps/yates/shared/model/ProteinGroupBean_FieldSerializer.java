package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProteinGroupBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.util.Set getAmounts(edu.scripps.yates.shared.model.ProteinGroupBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinGroupBean::amounts;
  }-*/;
  
  private static native void setAmounts(edu.scripps.yates.shared.model.ProteinGroupBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinGroupBean::amounts = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ProteinGroupBean getLightVersion(edu.scripps.yates.shared.model.ProteinGroupBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinGroupBean::lightVersion;
  }-*/;
  
  private static native void setLightVersion(edu.scripps.yates.shared.model.ProteinGroupBean instance, edu.scripps.yates.shared.model.ProteinGroupBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinGroupBean::lightVersion = value;
  }-*/;
  
  private static native int getNumPSMs(edu.scripps.yates.shared.model.ProteinGroupBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinGroupBean::numPSMs;
  }-*/;
  
  private static native void setNumPSMs(edu.scripps.yates.shared.model.ProteinGroupBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinGroupBean::numPSMs = value;
  }-*/;
  
  private static native java.util.Map getNumPSMsByCondition(edu.scripps.yates.shared.model.ProteinGroupBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinGroupBean::numPSMsByCondition;
  }-*/;
  
  private static native void setNumPSMsByCondition(edu.scripps.yates.shared.model.ProteinGroupBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinGroupBean::numPSMsByCondition = value;
  }-*/;
  
  private static native int getNumPeptides(edu.scripps.yates.shared.model.ProteinGroupBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinGroupBean::numPeptides;
  }-*/;
  
  private static native void setNumPeptides(edu.scripps.yates.shared.model.ProteinGroupBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinGroupBean::numPeptides = value;
  }-*/;
  
  private static native java.util.List getPeptides(edu.scripps.yates.shared.model.ProteinGroupBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinGroupBean::peptides;
  }-*/;
  
  private static native void setPeptides(edu.scripps.yates.shared.model.ProteinGroupBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinGroupBean::peptides = value;
  }-*/;
  
  private static native java.util.Set getPsmDBIds(edu.scripps.yates.shared.model.ProteinGroupBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinGroupBean::psmDBIds;
  }-*/;
  
  private static native void setPsmDBIds(edu.scripps.yates.shared.model.ProteinGroupBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinGroupBean::psmDBIds = value;
  }-*/;
  
  private static native java.util.Map getRatioDistributions(edu.scripps.yates.shared.model.ProteinGroupBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinGroupBean::ratioDistributions;
  }-*/;
  
  private static native void setRatioDistributions(edu.scripps.yates.shared.model.ProteinGroupBean instance, java.util.Map value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinGroupBean::ratioDistributions = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ProteinGroupBean instance) throws SerializationException {
    setAmounts(instance, (java.util.Set) streamReader.readObject());
    setLightVersion(instance, (edu.scripps.yates.shared.model.ProteinGroupBean) streamReader.readObject());
    setNumPSMs(instance, streamReader.readInt());
    setNumPSMsByCondition(instance, (java.util.Map) streamReader.readObject());
    setNumPeptides(instance, streamReader.readInt());
    setPeptides(instance, (java.util.List) streamReader.readObject());
    setPsmDBIds(instance, (java.util.Set) streamReader.readObject());
    setRatioDistributions(instance, (java.util.Map) streamReader.readObject());
    
    com.google.gwt.user.client.rpc.core.java.util.ArrayList_CustomFieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.scripps.yates.shared.model.ProteinGroupBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ProteinGroupBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ProteinGroupBean instance) throws SerializationException {
    streamWriter.writeObject(getAmounts(instance));
    streamWriter.writeObject(getLightVersion(instance));
    streamWriter.writeInt(getNumPSMs(instance));
    streamWriter.writeObject(getNumPSMsByCondition(instance));
    streamWriter.writeInt(getNumPeptides(instance));
    streamWriter.writeObject(getPeptides(instance));
    streamWriter.writeObject(getPsmDBIds(instance));
    streamWriter.writeObject(getRatioDistributions(instance));
    
    com.google.gwt.user.client.rpc.core.java.util.ArrayList_CustomFieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ProteinGroupBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinGroupBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ProteinGroupBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinGroupBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ProteinGroupBean)object);
  }
  
}
