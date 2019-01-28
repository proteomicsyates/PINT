package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class FileTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean instance) throws SerializationException {
    instance.fastaDigestion = (edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean) streamReader.readObject();
    instance.format = (edu.scripps.yates.shared.model.FileFormat) streamReader.readObject();
    instance.id = streamReader.readString();
    instance.name = streamReader.readString();
    instance.relativePath = streamReader.readString();
    instance.serverRef = streamReader.readString();
    instance.sheets = (edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean) streamReader.readObject();
    instance.url = streamReader.readString();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean instance) throws SerializationException {
    streamWriter.writeObject(instance.fastaDigestion);
    streamWriter.writeObject(instance.format);
    streamWriter.writeString(instance.id);
    streamWriter.writeString(instance.name);
    streamWriter.writeString(instance.relativePath);
    streamWriter.writeString(instance.serverRef);
    streamWriter.writeObject(instance.sheets);
    streamWriter.writeString(instance.url);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean)object);
  }
  
}
