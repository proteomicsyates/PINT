package edu.scripps.yates.shared.model.projectCreator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class FileNameWithTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean getFastaDigestionBean(edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::fastaDigestionBean;
  }-*/;
  
  private static native void setFastaDigestionBean(edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance, edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::fastaDigestionBean = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.FileFormat getFileFormat(edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::fileFormat;
  }-*/;
  
  private static native void setFileFormat(edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance, edu.scripps.yates.shared.model.FileFormat value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::fileFormat = value;
  }-*/;
  
  private static native java.lang.String getFileName(edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::fileName;
  }-*/;
  
  private static native void setFileName(edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::fileName = value;
  }-*/;
  
  private static native java.lang.String getId(edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::id;
  }-*/;
  
  private static native void setId(edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::id = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance) throws SerializationException {
    setFastaDigestionBean(instance, (edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean) streamReader.readObject());
    setFileFormat(instance, (edu.scripps.yates.shared.model.FileFormat) streamReader.readObject());
    setFileName(instance, streamReader.readString());
    setId(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean instance) throws SerializationException {
    streamWriter.writeObject(getFastaDigestionBean(instance));
    streamWriter.writeObject(getFileFormat(instance));
    streamWriter.writeString(getFileName(instance));
    streamWriter.writeString(getId(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean)object);
  }
  
}
