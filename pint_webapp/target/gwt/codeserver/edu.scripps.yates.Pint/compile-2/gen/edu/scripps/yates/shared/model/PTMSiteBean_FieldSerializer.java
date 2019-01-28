package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PTMSiteBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getAa(edu.scripps.yates.shared.model.PTMSiteBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PTMSiteBean::aa;
  }-*/;
  
  private static native void setAa(edu.scripps.yates.shared.model.PTMSiteBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PTMSiteBean::aa = value;
  }-*/;
  
  private static native int getPosition(edu.scripps.yates.shared.model.PTMSiteBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PTMSiteBean::position;
  }-*/;
  
  private static native void setPosition(edu.scripps.yates.shared.model.PTMSiteBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PTMSiteBean::position = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ScoreBean getScore(edu.scripps.yates.shared.model.PTMSiteBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PTMSiteBean::score;
  }-*/;
  
  private static native void setScore(edu.scripps.yates.shared.model.PTMSiteBean instance, edu.scripps.yates.shared.model.ScoreBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PTMSiteBean::score = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.PTMSiteBean instance) throws SerializationException {
    setAa(instance, streamReader.readString());
    setPosition(instance, streamReader.readInt());
    setScore(instance, (edu.scripps.yates.shared.model.ScoreBean) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.model.PTMSiteBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.PTMSiteBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.PTMSiteBean instance) throws SerializationException {
    streamWriter.writeString(getAa(instance));
    streamWriter.writeInt(getPosition(instance));
    streamWriter.writeObject(getScore(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.PTMSiteBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.PTMSiteBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.PTMSiteBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.PTMSiteBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.PTMSiteBean)object);
  }
  
}
