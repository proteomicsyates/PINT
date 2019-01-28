package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ExperimentalConditionBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.ExperimentalConditionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.ExperimentalConditionBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::description = value;
  }-*/;
  
  private static native java.lang.String getName(edu.scripps.yates.shared.model.ExperimentalConditionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.ExperimentalConditionBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::name = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ProjectBean getProject(edu.scripps.yates.shared.model.ExperimentalConditionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::project;
  }-*/;
  
  private static native void setProject(edu.scripps.yates.shared.model.ExperimentalConditionBean instance, edu.scripps.yates.shared.model.ProjectBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::project = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.SampleBean getSample(edu.scripps.yates.shared.model.ExperimentalConditionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::sample;
  }-*/;
  
  private static native void setSample(edu.scripps.yates.shared.model.ExperimentalConditionBean instance, edu.scripps.yates.shared.model.SampleBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::sample = value;
  }-*/;
  
  private static native java.lang.String getUnit(edu.scripps.yates.shared.model.ExperimentalConditionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::unit;
  }-*/;
  
  private static native void setUnit(edu.scripps.yates.shared.model.ExperimentalConditionBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::unit = value;
  }-*/;
  
  private static native java.lang.Double getValue(edu.scripps.yates.shared.model.ExperimentalConditionBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::value;
  }-*/;
  
  private static native void setValue(edu.scripps.yates.shared.model.ExperimentalConditionBean instance, java.lang.Double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ExperimentalConditionBean::value = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ExperimentalConditionBean instance) throws SerializationException {
    setDescription(instance, streamReader.readString());
    setName(instance, streamReader.readString());
    setProject(instance, (edu.scripps.yates.shared.model.ProjectBean) streamReader.readObject());
    setSample(instance, (edu.scripps.yates.shared.model.SampleBean) streamReader.readObject());
    setUnit(instance, streamReader.readString());
    setValue(instance, (java.lang.Double) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.model.ExperimentalConditionBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ExperimentalConditionBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ExperimentalConditionBean instance) throws SerializationException {
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeString(getName(instance));
    streamWriter.writeObject(getProject(instance));
    streamWriter.writeObject(getSample(instance));
    streamWriter.writeString(getUnit(instance));
    streamWriter.writeObject(getValue(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ExperimentalConditionBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ExperimentalConditionBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ExperimentalConditionBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ExperimentalConditionBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ExperimentalConditionBean)object);
  }
  
}
