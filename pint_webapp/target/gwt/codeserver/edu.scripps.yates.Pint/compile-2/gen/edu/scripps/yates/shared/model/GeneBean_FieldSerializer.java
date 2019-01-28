package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class GeneBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getGeneID(edu.scripps.yates.shared.model.GeneBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.GeneBean::geneID;
  }-*/;
  
  private static native void setGeneID(edu.scripps.yates.shared.model.GeneBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.GeneBean::geneID = value;
  }-*/;
  
  private static native java.lang.String getGeneType(edu.scripps.yates.shared.model.GeneBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.GeneBean::geneType;
  }-*/;
  
  private static native void setGeneType(edu.scripps.yates.shared.model.GeneBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.GeneBean::geneType = value;
  }-*/;
  
  private static native int getHgncNumber(edu.scripps.yates.shared.model.GeneBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.GeneBean::hgncNumber;
  }-*/;
  
  private static native void setHgncNumber(edu.scripps.yates.shared.model.GeneBean instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.GeneBean::hgncNumber = value;
  }-*/;
  
  private static native java.lang.String getName(edu.scripps.yates.shared.model.GeneBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.GeneBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.GeneBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.GeneBean::name = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.GeneBean instance) throws SerializationException {
    setGeneID(instance, streamReader.readString());
    setGeneType(instance, streamReader.readString());
    setHgncNumber(instance, streamReader.readInt());
    setName(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.GeneBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.GeneBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.GeneBean instance) throws SerializationException {
    streamWriter.writeString(getGeneID(instance));
    streamWriter.writeString(getGeneType(instance));
    streamWriter.writeInt(getHgncNumber(instance));
    streamWriter.writeString(getName(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.GeneBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.GeneBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.GeneBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.GeneBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.GeneBean)object);
  }
  
}
