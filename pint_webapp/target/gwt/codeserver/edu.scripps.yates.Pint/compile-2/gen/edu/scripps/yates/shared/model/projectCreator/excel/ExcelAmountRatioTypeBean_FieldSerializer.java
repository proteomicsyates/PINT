package edu.scripps.yates.shared.model.projectCreator.excel;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ExcelAmountRatioTypeBean_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean instance) throws SerializationException {
    instance.columnRef = streamReader.readString();
    instance.denominator = streamReader.readString();
    instance.msRunRef = streamReader.readString();
    instance.name = streamReader.readString();
    instance.numerator = streamReader.readString();
    instance.peptideSequence = (edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean) streamReader.readObject();
    instance.proteinAccession = (edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean) streamReader.readObject();
    instance.psmId = (edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean) streamReader.readObject();
    instance.ratioScore = (edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean) streamReader.readObject();
    
  }
  
  public static edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean instance) throws SerializationException {
    streamWriter.writeString(instance.columnRef);
    streamWriter.writeString(instance.denominator);
    streamWriter.writeString(instance.msRunRef);
    streamWriter.writeString(instance.name);
    streamWriter.writeString(instance.numerator);
    streamWriter.writeObject(instance.peptideSequence);
    streamWriter.writeObject(instance.proteinAccession);
    streamWriter.writeObject(instance.psmId);
    streamWriter.writeObject(instance.ratioScore);
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean)object);
  }
  
}
