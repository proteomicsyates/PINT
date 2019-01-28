package edu.scripps.yates.shared.columns;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ColumnWithVisibility_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.columns.ColumnName getColumn(edu.scripps.yates.shared.columns.ColumnWithVisibility instance) /*-{
    return instance.@edu.scripps.yates.shared.columns.ColumnWithVisibility::column;
  }-*/;
  
  private static native void setColumn(edu.scripps.yates.shared.columns.ColumnWithVisibility instance, edu.scripps.yates.shared.columns.ColumnName value) 
  /*-{
    instance.@edu.scripps.yates.shared.columns.ColumnWithVisibility::column = value;
  }-*/;
  
  private static native boolean getVisible(edu.scripps.yates.shared.columns.ColumnWithVisibility instance) /*-{
    return instance.@edu.scripps.yates.shared.columns.ColumnWithVisibility::visible;
  }-*/;
  
  private static native void setVisible(edu.scripps.yates.shared.columns.ColumnWithVisibility instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.columns.ColumnWithVisibility::visible = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.columns.ColumnWithVisibility instance) throws SerializationException {
    setColumn(instance, (edu.scripps.yates.shared.columns.ColumnName) streamReader.readObject());
    setVisible(instance, streamReader.readBoolean());
    
  }
  
  public static edu.scripps.yates.shared.columns.ColumnWithVisibility instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.columns.ColumnWithVisibility();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.columns.ColumnWithVisibility instance) throws SerializationException {
    streamWriter.writeObject(getColumn(instance));
    streamWriter.writeBoolean(getVisible(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.columns.ColumnWithVisibility_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.ColumnWithVisibility_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.columns.ColumnWithVisibility)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.ColumnWithVisibility_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.columns.ColumnWithVisibility)object);
  }
  
}
