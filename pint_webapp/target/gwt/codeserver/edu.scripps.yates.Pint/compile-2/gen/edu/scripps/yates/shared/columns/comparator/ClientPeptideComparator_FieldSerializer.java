package edu.scripps.yates.shared.columns.comparator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class ClientPeptideComparator_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator instance) throws SerializationException {
    
    edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator instance) throws SerializationException {
    
    edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator)object);
  }
  
}
