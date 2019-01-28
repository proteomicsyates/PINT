package edu.scripps.yates;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.RpcToken;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;

public class ImportWizardService_Proxy extends RemoteServiceProxy implements edu.scripps.yates.ImportWizardServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "edu.scripps.yates.ImportWizardService";
  private static final String SERIALIZATION_POLICY ="1A06BD12C65E8885A6BD64CCF9A440A3";
  private static final edu.scripps.yates.ImportWizardService_TypeSerializer SERIALIZER = new edu.scripps.yates.ImportWizardService_TypeSerializer();
  
  public ImportWizardService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "importWizard", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public void addDataFile(java.lang.String sessionID, int jobID, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean dataFile, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "addDataFile");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(dataFile);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void addDataFiles(java.lang.String sessionID, int jobID, java.util.List files, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "addDataFiles");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("java.util.List");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(files);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void addRemoteFile(java.lang.String sessionID, int jobID, edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean remoteFilesWithType, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "addRemoteFile");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean/3795078975");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(remoteFilesWithType);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void addRemoteFiles(java.lang.String sessionID, int jobID, java.util.List remoteFilesWithTypes, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "addRemoteFiles");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("java.util.List");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(remoteFilesWithTypes);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void checkDataFileAvailability(java.lang.String sessionID, int jobID, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean fileNameWithTypeBean, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "checkDataFileAvailability");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(fileNameWithTypeBean);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void checkRemoteFileInfoAccessibility(java.lang.String sessionID, edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean remoteFile, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "checkRemoteFileInfoAccessibility");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean/3795078975");
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(remoteFile);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void checkServerAccessibility(java.lang.String sessionID, edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean server, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "checkServerAccessibility");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean/1597661448");
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(server);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void checkUserLogin(java.lang.String userName, java.lang.String encryptedPassword, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "checkUserLogin");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(userName);
      streamWriter.writeString(encryptedPassword);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void generateNewImportProcessID(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "generateNewImportProcessID");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getAvailableACCTypes(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getAvailableACCTypes");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getDataSourceBeans(java.lang.String sessionID, int jobID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getDataSourceBeans");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getExcelFileBean(java.lang.String sessionID, int jobId, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean file, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getExcelFileBean");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobId);
      streamWriter.writeObject(file);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getFileFormats(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getFileFormats");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getFileSummary(int importID, java.lang.String sessionID, edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean file, com.google.gwt.user.client.rpc.AsyncCallback asyncCallback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getFileSummary");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("I");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean/2703322694");
      streamWriter.writeInt(importID);
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(file);
      helper.finish(asyncCallback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      asyncCallback.onFailure(ex);
    }
  }
  
  public void getOrganismList(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getOrganismList");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPTMNames(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getPTMNames");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPintImportCfgTypeBean(java.lang.String sessionID, int jobID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getPintImportCfgTypeBean");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProjectBean(java.lang.String sessionID, int jobID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getProjectBean");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getRandomProteinAccessions(java.lang.String sessionID, int importJobID, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean dataFileBean, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean fastaFileBean, int numTestCases, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getRandomProteinAccessions");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 5);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(importJobID);
      streamWriter.writeObject(dataFileBean);
      streamWriter.writeObject(fastaFileBean);
      streamWriter.writeInt(numTestCases);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getRandomValues(java.lang.String sessionID, int jobId, edu.scripps.yates.shared.model.projectCreator.ExcelDataReference excelDataReference, int numValues, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getRandomValues");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 4);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.ExcelDataReference/3402148549");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobId);
      streamWriter.writeObject(excelDataReference);
      streamWriter.writeInt(numValues);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getScoreTypes(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getScoreTypes");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getTaxId(java.lang.String sessionID, java.lang.String organismName, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getTaxId");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(organismName);
      helper.finish(callback, ResponseReader.STRING);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getTissueList(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getTissueList");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getUploadedFileID(int importID, java.lang.String uploadedFileSignature, com.google.gwt.user.client.rpc.AsyncCallback asyncCallback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "getUploadedFileID");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("I");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeInt(importID);
      streamWriter.writeString(uploadedFileSignature);
      helper.finish(asyncCallback, ResponseReader.STRING);
    } catch (SerializationException ex) {
      asyncCallback.onFailure(ex);
    }
  }
  
  public void moveDataFile(java.lang.String sessionID, int jobID, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean fileOLD, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean fileNew, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "moveDataFile");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 4);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(fileOLD);
      streamWriter.writeObject(fileNew);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void removeDataFile(java.lang.String sessionID, int jobID, edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean dataFile, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "removeDataFile");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean/3337340184");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(dataFile);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void saveImportConfiguration(int jobID, edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean pintImportCfgBean, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "saveImportConfiguration");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean/3221725003");
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(pintImportCfgBean);
      helper.finish(callback, ResponseReader.BOOLEAN);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void startNewImportProcess(java.lang.String sessionID, java.lang.String uploadedProjectCfgFileName, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "startNewImportProcess");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(uploadedProjectCfgFileName);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void startNewImportProcess(java.lang.String sessionID, java.lang.String projectTag, java.util.List dataFileNames, java.util.List remoteFiles, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "startNewImportProcess");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 4);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.util.List");
      streamWriter.writeString("java.util.List");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(dataFileNames);
      streamWriter.writeObject(remoteFiles);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void submitProject(int jobID, edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean pintImportCfgBean, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "submitProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean/3221725003");
      streamWriter.writeInt(jobID);
      streamWriter.writeObject(pintImportCfgBean);
      helper.finish(callback, ResponseReader.STRING);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void updateFileFormat(java.lang.String sessionID, int importID, edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean fileTypeBean, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "updateFileFormat");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean/2703322694");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(importID);
      streamWriter.writeObject(fileTypeBean);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void validatePintImportCfg(edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean pintImportCfg, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ImportWizardService_Proxy", "validatePintImportCfg");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean/1929374072");
      streamWriter.writeObject(pintImportCfg);
      helper.finish(callback, ResponseReader.STRING);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  @Override
  public SerializationStreamWriter createStreamWriter() {
    ClientSerializationStreamWriter toReturn =
      (ClientSerializationStreamWriter) super.createStreamWriter();
    if (getRpcToken() != null) {
      toReturn.addFlags(ClientSerializationStreamWriter.FLAG_RPC_TOKEN_INCLUDED);
    }
    return toReturn;
  }
  @Override
  protected void checkRpcTokenType(RpcToken token) {
    if (!(token instanceof com.google.gwt.user.client.rpc.XsrfToken)) {
      throw new RpcTokenException("Invalid RpcToken type: expected 'com.google.gwt.user.client.rpc.XsrfToken' but got '" + token.getClass() + "'");
    }
  }
}
