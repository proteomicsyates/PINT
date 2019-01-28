package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class RatioDescriptorBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.model.SharedAggregationLevel getAggregationLevel(edu.scripps.yates.shared.model.RatioDescriptorBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::aggregationLevel;
  }-*/;
  
  private static native void setAggregationLevel(edu.scripps.yates.shared.model.RatioDescriptorBean instance, edu.scripps.yates.shared.model.SharedAggregationLevel value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::aggregationLevel = value;
  }-*/;
  
  private static native java.lang.String getCondition1Name(edu.scripps.yates.shared.model.RatioDescriptorBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::condition1Name;
  }-*/;
  
  private static native void setCondition1Name(edu.scripps.yates.shared.model.RatioDescriptorBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::condition1Name = value;
  }-*/;
  
  private static native java.lang.String getCondition2Name(edu.scripps.yates.shared.model.RatioDescriptorBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::condition2Name;
  }-*/;
  
  private static native void setCondition2Name(edu.scripps.yates.shared.model.RatioDescriptorBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::condition2Name = value;
  }-*/;
  
  private static native java.lang.String getPeptideScoreName(edu.scripps.yates.shared.model.RatioDescriptorBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::peptideScoreName;
  }-*/;
  
  private static native void setPeptideScoreName(edu.scripps.yates.shared.model.RatioDescriptorBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::peptideScoreName = value;
  }-*/;
  
  private static native java.lang.String getProjectTag(edu.scripps.yates.shared.model.RatioDescriptorBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::projectTag;
  }-*/;
  
  private static native void setProjectTag(edu.scripps.yates.shared.model.RatioDescriptorBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::projectTag = value;
  }-*/;
  
  private static native java.lang.String getProteinScoreName(edu.scripps.yates.shared.model.RatioDescriptorBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::proteinScoreName;
  }-*/;
  
  private static native void setProteinScoreName(edu.scripps.yates.shared.model.RatioDescriptorBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::proteinScoreName = value;
  }-*/;
  
  private static native java.lang.String getPsmScoreName(edu.scripps.yates.shared.model.RatioDescriptorBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::psmScoreName;
  }-*/;
  
  private static native void setPsmScoreName(edu.scripps.yates.shared.model.RatioDescriptorBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::psmScoreName = value;
  }-*/;
  
  private static native java.lang.String getRatioName(edu.scripps.yates.shared.model.RatioDescriptorBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::ratioName;
  }-*/;
  
  private static native void setRatioName(edu.scripps.yates.shared.model.RatioDescriptorBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDescriptorBean::ratioName = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.RatioDescriptorBean instance) throws SerializationException {
    setAggregationLevel(instance, (edu.scripps.yates.shared.model.SharedAggregationLevel) streamReader.readObject());
    setCondition1Name(instance, streamReader.readString());
    setCondition2Name(instance, streamReader.readString());
    setPeptideScoreName(instance, streamReader.readString());
    setProjectTag(instance, streamReader.readString());
    setProteinScoreName(instance, streamReader.readString());
    setPsmScoreName(instance, streamReader.readString());
    setRatioName(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.RatioDescriptorBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.RatioDescriptorBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.RatioDescriptorBean instance) throws SerializationException {
    streamWriter.writeObject(getAggregationLevel(instance));
    streamWriter.writeString(getCondition1Name(instance));
    streamWriter.writeString(getCondition2Name(instance));
    streamWriter.writeString(getPeptideScoreName(instance));
    streamWriter.writeString(getProjectTag(instance));
    streamWriter.writeString(getProteinScoreName(instance));
    streamWriter.writeString(getPsmScoreName(instance));
    streamWriter.writeString(getRatioName(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.RatioDescriptorBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.RatioDescriptorBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.RatioDescriptorBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.RatioDescriptorBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.RatioDescriptorBean)object);
  }
  
}
