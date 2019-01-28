package edu.scripps.yates.shared.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class MSRunBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.util.Date getDate(edu.scripps.yates.shared.model.MSRunBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.MSRunBean::date;
  }-*/;
  
  private static native void setDate(edu.scripps.yates.shared.model.MSRunBean instance, java.util.Date value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.MSRunBean::date = value;
  }-*/;
  
  private static native java.lang.Integer getDbID(edu.scripps.yates.shared.model.MSRunBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.MSRunBean::dbID;
  }-*/;
  
  private static native void setDbID(edu.scripps.yates.shared.model.MSRunBean instance, java.lang.Integer value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.MSRunBean::dbID = value;
  }-*/;
  
  private static native java.lang.String getEnzymeNocutResidues(edu.scripps.yates.shared.model.MSRunBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.MSRunBean::enzymeNocutResidues;
  }-*/;
  
  private static native void setEnzymeNocutResidues(edu.scripps.yates.shared.model.MSRunBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.MSRunBean::enzymeNocutResidues = value;
  }-*/;
  
  private static native java.lang.String getEnzymeResidues(edu.scripps.yates.shared.model.MSRunBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.MSRunBean::enzymeResidues;
  }-*/;
  
  private static native void setEnzymeResidues(edu.scripps.yates.shared.model.MSRunBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.MSRunBean::enzymeResidues = value;
  }-*/;
  
  private static native java.lang.String getFastaFileRef(edu.scripps.yates.shared.model.MSRunBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.MSRunBean::fastaFileRef;
  }-*/;
  
  private static native void setFastaFileRef(edu.scripps.yates.shared.model.MSRunBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.MSRunBean::fastaFileRef = value;
  }-*/;
  
  private static native java.lang.String getPath(edu.scripps.yates.shared.model.MSRunBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.MSRunBean::path;
  }-*/;
  
  private static native void setPath(edu.scripps.yates.shared.model.MSRunBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.MSRunBean::path = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.ProjectBean getProject(edu.scripps.yates.shared.model.MSRunBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.MSRunBean::project;
  }-*/;
  
  private static native void setProject(edu.scripps.yates.shared.model.MSRunBean instance, edu.scripps.yates.shared.model.ProjectBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.MSRunBean::project = value;
  }-*/;
  
  private static native java.lang.String getRunID(edu.scripps.yates.shared.model.MSRunBean instance) /*-{
    return instance.@edu.scripps.yates.shared.model.MSRunBean::runID;
  }-*/;
  
  private static native void setRunID(edu.scripps.yates.shared.model.MSRunBean instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.model.MSRunBean::runID = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.MSRunBean instance) throws SerializationException {
    setDate(instance, (java.util.Date) streamReader.readObject());
    setDbID(instance, (java.lang.Integer) streamReader.readObject());
    setEnzymeNocutResidues(instance, streamReader.readString());
    setEnzymeResidues(instance, streamReader.readString());
    setFastaFileRef(instance, streamReader.readString());
    setPath(instance, streamReader.readString());
    setProject(instance, (edu.scripps.yates.shared.model.ProjectBean) streamReader.readObject());
    setRunID(instance, streamReader.readString());
    
  }
  
  public static edu.scripps.yates.shared.model.MSRunBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.MSRunBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.MSRunBean instance) throws SerializationException {
    streamWriter.writeObject(getDate(instance));
    streamWriter.writeObject(getDbID(instance));
    streamWriter.writeString(getEnzymeNocutResidues(instance));
    streamWriter.writeString(getEnzymeResidues(instance));
    streamWriter.writeString(getFastaFileRef(instance));
    streamWriter.writeString(getPath(instance));
    streamWriter.writeObject(getProject(instance));
    streamWriter.writeString(getRunID(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.MSRunBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.MSRunBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.MSRunBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.MSRunBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.MSRunBean)object);
  }
  
}
