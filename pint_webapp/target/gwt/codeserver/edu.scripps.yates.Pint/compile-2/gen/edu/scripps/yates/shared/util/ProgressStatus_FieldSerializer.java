package edu.scripps.yates.shared.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProgressStatus_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  @com.google.gwt.core.client.UnsafeNativeLong
  private static native long getCurrentStep(edu.scripps.yates.shared.util.ProgressStatus instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProgressStatus::currentStep;
  }-*/;
  
  @com.google.gwt.core.client.UnsafeNativeLong
  private static native void setCurrentStep(edu.scripps.yates.shared.util.ProgressStatus instance, long value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProgressStatus::currentStep = value;
  }-*/;
  
  @com.google.gwt.core.client.UnsafeNativeLong
  private static native long getMaxSteps(edu.scripps.yates.shared.util.ProgressStatus instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProgressStatus::maxSteps;
  }-*/;
  
  @com.google.gwt.core.client.UnsafeNativeLong
  private static native void setMaxSteps(edu.scripps.yates.shared.util.ProgressStatus instance, long value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProgressStatus::maxSteps = value;
  }-*/;
  
  private static native double getProgressPercentage(edu.scripps.yates.shared.util.ProgressStatus instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProgressStatus::progressPercentage;
  }-*/;
  
  private static native void setProgressPercentage(edu.scripps.yates.shared.util.ProgressStatus instance, double value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProgressStatus::progressPercentage = value;
  }-*/;
  
  private static native java.lang.String getTaskDescription(edu.scripps.yates.shared.util.ProgressStatus instance) /*-{
    return instance.@edu.scripps.yates.shared.util.ProgressStatus::taskDescription;
  }-*/;
  
  private static native void setTaskDescription(edu.scripps.yates.shared.util.ProgressStatus instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.ProgressStatus::taskDescription = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.ProgressStatus instance) throws SerializationException {
    setCurrentStep(instance, streamReader.readLong());
    setMaxSteps(instance, streamReader.readLong());
    setProgressPercentage(instance, streamReader.readDouble());
    setTaskDescription(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.util.ProgressStatus instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.util.ProgressStatus();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.ProgressStatus instance) throws SerializationException {
    streamWriter.writeLong(getCurrentStep(instance));
    streamWriter.writeLong(getMaxSteps(instance));
    streamWriter.writeDouble(getProgressPercentage(instance));
    streamWriter.writeString(getTaskDescription(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.util.ProgressStatus_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.ProgressStatus_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.util.ProgressStatus)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.ProgressStatus_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.util.ProgressStatus)object);
  }
  
}
