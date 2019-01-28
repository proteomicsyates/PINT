package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class MsRunTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean instance) throws SerializationException {
    instance.date = (java.util.Date) streamReader.readObject();
    instance.enzymeNocutResidues = streamReader.readString();
    instance.enzymeResidues = streamReader.readString();
    instance.fastaFileRef = streamReader.readString();
    instance.id = streamReader.readString();
    instance.path = streamReader.readString();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean instance) throws SerializationException {
    streamWriter.writeObject(instance.date);
    streamWriter.writeString(instance.enzymeNocutResidues);
    streamWriter.writeString(instance.enzymeResidues);
    streamWriter.writeString(instance.fastaFileRef);
    streamWriter.writeString(instance.id);
    streamWriter.writeString(instance.path);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean)object);
  }
  
}
