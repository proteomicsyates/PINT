package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProteinAnnotationBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getName(edu.scripps.yates.shared.model.ProteinAnnotationBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinAnnotationBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.ProteinAnnotationBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinAnnotationBean::name = value;
  }-*/;
  
  private static native java.lang.String getSource(edu.scripps.yates.shared.model.ProteinAnnotationBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinAnnotationBean::source;
  }-*/;
  
  private static native void setSource(edu.scripps.yates.shared.model.ProteinAnnotationBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinAnnotationBean::source = value;
  }-*/;
  
  private static native java.lang.String getType(edu.scripps.yates.shared.model.ProteinAnnotationBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinAnnotationBean::type;
  }-*/;
  
  private static native void setType(edu.scripps.yates.shared.model.ProteinAnnotationBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinAnnotationBean::type = value;
  }-*/;
  
  private static native java.lang.String getValue(edu.scripps.yates.shared.model.ProteinAnnotationBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProteinAnnotationBean::value;
  }-*/;
  
  private static native void setValue(edu.scripps.yates.shared.model.ProteinAnnotationBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProteinAnnotationBean::value = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ProteinAnnotationBean instance) throws SerializationException {
    setName(instance, streamReader.readString());
    setSource(instance, streamReader.readString());
    setType(instance, streamReader.readString());
    setValue(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.ProteinAnnotationBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ProteinAnnotationBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ProteinAnnotationBean instance) throws SerializationException {
    streamWriter.writeString(getName(instance));
    streamWriter.writeString(getSource(instance));
    streamWriter.writeString(getType(instance));
    streamWriter.writeString(getValue(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ProteinAnnotationBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinAnnotationBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ProteinAnnotationBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinAnnotationBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ProteinAnnotationBean)object);
  }
  
}
