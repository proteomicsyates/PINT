package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class SampleTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean instance) throws SerializationException {
    instance.description = streamReader.readString();
    instance.id = streamReader.readString();
    instance.labelRef = streamReader.readString();
    instance.organismRef = streamReader.readString();
    instance.tissueRef = streamReader.readString();
    instance.wt = (java.lang.Boolean) streamReader.readObject();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean instance) throws SerializationException {
    streamWriter.writeString(instance.description);
    streamWriter.writeString(instance.id);
    streamWriter.writeString(instance.labelRef);
    streamWriter.writeString(instance.organismRef);
    streamWriter.writeString(instance.tissueRef);
    streamWriter.writeObject(instance.wt);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean)object);
  }
  
}
