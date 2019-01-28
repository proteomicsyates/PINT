package edu.scripps.yates.shared.exceptions;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PintException_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE getPintErrorType(edu.scripps.yates.shared.exceptions.PintException instance) /*-{
    return instance.@edu.scripps.yates.shared.exceptions.PintException::pintErrorType;
  }-*/;
  
  private static native void setPintErrorType(edu.scripps.yates.shared.exceptions.PintException instance, edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE value) 
  /*-{
    instance.@edu.scripps.yates.shared.exceptions.PintException::pintErrorType = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.exceptions.PintException instance) throws SerializationException {
    setPintErrorType(instance, (edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE) streamReader.readObject());
    
    com.google.gwt.user.client.rpc.core.java.lang.Exception_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.scripps.yates.shared.exceptions.PintException instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.exceptions.PintException();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.exceptions.PintException instance) throws SerializationException {
    streamWriter.writeObject(getPintErrorType(instance));
    
    com.google.gwt.user.client.rpc.core.java.lang.Exception_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.exceptions.PintException_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.exceptions.PintException_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.exceptions.PintException)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.exceptions.PintException_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.exceptions.PintException)object);
  }
  
}
