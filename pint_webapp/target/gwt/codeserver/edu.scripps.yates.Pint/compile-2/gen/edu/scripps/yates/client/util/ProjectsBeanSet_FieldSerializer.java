package edu.scripps.yates.client.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProjectsBeanSet_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.client.util.ProjectsBeanSet instance) throws SerializationException {
    
    com.google.gwt.user.client.rpc.core.java.util.HashSet_CustomFieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.scripps.yates.client.util.ProjectsBeanSet instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.client.util.ProjectsBeanSet();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.client.util.ProjectsBeanSet instance) throws SerializationException {
    
    com.google.gwt.user.client.rpc.core.java.util.HashSet_CustomFieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.client.util.ProjectsBeanSet_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.client.util.ProjectsBeanSet_FieldSerializer.deserialize(reader, (edu.scripps.yates.client.util.ProjectsBeanSet)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.client.util.ProjectsBeanSet_FieldSerializer.serialize(writer, (edu.scripps.yates.client.util.ProjectsBeanSet)object);
  }
  
}
