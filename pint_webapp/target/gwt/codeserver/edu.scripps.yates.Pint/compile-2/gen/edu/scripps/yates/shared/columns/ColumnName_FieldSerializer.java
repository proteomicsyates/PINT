package edu.scripps.yates.shared.columns;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ColumnName_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native boolean getAddColumnByDefault(edu.scripps.yates.shared.columns.ColumnName instance) /*-{
    return instance.@edu.scripps.yates.shared.columns.ColumnName::addColumnByDefault;
  }-*/;
  
  private static native void setAddColumnByDefault(edu.scripps.yates.shared.columns.ColumnName instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.columns.ColumnName::addColumnByDefault = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.HorizontalAlignmentSharedConstant getHorizontalAlignment(edu.scripps.yates.shared.columns.ColumnName instance) /*-{
    return instance.@edu.scripps.yates.shared.columns.ColumnName::horizontalAlignment;
  }-*/;
  
  private static native void setHorizontalAlignment(edu.scripps.yates.shared.columns.ColumnName instance, edu.scripps.yates.shared.util.HorizontalAlignmentSharedConstant value) 
  /*-{
    instance.@edu.scripps.yates.shared.columns.ColumnName::horizontalAlignment = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.columns.ColumnName instance) throws SerializationException {
    // Enum deserialization is handled via the instantiate method
  }
  
  public static edu.scripps.yates.shared.columns.ColumnName instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int ordinal = streamReader.readInt();
    edu.scripps.yates.shared.columns.ColumnName[] values = edu.scripps.yates.shared.columns.ColumnName.values();
    assert (ordinal >= 0 && ordinal < values.length);
    return values[ordinal];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.columns.ColumnName instance) throws SerializationException {
    assert (instance != null);
    streamWriter.writeInt(instance.ordinal());
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.columns.ColumnName_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.ColumnName_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.columns.ColumnName)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.ColumnName_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.columns.ColumnName)object);
  }
  
}
