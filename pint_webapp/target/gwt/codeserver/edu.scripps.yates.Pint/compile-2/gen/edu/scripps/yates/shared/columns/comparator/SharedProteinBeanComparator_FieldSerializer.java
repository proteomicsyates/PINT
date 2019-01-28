package edu.scripps.yates.shared.columns.comparator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class SharedProteinBeanComparator_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator instance) throws SerializationException {
    
    edu.scripps.yates.shared.columns.comparator.AbstractProteinBeanComparator_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator instance) throws SerializationException {
    
    edu.scripps.yates.shared.columns.comparator.AbstractProteinBeanComparator_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator)object);
  }
  
}
