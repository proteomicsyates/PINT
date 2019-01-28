package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ThresholdBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.ThresholdBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ThresholdBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.ThresholdBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ThresholdBean::description = value;
  }-*/;
  
  private static native java.lang.String getName(edu.scripps.yates.shared.model.ThresholdBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ThresholdBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.ThresholdBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ThresholdBean::name = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ThresholdBean instance) throws SerializationException {
    setDescription(instance, streamReader.readString());
    setName(instance, streamReader.readString());
    instance.pass = streamReader.readBoolean();
    
  }
  
  public static edu.scripps.yates.shared.model.ThresholdBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ThresholdBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ThresholdBean instance) throws SerializationException {
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeString(getName(instance));
    streamWriter.writeBoolean(instance.pass);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ThresholdBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ThresholdBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ThresholdBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ThresholdBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ThresholdBean)object);
  }
  
}
