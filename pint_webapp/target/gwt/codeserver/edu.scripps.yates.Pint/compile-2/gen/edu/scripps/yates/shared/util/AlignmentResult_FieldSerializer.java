package edu.scripps.yates.shared.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class AlignmentResult_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native int getAlignmentLength(edu.scripps.yates.shared.util.AlignmentResult instance) /*-{
    return instance.@edu.scripps.yates.shared.util.AlignmentResult::alignmentLength;
  }-*/;
  
  private static native void setAlignmentLength(edu.scripps.yates.shared.util.AlignmentResult instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.AlignmentResult::alignmentLength = value;
  }-*/;
  
  private static native java.lang.String getAlignmentString(edu.scripps.yates.shared.util.AlignmentResult instance) /*-{
    return instance.@edu.scripps.yates.shared.util.AlignmentResult::alignmentString;
  }-*/;
  
  private static native void setAlignmentString(edu.scripps.yates.shared.util.AlignmentResult instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.AlignmentResult::alignmentString = value;
  }-*/;
  
  private static native int getFinalAlignmentScore(edu.scripps.yates.shared.util.AlignmentResult instance) /*-{
    return instance.@edu.scripps.yates.shared.util.AlignmentResult::finalAlignmentScore;
  }-*/;
  
  private static native void setFinalAlignmentScore(edu.scripps.yates.shared.util.AlignmentResult instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.AlignmentResult::finalAlignmentScore = value;
  }-*/;
  
  private static native int getIdenticalLength(edu.scripps.yates.shared.util.AlignmentResult instance) /*-{
    return instance.@edu.scripps.yates.shared.util.AlignmentResult::identicalLength;
  }-*/;
  
  private static native void setIdenticalLength(edu.scripps.yates.shared.util.AlignmentResult instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.AlignmentResult::identicalLength = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.PeptideBean getSeq1(edu.scripps.yates.shared.util.AlignmentResult instance) /*-{
    return instance.@edu.scripps.yates.shared.util.AlignmentResult::seq1;
  }-*/;
  
  private static native void setSeq1(edu.scripps.yates.shared.util.AlignmentResult instance, edu.scripps.yates.shared.model.PeptideBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.AlignmentResult::seq1 = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.model.PeptideBean getSeq2(edu.scripps.yates.shared.util.AlignmentResult instance) /*-{
    return instance.@edu.scripps.yates.shared.util.AlignmentResult::seq2;
  }-*/;
  
  private static native void setSeq2(edu.scripps.yates.shared.util.AlignmentResult instance, edu.scripps.yates.shared.model.PeptideBean value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.AlignmentResult::seq2 = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.AlignmentResult instance) throws SerializationException {
    setAlignmentLength(instance, streamReader.readInt());
    setAlignmentString(instance, streamReader.readString());
    setFinalAlignmentScore(instance, streamReader.readInt());
    setIdenticalLength(instance, streamReader.readInt());
    setSeq1(instance, (edu.scripps.yates.shared.model.PeptideBean) streamReader.readObject());
    setSeq2(instance, (edu.scripps.yates.shared.model.PeptideBean) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.util.AlignmentResult instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.util.AlignmentResult();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.AlignmentResult instance) throws SerializationException {
    streamWriter.writeInt(getAlignmentLength(instance));
    streamWriter.writeString(getAlignmentString(instance));
    streamWriter.writeInt(getFinalAlignmentScore(instance));
    streamWriter.writeInt(getIdenticalLength(instance));
    streamWriter.writeObject(getSeq1(instance));
    streamWriter.writeObject(getSeq2(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.util.AlignmentResult_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.AlignmentResult_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.util.AlignmentResult)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.AlignmentResult_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.util.AlignmentResult)object);
  }
  
}
