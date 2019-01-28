package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class LabelBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.Double getMassDiff(edu.scripps.yates.shared.model.LabelBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.LabelBean::massDiff;
  }-*/;
  
  private static native void setMassDiff(edu.scripps.yates.shared.model.LabelBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.LabelBean::massDiff = value;
  }-*/;
  
  private static native java.lang.String getName(edu.scripps.yates.shared.model.LabelBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.LabelBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.LabelBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.LabelBean::name = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.LabelBean instance) throws SerializationException {
    setMassDiff(instance, (java.lang.Double) streamReader.readObject());
    setName(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.LabelBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.LabelBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.LabelBean instance) throws SerializationException {
    streamWriter.writeObject(getMassDiff(instance));
    streamWriter.writeString(getName(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.LabelBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.LabelBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.LabelBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.LabelBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.LabelBean)object);
  }
  
}
