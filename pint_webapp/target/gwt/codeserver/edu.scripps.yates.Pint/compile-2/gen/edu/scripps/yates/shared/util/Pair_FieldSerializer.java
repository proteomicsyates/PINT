package edu.scripps.yates.shared.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class Pair_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.Object getFirstElement(edu.scripps.yates.shared.util.Pair instance) /*-{
    return instance.@edu.scripps.yates.shared.util.Pair::firstElement;
  }-*/;
  
  private static native void setFirstElement(edu.scripps.yates.shared.util.Pair instance, java.lang.Object value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.Pair::firstElement = value;
  }-*/;
  
  private static native java.lang.Object getSecondElement(edu.scripps.yates.shared.util.Pair instance) /*-{
    return instance.@edu.scripps.yates.shared.util.Pair::secondElement;
  }-*/;
  
  private static native void setSecondElement(edu.scripps.yates.shared.util.Pair instance, java.lang.Object value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.Pair::secondElement = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.Pair instance) throws SerializationException {
    setFirstElement(instance, streamReader.readObject());
    setSecondElement(instance, streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.util.Pair instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.util.Pair();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.Pair instance) throws SerializationException {
    streamWriter.writeObject(getFirstElement(instance));
    streamWriter.writeObject(getSecondElement(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.util.Pair_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.Pair_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.util.Pair)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.Pair_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.util.Pair)object);
  }
  
}
