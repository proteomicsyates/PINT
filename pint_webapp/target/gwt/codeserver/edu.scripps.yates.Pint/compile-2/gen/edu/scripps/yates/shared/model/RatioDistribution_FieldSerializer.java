package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class RatioDistribution_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native double getMaxRatio(edu.scripps.yates.shared.model.RatioDistribution instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDistribution::maxRatio;
  }-*/;
  
  private static native void setMaxRatio(edu.scripps.yates.shared.model.RatioDistribution instance, double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDistribution::maxRatio = value;
  }-*/;
  
  private static native double getMinRatio(edu.scripps.yates.shared.model.RatioDistribution instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDistribution::minRatio;
  }-*/;
  
  private static native void setMinRatio(edu.scripps.yates.shared.model.RatioDistribution instance, double value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDistribution::minRatio = value;
  }-*/;
  
  private static native java.lang.String getRatioKey(edu.scripps.yates.shared.model.RatioDistribution instance) /*-{
    return instance.@edu.scripps.yates.shared.model.RatioDistribution::ratioKey;
  }-*/;
  
  private static native void setRatioKey(edu.scripps.yates.shared.model.RatioDistribution instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.RatioDistribution::ratioKey = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.RatioDistribution instance) throws SerializationException {
    setMaxRatio(instance, streamReader.readDouble());
    setMinRatio(instance, streamReader.readDouble());
    setRatioKey(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.RatioDistribution instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.RatioDistribution();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.RatioDistribution instance) throws SerializationException {
    streamWriter.writeDouble(getMaxRatio(instance));
    streamWriter.writeDouble(getMinRatio(instance));
    streamWriter.writeString(getRatioKey(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.RatioDistribution_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.RatioDistribution_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.RatioDistribution)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.RatioDistribution_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.RatioDistribution)object);
  }
  
}
