package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PintImportCfgTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean instance) throws SerializationException {
    instance.fileSet = (edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean) streamReader.readObject();
    instance.importID = streamReader.readInt();
    instance.project = (edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean) streamReader.readObject();
    instance.servers = (edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean) streamReader.readObject();
    instance.version = streamReader.readString();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean instance) throws SerializationException {
    streamWriter.writeObject(instance.fileSet);
    streamWriter.writeInt(instance.importID);
    streamWriter.writeObject(instance.project);
    streamWriter.writeObject(instance.servers);
    streamWriter.writeString(instance.version);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean)object);
  }
  
}
