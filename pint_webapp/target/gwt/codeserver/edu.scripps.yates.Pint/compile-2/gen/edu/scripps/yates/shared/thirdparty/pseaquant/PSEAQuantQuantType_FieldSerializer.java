package edu.scripps.yates.shared.thirdparty.pseaquant;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PSEAQuantQuantType_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType instance) throws SerializationException {
    // Enum deserialization is handled via the instantiate method
  }
  
  public static edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int ordinal = streamReader.readInt();
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType[] values = edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType.values();
    assert (ordinal >= 0 && ordinal < values.length);
    return values[ordinal];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType instance) throws SerializationException {
    assert (instance != null);
    streamWriter.writeInt(instance.ordinal());
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType)object);
  }
  
}
