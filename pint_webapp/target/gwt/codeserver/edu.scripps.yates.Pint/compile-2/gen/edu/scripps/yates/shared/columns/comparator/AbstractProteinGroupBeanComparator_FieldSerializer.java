package edu.scripps.yates.shared.columns.comparator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class AbstractProteinGroupBeanComparator_FieldSerializer {
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.columns.comparator.AbstractProteinGroupBeanComparator instance) throws SerializationException {
    
    edu.scripps.yates.shared.columns.comparator.AbstractBeanComparator_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.columns.comparator.AbstractProteinGroupBeanComparator instance) throws SerializationException {
    
    edu.scripps.yates.shared.columns.comparator.AbstractBeanComparator_FieldSerializer.serialize(streamWriter, instance);
  }
  
}
