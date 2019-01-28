package edu.scripps.yates.shared.thirdparty.pseaquant;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PSEAQuantLiteratureBias_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias instance) throws SerializationException {
    // Enum deserialization is handled via the instantiate method
  }
  
  public static edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int ordinal = streamReader.readInt();
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias[] values = edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias.values();
    assert (ordinal >= 0 && ordinal < values.length);
    return values[ordinal];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias instance) throws SerializationException {
    assert (instance != null);
    streamWriter.writeInt(instance.ordinal());
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias)object);
  }
  
}
