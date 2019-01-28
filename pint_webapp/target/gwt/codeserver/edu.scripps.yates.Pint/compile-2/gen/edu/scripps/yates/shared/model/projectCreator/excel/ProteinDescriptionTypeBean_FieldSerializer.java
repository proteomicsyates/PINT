package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProteinDescriptionTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean instance) throws SerializationException {
    instance.columnRef = streamReader.readString();
    instance.groupSeparator = streamReader.readString();
    instance.groups = streamReader.readBoolean();
    instance.regexp = streamReader.readString();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean instance) throws SerializationException {
    streamWriter.writeString(instance.columnRef);
    streamWriter.writeString(instance.groupSeparator);
    streamWriter.writeBoolean(instance.groups);
    streamWriter.writeString(instance.regexp);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean)object);
  }
  
}
