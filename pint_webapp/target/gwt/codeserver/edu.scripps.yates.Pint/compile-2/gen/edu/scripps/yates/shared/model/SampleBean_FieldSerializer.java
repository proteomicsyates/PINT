package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class SampleBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.SampleBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.SampleBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.SampleBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.SampleBean::description = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.LabelBean getLabel(edu.scripps.yates.shared.model.SampleBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.SampleBean::label;
  }-*/;
  
  private static native void setLabel(edu.scripps.yates.shared.model.SampleBean instance, edu.scripps.yates.shared.model.LabelBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.SampleBean::label = value;
  }-*/;
  
  private static native java.lang.String getName(edu.scripps.yates.shared.model.SampleBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.SampleBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.SampleBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.SampleBean::name = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.OrganismBean getOrganism(edu.scripps.yates.shared.model.SampleBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.SampleBean::organism;
  }-*/;
  
  private static native void setOrganism(edu.scripps.yates.shared.model.SampleBean instance, edu.scripps.yates.shared.model.OrganismBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.SampleBean::organism = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ProjectBean getProject(edu.scripps.yates.shared.model.SampleBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.SampleBean::project;
  }-*/;
  
  private static native void setProject(edu.scripps.yates.shared.model.SampleBean instance, edu.scripps.yates.shared.model.ProjectBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.SampleBean::project = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.TissueBean getTissue(edu.scripps.yates.shared.model.SampleBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.SampleBean::tissue;
  }-*/;
  
  private static native void setTissue(edu.scripps.yates.shared.model.SampleBean instance, edu.scripps.yates.shared.model.TissueBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.SampleBean::tissue = value;
  }-*/;
  
  private static native boolean getWildType(edu.scripps.yates.shared.model.SampleBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.SampleBean::wildType;
  }-*/;
  
  private static native void setWildType(edu.scripps.yates.shared.model.SampleBean instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.SampleBean::wildType = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.SampleBean instance) throws SerializationException {
    setDescription(instance, streamReader.readString());
    setLabel(instance, (edu.scripps.yates.shared.model.LabelBean) streamReader.readObject());
    setName(instance, streamReader.readString());
    setOrganism(instance, (edu.scripps.yates.shared.model.OrganismBean) streamReader.readObject());
    setProject(instance, (edu.scripps.yates.shared.model.ProjectBean) streamReader.readObject());
    setTissue(instance, (edu.scripps.yates.shared.model.TissueBean) streamReader.readObject());
    setWildType(instance, streamReader.readBoolean());
    
  }
  
  public static edu.scripps.yates.shared.model.SampleBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.SampleBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.SampleBean instance) throws SerializationException {
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeObject(getLabel(instance));
    streamWriter.writeString(getName(instance));
    streamWriter.writeObject(getOrganism(instance));
    streamWriter.writeObject(getProject(instance));
    streamWriter.writeObject(getTissue(instance));
    streamWriter.writeBoolean(getWildType(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.SampleBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.SampleBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.SampleBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.SampleBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.SampleBean)object);
  }
  
}
