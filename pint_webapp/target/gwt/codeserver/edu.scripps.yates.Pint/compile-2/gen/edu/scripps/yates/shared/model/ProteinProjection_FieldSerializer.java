package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProteinProjection_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getAcc(edu.scripps.yates.shared.model.ProteinProjection instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinProjection::acc;
  }-*/;
  
  private static native void setAcc(edu.scripps.yates.shared.model.ProteinProjection instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinProjection::acc = value;
  }-*/;
  
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.ProteinProjection instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinProjection::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.ProteinProjection instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinProjection::description = value;
  }-*/;
  
  private static native java.lang.String getGene(edu.scripps.yates.shared.model.ProteinProjection instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinProjection::gene;
  }-*/;
  
  private static native void setGene(edu.scripps.yates.shared.model.ProteinProjection instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinProjection::gene = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ProteinProjection instance) throws SerializationException {
    setAcc(instance, streamReader.readString());
    setDescription(instance, streamReader.readString());
    setGene(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.ProteinProjection instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ProteinProjection();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ProteinProjection instance) throws SerializationException {
    streamWriter.writeString(getAcc(instance));
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeString(getGene(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ProteinProjection_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinProjection_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ProteinProjection)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinProjection_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ProteinProjection)object);
  }
  
}
