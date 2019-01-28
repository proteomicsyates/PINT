package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class TissueSetTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean instance) throws SerializationException {
    instance.tissue = (java.util.List) streamReader.readObject();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean instance) throws SerializationException {
    streamWriter.writeObject(instance.tissue);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean)object);
  }
  
}
