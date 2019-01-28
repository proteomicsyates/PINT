package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class RatioBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.model.ScoreBean getAssociatedConfidenceScore(edu.scripps.yates.shared.model.RatioBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioBean::associatedConfidenceScore;
  }-*/;
  
  private static native void setAssociatedConfidenceScore(edu.scripps.yates.shared.model.RatioBean instance, edu.scripps.yates.shared.model.ScoreBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioBean::associatedConfidenceScore = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ExperimentalConditionBean getCondition1(edu.scripps.yates.shared.model.RatioBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioBean::condition1;
  }-*/;
  
  private static native void setCondition1(edu.scripps.yates.shared.model.RatioBean instance, edu.scripps.yates.shared.model.ExperimentalConditionBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioBean::condition1 = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ExperimentalConditionBean getCondition2(edu.scripps.yates.shared.model.RatioBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioBean::condition2;
  }-*/;
  
  private static native void setCondition2(edu.scripps.yates.shared.model.RatioBean instance, edu.scripps.yates.shared.model.ExperimentalConditionBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioBean::condition2 = value;
  }-*/;
  
  private static native int getDbID(edu.scripps.yates.shared.model.RatioBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioBean::dbID;
  }-*/;
  
  private static native void setDbID(edu.scripps.yates.shared.model.RatioBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioBean::dbID = value;
  }-*/;
  
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.RatioBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.RatioBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioBean::description = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.RatioDescriptorBean getRatioDescriptorBean(edu.scripps.yates.shared.model.RatioBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioBean::ratioDescriptorBean;
  }-*/;
  
  private static native void setRatioDescriptorBean(edu.scripps.yates.shared.model.RatioBean instance, edu.scripps.yates.shared.model.RatioDescriptorBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioBean::ratioDescriptorBean = value;
  }-*/;
  
  private static native double getValue(edu.scripps.yates.shared.model.RatioBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioBean::value;
  }-*/;
  
  private static native void setValue(edu.scripps.yates.shared.model.RatioBean instance, double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioBean::value = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.RatioBean instance) throws SerializationException {
    setAssociatedConfidenceScore(instance, (edu.scripps.yates.shared.model.ScoreBean) streamReader.readObject());
    setCondition1(instance, (edu.scripps.yates.shared.model.ExperimentalConditionBean) streamReader.readObject());
    setCondition2(instance, (edu.scripps.yates.shared.model.ExperimentalConditionBean) streamReader.readObject());
    setDbID(instance, streamReader.readInt());
    setDescription(instance, streamReader.readString());
    setRatioDescriptorBean(instance, (edu.scripps.yates.shared.model.RatioDescriptorBean) streamReader.readObject());
    setValue(instance, streamReader.readDouble());
    
  }
  
  public static edu.scripps.yates.shared.model.RatioBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.RatioBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.RatioBean instance) throws SerializationException {
    streamWriter.writeObject(getAssociatedConfidenceScore(instance));
    streamWriter.writeObject(getCondition1(instance));
    streamWriter.writeObject(getCondition2(instance));
    streamWriter.writeInt(getDbID(instance));
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeObject(getRatioDescriptorBean(instance));
    streamWriter.writeDouble(getValue(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.RatioBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.RatioBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.RatioBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.RatioBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.RatioBean)object);
  }
  
}
