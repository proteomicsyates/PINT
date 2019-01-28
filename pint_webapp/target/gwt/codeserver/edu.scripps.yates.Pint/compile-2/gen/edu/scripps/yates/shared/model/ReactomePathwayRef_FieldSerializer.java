package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ReactomePathwayRef_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.ReactomePathwayRef instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ReactomePathwayRef::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.ReactomePathwayRef instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ReactomePathwayRef::description = value;
  }-*/;
  
  private static native java.lang.String getId(edu.scripps.yates.shared.model.ReactomePathwayRef instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ReactomePathwayRef::id;
  }-*/;
  
  private static native void setId(edu.scripps.yates.shared.model.ReactomePathwayRef instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ReactomePathwayRef::id = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ReactomePathwayRef instance) throws SerializationException {
    setDescription(instance, streamReader.readString());
    setId(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.ReactomePathwayRef instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ReactomePathwayRef();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ReactomePathwayRef instance) throws SerializationException {
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeString(getId(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ReactomePathwayRef_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ReactomePathwayRef_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ReactomePathwayRef)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ReactomePathwayRef_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ReactomePathwayRef)object);
  }
  
}
