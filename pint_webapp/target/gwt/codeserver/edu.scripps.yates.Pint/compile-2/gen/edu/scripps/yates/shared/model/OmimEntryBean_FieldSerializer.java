package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class OmimEntryBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.util.List getAlternativeTitles(edu.scripps.yates.shared.model.OmimEntryBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.OmimEntryBean::alternativeTitles;
  }-*/;
  
  private static native void setAlternativeTitles(edu.scripps.yates.shared.model.OmimEntryBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.OmimEntryBean::alternativeTitles = value;
  }-*/;
  
  private static native java.lang.Integer getId(edu.scripps.yates.shared.model.OmimEntryBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.OmimEntryBean::id;
  }-*/;
  
  private static native void setId(edu.scripps.yates.shared.model.OmimEntryBean instance, java.lang.Integer value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.OmimEntryBean::id = value;
  }-*/;
  
  private static native java.lang.String getPreferredTitle(edu.scripps.yates.shared.model.OmimEntryBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.OmimEntryBean::preferredTitle;
  }-*/;
  
  private static native void setPreferredTitle(edu.scripps.yates.shared.model.OmimEntryBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.OmimEntryBean::preferredTitle = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.OmimEntryBean instance) throws SerializationException {
    setAlternativeTitles(instance, (java.util.List) streamReader.readObject());
    setId(instance, (java.lang.Integer) streamReader.readObject());
    setPreferredTitle(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.OmimEntryBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.OmimEntryBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.OmimEntryBean instance) throws SerializationException {
    streamWriter.writeObject(getAlternativeTitles(instance));
    streamWriter.writeObject(getId(instance));
    streamWriter.writeString(getPreferredTitle(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.OmimEntryBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.OmimEntryBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.OmimEntryBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.OmimEntryBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.OmimEntryBean)object);
  }
  
}
