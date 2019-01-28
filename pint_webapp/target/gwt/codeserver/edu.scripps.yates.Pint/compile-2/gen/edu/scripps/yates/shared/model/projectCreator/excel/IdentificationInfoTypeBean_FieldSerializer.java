package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class IdentificationInfoTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean instance) throws SerializationException {
    instance.excelIdentInfo = (java.util.List) streamReader.readObject();
    instance.remoteFilesIdentInfo = (java.util.List) streamReader.readObject();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean instance) throws SerializationException {
    streamWriter.writeObject(instance.excelIdentInfo);
    streamWriter.writeObject(instance.remoteFilesIdentInfo);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean)object);
  }
  
}
