package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ScoreBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.ScoreBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ScoreBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.ScoreBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ScoreBean::description = value;
  }-*/;
  
  private static native java.lang.String getScoreName(edu.scripps.yates.shared.model.ScoreBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ScoreBean::scoreName;
  }-*/;
  
  private static native void setScoreName(edu.scripps.yates.shared.model.ScoreBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ScoreBean::scoreName = value;
  }-*/;
  
  private static native java.lang.String getScoreType(edu.scripps.yates.shared.model.ScoreBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ScoreBean::scoreType;
  }-*/;
  
  private static native void setScoreType(edu.scripps.yates.shared.model.ScoreBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ScoreBean::scoreType = value;
  }-*/;
  
  private static native java.lang.String getValue(edu.scripps.yates.shared.model.ScoreBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ScoreBean::value;
  }-*/;
  
  private static native void setValue(edu.scripps.yates.shared.model.ScoreBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ScoreBean::value = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ScoreBean instance) throws SerializationException {
    setDescription(instance, streamReader.readString());
    setScoreName(instance, streamReader.readString());
    setScoreType(instance, streamReader.readString());
    setValue(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.ScoreBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ScoreBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ScoreBean instance) throws SerializationException {
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeString(getScoreName(instance));
    streamWriter.writeString(getScoreType(instance));
    streamWriter.writeString(getValue(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ScoreBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ScoreBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ScoreBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ScoreBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ScoreBean)object);
  }
  
}
