package edu.scripps.yates.shared.util.sublists;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class DataSubList_FieldSerializer {
  private static native java.util.List getDataList(edu.scripps.yates.shared.util.sublists.DataSubList instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.DataSubList::dataList;
  }-*/;
  
  private static native void setDataList(edu.scripps.yates.shared.util.sublists.DataSubList instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.DataSubList::dataList = value;
  }-*/;
  
  private static native int getTotalNumber(edu.scripps.yates.shared.util.sublists.DataSubList instance) /*-{
    return instance.@edu.scripps.yates.shared.util.sublists.DataSubList::totalNumber;
  }-*/;
  
  private static native void setTotalNumber(edu.scripps.yates.shared.util.sublists.DataSubList instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.sublists.DataSubList::totalNumber = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.sublists.DataSubList instance) throws SerializationException {
    setDataList(instance, (java.util.List) streamReader.readObject());
    setTotalNumber(instance, streamReader.readInt());
    
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.sublists.DataSubList instance) throws SerializationException {
    streamWriter.writeObject(getDataList(instance));
    streamWriter.writeInt(getTotalNumber(instance));
    
  }
  
}
