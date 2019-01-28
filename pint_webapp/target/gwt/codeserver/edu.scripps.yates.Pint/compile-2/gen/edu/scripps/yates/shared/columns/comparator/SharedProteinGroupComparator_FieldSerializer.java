package edu.scripps.yates.shared.columns.comparator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class SharedProteinGroupComparator_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator instance) throws SerializationException {
    
    edu.scripps.yates.shared.columns.comparator.AbstractProteinGroupBeanComparator_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator instance) throws SerializationException {
    
    edu.scripps.yates.shared.columns.comparator.AbstractProteinGroupBeanComparator_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator)object);
  }
  
}
