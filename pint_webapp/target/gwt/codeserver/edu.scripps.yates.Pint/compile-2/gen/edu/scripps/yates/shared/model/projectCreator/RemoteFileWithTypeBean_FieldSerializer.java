package edu.scripps.yates.shared.model.projectCreator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class RemoteFileWithTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getRemotePath(edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean::remotePath;
  }-*/;
  
  private static native void setRemotePath(edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean::remotePath = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean getServer(edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean::server;
  }-*/;
  
  private static native void setServer(edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean instance, edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean::server = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean instance) throws SerializationException {
    setRemotePath(instance, streamReader.readString());
    setServer(instance, (edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean) streamReader.readObject());
    
    edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean instance) throws SerializationException {
    streamWriter.writeString(getRemotePath(instance));
    streamWriter.writeObject(getServer(instance));
    
    edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean)object);
  }
  
}
