package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PintImportCfgBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean instance) throws SerializationException {
    
    edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean instance) throws SerializationException {
    
    edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean)object);
  }
  
}
