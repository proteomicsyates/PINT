package edu.scripps.yates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.rpc.impl.TypeHandler;
import java.util.HashMap;
import java.util.Map;
import com.google.gwt.core.client.GwtScriptOnly;

public class ProteinRetrievalService_TypeSerializer extends com.google.gwt.user.client.rpc.impl.SerializerBase {
  private static final MethodMap methodMapNative;
  private static final JsArrayString signatureMapNative;
  
  static {
    methodMapNative = loadMethodsNative();
    signatureMapNative = loadSignaturesNative();
  }
  
  @SuppressWarnings("deprecation")
  @GwtScriptOnly
  private static native MethodMap loadMethodsNative() /*-{
    var result = {};
    result["[C/2871596207"] = [
        @com.google.gwt.user.client.rpc.core.char_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.char_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[C),
        @com.google.gwt.user.client.rpc.core.char_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[C)
      ];
    
    result["com.google.gwt.i18n.shared.impl.DateRecord/3173882066"] = [
        @com.google.gwt.i18n.shared.impl.DateRecord_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.i18n.shared.impl.DateRecord_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/i18n/shared/impl/DateRecord;),
        @com.google.gwt.i18n.shared.impl.DateRecord_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lcom/google/gwt/i18n/shared/impl/DateRecord;)
      ];
    
    result["com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533"] = [
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/rpc/IncompatibleRemoteServiceException;),
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lcom/google/gwt/user/client/rpc/IncompatibleRemoteServiceException;)
      ];
    
    result["com.google.gwt.user.client.rpc.RpcTokenException/2345075298"] = [
        @com.google.gwt.user.client.rpc.RpcTokenException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.RpcTokenException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/rpc/RpcTokenException;),
      ];
    
    result["com.google.gwt.user.client.rpc.XsrfToken/4254043109"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.XsrfToken_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lcom/google/gwt/user/client/rpc/XsrfToken;)
      ];
    
    result["com.google.gwt.user.client.ui.ChangeListenerCollection/287642957"] = [
        @com.google.gwt.user.client.ui.ChangeListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.ChangeListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/ChangeListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.ClickListenerCollection/2152455986"] = [
        @com.google.gwt.user.client.ui.ClickListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.ClickListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/ClickListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.FocusListenerCollection/119490835"] = [
        @com.google.gwt.user.client.ui.FocusListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.FocusListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/FocusListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.FormHandlerCollection/3088681894"] = [
        @com.google.gwt.user.client.ui.FormHandlerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.FormHandlerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/FormHandlerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.KeyboardListenerCollection/1040442242"] = [
        @com.google.gwt.user.client.ui.KeyboardListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.KeyboardListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/KeyboardListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.LoadListenerCollection/3174178888"] = [
        @com.google.gwt.user.client.ui.LoadListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.LoadListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/LoadListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.MouseListenerCollection/465158911"] = [
        @com.google.gwt.user.client.ui.MouseListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.MouseListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/MouseListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.MouseWheelListenerCollection/370304552"] = [
        @com.google.gwt.user.client.ui.MouseWheelListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.MouseWheelListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/MouseWheelListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.PopupListenerCollection/1920131050"] = [
        @com.google.gwt.user.client.ui.PopupListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.PopupListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/PopupListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.ScrollListenerCollection/210686268"] = [
        @com.google.gwt.user.client.ui.ScrollListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.ScrollListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/ScrollListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.TabListenerCollection/3768293299"] = [
        @com.google.gwt.user.client.ui.TabListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.TabListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/TabListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.TableListenerCollection/2254740473"] = [
        @com.google.gwt.user.client.ui.TableListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.TableListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/TableListenerCollection;),
      ];
    
    result["com.google.gwt.user.client.ui.TreeListenerCollection/3716243734"] = [
        @com.google.gwt.user.client.ui.TreeListenerCollection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.ui.TreeListenerCollection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/ui/TreeListenerCollection;),
      ];
    
    result["edu.scripps.yates.client.util.ProjectsBeanSet/37067652"] = [
        @edu.scripps.yates.client.util.ProjectsBeanSet_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.client.util.ProjectsBeanSet_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/client/util/ProjectsBeanSet;),
      ];
    
    result["edu.scripps.yates.shared.columns.ColumnName/1607171737"] = [
        @edu.scripps.yates.shared.columns.ColumnName_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.ColumnName_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/ColumnName;),
        @edu.scripps.yates.shared.columns.ColumnName_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/ColumnName;)
      ];
    
    result["edu.scripps.yates.shared.columns.ColumnWithVisibility/2625601546"] = [
        @edu.scripps.yates.shared.columns.ColumnWithVisibility_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.ColumnWithVisibility_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/ColumnWithVisibility;),
      ];
    
    result["[Ledu.scripps.yates.shared.columns.ColumnWithVisibility;/694075138"] = [
        @edu.scripps.yates.shared.columns.ColumnWithVisibility_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.ColumnWithVisibility_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/columns/ColumnWithVisibility;),
      ];
    
    result["edu.scripps.yates.shared.columns.comparator.ClientPSMComparator/1743981359"] = [
        @edu.scripps.yates.shared.columns.comparator.ClientPSMComparator_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.comparator.ClientPSMComparator_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/comparator/ClientPSMComparator;),
        @edu.scripps.yates.shared.columns.comparator.ClientPSMComparator_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/comparator/ClientPSMComparator;)
      ];
    
    result["edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator/430421200"] = [
        @edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/comparator/ClientPeptideComparator;),
        @edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/comparator/ClientPeptideComparator;)
      ];
    
    result["edu.scripps.yates.shared.columns.comparator.ClientProteinComparator/3300355168"] = [
        @edu.scripps.yates.shared.columns.comparator.ClientProteinComparator_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.comparator.ClientProteinComparator_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/comparator/ClientProteinComparator;),
        @edu.scripps.yates.shared.columns.comparator.ClientProteinComparator_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/comparator/ClientProteinComparator;)
      ];
    
    result["edu.scripps.yates.shared.columns.comparator.ClientProteinGroupComparator/1388218284"] = [
        @edu.scripps.yates.shared.columns.comparator.ClientProteinGroupComparator_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.comparator.ClientProteinGroupComparator_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/comparator/ClientProteinGroupComparator;),
        @edu.scripps.yates.shared.columns.comparator.ClientProteinGroupComparator_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/comparator/ClientProteinGroupComparator;)
      ];
    
    result["edu.scripps.yates.shared.columns.comparator.SharedPSMBeanComparator/3227813688"] = [
        @edu.scripps.yates.shared.columns.comparator.SharedPSMBeanComparator_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.comparator.SharedPSMBeanComparator_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/comparator/SharedPSMBeanComparator;),
        @edu.scripps.yates.shared.columns.comparator.SharedPSMBeanComparator_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/comparator/SharedPSMBeanComparator;)
      ];
    
    result["edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator/1769512901"] = [
        @edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/comparator/SharedPeptideBeanComparator;),
        @edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/comparator/SharedPeptideBeanComparator;)
      ];
    
    result["edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator/35659945"] = [
        @edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/comparator/SharedProteinBeanComparator;),
        @edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/comparator/SharedProteinBeanComparator;)
      ];
    
    result["edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator/3109117823"] = [
        @edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/columns/comparator/SharedProteinGroupComparator;),
        @edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/columns/comparator/SharedProteinGroupComparator;)
      ];
    
    result["edu.scripps.yates.shared.exceptions.PintException/3675183916"] = [
        @edu.scripps.yates.shared.exceptions.PintException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.exceptions.PintException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/exceptions/PintException;),
      ];
    
    result["edu.scripps.yates.shared.exceptions.PintException$PINT_ERROR_TYPE/732031063"] = [
        @edu.scripps.yates.shared.exceptions.PintException_PINT_ERROR_TYPE_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.exceptions.PintException_PINT_ERROR_TYPE_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/exceptions/PintException$PINT_ERROR_TYPE;),
      ];
    
    result["edu.scripps.yates.shared.model.AccessionBean/1179269079"] = [
        @edu.scripps.yates.shared.model.AccessionBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.AccessionBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/AccessionBean;),
        @edu.scripps.yates.shared.model.AccessionBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/AccessionBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.AccessionBean;/3783300453"] = [
        @edu.scripps.yates.shared.model.AccessionBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.AccessionBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/AccessionBean;),
        @edu.scripps.yates.shared.model.AccessionBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/AccessionBean;)
      ];
    
    result["edu.scripps.yates.shared.model.AccessionType/1337431858"] = [
        @edu.scripps.yates.shared.model.AccessionType_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.AccessionType_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/AccessionType;),
        @edu.scripps.yates.shared.model.AccessionType_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/AccessionType;)
      ];
    
    result["edu.scripps.yates.shared.model.AmountBean/4098098218"] = [
        @edu.scripps.yates.shared.model.AmountBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.AmountBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/AmountBean;),
        @edu.scripps.yates.shared.model.AmountBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/AmountBean;)
      ];
    
    result["edu.scripps.yates.shared.model.AmountType/868712068"] = [
        @edu.scripps.yates.shared.model.AmountType_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.AmountType_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/AmountType;),
        @edu.scripps.yates.shared.model.AmountType_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/AmountType;)
      ];
    
    result["edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534"] = [
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ExperimentalConditionBean;),
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ExperimentalConditionBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.ExperimentalConditionBean;/2104000986"] = [
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/ExperimentalConditionBean;),
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/ExperimentalConditionBean;)
      ];
    
    result["edu.scripps.yates.shared.model.GeneBean/3706689610"] = [
        @edu.scripps.yates.shared.model.GeneBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.GeneBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/GeneBean;),
        @edu.scripps.yates.shared.model.GeneBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/GeneBean;)
      ];
    
    result["edu.scripps.yates.shared.model.LabelBean/940945395"] = [
        @edu.scripps.yates.shared.model.LabelBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.LabelBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/LabelBean;),
        @edu.scripps.yates.shared.model.LabelBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/LabelBean;)
      ];
    
    result["edu.scripps.yates.shared.model.MSRunBean/2607158497"] = [
        @edu.scripps.yates.shared.model.MSRunBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.MSRunBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/MSRunBean;),
        @edu.scripps.yates.shared.model.MSRunBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/MSRunBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.MSRunBean;/704096898"] = [
        @edu.scripps.yates.shared.model.MSRunBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.MSRunBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/MSRunBean;),
        @edu.scripps.yates.shared.model.MSRunBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/MSRunBean;)
      ];
    
    result["edu.scripps.yates.shared.model.OmimEntryBean/3230201098"] = [
        @edu.scripps.yates.shared.model.OmimEntryBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.OmimEntryBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/OmimEntryBean;),
        @edu.scripps.yates.shared.model.OmimEntryBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/OmimEntryBean;)
      ];
    
    result["edu.scripps.yates.shared.model.OrganismBean/3624747203"] = [
        @edu.scripps.yates.shared.model.OrganismBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.OrganismBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/OrganismBean;),
        @edu.scripps.yates.shared.model.OrganismBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/OrganismBean;)
      ];
    
    result["edu.scripps.yates.shared.model.PSMBean/1043100411"] = [
        @edu.scripps.yates.shared.model.PSMBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PSMBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/PSMBean;),
        @edu.scripps.yates.shared.model.PSMBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/PSMBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.PSMBean;/2127744086"] = [
        @edu.scripps.yates.shared.model.PSMBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PSMBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/PSMBean;),
        @edu.scripps.yates.shared.model.PSMBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/PSMBean;)
      ];
    
    result["edu.scripps.yates.shared.model.PTMBean/4103372584"] = [
        @edu.scripps.yates.shared.model.PTMBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PTMBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/PTMBean;),
        @edu.scripps.yates.shared.model.PTMBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/PTMBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.PTMBean;/846778807"] = [
        @edu.scripps.yates.shared.model.PTMBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PTMBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/PTMBean;),
        @edu.scripps.yates.shared.model.PTMBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/PTMBean;)
      ];
    
    result["edu.scripps.yates.shared.model.PTMSiteBean/3817628121"] = [
        @edu.scripps.yates.shared.model.PTMSiteBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PTMSiteBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/PTMSiteBean;),
        @edu.scripps.yates.shared.model.PTMSiteBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/PTMSiteBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.PTMSiteBean;/1487925607"] = [
        @edu.scripps.yates.shared.model.PTMSiteBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PTMSiteBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/PTMSiteBean;),
        @edu.scripps.yates.shared.model.PTMSiteBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/PTMSiteBean;)
      ];
    
    result["edu.scripps.yates.shared.model.PeptideBean/1357931400"] = [
        @edu.scripps.yates.shared.model.PeptideBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PeptideBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/PeptideBean;),
        @edu.scripps.yates.shared.model.PeptideBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/PeptideBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.PeptideBean;/798996019"] = [
        @edu.scripps.yates.shared.model.PeptideBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PeptideBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/PeptideBean;),
        @edu.scripps.yates.shared.model.PeptideBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/PeptideBean;)
      ];
    
    result["edu.scripps.yates.shared.model.PeptideRelation/2085298937"] = [
        @edu.scripps.yates.shared.model.PeptideRelation_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.PeptideRelation_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/PeptideRelation;),
        @edu.scripps.yates.shared.model.PeptideRelation_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/PeptideRelation;)
      ];
    
    result["edu.scripps.yates.shared.model.ProjectBean/194705391"] = [
        @edu.scripps.yates.shared.model.ProjectBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProjectBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ProjectBean;),
        @edu.scripps.yates.shared.model.ProjectBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ProjectBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.ProjectBean;/1759271678"] = [
        @edu.scripps.yates.shared.model.ProjectBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProjectBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/ProjectBean;),
      ];
    
    result["edu.scripps.yates.shared.model.ProteinAnnotationBean/2289051462"] = [
        @edu.scripps.yates.shared.model.ProteinAnnotationBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinAnnotationBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ProteinAnnotationBean;),
        @edu.scripps.yates.shared.model.ProteinAnnotationBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ProteinAnnotationBean;)
      ];
    
    result["edu.scripps.yates.shared.model.ProteinBean/3592343331"] = [
        @edu.scripps.yates.shared.model.ProteinBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ProteinBean;),
        @edu.scripps.yates.shared.model.ProteinBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ProteinBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.ProteinBean;/300916462"] = [
        @edu.scripps.yates.shared.model.ProteinBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/ProteinBean;),
      ];
    
    result["edu.scripps.yates.shared.model.ProteinEvidence/446267528"] = [
        @edu.scripps.yates.shared.model.ProteinEvidence_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinEvidence_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ProteinEvidence;),
        @edu.scripps.yates.shared.model.ProteinEvidence_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ProteinEvidence;)
      ];
    
    result["edu.scripps.yates.shared.model.ProteinGroupBean/474264455"] = [
        @edu.scripps.yates.shared.model.ProteinGroupBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinGroupBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ProteinGroupBean;),
        @edu.scripps.yates.shared.model.ProteinGroupBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ProteinGroupBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.ProteinGroupBean;/162212666"] = [
        @edu.scripps.yates.shared.model.ProteinGroupBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinGroupBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/ProteinGroupBean;),
      ];
    
    result["edu.scripps.yates.shared.model.ProteinPeptideCluster/149882088"] = [
        @edu.scripps.yates.shared.model.ProteinPeptideCluster_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinPeptideCluster_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ProteinPeptideCluster;),
      ];
    
    result["edu.scripps.yates.shared.model.ProteinProjection/3690754667"] = [
        @edu.scripps.yates.shared.model.ProteinProjection_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinProjection_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ProteinProjection;),
      ];
    
    result["[Ledu.scripps.yates.shared.model.ProteinProjection;/2735184025"] = [
        @edu.scripps.yates.shared.model.ProteinProjection_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProteinProjection_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/ProteinProjection;),
      ];
    
    result["edu.scripps.yates.shared.model.RatioBean/320981549"] = [
        @edu.scripps.yates.shared.model.RatioBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.RatioBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/RatioBean;),
        @edu.scripps.yates.shared.model.RatioBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/RatioBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.RatioBean;/503966014"] = [
        @edu.scripps.yates.shared.model.RatioBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.RatioBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/RatioBean;),
        @edu.scripps.yates.shared.model.RatioBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/RatioBean;)
      ];
    
    result["edu.scripps.yates.shared.model.RatioDescriptorBean/1873648207"] = [
        @edu.scripps.yates.shared.model.RatioDescriptorBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.RatioDescriptorBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/RatioDescriptorBean;),
        @edu.scripps.yates.shared.model.RatioDescriptorBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/RatioDescriptorBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.RatioDescriptorBean;/1259848886"] = [
        @edu.scripps.yates.shared.model.RatioDescriptorBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.RatioDescriptorBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/RatioDescriptorBean;),
      ];
    
    result["edu.scripps.yates.shared.model.RatioDistribution/499199692"] = [
        @edu.scripps.yates.shared.model.RatioDistribution_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.RatioDistribution_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/RatioDistribution;),
        @edu.scripps.yates.shared.model.RatioDistribution_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/RatioDistribution;)
      ];
    
    result["edu.scripps.yates.shared.model.ReactomePathwayRef/3521162694"] = [
        @edu.scripps.yates.shared.model.ReactomePathwayRef_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ReactomePathwayRef_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ReactomePathwayRef;),
        @edu.scripps.yates.shared.model.ReactomePathwayRef_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ReactomePathwayRef;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.ReactomePathwayRef;/2673934375"] = [
        @edu.scripps.yates.shared.model.ReactomePathwayRef_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ReactomePathwayRef_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/ReactomePathwayRef;),
        @edu.scripps.yates.shared.model.ReactomePathwayRef_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/ReactomePathwayRef;)
      ];
    
    result["edu.scripps.yates.shared.model.SampleBean/2816907194"] = [
        @edu.scripps.yates.shared.model.SampleBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.SampleBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/SampleBean;),
        @edu.scripps.yates.shared.model.SampleBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/SampleBean;)
      ];
    
    result["edu.scripps.yates.shared.model.ScoreBean/1806183361"] = [
        @edu.scripps.yates.shared.model.ScoreBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ScoreBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ScoreBean;),
        @edu.scripps.yates.shared.model.ScoreBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ScoreBean;)
      ];
    
    result["edu.scripps.yates.shared.model.SharedAggregationLevel/2363646906"] = [
        @edu.scripps.yates.shared.model.SharedAggregationLevel_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.SharedAggregationLevel_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/SharedAggregationLevel;),
        @edu.scripps.yates.shared.model.SharedAggregationLevel_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/SharedAggregationLevel;)
      ];
    
    result["edu.scripps.yates.shared.model.ThresholdBean/3079097373"] = [
        @edu.scripps.yates.shared.model.ThresholdBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ThresholdBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ThresholdBean;),
        @edu.scripps.yates.shared.model.ThresholdBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/ThresholdBean;)
      ];
    
    result["edu.scripps.yates.shared.model.TissueBean/1156663452"] = [
        @edu.scripps.yates.shared.model.TissueBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.TissueBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/TissueBean;),
        @edu.scripps.yates.shared.model.TissueBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/TissueBean;)
      ];
    
    result["edu.scripps.yates.shared.model.UniprotFeatureBean/14164131"] = [
        @edu.scripps.yates.shared.model.UniprotFeatureBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.UniprotFeatureBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/UniprotFeatureBean;),
        @edu.scripps.yates.shared.model.UniprotFeatureBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/UniprotFeatureBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.UniprotFeatureBean;/2704040475"] = [
        @edu.scripps.yates.shared.model.UniprotFeatureBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.UniprotFeatureBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/UniprotFeatureBean;),
        @edu.scripps.yates.shared.model.UniprotFeatureBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/UniprotFeatureBean;)
      ];
    
    result["edu.scripps.yates.shared.model.UniprotProteinExistence/3696157605"] = [
        @edu.scripps.yates.shared.model.UniprotProteinExistence_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.UniprotProteinExistence_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/UniprotProteinExistence;),
        @edu.scripps.yates.shared.model.UniprotProteinExistence_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/UniprotProteinExistence;)
      ];
    
    result["edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase/1802311985"] = [
        ,
        ,
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/thirdparty/pseaquant/PSEAQuantAnnotationDatabase;)
      ];
    
    result["edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol/891003955"] = [
        ,
        ,
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/thirdparty/pseaquant/PSEAQuantCVTol;)
      ];
    
    result["edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias/29143058"] = [
        ,
        ,
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/thirdparty/pseaquant/PSEAQuantLiteratureBias;)
      ];
    
    result["edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType/1784699068"] = [
        ,
        ,
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/thirdparty/pseaquant/PSEAQuantQuantType;)
      ];
    
    result["edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate/2963508585"] = [
        ,
        ,
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/thirdparty/pseaquant/PSEAQuantReplicate;)
      ];
    
    result["[Ledu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate;/177262550"] = [
        ,
        ,
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/thirdparty/pseaquant/PSEAQuantReplicate;)
      ];
    
    result["edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult/3327603775"] = [
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/thirdparty/pseaquant/PSEAQuantResult;),
      ];
    
    result["edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism/3199015948"] = [
        ,
        ,
        @edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/thirdparty/pseaquant/PSEAQuantSupportedOrganism;)
      ];
    
    result["edu.scripps.yates.shared.util.AlignmentResult/3049491824"] = [
        @edu.scripps.yates.shared.util.AlignmentResult_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.AlignmentResult_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/AlignmentResult;),
      ];
    
    result["edu.scripps.yates.shared.util.DefaultView/3854706437"] = [
        @edu.scripps.yates.shared.util.DefaultView_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.DefaultView_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/DefaultView;),
      ];
    
    result["edu.scripps.yates.shared.util.DefaultView$ORDER/207293086"] = [
        @edu.scripps.yates.shared.util.DefaultView_ORDER_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.DefaultView_ORDER_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/DefaultView$ORDER;),
      ];
    
    result["edu.scripps.yates.shared.util.DefaultView$TAB/2610361327"] = [
        @edu.scripps.yates.shared.util.DefaultView_TAB_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.DefaultView_TAB_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/DefaultView$TAB;),
      ];
    
    result["edu.scripps.yates.shared.util.FileDescriptor/1281479038"] = [
        @edu.scripps.yates.shared.util.FileDescriptor_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.FileDescriptor_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/FileDescriptor;),
      ];
    
    result["edu.scripps.yates.shared.util.Pair/3303000628"] = [
        @edu.scripps.yates.shared.util.Pair_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.Pair_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/Pair;),
        @edu.scripps.yates.shared.util.Pair_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/util/Pair;)
      ];
    
    result["[Ledu.scripps.yates.shared.util.Pair;/2239445102"] = [
        @edu.scripps.yates.shared.util.Pair_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.Pair_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/util/Pair;),
        @edu.scripps.yates.shared.util.Pair_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/util/Pair;)
      ];
    
    result["edu.scripps.yates.shared.util.ProgressStatus/1968355620"] = [
        @edu.scripps.yates.shared.util.ProgressStatus_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.ProgressStatus_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/ProgressStatus;),
      ];
    
    result["edu.scripps.yates.shared.util.ProjectNamedQuery/231077391"] = [
        @edu.scripps.yates.shared.util.ProjectNamedQuery_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.ProjectNamedQuery_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/ProjectNamedQuery;),
      ];
    
    result["[Ledu.scripps.yates.shared.util.ProjectNamedQuery;/766885247"] = [
        @edu.scripps.yates.shared.util.ProjectNamedQuery_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.ProjectNamedQuery_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/util/ProjectNamedQuery;),
      ];
    
    result["edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults/2554405134"] = [
        @edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/ProteinPeptideClusterAlignmentResults;),
      ];
    
    result["edu.scripps.yates.shared.util.sublists.PeptideBeanSubList/1367545352"] = [
        @edu.scripps.yates.shared.util.sublists.PeptideBeanSubList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.sublists.PeptideBeanSubList_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/sublists/PeptideBeanSubList;),
      ];
    
    result["edu.scripps.yates.shared.util.sublists.ProteinBeanSubList/2147660758"] = [
        @edu.scripps.yates.shared.util.sublists.ProteinBeanSubList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.sublists.ProteinBeanSubList_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/sublists/ProteinBeanSubList;),
      ];
    
    result["edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList/537387498"] = [
        @edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/sublists/ProteinGroupBeanSubList;),
      ];
    
    result["edu.scripps.yates.shared.util.sublists.PsmBeanSubList/1806414622"] = [
        @edu.scripps.yates.shared.util.sublists.PsmBeanSubList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.sublists.PsmBeanSubList_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/sublists/PsmBeanSubList;),
      ];
    
    result["edu.scripps.yates.shared.util.sublists.QueryResultSubLists/3160449389"] = [
        @edu.scripps.yates.shared.util.sublists.QueryResultSubLists_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.util.sublists.QueryResultSubLists_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/util/sublists/QueryResultSubLists;),
      ];
    
    result["java.lang.Boolean/476441737"] = [
        @com.google.gwt.user.client.rpc.core.java.lang.Boolean_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.lang.Boolean_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/lang/Boolean;),
        @com.google.gwt.user.client.rpc.core.java.lang.Boolean_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/lang/Boolean;)
      ];
    
    result["java.lang.Double/858496421"] = [
        @com.google.gwt.user.client.rpc.core.java.lang.Double_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.lang.Double_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/lang/Double;),
        @com.google.gwt.user.client.rpc.core.java.lang.Double_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/lang/Double;)
      ];
    
    result["java.lang.Integer/3438268394"] = [
        @com.google.gwt.user.client.rpc.core.java.lang.Integer_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.lang.Integer_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/lang/Integer;),
        @com.google.gwt.user.client.rpc.core.java.lang.Integer_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/lang/Integer;)
      ];
    
    result["java.lang.String/2004016611"] = [
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/lang/String;),
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/lang/String;)
      ];
    
    result["[Ljava.lang.String;/2600011424"] = [
        @com.google.gwt.user.client.rpc.core.java.lang.String_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.lang.String_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ljava/lang/String;),
        @com.google.gwt.user.client.rpc.core.java.lang.String_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/lang/String;)
      ];
    
    result["java.sql.Date/730999118"] = [
        @com.google.gwt.user.client.rpc.core.java.sql.Date_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.sql.Date_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/sql/Date;),
        @com.google.gwt.user.client.rpc.core.java.sql.Date_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/sql/Date;)
      ];
    
    result["java.sql.Time/1816797103"] = [
        @com.google.gwt.user.client.rpc.core.java.sql.Time_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.sql.Time_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/sql/Time;),
        @com.google.gwt.user.client.rpc.core.java.sql.Time_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/sql/Time;)
      ];
    
    result["java.sql.Timestamp/3040052672"] = [
        @com.google.gwt.user.client.rpc.core.java.sql.Timestamp_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.sql.Timestamp_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/sql/Timestamp;),
        @com.google.gwt.user.client.rpc.core.java.sql.Timestamp_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/sql/Timestamp;)
      ];
    
    result["[Ljava.util.AbstractMap;/793369879"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.util.AbstractMap_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/util/AbstractMap;)
      ];
    
    result["java.util.ArrayList/4159755760"] = [
        @com.google.gwt.user.client.rpc.core.java.util.ArrayList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.ArrayList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/ArrayList;),
        @com.google.gwt.user.client.rpc.core.java.util.ArrayList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/ArrayList;)
      ];
    
    result["java.util.Arrays$ArrayList/2507071751"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Arrays.ArrayList_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Arrays.ArrayList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/List;),
        @com.google.gwt.user.client.rpc.core.java.util.Arrays.ArrayList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/List;)
      ];
    
    result["java.util.Collections$EmptyList/4157118744"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyList_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/List;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/List;)
      ];
    
    result["java.util.Collections$EmptyMap/4174664486"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyMap_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyMap_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Map;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyMap_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Map;)
      ];
    
    result["java.util.Collections$EmptySet/3523698179"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptySet_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptySet_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Set;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptySet_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Set;)
      ];
    
    result["java.util.Collections$SingletonList/1586180994"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Collections.SingletonList_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.SingletonList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/List;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.SingletonList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/List;)
      ];
    
    result["java.util.Date/3385151746"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Date_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Date_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Date;),
        @com.google.gwt.user.client.rpc.core.java.util.Date_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Date;)
      ];
    
    result["java.util.HashMap/1797211028"] = [
        @com.google.gwt.user.client.rpc.core.java.util.HashMap_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.HashMap_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/HashMap;),
        @com.google.gwt.user.client.rpc.core.java.util.HashMap_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/HashMap;)
      ];
    
    result["[Ljava.util.HashMap;/1665718734"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.util.HashMap_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/util/HashMap;)
      ];
    
    result["java.util.HashSet/3273092938"] = [
        @com.google.gwt.user.client.rpc.core.java.util.HashSet_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.HashSet_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/HashSet;),
        @com.google.gwt.user.client.rpc.core.java.util.HashSet_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/HashSet;)
      ];
    
    result["java.util.IdentityHashMap/1839153020"] = [
        @com.google.gwt.user.client.rpc.core.java.util.IdentityHashMap_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.IdentityHashMap_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/IdentityHashMap;),
        @com.google.gwt.user.client.rpc.core.java.util.IdentityHashMap_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/IdentityHashMap;)
      ];
    
    result["[Ljava.util.IdentityHashMap;/2145185757"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.util.IdentityHashMap_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/util/IdentityHashMap;)
      ];
    
    result["java.util.LinkedHashMap/3008245022"] = [
        @com.google.gwt.user.client.rpc.core.java.util.LinkedHashMap_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedHashMap_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/LinkedHashMap;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedHashMap_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/LinkedHashMap;)
      ];
    
    result["[Ljava.util.LinkedHashMap;/3261192069"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.util.LinkedHashMap_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/util/LinkedHashMap;)
      ];
    
    result["java.util.LinkedHashSet/95640124"] = [
        @com.google.gwt.user.client.rpc.core.java.util.LinkedHashSet_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedHashSet_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/LinkedHashSet;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedHashSet_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/LinkedHashSet;)
      ];
    
    result["java.util.LinkedList/3953877921"] = [
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/LinkedList;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/LinkedList;)
      ];
    
    result["[Ljava.util.Map;/1931242982"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.util.Map_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/util/Map;)
      ];
    
    result["[Ljava.util.NavigableMap;/451570914"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.util.NavigableMap_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/util/NavigableMap;)
      ];
    
    result["[Ljava.util.SortedMap;/4128485282"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.util.SortedMap_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/util/SortedMap;)
      ];
    
    result["java.util.Stack/1346942793"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Stack;),
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Stack;)
      ];
    
    result["java.util.TreeMap/1493889780"] = [
        @com.google.gwt.user.client.rpc.core.java.util.TreeMap_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.TreeMap_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/TreeMap;),
        @com.google.gwt.user.client.rpc.core.java.util.TreeMap_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/TreeMap;)
      ];
    
    result["[Ljava.util.TreeMap;/317516023"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.util.TreeMap_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/util/TreeMap;)
      ];
    
    result["java.util.TreeSet/4043497002"] = [
        @com.google.gwt.user.client.rpc.core.java.util.TreeSet_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.TreeSet_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/TreeSet;),
        @com.google.gwt.user.client.rpc.core.java.util.TreeSet_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/TreeSet;)
      ];
    
    result["java.util.Vector/3057315478"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Vector_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Vector_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Vector;),
        @com.google.gwt.user.client.rpc.core.java.util.Vector_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Vector;)
      ];
    
    result["org.reactome.web.pwp.model.util.LruCache/171488710"] = [
        @org.reactome.web.pwp.model.util.LruCache_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @org.reactome.web.pwp.model.util.LruCache_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lorg/reactome/web/pwp/model/util/LruCache;),
        @org.reactome.web.pwp.model.util.LruCache_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lorg/reactome/web/pwp/model/util/LruCache;)
      ];
    
    result["[Lorg.reactome.web.pwp.model.util.LruCache;/2619688544"] = [
        ,
        ,
        @org.reactome.web.pwp.model.util.LruCache_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Lorg/reactome/web/pwp/model/util/LruCache;)
      ];
    
    return result;
  }-*/;
  
