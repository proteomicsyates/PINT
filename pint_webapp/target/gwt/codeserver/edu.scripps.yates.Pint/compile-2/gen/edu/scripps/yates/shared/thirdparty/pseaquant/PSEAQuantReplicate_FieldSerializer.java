package edu.scripps.yates.shared.thirdparty.pseaquant;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PSEAQuantReplicate_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native boolean getCondition(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instance) /*-{
    return instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate::condition;
  }-*/;
  
  private static native void setCondition(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate::condition = value;
  }-*/;
  
  private static native java.util.List getListOfPairs(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instance) /*-{
    return instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate::listOfPairs;
  }-*/;
  
  private static native void setListOfPairs(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate::listOfPairs = value;
  }-*/;
  
  private static native boolean getMsRun(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instance) /*-{
    return instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate::msRun;
  }-*/;
  
  private static native void setMsRun(edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate::msRun = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instance) throws SerializationException {
    setCondition(instance, streamReader.readBoolean());
    setListOfPairs(instance, (java.util.List) streamReader.readObject());
    setMsRun(instance, streamReader.readBoolean());
    
  }
  
  public static edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate instance) throws SerializationException {
    streamWriter.writeBoolean(getCondition(instance));
    streamWriter.writeObject(getListOfPairs(instance));
    streamWriter.writeBoolean(getMsRun(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate)object);
  }
  
}
