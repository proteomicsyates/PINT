package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class UniprotFeatureBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.UniprotFeatureBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::description = value;
  }-*/;
  
  private static native java.lang.String getFeatureType(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::featureType;
  }-*/;
  
  private static native void setFeatureType(edu.scripps.yates.shared.model.UniprotFeatureBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::featureType = value;
  }-*/;
  
  private static native java.lang.String getOriginal(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::original;
  }-*/;
  
  private static native void setOriginal(edu.scripps.yates.shared.model.UniprotFeatureBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::original = value;
  }-*/;
  
  private static native int getPositionEnd(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::positionEnd;
  }-*/;
  
  private static native void setPositionEnd(edu.scripps.yates.shared.model.UniprotFeatureBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::positionEnd = value;
  }-*/;
  
  private static native int getPositionStart(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::positionStart;
  }-*/;
  
  private static native void setPositionStart(edu.scripps.yates.shared.model.UniprotFeatureBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::positionStart = value;
  }-*/;
  
  private static native java.lang.String getRef(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::ref;
  }-*/;
  
  private static native void setRef(edu.scripps.yates.shared.model.UniprotFeatureBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::ref = value;
  }-*/;
  
  private static native java.lang.String getStatus(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::status;
  }-*/;
  
  private static native void setStatus(edu.scripps.yates.shared.model.UniprotFeatureBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::status = value;
  }-*/;
  
  private static native java.lang.String getString(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::string;
  }-*/;
  
  private static native void setString(edu.scripps.yates.shared.model.UniprotFeatureBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::string = value;
  }-*/;
  
  private static native java.lang.String getVariation(edu.scripps.yates.shared.model.UniprotFeatureBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::variation;
  }-*/;
  
  private static native void setVariation(edu.scripps.yates.shared.model.UniprotFeatureBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.UniprotFeatureBean::variation = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.UniprotFeatureBean instance) throws SerializationException {
    setDescription(instance, streamReader.readString());
    setFeatureType(instance, streamReader.readString());
    setOriginal(instance, streamReader.readString());
    setPositionEnd(instance, streamReader.readInt());
    setPositionStart(instance, streamReader.readInt());
    setRef(instance, streamReader.readString());
    setStatus(instance, streamReader.readString());
    setString(instance, streamReader.readString());
    setVariation(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.UniprotFeatureBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.UniprotFeatureBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.UniprotFeatureBean instance) throws SerializationException {
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeString(getFeatureType(instance));
    streamWriter.writeString(getOriginal(instance));
    streamWriter.writeInt(getPositionEnd(instance));
    streamWriter.writeInt(getPositionStart(instance));
    streamWriter.writeString(getRef(instance));
    streamWriter.writeString(getStatus(instance));
    streamWriter.writeString(getString(instance));
    streamWriter.writeString(getVariation(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.UniprotFeatureBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.UniprotFeatureBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.UniprotFeatureBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.UniprotFeatureBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.UniprotFeatureBean)object);
  }
  
}
