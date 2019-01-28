package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ExperimentalDesignTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean instance) throws SerializationException {
    instance.labelSet = (edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean) streamReader.readObject();
    instance.organismSet = (edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean) streamReader.readObject();
    instance.sampleSet = (edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean) streamReader.readObject();
    instance.tissueSet = (edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean) streamReader.readObject();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean instance) throws SerializationException {
    streamWriter.writeObject(instance.labelSet);
    streamWriter.writeObject(instance.organismSet);
    streamWriter.writeObject(instance.sampleSet);
    streamWriter.writeObject(instance.tissueSet);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean)object);
  }
  
}
