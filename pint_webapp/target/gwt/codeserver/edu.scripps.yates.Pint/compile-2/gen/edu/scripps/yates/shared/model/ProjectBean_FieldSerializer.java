package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProjectBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native boolean getBig(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::big;
  }-*/;
  
  private static native void setBig(edu.scripps.yates.shared.model.ProjectBean instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::big = value;
  }-*/;
  
  private static native java.util.List getConditions(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::conditions;
  }-*/;
  
  private static native void setConditions(edu.scripps.yates.shared.model.ProjectBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::conditions = value;
  }-*/;
  
  private static native java.lang.Integer getDbId(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::dbId;
  }-*/;
  
  private static native void setDbId(edu.scripps.yates.shared.model.ProjectBean instance, java.lang.Integer value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::dbId = value;
  }-*/;
  
  private static native java.lang.String getDescription(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::description;
  }-*/;
  
  private static native void setDescription(edu.scripps.yates.shared.model.ProjectBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::description = value;
  }-*/;
  
  private static native boolean getHidden(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::hidden;
  }-*/;
  
  private static native void setHidden(edu.scripps.yates.shared.model.ProjectBean instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::hidden = value;
  }-*/;
  
  private static native java.util.List getMsRuns(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::msRuns;
  }-*/;
  
  private static native void setMsRuns(edu.scripps.yates.shared.model.ProjectBean instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::msRuns = value;
  }-*/;
  
  private static native java.lang.String getName(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::name;
  }-*/;
  
  private static native void setName(edu.scripps.yates.shared.model.ProjectBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::name = value;
  }-*/;
  
  private static native boolean getPublicAvailable(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::publicAvailable;
  }-*/;
  
  private static native void setPublicAvailable(edu.scripps.yates.shared.model.ProjectBean instance, boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::publicAvailable = value;
  }-*/;
  
  private static native java.lang.String getPubmedLink(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::pubmedLink;
  }-*/;
  
  private static native void setPubmedLink(edu.scripps.yates.shared.model.ProjectBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::pubmedLink = value;
  }-*/;
  
  private static native java.util.Date getReleaseDate(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::releaseDate;
  }-*/;
  
  private static native void setReleaseDate(edu.scripps.yates.shared.model.ProjectBean instance, java.util.Date value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::releaseDate = value;
  }-*/;
  
  private static native java.lang.String getTag(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::tag;
  }-*/;
  
  private static native void setTag(edu.scripps.yates.shared.model.ProjectBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::tag = value;
  }-*/;
  
  private static native java.util.Date getUploadedDate(edu.scripps.yates.shared.model.ProjectBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.ProjectBean::uploadedDate;
  }-*/;
  
  private static native void setUploadedDate(edu.scripps.yates.shared.model.ProjectBean instance, java.util.Date value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.ProjectBean::uploadedDate = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.ProjectBean instance) throws SerializationException {
    setBig(instance, streamReader.readBoolean());
    setConditions(instance, (java.util.List) streamReader.readObject());
    setDbId(instance, (java.lang.Integer) streamReader.readObject());
    setDescription(instance, streamReader.readString());
    setHidden(instance, streamReader.readBoolean());
    setMsRuns(instance, (java.util.List) streamReader.readObject());
    setName(instance, streamReader.readString());
    setPublicAvailable(instance, streamReader.readBoolean());
    setPubmedLink(instance, streamReader.readString());
    setReleaseDate(instance, (java.util.Date) streamReader.readObject());
    setTag(instance, streamReader.readString());
    setUploadedDate(instance, (java.util.Date) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.model.ProjectBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.ProjectBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.ProjectBean instance) throws SerializationException {
    streamWriter.writeBoolean(getBig(instance));
    streamWriter.writeObject(getConditions(instance));
    streamWriter.writeObject(getDbId(instance));
    streamWriter.writeString(getDescription(instance));
    streamWriter.writeBoolean(getHidden(instance));
    streamWriter.writeObject(getMsRuns(instance));
    streamWriter.writeString(getName(instance));
    streamWriter.writeBoolean(getPublicAvailable(instance));
    streamWriter.writeString(getPubmedLink(instance));
    streamWriter.writeObject(getReleaseDate(instance));
    streamWriter.writeString(getTag(instance));
    streamWriter.writeObject(getUploadedDate(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.ProjectBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProjectBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.ProjectBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.ProjectBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.ProjectBean)object);
  }
  
}
