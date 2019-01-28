package edu.scripps.yates.shared.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class FileDescriptor_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getName(edu.scripps.yates.shared.util.FileDescriptor instance) /*-{
    return instance.@edu.scripps.yates.shared.util.FileDescriptor::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.util.FileDescriptor instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.FileDescriptor::name = value;
  }-*/;
  
  private static native java.lang.String getSize(edu.scripps.yates.shared.util.FileDescriptor instance) /*-{
    return instance.@edu.scripps.yates.shared.util.FileDescriptor::size;
  }-*/;
  
  private static native void setSize(edu.scripps.yates.shared.util.FileDescriptor instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.FileDescriptor::size = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.FileDescriptor instance) throws SerializationException {
    setName(instance, streamReader.readString());
    setSize(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.util.FileDescriptor instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.util.FileDescriptor();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.FileDescriptor instance) throws SerializationException {
    streamWriter.writeString(getName(instance));
    streamWriter.writeString(getSize(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.util.FileDescriptor_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.FileDescriptor_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.util.FileDescriptor)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.FileDescriptor_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.util.FileDescriptor)object);
  }
  
}
