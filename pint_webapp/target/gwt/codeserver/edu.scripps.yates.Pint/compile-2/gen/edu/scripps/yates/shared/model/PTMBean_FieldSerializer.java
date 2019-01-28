package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PTMBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getCvId(edu.scripps.yates.shared.model.PTMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PTMBean::cvId;
  }-*/;
  
  private static native void setCvId(edu.scripps.yates.shared.model.PTMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PTMBean::cvId = value;
  }-*/;
  
  private static native double getMassShift(edu.scripps.yates.shared.model.PTMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PTMBean::massShift;
  }-*/;
  
  private static native void setMassShift(edu.scripps.yates.shared.model.PTMBean instance, double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PTMBean::massShift = value;
  }-*/;
  
  private static native java.lang.String getName(edu.scripps.yates.shared.model.PTMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PTMBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.PTMBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PTMBean::name = value;
  }-*/;
  
  private static native java.util.List getPtmSites(edu.scripps.yates.shared.model.PTMBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.PTMBean::ptmSites;
  }-*/;
  
  private static native void setPtmSites(edu.scripps.yates.shared.model.PTMBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.PTMBean::ptmSites = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.PTMBean instance) throws SerializationException {
    setCvId(instance, streamReader.readString());
    setMassShift(instance, streamReader.readDouble());
    setName(instance, streamReader.readString());
    setPtmSites(instance, (java.util.List) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.model.PTMBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.PTMBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.PTMBean instance) throws SerializationException {
    streamWriter.writeString(getCvId(instance));
    streamWriter.writeDouble(getMassShift(instance));
    streamWriter.writeString(getName(instance));
    streamWriter.writeObject(getPtmSites(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.PTMBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.PTMBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.PTMBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.PTMBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.PTMBean)object);
  }
  
}
