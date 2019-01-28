package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class RemoteFilesRatioTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean instance) throws SerializationException {
    instance.denominator = streamReader.readString();
    instance.discardDecoys = streamReader.readString();
    instance.fileRef = streamReader.readString();
    instance.id = streamReader.readString();
    instance.msRunRef = streamReader.readString();
    instance.numerator = streamReader.readString();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean instance) throws SerializationException {
    streamWriter.writeString(instance.denominator);
    streamWriter.writeString(instance.discardDecoys);
    streamWriter.writeString(instance.fileRef);
    streamWriter.writeString(instance.id);
    streamWriter.writeString(instance.msRunRef);
    streamWriter.writeString(instance.numerator);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean)object);
  }
  
}
