package edu.scripps.yates.shared.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProjectNamedQuery_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native int getIndex(edu.scripps.yates.shared.util.ProjectNamedQuery instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProjectNamedQuery::index;
  }-*/;
  
  private static native void setIndex(edu.scripps.yates.shared.util.ProjectNamedQuery instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProjectNamedQuery::index = value;
  }-*/;
  
  private static native java.lang.String getName(edu.scripps.yates.shared.util.ProjectNamedQuery instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProjectNamedQuery::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.util.ProjectNamedQuery instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProjectNamedQuery::name = value;
  }-*/;
  
  private static native java.lang.String getQuery(edu.scripps.yates.shared.util.ProjectNamedQuery instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProjectNamedQuery::query;
  }-*/;
  
  private static native void setQuery(edu.scripps.yates.shared.util.ProjectNamedQuery instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProjectNamedQuery::query = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.ProjectNamedQuery instance) throws SerializationException {
    setIndex(instance, streamReader.readInt());
    setName(instance, streamReader.readString());
    setQuery(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.util.ProjectNamedQuery instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.util.ProjectNamedQuery();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.ProjectNamedQuery instance) throws SerializationException {
    streamWriter.writeInt(getIndex(instance));
    streamWriter.writeString(getName(instance));
    streamWriter.writeString(getQuery(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.util.ProjectNamedQuery_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.ProjectNamedQuery_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.util.ProjectNamedQuery)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.ProjectNamedQuery_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.util.ProjectNamedQuery)object);
  }
  
}
