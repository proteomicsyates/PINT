package edu.scripps.yates.shared.thirdparty.pseaquant;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PSEAQuantResult_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getErrorMessage(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instance) /*-{
    return instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult::errorMessage;
  }-*/;
  
  private static native void setErrorMessage(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult::errorMessage = value;
  }-*/;
  
  private static native java.lang.String getLinkToRatios(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instance) /*-{
    return instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult::linkToRatios;
  }-*/;
  
  private static native void setLinkToRatios(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult::linkToRatios = value;
  }-*/;
  
  private static native java.lang.String getLinkToResults(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instance) /*-{
    return instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult::linkToResults;
  }-*/;
  
  private static native void setLinkToResults(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult::linkToResults = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instance) throws SerializationException {
    setErrorMessage(instance, streamReader.readString());
    setLinkToRatios(instance, streamReader.readString());
    setLinkToResults(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult instance) throws SerializationException {
    streamWriter.writeString(getErrorMessage(instance));
    streamWriter.writeString(getLinkToRatios(instance));
    streamWriter.writeString(getLinkToResults(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult)object);
  }
  
}