  @SuppressWarnings("deprecation")
  @GwtScriptOnly
  private static native JsArrayString loadSignaturesNative() /*-{
    var result = [];
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@char[]::class)] = "[C/2871596207";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.i18n.shared.impl.DateRecord::class)] = "com.google.gwt.i18n.shared.impl.DateRecord/3173882066";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException::class)] = "com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.rpc.RpcTokenException::class)] = "com.google.gwt.user.client.rpc.RpcTokenException/2345075298";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.rpc.XsrfToken::class)] = "com.google.gwt.user.client.rpc.XsrfToken/4254043109";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.ChangeListenerCollection::class)] = "com.google.gwt.user.client.ui.ChangeListenerCollection/287642957";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.ClickListenerCollection::class)] = "com.google.gwt.user.client.ui.ClickListenerCollection/2152455986";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.FocusListenerCollection::class)] = "com.google.gwt.user.client.ui.FocusListenerCollection/119490835";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.FormHandlerCollection::class)] = "com.google.gwt.user.client.ui.FormHandlerCollection/3088681894";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.KeyboardListenerCollection::class)] = "com.google.gwt.user.client.ui.KeyboardListenerCollection/1040442242";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.LoadListenerCollection::class)] = "com.google.gwt.user.client.ui.LoadListenerCollection/3174178888";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.MouseListenerCollection::class)] = "com.google.gwt.user.client.ui.MouseListenerCollection/465158911";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.MouseWheelListenerCollection::class)] = "com.google.gwt.user.client.ui.MouseWheelListenerCollection/370304552";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.PopupListenerCollection::class)] = "com.google.gwt.user.client.ui.PopupListenerCollection/1920131050";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.ScrollListenerCollection::class)] = "com.google.gwt.user.client.ui.ScrollListenerCollection/210686268";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.TabListenerCollection::class)] = "com.google.gwt.user.client.ui.TabListenerCollection/3768293299";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.TableListenerCollection::class)] = "com.google.gwt.user.client.ui.TableListenerCollection/2254740473";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.ui.TreeListenerCollection::class)] = "com.google.gwt.user.client.ui.TreeListenerCollection/3716243734";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.client.util.ProjectsBeanSet::class)] = "edu.scripps.yates.client.util.ProjectsBeanSet/37067652";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.ColumnName::class)] = "edu.scripps.yates.shared.columns.ColumnName/1607171737";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.ColumnWithVisibility::class)] = "edu.scripps.yates.shared.columns.ColumnWithVisibility/2625601546";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.ColumnWithVisibility[]::class)] = "[Ledu.scripps.yates.shared.columns.ColumnWithVisibility;/694075138";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.comparator.ClientPSMComparator::class)] = "edu.scripps.yates.shared.columns.comparator.ClientPSMComparator/1743981359";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator::class)] = "edu.scripps.yates.shared.columns.comparator.ClientPeptideComparator/430421200";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.comparator.ClientProteinComparator::class)] = "edu.scripps.yates.shared.columns.comparator.ClientProteinComparator/3300355168";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.comparator.ClientProteinGroupComparator::class)] = "edu.scripps.yates.shared.columns.comparator.ClientProteinGroupComparator/1388218284";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.comparator.SharedPSMBeanComparator::class)] = "edu.scripps.yates.shared.columns.comparator.SharedPSMBeanComparator/3227813688";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator::class)] = "edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator/1769512901";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator::class)] = "edu.scripps.yates.shared.columns.comparator.SharedProteinBeanComparator/35659945";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator::class)] = "edu.scripps.yates.shared.columns.comparator.SharedProteinGroupComparator/3109117823";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.exceptions.PintException::class)] = "edu.scripps.yates.shared.exceptions.PintException/3675183916";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE::class)] = "edu.scripps.yates.shared.exceptions.PintException$PINT_ERROR_TYPE/732031063";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.AccessionBean::class)] = "edu.scripps.yates.shared.model.AccessionBean/1179269079";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.AccessionBean[]::class)] = "[Ledu.scripps.yates.shared.model.AccessionBean;/3783300453";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.AccessionType::class)] = "edu.scripps.yates.shared.model.AccessionType/1337431858";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.AmountBean::class)] = "edu.scripps.yates.shared.model.AmountBean/4098098218";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.AmountType::class)] = "edu.scripps.yates.shared.model.AmountType/868712068";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ExperimentalConditionBean::class)] = "edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ExperimentalConditionBean[]::class)] = "[Ledu.scripps.yates.shared.model.ExperimentalConditionBean;/2104000986";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.GeneBean::class)] = "edu.scripps.yates.shared.model.GeneBean/3706689610";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.LabelBean::class)] = "edu.scripps.yates.shared.model.LabelBean/940945395";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.MSRunBean::class)] = "edu.scripps.yates.shared.model.MSRunBean/2607158497";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.MSRunBean[]::class)] = "[Ledu.scripps.yates.shared.model.MSRunBean;/704096898";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.OmimEntryBean::class)] = "edu.scripps.yates.shared.model.OmimEntryBean/3230201098";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.OrganismBean::class)] = "edu.scripps.yates.shared.model.OrganismBean/3624747203";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PSMBean::class)] = "edu.scripps.yates.shared.model.PSMBean/1043100411";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PSMBean[]::class)] = "[Ledu.scripps.yates.shared.model.PSMBean;/2127744086";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PTMBean::class)] = "edu.scripps.yates.shared.model.PTMBean/4103372584";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PTMBean[]::class)] = "[Ledu.scripps.yates.shared.model.PTMBean;/846778807";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PTMSiteBean::class)] = "edu.scripps.yates.shared.model.PTMSiteBean/3817628121";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PTMSiteBean[]::class)] = "[Ledu.scripps.yates.shared.model.PTMSiteBean;/1487925607";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PeptideBean::class)] = "edu.scripps.yates.shared.model.PeptideBean/1357931400";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PeptideBean[]::class)] = "[Ledu.scripps.yates.shared.model.PeptideBean;/798996019";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.PeptideRelation::class)] = "edu.scripps.yates.shared.model.PeptideRelation/2085298937";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProjectBean::class)] = "edu.scripps.yates.shared.model.ProjectBean/194705391";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProjectBean[]::class)] = "[Ledu.scripps.yates.shared.model.ProjectBean;/1759271678";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinAnnotationBean::class)] = "edu.scripps.yates.shared.model.ProteinAnnotationBean/2289051462";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinBean::class)] = "edu.scripps.yates.shared.model.ProteinBean/3592343331";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinBean[]::class)] = "[Ledu.scripps.yates.shared.model.ProteinBean;/300916462";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinEvidence::class)] = "edu.scripps.yates.shared.model.ProteinEvidence/446267528";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinGroupBean::class)] = "edu.scripps.yates.shared.model.ProteinGroupBean/474264455";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinGroupBean[]::class)] = "[Ledu.scripps.yates.shared.model.ProteinGroupBean;/162212666";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinPeptideCluster::class)] = "edu.scripps.yates.shared.model.ProteinPeptideCluster/149882088";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinProjection::class)] = "edu.scripps.yates.shared.model.ProteinProjection/3690754667";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProteinProjection[]::class)] = "[Ledu.scripps.yates.shared.model.ProteinProjection;/2735184025";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.RatioBean::class)] = "edu.scripps.yates.shared.model.RatioBean/320981549";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.RatioBean[]::class)] = "[Ledu.scripps.yates.shared.model.RatioBean;/503966014";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.RatioDescriptorBean::class)] = "edu.scripps.yates.shared.model.RatioDescriptorBean/1873648207";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.RatioDescriptorBean[]::class)] = "[Ledu.scripps.yates.shared.model.RatioDescriptorBean;/1259848886";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.RatioDistribution::class)] = "edu.scripps.yates.shared.model.RatioDistribution/499199692";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ReactomePathwayRef::class)] = "edu.scripps.yates.shared.model.ReactomePathwayRef/3521162694";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ReactomePathwayRef[]::class)] = "[Ledu.scripps.yates.shared.model.ReactomePathwayRef;/2673934375";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.SampleBean::class)] = "edu.scripps.yates.shared.model.SampleBean/2816907194";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ScoreBean::class)] = "edu.scripps.yates.shared.model.ScoreBean/1806183361";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.SharedAggregationLevel::class)] = "edu.scripps.yates.shared.model.SharedAggregationLevel/2363646906";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ThresholdBean::class)] = "edu.scripps.yates.shared.model.ThresholdBean/3079097373";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.TissueBean::class)] = "edu.scripps.yates.shared.model.TissueBean/1156663452";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.UniprotFeatureBean::class)] = "edu.scripps.yates.shared.model.UniprotFeatureBean/14164131";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.UniprotFeatureBean[]::class)] = "[Ledu.scripps.yates.shared.model.UniprotFeatureBean;/2704040475";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.UniprotProteinExistence::class)] = "edu.scripps.yates.shared.model.UniprotProteinExistence/3696157605";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase::class)] = "edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase/1802311985";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol::class)] = "edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol/891003955";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias::class)] = "edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias/29143058";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType::class)] = "edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType/1784699068";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate::class)] = "edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate/2963508585";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate[]::class)] = "[Ledu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate;/177262550";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult::class)] = "edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult/3327603775";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism::class)] = "edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism/3199015948";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.AlignmentResult::class)] = "edu.scripps.yates.shared.util.AlignmentResult/3049491824";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.DefaultView::class)] = "edu.scripps.yates.shared.util.DefaultView/3854706437";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.DefaultView.ORDER::class)] = "edu.scripps.yates.shared.util.DefaultView$ORDER/207293086";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.DefaultView.TAB::class)] = "edu.scripps.yates.shared.util.DefaultView$TAB/2610361327";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.FileDescriptor::class)] = "edu.scripps.yates.shared.util.FileDescriptor/1281479038";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.Pair::class)] = "edu.scripps.yates.shared.util.Pair/3303000628";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.Pair[]::class)] = "[Ledu.scripps.yates.shared.util.Pair;/2239445102";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.ProgressStatus::class)] = "edu.scripps.yates.shared.util.ProgressStatus/1968355620";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.ProjectNamedQuery::class)] = "edu.scripps.yates.shared.util.ProjectNamedQuery/231077391";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.ProjectNamedQuery[]::class)] = "[Ledu.scripps.yates.shared.util.ProjectNamedQuery;/766885247";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults::class)] = "edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults/2554405134";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.sublists.PeptideBeanSubList::class)] = "edu.scripps.yates.shared.util.sublists.PeptideBeanSubList/1367545352";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.sublists.ProteinBeanSubList::class)] = "edu.scripps.yates.shared.util.sublists.ProteinBeanSubList/2147660758";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList::class)] = "edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList/537387498";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.sublists.PsmBeanSubList::class)] = "edu.scripps.yates.shared.util.sublists.PsmBeanSubList/1806414622";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.util.sublists.QueryResultSubLists::class)] = "edu.scripps.yates.shared.util.sublists.QueryResultSubLists/3160449389";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.Boolean::class)] = "java.lang.Boolean/476441737";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.Double::class)] = "java.lang.Double/858496421";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.Integer::class)] = "java.lang.Integer/3438268394";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.String::class)] = "java.lang.String/2004016611";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.String[]::class)] = "[Ljava.lang.String;/2600011424";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.sql.Date::class)] = "java.sql.Date/730999118";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.sql.Time::class)] = "java.sql.Time/1816797103";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.sql.Timestamp::class)] = "java.sql.Timestamp/3040052672";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.AbstractMap[]::class)] = "[Ljava.util.AbstractMap;/793369879";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.ArrayList::class)] = "java.util.ArrayList/4159755760";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Arrays.ArrayList::class)] = "java.util.Arrays$ArrayList/2507071751";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Collections.EmptyList::class)] = "java.util.Collections$EmptyList/4157118744";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Collections.EmptyMap::class)] = "java.util.Collections$EmptyMap/4174664486";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Collections.EmptySet::class)] = "java.util.Collections$EmptySet/3523698179";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Collections.SingletonList::class)] = "java.util.Collections$SingletonList/1586180994";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Date::class)] = "java.util.Date/3385151746";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.HashMap::class)] = "java.util.HashMap/1797211028";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.HashMap[]::class)] = "[Ljava.util.HashMap;/1665718734";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.HashSet::class)] = "java.util.HashSet/3273092938";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.IdentityHashMap::class)] = "java.util.IdentityHashMap/1839153020";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.IdentityHashMap[]::class)] = "[Ljava.util.IdentityHashMap;/2145185757";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.LinkedHashMap::class)] = "java.util.LinkedHashMap/3008245022";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.LinkedHashMap[]::class)] = "[Ljava.util.LinkedHashMap;/3261192069";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.LinkedHashSet::class)] = "java.util.LinkedHashSet/95640124";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.LinkedList::class)] = "java.util.LinkedList/3953877921";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Map[]::class)] = "[Ljava.util.Map;/1931242982";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.NavigableMap[]::class)] = "[Ljava.util.NavigableMap;/451570914";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.SortedMap[]::class)] = "[Ljava.util.SortedMap;/4128485282";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Stack::class)] = "java.util.Stack/1346942793";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.TreeMap::class)] = "java.util.TreeMap/1493889780";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.TreeMap[]::class)] = "[Ljava.util.TreeMap;/317516023";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.TreeSet::class)] = "java.util.TreeSet/4043497002";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Vector::class)] = "java.util.Vector/3057315478";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@org.reactome.web.pwp.model.util.LruCache::class)] = "org.reactome.web.pwp.model.util.LruCache/171488710";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@org.reactome.web.pwp.model.util.LruCache[]::class)] = "[Lorg.reactome.web.pwp.model.util.LruCache;/2619688544";
    return result;
  }-*/;
  
  public ProteinRetrievalService_TypeSerializer() {
    super(null, methodMapNative, null, signatureMapNative);
  }
  
}
