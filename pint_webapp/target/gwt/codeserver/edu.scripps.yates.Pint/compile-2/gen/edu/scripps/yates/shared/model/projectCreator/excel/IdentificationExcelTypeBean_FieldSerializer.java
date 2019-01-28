package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class IdentificationExcelTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean instance) throws SerializationException {
    instance.discardDecoys = streamReader.readString();
    instance.msRunRef = streamReader.readString();
    instance.peptideScore = (java.util.List) streamReader.readObject();
    instance.proteinAccession = (edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean) streamReader.readObject();
    instance.proteinAnnotations = (edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean) streamReader.readObject();
    instance.proteinDescription = (edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean) streamReader.readObject();
    instance.proteinScore = (java.util.List) streamReader.readObject();
    instance.proteinThresholds = (edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean) streamReader.readObject();
    instance.psmScore = (java.util.List) streamReader.readObject();
    instance.ptmScore = (java.util.List) streamReader.readObject();
    instance.sequence = (edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean) streamReader.readObject();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean instance) throws SerializationException {
    streamWriter.writeString(instance.discardDecoys);
    streamWriter.writeString(instance.msRunRef);
    streamWriter.writeObject(instance.peptideScore);
    streamWriter.writeObject(instance.proteinAccession);
    streamWriter.writeObject(instance.proteinAnnotations);
    streamWriter.writeObject(instance.proteinDescription);
    streamWriter.writeObject(instance.proteinScore);
    streamWriter.writeObject(instance.proteinThresholds);
    streamWriter.writeObject(instance.psmScore);
    streamWriter.writeObject(instance.ptmScore);
    streamWriter.writeObject(instance.sequence);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean)object);
  }
  
}
