package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class DataSourceBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean getFastaDigestionBean(edu.scripps.yates.shared.model.DataSourceBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.DataSourceBean::fastaDigestionBean;
  }-*/;
  
  private static native void setFastaDigestionBean(edu.scripps.yates.shared.model.DataSourceBean instance, edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.DataSourceBean::fastaDigestionBean = value;
  }-*/;
  
  private static native java.lang.String getFileName(edu.scripps.yates.shared.model.DataSourceBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.DataSourceBean::fileName;
  }-*/;
  
  private static native void setFileName(edu.scripps.yates.shared.model.DataSourceBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.DataSourceBean::fileName = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.FileFormat getFormat(edu.scripps.yates.shared.model.DataSourceBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.DataSourceBean::format;
  }-*/;
  
  private static native void setFormat(edu.scripps.yates.shared.model.DataSourceBean instance, edu.scripps.yates.shared.model.FileFormat value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.DataSourceBean::format = value;
  }-*/;
  
  private static native java.lang.String getId(edu.scripps.yates.shared.model.DataSourceBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.DataSourceBean::id;
  }-*/;
  
  private static native void setId(edu.scripps.yates.shared.model.DataSourceBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.DataSourceBean::id = value;
  }-*/;
  
  private static native java.lang.String getRelativePath(edu.scripps.yates.shared.model.DataSourceBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.DataSourceBean::relativePath;
  }-*/;
  
  private static native void setRelativePath(edu.scripps.yates.shared.model.DataSourceBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.DataSourceBean::relativePath = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean getServer(edu.scripps.yates.shared.model.DataSourceBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.DataSourceBean::server;
  }-*/;
  
  private static native void setServer(edu.scripps.yates.shared.model.DataSourceBean instance, edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.DataSourceBean::server = value;
  }-*/;
  
  private static native java.lang.String getUrl(edu.scripps.yates.shared.model.DataSourceBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.DataSourceBean::url;
  }-*/;
  
  private static native void setUrl(edu.scripps.yates.shared.model.DataSourceBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.DataSourceBean::url = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.DataSourceBean instance) throws SerializationException {
    setFastaDigestionBean(instance, (edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean) streamReader.readObject());
    setFileName(instance, streamReader.readString());
    setFormat(instance, (edu.scripps.yates.shared.model.FileFormat) streamReader.readObject());
    setId(instance, streamReader.readString());
    setRelativePath(instance, streamReader.readString());
    setServer(instance, (edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean) streamReader.readObject());
    setUrl(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.DataSourceBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.DataSourceBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.DataSourceBean instance) throws SerializationException {
    streamWriter.writeObject(getFastaDigestionBean(instance));
    streamWriter.writeString(getFileName(instance));
    streamWriter.writeObject(getFormat(instance));
    streamWriter.writeString(getId(instance));
    streamWriter.writeString(getRelativePath(instance));
    streamWriter.writeObject(getServer(instance));
    streamWriter.writeString(getUrl(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.DataSourceBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.DataSourceBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.DataSourceBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.DataSourceBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.DataSourceBean)object);
  }
  
}
