package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PsmTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getColumnRef(edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean::columnRef;
  }-*/;
  
  private static native void setColumnRef(edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean::columnRef = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean instance) throws SerializationException {
    setColumnRef(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean instance) throws SerializationException {
    streamWriter.writeString(getColumnRef(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean)object);
  }
  
}
