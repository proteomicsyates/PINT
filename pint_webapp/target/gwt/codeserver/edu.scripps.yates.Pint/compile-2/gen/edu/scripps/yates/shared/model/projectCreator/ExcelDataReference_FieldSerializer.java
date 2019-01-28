package edu.scripps.yates.shared.model.projectCreator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ExcelDataReference_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getColumnId(edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::columnId;
  }-*/;
  
  private static native void setColumnId(edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::columnId = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean getExcelFileNameWithTypeBean(edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::excelFileNameWithTypeBean;
  }-*/;
  
  private static native void setExcelFileNameWithTypeBean(edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::excelFileNameWithTypeBean = value;
  }-*/;
  
  private static native java.lang.String getRegexp(edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::regexp;
  }-*/;
  
  private static native void setRegexp(edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::regexp = value;
  }-*/;
  
  private static native java.lang.String getSheetId(edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::sheetId;
  }-*/;
  
  private static native void setSheetId(edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::sheetId = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance) throws SerializationException {
    setColumnId(instance, streamReader.readString());
    setExcelFileNameWithTypeBean(instance, (edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean) streamReader.readObject());
    setRegexp(instance, streamReader.readString());
    setSheetId(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.ExcelDataReference();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.ExcelDataReference instance) throws SerializationException {
    streamWriter.writeString(getColumnId(instance));
    streamWriter.writeObject(getExcelFileNameWithTypeBean(instance));
    streamWriter.writeString(getRegexp(instance));
    streamWriter.writeString(getSheetId(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.ExcelDataReference_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.ExcelDataReference_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.ExcelDataReference)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.ExcelDataReference_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.ExcelDataReference)object);
  }
  
}
