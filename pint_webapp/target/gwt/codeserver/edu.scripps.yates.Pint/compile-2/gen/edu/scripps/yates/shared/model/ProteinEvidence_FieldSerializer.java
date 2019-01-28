package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProteinEvidence_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ProteinEvidence instance) throws SerializationException {
    // Enum deserialization is handled via the instantiate method
  }
  
  public static edu.scripps.yates.shared.model.ProteinEvidence instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int ordinal = streamReader.readInt();
    edu.scripps.yates.shared.model.ProteinEvidence[] values = edu.scripps.yates.shared.model.ProteinEvidence.values();
    assert (ordinal >= 0 && ordinal < values.length);
    return values[ordinal];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ProteinEvidence instance) throws SerializationException {
    assert (instance != null);
    streamWriter.writeInt(instance.ordinal());
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ProteinEvidence_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinEvidence_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ProteinEvidence)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProteinEvidence_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ProteinEvidence)object);
  }
  
}
