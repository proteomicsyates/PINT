package edu.scripps.yates.shared.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class DefaultView_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native edu.scripps.yates.shared.util.DefaultView.TAB getDefaultTab(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::defaultTab;
  }-*/;
  
  private static native void setDefaultTab(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.util.DefaultView.TAB value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::defaultTab = value;
  }-*/;
  
  private static native java.util.Set getHiddenPTMs(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::hiddenPTMs;
  }-*/;
  
  private static native void setHiddenPTMs(edu.scripps.yates.shared.util.DefaultView instance, java.util.Set value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::hiddenPTMs = value;
  }-*/;
  
  private static native java.util.List getPeptideDefaultView(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::peptideDefaultView;
  }-*/;
  
  private static native void setPeptideDefaultView(edu.scripps.yates.shared.util.DefaultView instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::peptideDefaultView = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.DefaultView.ORDER getPeptideOrder(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::peptideOrder;
  }-*/;
  
  private static native void setPeptideOrder(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.util.DefaultView.ORDER value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::peptideOrder = value;
  }-*/;
  
  private static native int getPeptidePageSize(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::peptidePageSize;
  }-*/;
  
  private static native void setPeptidePageSize(edu.scripps.yates.shared.util.DefaultView instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::peptidePageSize = value;
  }-*/;
  
  private static native java.lang.String getPeptideSortingScore(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::peptideSortingScore;
  }-*/;
  
  private static native void setPeptideSortingScore(edu.scripps.yates.shared.util.DefaultView instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::peptideSortingScore = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.columns.ColumnName getPeptidesSortedBy(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::peptidesSortedBy;
  }-*/;
  
  private static native void setPeptidesSortedBy(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.columns.ColumnName value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::peptidesSortedBy = value;
  }-*/;
  
  private static native java.lang.String getProjectDescription(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::projectDescription;
  }-*/;
  
  private static native void setProjectDescription(edu.scripps.yates.shared.util.DefaultView instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::projectDescription = value;
  }-*/;
  
  private static native java.lang.String getProjectInstructions(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::projectInstructions;
  }-*/;
  
  private static native void setProjectInstructions(edu.scripps.yates.shared.util.DefaultView instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::projectInstructions = value;
  }-*/;
  
  private static native java.util.List getProjectNamedQueries(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::projectNamedQueries;
  }-*/;
  
  private static native void setProjectNamedQueries(edu.scripps.yates.shared.util.DefaultView instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::projectNamedQueries = value;
  }-*/;
  
  private static native java.lang.String getProjectTag(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::projectTag;
  }-*/;
  
  private static native void setProjectTag(edu.scripps.yates.shared.util.DefaultView instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::projectTag = value;
  }-*/;
  
  private static native java.lang.String getProjectViewComments(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::projectViewComments;
  }-*/;
  
  private static native void setProjectViewComments(edu.scripps.yates.shared.util.DefaultView instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::projectViewComments = value;
  }-*/;
  
  private static native java.util.List getProteinDefaultView(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinDefaultView;
  }-*/;
  
  private static native void setProteinDefaultView(edu.scripps.yates.shared.util.DefaultView instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinDefaultView = value;
  }-*/;
  
  private static native java.util.List getProteinGroupDefaultView(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupDefaultView;
  }-*/;
  
  private static native void setProteinGroupDefaultView(edu.scripps.yates.shared.util.DefaultView instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupDefaultView = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.DefaultView.ORDER getProteinGroupOrder(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupOrder;
  }-*/;
  
  private static native void setProteinGroupOrder(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.util.DefaultView.ORDER value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupOrder = value;
  }-*/;
  
  private static native int getProteinGroupPageSize(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupPageSize;
  }-*/;
  
  private static native void setProteinGroupPageSize(edu.scripps.yates.shared.util.DefaultView instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupPageSize = value;
  }-*/;
  
  private static native java.lang.String getProteinGroupSortingScore(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupSortingScore;
  }-*/;
  
  private static native void setProteinGroupSortingScore(edu.scripps.yates.shared.util.DefaultView instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupSortingScore = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.columns.ColumnName getProteinGroupsSortedBy(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupsSortedBy;
  }-*/;
  
  private static native void setProteinGroupsSortedBy(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.columns.ColumnName value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinGroupsSortedBy = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.DefaultView.ORDER getProteinOrder(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinOrder;
  }-*/;
  
  private static native void setProteinOrder(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.util.DefaultView.ORDER value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinOrder = value;
  }-*/;
  
  private static native int getProteinPageSize(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinPageSize;
  }-*/;
  
  private static native void setProteinPageSize(edu.scripps.yates.shared.util.DefaultView instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinPageSize = value;
  }-*/;
  
  private static native java.lang.String getProteinSortingScore(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinSortingScore;
  }-*/;
  
  private static native void setProteinSortingScore(edu.scripps.yates.shared.util.DefaultView instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinSortingScore = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.columns.ColumnName getProteinsSortedBy(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::proteinsSortedBy;
  }-*/;
  
  private static native void setProteinsSortedBy(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.columns.ColumnName value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::proteinsSortedBy = value;
  }-*/;
  
  private static native java.util.List getPsmDefaultView(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::psmDefaultView;
  }-*/;
  
  private static native void setPsmDefaultView(edu.scripps.yates.shared.util.DefaultView instance, java.util.List value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::psmDefaultView = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.util.DefaultView.ORDER getPsmOrder(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::psmOrder;
  }-*/;
  
  private static native void setPsmOrder(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.util.DefaultView.ORDER value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::psmOrder = value;
  }-*/;
  
  private static native int getPsmPageSize(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::psmPageSize;
  }-*/;
  
  private static native void setPsmPageSize(edu.scripps.yates.shared.util.DefaultView instance, int value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::psmPageSize = value;
  }-*/;
  
  private static native java.lang.String getPsmSortingScore(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::psmSortingScore;
  }-*/;
  
  private static native void setPsmSortingScore(edu.scripps.yates.shared.util.DefaultView instance, java.lang.String value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::psmSortingScore = value;
  }-*/;
  
  private static native edu.scripps.yates.shared.columns.ColumnName getPsmsSortedBy(edu.scripps.yates.shared.util.DefaultView instance) /*-{
    return instance.@edu.scripps.yates.shared.util.DefaultView::psmsSortedBy;
  }-*/;
  
  private static native void setPsmsSortedBy(edu.scripps.yates.shared.util.DefaultView instance, edu.scripps.yates.shared.columns.ColumnName value) 
  /*-{
    instance.@edu.scripps.yates.shared.util.DefaultView::psmsSortedBy = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.scripps.yates.shared.util.DefaultView instance) throws SerializationException {
    setDefaultTab(instance, (edu.scripps.yates.shared.util.DefaultView.TAB) streamReader.readObject());
    setHiddenPTMs(instance, (java.util.Set) streamReader.readObject());
    setPeptideDefaultView(instance, (java.util.List) streamReader.readObject());
    setPeptideOrder(instance, (edu.scripps.yates.shared.util.DefaultView.ORDER) streamReader.readObject());
    setPeptidePageSize(instance, streamReader.readInt());
    setPeptideSortingScore(instance, streamReader.readString());
    setPeptidesSortedBy(instance, (edu.scripps.yates.shared.columns.ColumnName) streamReader.readObject());
    setProjectDescription(instance, streamReader.readString());
    setProjectInstructions(instance, streamReader.readString());
    setProjectNamedQueries(instance, (java.util.List) streamReader.readObject());
    setProjectTag(instance, streamReader.readString());
    setProjectViewComments(instance, streamReader.readString());
    setProteinDefaultView(instance, (java.util.List) streamReader.readObject());
    setProteinGroupDefaultView(instance, (java.util.List) streamReader.readObject());
    setProteinGroupOrder(instance, (edu.scripps.yates.shared.util.DefaultView.ORDER) streamReader.readObject());
    setProteinGroupPageSize(instance, streamReader.readInt());
    setProteinGroupSortingScore(instance, streamReader.readString());
    setProteinGroupsSortedBy(instance, (edu.scripps.yates.shared.columns.ColumnName) streamReader.readObject());
    setProteinOrder(instance, (edu.scripps.yates.shared.util.DefaultView.ORDER) streamReader.readObject());
    setProteinPageSize(instance, streamReader.readInt());
    setProteinSortingScore(instance, streamReader.readString());
    setProteinsSortedBy(instance, (edu.scripps.yates.shared.columns.ColumnName) streamReader.readObject());
    setPsmDefaultView(instance, (java.util.List) streamReader.readObject());
    setPsmOrder(instance, (edu.scripps.yates.shared.util.DefaultView.ORDER) streamReader.readObject());
    setPsmPageSize(instance, streamReader.readInt());
    setPsmSortingScore(instance, streamReader.readString());
    setPsmsSortedBy(instance, (edu.scripps.yates.shared.columns.ColumnName) streamReader.readObject());
    
  }
  
  public static edu.scripps.yates.shared.util.DefaultView instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.scripps.yates.shared.util.DefaultView();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.scripps.yates.shared.util.DefaultView instance) throws SerializationException {
    streamWriter.writeObject(getDefaultTab(instance));
    streamWriter.writeObject(getHiddenPTMs(instance));
    streamWriter.writeObject(getPeptideDefaultView(instance));
    streamWriter.writeObject(getPeptideOrder(instance));
    streamWriter.writeInt(getPeptidePageSize(instance));
    streamWriter.writeString(getPeptideSortingScore(instance));
    streamWriter.writeObject(getPeptidesSortedBy(instance));
    streamWriter.writeString(getProjectDescription(instance));
    streamWriter.writeString(getProjectInstructions(instance));
    streamWriter.writeObject(getProjectNamedQueries(instance));
    streamWriter.writeString(getProjectTag(instance));
    streamWriter.writeString(getProjectViewComments(instance));
    streamWriter.writeObject(getProteinDefaultView(instance));
    streamWriter.writeObject(getProteinGroupDefaultView(instance));
    streamWriter.writeObject(getProteinGroupOrder(instance));
    streamWriter.writeInt(getProteinGroupPageSize(instance));
    streamWriter.writeString(getProteinGroupSortingScore(instance));
    streamWriter.writeObject(getProteinGroupsSortedBy(instance));
    streamWriter.writeObject(getProteinOrder(instance));
    streamWriter.writeInt(getProteinPageSize(instance));
    streamWriter.writeString(getProteinSortingScore(instance));
    streamWriter.writeObject(getProteinsSortedBy(instance));
    streamWriter.writeObject(getPsmDefaultView(instance));
    streamWriter.writeObject(getPsmOrder(instance));
    streamWriter.writeInt(getPsmPageSize(instance));
    streamWriter.writeString(getPsmSortingScore(instance));
    streamWriter.writeObject(getPsmsSortedBy(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.scripps.yates.shared.util.DefaultView_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.DefaultView_FieldSerializer.deserialize(reader, (edu.scripps.yates.shared.util.DefaultView)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.scripps.yates.shared.util.DefaultView_FieldSerializer.serialize(writer, (edu.scripps.yates.shared.util.DefaultView)object);
  }
  
}
