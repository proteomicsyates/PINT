package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class OrganismBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getName(edu.scripps.yates.shared.model.OrganismBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.OrganismBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.OrganismBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.OrganismBean::name = value;
  }-*/;
  
  private static native java.lang.String getNcbiTaxID(edu.scripps.yates.shared.model.OrganismBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.OrganismBean::ncbiTaxID;
  }-*/;
  
  private static native void setNcbiTaxID(edu.scripps.yates.shared.model.OrganismBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.OrganismBean::ncbiTaxID = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.OrganismBean instance) throws SerializationException {
    setName(instance, streamReader.readString());
    setNcbiTaxID(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.OrganismBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.OrganismBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.OrganismBean instance) throws SerializationException {
    streamWriter.writeString(getName(instance));
    streamWriter.writeString(getNcbiTaxID(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.OrganismBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.OrganismBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.OrganismBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.OrganismBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.OrganismBean)object);
  }
  
}
