package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ExperimentalConditionTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean instance) throws SerializationException {
    instance.description = streamReader.readString();
    instance.id = streamReader.readString();
    instance.identificationInfo = (edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean) streamReader.readObject();
    instance.quantificationInfo = (edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean) streamReader.readObject();
    instance.sampleRef = streamReader.readString();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean instance) throws SerializationException {
    streamWriter.writeString(instance.description);
    streamWriter.writeString(instance.id);
    streamWriter.writeObject(instance.identificationInfo);
    streamWriter.writeObject(instance.quantificationInfo);
    streamWriter.writeString(instance.sampleRef);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean)object);
  }
  
}
