package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class TissueBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.TissueBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.TissueBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.TissueBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.TissueBean::description = value;
  }-*/;
  
  private static native java.lang.String getTissueID(edu.scripps.yates.shared.model.TissueBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.TissueBean::tissueID;
  }-*/;
  
  private static native void setTissueID(edu.scripps.yates.shared.model.TissueBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.TissueBean::tissueID = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.TissueBean instance) throws SerializationException {
    setDescription(instance, streamReader.readString());
    setTissueID(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.TissueBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.TissueBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.TissueBean instance) throws SerializationException {
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeString(getTissueID(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.TissueBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.TissueBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.TissueBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.TissueBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.TissueBean)object);
  }
  
}
