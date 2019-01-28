package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class FileSummary_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getFileSizeString(edu.scripps.yates.shared.model.FileSummary instance) /*-{
    return instance.@edu.scripps.yates.shared.model.FileSummary::fileSizeString;
  }-*/;
  
  private static native void setFileSizeString(edu.scripps.yates.shared.model.FileSummary instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.FileSummary::fileSizeString = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean getFileTypeBean(edu.scripps.yates.shared.model.FileSummary instance) /*-{
    return instance.@edu.scripps.yates.shared.model.FileSummary::fileTypeBean;
  }-*/;
  
  private static native void setFileTypeBean(edu.scripps.yates.shared.model.FileSummary instance, edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.FileSummary::fileTypeBean = value;
  }-*/;
  
  private static native int getNumPSMs(edu.scripps.yates.shared.model.FileSummary instance) /*-{
    return instance.@edu.scripps.yates.shared.model.FileSummary::numPSMs;
  }-*/;
  
  private static native void setNumPSMs(edu.scripps.yates.shared.model.FileSummary instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.FileSummary::numPSMs = value;
  }-*/;
  
  private static native int getNumPeptides(edu.scripps.yates.shared.model.FileSummary instance) /*-{
    return instance.@edu.scripps.yates.shared.model.FileSummary::numPeptides;
  }-*/;
  
  private static native void setNumPeptides(edu.scripps.yates.shared.model.FileSummary instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.FileSummary::numPeptides = value;
  }-*/;
  
  private static native int getNumProteins(edu.scripps.yates.shared.model.FileSummary instance) /*-{
    return instance.@edu.scripps.yates.shared.model.FileSummary::numProteins;
  }-*/;
  
  private static native void setNumProteins(edu.scripps.yates.shared.model.FileSummary instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.FileSummary::numProteins = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.FileSummary instance) throws SerializationException {
    setFileSizeString(instance, streamReader.readString());
    setFileTypeBean(instance, (edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean) streamReader.readObject());
    setNumPSMs(instance, streamReader.readInt());
    setNumPeptides(instance, streamReader.readInt());
    setNumProteins(instance, streamReader.readInt());
    
  }
  
  public static edu.scripps.yates.shared.model.FileSummary instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.FileSummary();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.FileSummary instance) throws SerializationException {
    streamWriter.writeString(getFileSizeString(instance));
    streamWriter.writeObject(getFileTypeBean(instance));
    streamWriter.writeInt(getNumPSMs(instance));
    streamWriter.writeInt(getNumPeptides(instance));
    streamWriter.writeInt(getNumProteins(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.FileSummary_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.FileSummary_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.FileSummary)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.FileSummary_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.FileSummary)object);
  }
  
}
