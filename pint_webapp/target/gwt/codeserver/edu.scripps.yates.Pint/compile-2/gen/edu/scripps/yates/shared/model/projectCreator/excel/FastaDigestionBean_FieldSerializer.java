package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class FastaDigestionBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean instance) throws SerializationException {
    instance.cleavageAAs = streamReader.readString();
    instance.enzymeNoCutResidues = streamReader.readString();
    instance.enzymeOffset = streamReader.readInt();
    instance.isH2OPlusProtonAdded = streamReader.readBoolean();
    instance.misscleavages = streamReader.readInt();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean instance) throws SerializationException {
    streamWriter.writeString(instance.cleavageAAs);
    streamWriter.writeString(instance.enzymeNoCutResidues);
    streamWriter.writeInt(instance.enzymeOffset);
    streamWriter.writeBoolean(instance.isH2OPlusProtonAdded);
    streamWriter.writeInt(instance.misscleavages);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean)object);
  }
  
}
