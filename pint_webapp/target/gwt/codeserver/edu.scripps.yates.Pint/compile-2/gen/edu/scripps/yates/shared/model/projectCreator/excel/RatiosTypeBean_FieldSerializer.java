package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class RatiosTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean instance) throws SerializationException {
    instance.peptideAmountRatios = (edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean) streamReader.readObject();
    instance.proteinAmountRatios = (edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean) streamReader.readObject();
    instance.psmAmountRatios = (edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean) streamReader.readObject();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean instance) throws SerializationException {
    streamWriter.writeObject(instance.peptideAmountRatios);
    streamWriter.writeObject(instance.proteinAmountRatios);
    streamWriter.writeObject(instance.psmAmountRatios);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean)object);
  }
  
}
