package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ProjectTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean instance) throws SerializationException {
    instance.description = streamReader.readString();
    instance.experimentalConditions = (edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean) streamReader.readObject();
    instance.experimentalDesign = (edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean) streamReader.readObject();
    instance.msRuns = (edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean) streamReader.readObject();
    instance.name = streamReader.readString();
    instance.quantitative = (java.lang.Boolean) streamReader.readObject();
    instance.ratios = (edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean) streamReader.readObject();
    instance.releaseDate = (java.util.Date) streamReader.readObject();
    instance.tag = streamReader.readString();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean instance) throws SerializationException {
    streamWriter.writeString(instance.description);
    streamWriter.writeObject(instance.experimentalConditions);
    streamWriter.writeObject(instance.experimentalDesign);
    streamWriter.writeObject(instance.msRuns);
    streamWriter.writeString(instance.name);
    streamWriter.writeObject(instance.quantitative);
    streamWriter.writeObject(instance.ratios);
    streamWriter.writeObject(instance.releaseDate);
    streamWriter.writeString(instance.tag);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean)object);
  }
  
}
