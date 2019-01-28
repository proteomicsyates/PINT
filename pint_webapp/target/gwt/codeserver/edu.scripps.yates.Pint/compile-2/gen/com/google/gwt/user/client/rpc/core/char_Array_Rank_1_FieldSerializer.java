package com.google.gwt.user.client.rpc.core;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class char_Array_Rank_1_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, char[] instance) throws SerializationException {
    for (int i = 0, n = instance.length; i < n; ++i) {
      instance[i] = streamReader.readChar();
    }
  }
  
  public static char[] instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int size = streamReader.readInt();
    return new char[size];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, char[] instance) throws SerializationException {
    streamWriter.writeInt(instance.length);
    
    for (int i = 0, n = instance.length; i < n; ++i) {
      streamWriter.writeChar(instance[i]);
    }
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return com.google.gwt.user.client.rpc.core.char_Array_Rank_1_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    com.google.gwt.user.client.rpc.core.char_Array_Rank_1_FieldSerializer.deserialize(reader, (char[])object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    com.google.gwt.user.client.rpc.core.char_Array_Rank_1_FieldSerializer.serialize(writer, (char[])object);
  }
  
}
