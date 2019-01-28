package edu.scripps.yates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.rpc.impl.TypeHandler;
import java.util.HashMap;
import java.util.Map;
import com.google.gwt.core.client.GwtScriptOnly;

public class ImportWizardService_TypeSerializer extends com.google.gwt.user.client.rpc.impl.SerializerBase {
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
    
    result["edu.scripps.yates.shared.exceptions.PintException/3675183916"] = [
        @edu.scripps.yates.shared.exceptions.PintException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.exceptions.PintException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/exceptions/PintException;),
      ];
    
    result["edu.scripps.yates.shared.exceptions.PintException$PINT_ERROR_TYPE/732031063"] = [
        @edu.scripps.yates.shared.exceptions.PintException_PINT_ERROR_TYPE_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.exceptions.PintException_PINT_ERROR_TYPE_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/exceptions/PintException$PINT_ERROR_TYPE;),
      ];
    
    result["edu.scripps.yates.shared.model.AmountType/868712068"] = [
        @edu.scripps.yates.shared.model.AmountType_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.AmountType_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/AmountType;),
        @edu.scripps.yates.shared.model.AmountType_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/AmountType;)
      ];
    
    result["edu.scripps.yates.shared.model.DataSourceBean/437188228"] = [
        @edu.scripps.yates.shared.model.DataSourceBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.DataSourceBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/DataSourceBean;),
      ];
    
    result["[Ledu.scripps.yates.shared.model.DataSourceBean;/999815188"] = [
        @edu.scripps.yates.shared.model.DataSourceBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.DataSourceBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/DataSourceBean;),
      ];
    
    result["edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534"] = [
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ExperimentalConditionBean;),
      ];
    
    result["[Ledu.scripps.yates.shared.model.ExperimentalConditionBean;/2104000986"] = [
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ExperimentalConditionBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/ExperimentalConditionBean;),
      ];
    
    result["edu.scripps.yates.shared.model.FileFormat/1999197139"] = [
        @edu.scripps.yates.shared.model.FileFormat_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.FileFormat_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/FileFormat;),
        @edu.scripps.yates.shared.model.FileFormat_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/FileFormat;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.FileFormat;/3651551357"] = [
        @edu.scripps.yates.shared.model.FileFormat_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.FileFormat_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/FileFormat;),
      ];
    
    result["edu.scripps.yates.shared.model.FileSummary/813832004"] = [
        @edu.scripps.yates.shared.model.FileSummary_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.FileSummary_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/FileSummary;),
      ];
    
    result["edu.scripps.yates.shared.model.LabelBean/940945395"] = [
        @edu.scripps.yates.shared.model.LabelBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.LabelBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/LabelBean;),
      ];
    
    result["edu.scripps.yates.shared.model.MSRunBean/2607158497"] = [
        @edu.scripps.yates.shared.model.MSRunBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.MSRunBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/MSRunBean;),
      ];
    
    result["[Ledu.scripps.yates.shared.model.MSRunBean;/704096898"] = [
        @edu.scripps.yates.shared.model.MSRunBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.MSRunBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/MSRunBean;),
      ];
    
    result["edu.scripps.yates.shared.model.OrganismBean/3624747203"] = [
        @edu.scripps.yates.shared.model.OrganismBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.OrganismBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/OrganismBean;),
      ];
    
    result["edu.scripps.yates.shared.model.ProjectBean/194705391"] = [
        @edu.scripps.yates.shared.model.ProjectBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.ProjectBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/ProjectBean;),
      ];
    
    result["edu.scripps.yates.shared.model.SampleBean/2816907194"] = [
        @edu.scripps.yates.shared.model.SampleBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.SampleBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/SampleBean;),
      ];
    
    result["edu.scripps.yates.shared.model.TissueBean/1156663452"] = [
        @edu.scripps.yates.shared.model.TissueBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.TissueBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/TissueBean;),
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.ExcelDataReference/3402148549"] = [
        ,
        ,
        @edu.scripps.yates.shared.model.projectCreator.ExcelDataReference_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/ExcelDataReference;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184"] = [
        ,
        ,
        @edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/FileNameWithTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;/2032624454"] = [
        ,
        ,
        @edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/FileNameWithTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean/3795078975"] = [
        ,
        ,
        @edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/RemoteFileWithTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;/3801495488"] = [
        ,
        ,
        @edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/RemoteFileWithTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.AmountCombinationTypeBean/1560618108"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountCombinationTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountCombinationTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/AmountCombinationTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountCombinationTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/AmountCombinationTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean/615814808"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/AmountTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/AmountTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean;/3231424063"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/AmountTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/AmountTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean/623931705"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ColumnTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ColumnTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;/4104040992"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/ColumnTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/ColumnTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean/2727697045"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ExcelAmountRatioTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ExcelAmountRatioTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;/2141324727"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/ExcelAmountRatioTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/ExcelAmountRatioTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean/2814450547"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ExperimentalConditionTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ExperimentalConditionTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;/457111181"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/ExperimentalConditionTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/ExperimentalConditionTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean/2781234254"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ExperimentalConditionsTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ExperimentalConditionsTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean/2881831965"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ExperimentalDesignTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ExperimentalDesignTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean/2968153459"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/FastaDigestionBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/FastaDigestionBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean/303835331"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/FileSetTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/FileSetTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean/2703322694"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/FileTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/FileTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;/4209304828"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/FileTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/FileTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean/1074871252"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/IdentificationExcelTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/IdentificationExcelTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;/2099369771"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/IdentificationExcelTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/IdentificationExcelTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean/2146506819"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/IdentificationInfoTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/IdentificationInfoTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean/3035767996"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/LabelSetTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/LabelSetTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean/4011760336"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/LabelTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/LabelTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;/68483336"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/LabelTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/LabelTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean/1366228899"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/MsRunTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/MsRunTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;/1041126115"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/MsRunTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/MsRunTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean/3185218991"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/MsRunsTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/MsRunsTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean/2047247850"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/OrganismSetTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/OrganismSetTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean/1495819149"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/OrganismTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/OrganismTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;/827869284"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/OrganismTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/OrganismTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean/3274249885"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/PeptideRatiosTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/PeptideRatiosTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean/3221725003"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/PintImportCfgBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/PintImportCfgBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean/1929374072"] = [
        ,
        @edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/PintImportCfgTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/PintImportCfgTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean/1910602586"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ProjectTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ProjectTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean/969277473"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinAccessionTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinAccessionTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean/876007744"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinAnnotationTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinAnnotationTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean;/3352293687"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinAnnotationTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinAnnotationTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean/435014155"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinAnnotationsTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinAnnotationsTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean/2004357379"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinDescriptionTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinDescriptionTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean/264732941"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinRatiosTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinRatiosTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean/1354070335"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinThresholdTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinThresholdTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean;/3449893462"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinThresholdTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinThresholdTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean/1000030158"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinThresholdsTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ProteinThresholdsTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean/1411683221"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/PsmRatiosTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/PsmRatiosTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean/2197746369"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/PsmTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/PsmTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean/996213295"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/PtmScoreTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/PtmScoreTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean;/524456024"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/PtmScoreTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/PtmScoreTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean/3757473012"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/QuantificationExcelTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/QuantificationExcelTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;/3998358695"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/QuantificationExcelTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/QuantificationExcelTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean/2704710783"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/QuantificationInfoTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/QuantificationInfoTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean/2006781713"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/RatiosTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/RatiosTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean/3907916378"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/RemoteFilesRatioTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/RemoteFilesRatioTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;/155765680"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/RemoteFilesRatioTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/RemoteFilesRatioTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean/482043022"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/RemoteInfoTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/RemoteInfoTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;/1107417980"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/RemoteInfoTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/RemoteInfoTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean/2486782095"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/SampleSetTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/SampleSetTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean/1027099810"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/SampleTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/SampleTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;/3531128460"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/SampleTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/SampleTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean/450387415"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ScoreTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ScoreTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;/3033471420"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/ScoreTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/ScoreTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean/2623695042"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/SequenceTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/SequenceTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean/1597661448"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ServerTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ServerTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;/257352234"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/ServerTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/ServerTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean/911645338"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/ServersTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/ServersTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean/2817870756"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/SheetTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/SheetTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;/1375609492"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/SheetTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/SheetTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean/3559385418"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/SheetsTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/SheetsTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean/3471490246"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/TissueSetTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/TissueSetTypeBean;)
      ];
    
    result["edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean/2660810438"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ledu/scripps/yates/shared/model/projectCreator/excel/TissueTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ledu/scripps/yates/shared/model/projectCreator/excel/TissueTypeBean;)
      ];
    
    result["[Ledu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;/2535099572"] = [
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Ledu/scripps/yates/shared/model/projectCreator/excel/TissueTypeBean;),
        @edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ledu/scripps/yates/shared/model/projectCreator/excel/TissueTypeBean;)
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
    
    result["java.util.LinkedList/3953877921"] = [
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/LinkedList;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/LinkedList;)
      ];
    
    result["java.util.Stack/1346942793"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Stack;),
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Stack;)
      ];
    
    result["java.util.Vector/3057315478"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Vector_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Vector_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Vector;),
        @com.google.gwt.user.client.rpc.core.java.util.Vector_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Vector;)
      ];
    
    return result;
  }-*/;
  
  @SuppressWarnings("deprecation")
  @GwtScriptOnly
  private static native JsArrayString loadSignaturesNative() /*-{
    var result = [];
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.i18n.shared.impl.DateRecord::class)] = "com.google.gwt.i18n.shared.impl.DateRecord/3173882066";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException::class)] = "com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.rpc.RpcTokenException::class)] = "com.google.gwt.user.client.rpc.RpcTokenException/2345075298";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@com.google.gwt.user.client.rpc.XsrfToken::class)] = "com.google.gwt.user.client.rpc.XsrfToken/4254043109";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.exceptions.PintException::class)] = "edu.scripps.yates.shared.exceptions.PintException/3675183916";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE::class)] = "edu.scripps.yates.shared.exceptions.PintException$PINT_ERROR_TYPE/732031063";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.AmountType::class)] = "edu.scripps.yates.shared.model.AmountType/868712068";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.DataSourceBean::class)] = "edu.scripps.yates.shared.model.DataSourceBean/437188228";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.DataSourceBean[]::class)] = "[Ledu.scripps.yates.shared.model.DataSourceBean;/999815188";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ExperimentalConditionBean::class)] = "edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ExperimentalConditionBean[]::class)] = "[Ledu.scripps.yates.shared.model.ExperimentalConditionBean;/2104000986";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.FileFormat::class)] = "edu.scripps.yates.shared.model.FileFormat/1999197139";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.FileFormat[]::class)] = "[Ledu.scripps.yates.shared.model.FileFormat;/3651551357";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.FileSummary::class)] = "edu.scripps.yates.shared.model.FileSummary/813832004";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.LabelBean::class)] = "edu.scripps.yates.shared.model.LabelBean/940945395";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.MSRunBean::class)] = "edu.scripps.yates.shared.model.MSRunBean/2607158497";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.MSRunBean[]::class)] = "[Ledu.scripps.yates.shared.model.MSRunBean;/704096898";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.OrganismBean::class)] = "edu.scripps.yates.shared.model.OrganismBean/3624747203";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.ProjectBean::class)] = "edu.scripps.yates.shared.model.ProjectBean/194705391";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.SampleBean::class)] = "edu.scripps.yates.shared.model.SampleBean/2816907194";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.TissueBean::class)] = "edu.scripps.yates.shared.model.TissueBean/1156663452";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.ExcelDataReference::class)] = "edu.scripps.yates.shared.model.projectCreator.ExcelDataReference/3402148549";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;/2032624454";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean/3795078975";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;/3801495488";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.AmountCombinationTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.AmountCombinationTypeBean/1560618108";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean/615814808";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean;/3231424063";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean/623931705";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;/4104040992";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean/2727697045";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;/2141324727";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean/2814450547";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;/457111181";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean/2781234254";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean/2881831965";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean/2968153459";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean/303835331";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean/2703322694";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;/4209304828";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean/1074871252";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;/2099369771";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean/2146506819";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean/3035767996";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean/4011760336";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;/68483336";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean/1366228899";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;/1041126115";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean/3185218991";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean/2047247850";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean/1495819149";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;/827869284";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean/3274249885";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean/3221725003";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean/1929374072";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean/1910602586";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean/969277473";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean/876007744";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean;/3352293687";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean/435014155";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean/2004357379";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean/264732941";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean/1354070335";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean;/3449893462";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean/1000030158";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean/1411683221";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean/2197746369";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean/996213295";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean;/524456024";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean/3757473012";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;/3998358695";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean/2704710783";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean/2006781713";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean/3907916378";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;/155765680";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean/482043022";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;/1107417980";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean/2486782095";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean/1027099810";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;/3531128460";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean/450387415";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;/3033471420";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean/2623695042";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean/1597661448";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;/257352234";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean/911645338";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean/2817870756";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;/1375609492";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean/3559385418";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean/3471490246";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean::class)] = "edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean/2660810438";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean[]::class)] = "[Ledu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;/2535099572";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.Boolean::class)] = "java.lang.Boolean/476441737";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.Double::class)] = "java.lang.Double/858496421";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.Integer::class)] = "java.lang.Integer/3438268394";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.String::class)] = "java.lang.String/2004016611";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.lang.String[]::class)] = "[Ljava.lang.String;/2600011424";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.sql.Date::class)] = "java.sql.Date/730999118";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.sql.Time::class)] = "java.sql.Time/1816797103";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.sql.Timestamp::class)] = "java.sql.Timestamp/3040052672";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.ArrayList::class)] = "java.util.ArrayList/4159755760";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Arrays.ArrayList::class)] = "java.util.Arrays$ArrayList/2507071751";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Collections.EmptyList::class)] = "java.util.Collections$EmptyList/4157118744";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Collections.SingletonList::class)] = "java.util.Collections$SingletonList/1586180994";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Date::class)] = "java.util.Date/3385151746";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.LinkedList::class)] = "java.util.LinkedList/3953877921";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Stack::class)] = "java.util.Stack/1346942793";
    result[@javaemul.internal.HashCodes::getObjectIdentityHashCode(*)(@java.util.Vector::class)] = "java.util.Vector/3057315478";
    return result;
  }-*/;
  
  public ImportWizardService_TypeSerializer() {
    super(null, methodMapNative, null, signatureMapNative);
  }
  
}
