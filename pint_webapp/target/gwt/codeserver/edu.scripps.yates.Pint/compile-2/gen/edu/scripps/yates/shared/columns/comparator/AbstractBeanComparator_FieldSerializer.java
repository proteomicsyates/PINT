package edu.scripps.yates.shared.columns.comparator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class AbstractBeanComparator_FieldSerializer {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.columns.comparator.AbstractBeanComparator instance) throws SerializationException {
    instance.amountType = (edu.scripps.yates.shared.model.AmountType) streamReader.readObject();
    instance.columnName = (edu.scripps.yates.shared.columns.ColumnName) streamReader.readObject();
    instance.condition2Name = streamReader.readString();
    instance.conditionName = streamReader.readString();
    instance.projectTag = streamReader.readString();
    instance.ratioName = streamReader.readString();
    instance.scoreName = streamReader.readString();
    
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.columns.comparator.AbstractBeanComparator instance) throws SerializationException {
    streamWriter.writeObject(instance.amountType);
    streamWriter.writeObject(instance.columnName);
    streamWriter.writeString(instance.condition2Name);
    streamWriter.writeString(instance.conditionName);
    streamWriter.writeString(instance.projectTag);
    streamWriter.writeString(instance.ratioName);
    streamWriter.writeString(instance.scoreName);
    
  }
  
}
