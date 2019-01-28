package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class AmountBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.model.AmountType getAmountType(edu.scripps.yates.shared.model.AmountBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AmountBean::amountType;
  }-*/;
  
  private static native void setAmountType(edu.scripps.yates.shared.model.AmountBean instance, edu.scripps.yates.shared.model.AmountType value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AmountBean::amountType = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ExperimentalConditionBean getExperimentalCondition(edu.scripps.yates.shared.model.AmountBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AmountBean::experimentalCondition;
  }-*/;
  
  private static native void setExperimentalCondition(edu.scripps.yates.shared.model.AmountBean instance, edu.scripps.yates.shared.model.ExperimentalConditionBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AmountBean::experimentalCondition = value;
  }-*/;
  
  private static native boolean getIsComposed(edu.scripps.yates.shared.model.AmountBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AmountBean::isComposed;
  }-*/;
  
  private static native void setIsComposed(edu.scripps.yates.shared.model.AmountBean instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AmountBean::isComposed = value;
  }-*/;
  
  private static native java.lang.Boolean getManualSPC(edu.scripps.yates.shared.model.AmountBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AmountBean::manualSPC;
  }-*/;
  
  private static native void setManualSPC(edu.scripps.yates.shared.model.AmountBean instance, java.lang.Boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AmountBean::manualSPC = value;
  }-*/;
  
  private static native java.util.Set getMsRuns(edu.scripps.yates.shared.model.AmountBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AmountBean::msRuns;
  }-*/;
  
  private static native void setMsRuns(edu.scripps.yates.shared.model.AmountBean instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AmountBean::msRuns = value;
  }-*/;
  
  private static native double getValue(edu.scripps.yates.shared.model.AmountBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.AmountBean::value;
  }-*/;
  
  private static native void setValue(edu.scripps.yates.shared.model.AmountBean instance, double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.AmountBean::value = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.AmountBean instance) throws SerializationException {
    setAmountType(instance, (edu.scripps.yates.shared.model.AmountType) streamReader.readObject());
    setExperimentalCondition(instance, (edu.scripps.yates.shared.model.ExperimentalConditionBean) streamReader.readObject());
    setIsComposed(instance, streamReader.readBoolean());
    setManualSPC(instance, (java.lang.Boolean) streamReader.readObject());
    setMsRuns(instance, (java.util.Set) streamReader.readObject());
    setValue(instance, streamReader.readDouble());
    
  }
  
  public static edu.scripps.yates.shared.model.AmountBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.AmountBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.AmountBean instance) throws SerializationException {
    streamWriter.writeObject(getAmountType(instance));
    streamWriter.writeObject(getExperimentalCondition(instance));
    streamWriter.writeBoolean(getIsComposed(instance));
    streamWriter.writeObject(getManualSPC(instance));
    streamWriter.writeObject(getMsRuns(instance));
    streamWriter.writeDouble(getValue(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.AmountBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.AmountBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.AmountBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.AmountBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.AmountBean)object);
  }
  
}
