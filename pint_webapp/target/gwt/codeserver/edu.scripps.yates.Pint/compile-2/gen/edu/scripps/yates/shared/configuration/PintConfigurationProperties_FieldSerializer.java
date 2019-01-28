package edu.scripps.yates.shared.configuration;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class PintConfigurationProperties_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.lang.String getAdminPassword(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::adminPassword;
  }-*/;
  
  private static native void setAdminPassword(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::adminPassword = value;
  }-*/;
  
  private static native java.lang.String getDb_password(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::db_password;
  }-*/;
  
  private static native void setDb_password(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::db_password = value;
  }-*/;
  
  private static native java.lang.String getDb_url(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::db_url;
  }-*/;
  
  private static native void setDb_url(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::db_url = value;
  }-*/;
  
  private static native java.lang.String getDb_username(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::db_username;
  }-*/;
  
  private static native void setDb_username(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::db_username = value;
  }-*/;
  
  private static native java.lang.String getOmimKey(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::omimKey;
  }-*/;
  
  private static native void setOmimKey(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::omimKey = value;
  }-*/;
  
  private static native java.lang.Boolean getPreLoadPublicProjects(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::preLoadPublicProjects;
  }-*/;
  
  private static native void setPreLoadPublicProjects(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.Boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::preLoadPublicProjects = value;
  }-*/;
  
  private static native java.lang.String getProjectsToNotPreLoad(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::projectsToNotPreLoad;
  }-*/;
  
  private static native void setProjectsToNotPreLoad(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::projectsToNotPreLoad = value;
  }-*/;
  
  private static native java.lang.String getProjectsToPreLoad(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::projectsToPreLoad;
  }-*/;
  
  private static native void setProjectsToPreLoad(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::projectsToPreLoad = value;
  }-*/;
  
  private static native java.lang.Boolean getPsmCentric(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) /*-{
    return instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::psmCentric;
  }-*/;
  
  private static native void setPsmCentric(edu.scripps.yates.shared.configuration.PintConfigurationProperties instance, java.lang.Boolean value) 
  /*-{
    instance.@edu.scripps.yates.shared.configuration.PintConfigurationProperties::psmCentric = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) throws SerializationException {
    setAdminPassword(instance, streamReader.readString());
    setDb_password(instance, streamReader.readString());
    setDb_url(instance, streamReader.readString());
    setDb_username(instance, streamReader.readString());
    setOmimKey(instance, streamReader.readString());
    setPreLoadPublicProjects(instance, (java.lang.Boolean) streamReader.readObject());
    setProjectsToNotPreLoad(instance, streamReader.readString());
    setProjectsToPreLoad(instance, streamReader.readString());
    setPsmCentric(instance, (java.lang.Boolean) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.configuration.PintConfigurationProperties instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.configuration.PintConfigurationProperties();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.configuration.PintConfigurationProperties instance) throws SerializationException {
    streamWriter.writeString(getAdminPassword(instance));
    streamWriter.writeString(getDb_password(instance));
    streamWriter.writeString(getDb_url(instance));
    streamWriter.writeString(getDb_username(instance));
    streamWriter.writeString(getOmimKey(instance));
    streamWriter.writeObject(getPreLoadPublicProjects(instance));
    streamWriter.writeString(getProjectsToNotPreLoad(instance));
    streamWriter.writeString(getProjectsToPreLoad(instance));
    streamWriter.writeObject(getPsmCentric(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.configuration.PintConfigurationProperties_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.configuration.PintConfigurationProperties_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.configuration.PintConfigurationProperties)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.configuration.PintConfigurationProperties_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.configuration.PintConfigurationProperties)object);
  }
  
}
