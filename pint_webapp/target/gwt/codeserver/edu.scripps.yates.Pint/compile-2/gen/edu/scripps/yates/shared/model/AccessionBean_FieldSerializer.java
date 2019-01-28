package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class AccessionBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getAccession(edu.scripps.yates.shared.model.AccessionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AccessionBean::accession;
  }-*/;
  
  private static native void setAccession(edu.scripps.yates.shared.model.AccessionBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AccessionBean::accession = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.AccessionType getAccessionType(edu.scripps.yates.shared.model.AccessionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AccessionBean::accessionType;
  }-*/;
  
  private static native void setAccessionType(edu.scripps.yates.shared.model.AccessionBean instance, edu.scripps.yates.shared.model.AccessionType value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AccessionBean::accessionType = value;
  }-*/;
  
  private static native java.util.List getAlternativeNames(edu.scripps.yates.shared.model.AccessionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AccessionBean::alternativeNames;
  }-*/;
  
  private static native void setAlternativeNames(edu.scripps.yates.shared.model.AccessionBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AccessionBean::alternativeNames = value;
  }-*/;
  
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.AccessionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AccessionBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.AccessionBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AccessionBean::description = value;
  }-*/;
  
  private static native boolean getIsPrimaryAccession(edu.scripps.yates.shared.model.AccessionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AccessionBean::isPrimaryAccession;
  }-*/;
  
  private static native void setIsPrimaryAccession(edu.scripps.yates.shared.model.AccessionBean instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AccessionBean::isPrimaryAccession = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.AccessionBean instance) throws SerializationException {
    setAccession(instance, streamReader.readString());
    setAccessionType(instance, (edu.scripps.yates.shared.model.AccessionType) streamReader.readObject());
    setAlternativeNames(instance, (java.util.List) streamReader.readObject());
    setDescription(instance, streamReader.readString());
    setIsPrimaryAccession(instance, streamReader.readBoolean());
    
  }
  
  public static edu.scripps.yates.shared.model.AccessionBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.AccessionBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.AccessionBean instance) throws SerializationException {
    streamWriter.writeString(getAccession(instance));
    streamWriter.writeObject(getAccessionType(instance));
    streamWriter.writeObject(getAlternativeNames(instance));
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeBoolean(getIsPrimaryAccession(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.AccessionBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.AccessionBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.AccessionBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.AccessionBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.AccessionBean)object);
  }
  
}
