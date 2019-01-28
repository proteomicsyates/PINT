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

public class ProteinRetrievalService_Proxy extends RemoteServiceProxy implements edu.scripps.yates.ProteinRetrievalServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "edu.scripps.yates.ProteinRetrievalService";
  private static final String SERIALIZATION_POLICY ="0B02722861360F7AA4069C22944EFDED";
  private static final edu.scripps.yates.ProteinRetrievalService_TypeSerializer SERIALIZER = new edu.scripps.yates.ProteinRetrievalService_TypeSerializer();
  
  public ProteinRetrievalService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "proteins", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public void cancelQuery(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "cancelQuery");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void closeSession(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "closeSession");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.VOID);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getAnnotationTypes(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getAnnotationTypes");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getCommands(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getCommands");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getDefaultViewByProject(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getDefaultViewByProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getDownloadLinkForProteinGroupsFromQuery(java.lang.String sessionID, java.lang.String queryText, java.util.Set projectTags, boolean separateNonConclusiveProteins, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getDownloadLinkForProteinGroupsFromQuery");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 4);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.util.Set");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(queryText);
      streamWriter.writeObject(projectTags);
      streamWriter.writeBoolean(separateNonConclusiveProteins);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getDownloadLinkForProteinGroupsInProject(java.util.List projectTags, boolean separateNonConclusiveProteins, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getDownloadLinkForProteinGroupsInProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.util.List");
      streamWriter.writeString("Z");
      streamWriter.writeObject(projectTags);
      streamWriter.writeBoolean(separateNonConclusiveProteins);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getDownloadLinkForProteinsFromQuery(java.lang.String sessionID, java.lang.String queryText, java.util.Set projectTags, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getDownloadLinkForProteinsFromQuery");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.util.Set");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(queryText);
      streamWriter.writeObject(projectTags);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getDownloadLinkForProteinsInProject(java.util.List projectTags, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getDownloadLinkForProteinsInProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.util.List");
      streamWriter.writeObject(projectTags);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getDownloadLinkForReactomeAnalysisResult(java.lang.String sessionID, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getDownloadLinkForReactomeAnalysisResult");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getExperimentalConditionsFromProject(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getExperimentalConditionsFromProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getHiddenPTMs(java.lang.String sessionID, java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getHiddenPTMs");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getMsRunsFromProject(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getMsRunsFromProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumConditions(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumConditions");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumConditions(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumConditions");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumConditions(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumConditions");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.MSRunBean/2607158497");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(msRun);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentPeptides(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentPeptides");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentPeptides(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentPeptides");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentPeptides(java.lang.String projectTag, edu.scripps.yates.shared.model.ExperimentalConditionBean condition, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentPeptides");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(condition);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentPeptides(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentPeptides");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.MSRunBean/2607158497");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(msRun);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentPeptides(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentPeptides");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.SampleBean/2816907194");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(sample);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentProteins(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentProteins");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentProteins(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentProteins");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentProteins(java.lang.String projectTag, edu.scripps.yates.shared.model.ExperimentalConditionBean condition, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentProteins");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(condition);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentProteins(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentProteins");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.MSRunBean/2607158497");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(msRun);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumDifferentProteins(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumDifferentProteins");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.SampleBean/2816907194");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(sample);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumExperiments(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumExperiments");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumGenes(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumGenes");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumGenes(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumGenes");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumGenes(java.lang.String projectTag, edu.scripps.yates.shared.model.ExperimentalConditionBean condition, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumGenes");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(condition);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumGenes(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumGenes");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.MSRunBean/2607158497");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(msRun);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumGenes(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumGenes");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.SampleBean/2816907194");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(sample);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumMSRuns(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumMSRuns");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumMSRuns(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumMSRuns");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumMSRuns(java.lang.String projectTag, edu.scripps.yates.shared.model.ExperimentalConditionBean condition, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumMSRuns");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(condition);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumMSRuns(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumMSRuns");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.SampleBean/2816907194");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(sample);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumPSMs(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumPSMs");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumPSMs(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumPSMs");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumPSMs(java.lang.String projectTag, edu.scripps.yates.shared.model.ExperimentalConditionBean condition, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumPSMs");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.ExperimentalConditionBean/4268611534");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(condition);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumPSMs(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumPSMs");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.MSRunBean/2607158497");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(msRun);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumPSMs(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumPSMs");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.SampleBean/2816907194");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(sample);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getNumSamples(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getNumSamples");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.MSRunBean/2607158497");
      streamWriter.writeString(projectTag);
      streamWriter.writeObject(msRun);
      helper.finish(callback, ResponseReader.INT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getOrganismsByProject(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getOrganismsByProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPSMAmountTypesByCondition(java.lang.String sessionID, java.lang.String projectTag, java.lang.String conditionName, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPSMAmountTypesByCondition");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(projectTag);
      streamWriter.writeString(conditionName);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPSMBeansFromList(java.lang.String sessionID, int start, int end, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPSMBeansFromList");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPSMBeansFromListSorted(java.lang.String sessionID, int start, int end, java.util.Comparator comparator, boolean ascendant, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPSMBeansFromListSorted");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 5);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString("java.util.Comparator");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      streamWriter.writeObject(comparator);
      streamWriter.writeBoolean(ascendant);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPeptideAmountTypesByCondition(java.lang.String sessionID, java.lang.String projectTag, java.lang.String conditionName, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPeptideAmountTypesByCondition");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(projectTag);
      streamWriter.writeString(conditionName);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPeptideBeansFromList(java.lang.String sessionID, int start, int end, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPeptideBeansFromList");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPeptideBeansFromListSorted(java.lang.String sessionID, int start, int end, java.util.Comparator comparator, boolean ascendant, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPeptideBeansFromListSorted");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 5);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString("java.util.Comparator");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      streamWriter.writeObject(comparator);
      streamWriter.writeBoolean(ascendant);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPeptideBeansFromPeptideProviderFromList(java.lang.String sessionID, edu.scripps.yates.shared.model.interfaces.ContainsPeptides peptideProvider, int start, int end, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPeptideBeansFromPeptideProviderFromList");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 4);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.interfaces.ContainsPeptides");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(peptideProvider);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPeptideBeansFromPeptideProviderFromListSorted(java.lang.String sessionID, edu.scripps.yates.shared.model.interfaces.ContainsPeptides peptideProvider, int start, int end, java.util.Comparator comparator, boolean ascending, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPeptideBeansFromPeptideProviderFromListSorted");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 6);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.interfaces.ContainsPeptides");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString("java.util.Comparator");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(peptideProvider);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      streamWriter.writeObject(comparator);
      streamWriter.writeBoolean(ascending);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProgressStatus(java.lang.String sessionID, java.lang.String taskKey, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProgressStatus");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(taskKey);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProjectBean(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProjectBean");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProjectBeans(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProjectBeans");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProjectBeans(java.util.Set projectTags, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProjectBeans");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.util.Set");
      streamWriter.writeObject(projectTags);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProjectTags(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProjectTags");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinAmountTypesByCondition(java.lang.String sessionID, java.lang.String projectTag, java.lang.String conditionName, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinAmountTypesByCondition");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(projectTag);
      streamWriter.writeString(conditionName);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinBeansFromList(java.lang.String sessionID, int start, int end, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinBeansFromList");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinBeansFromListSorted(java.lang.String sessionID, int start, int end, java.util.Comparator comparator, boolean ascendant, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinBeansFromListSorted");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 5);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString("java.util.Comparator");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      streamWriter.writeObject(comparator);
      streamWriter.writeBoolean(ascendant);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinGroupBeansFromList(java.lang.String sessionID, int start, int end, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinGroupBeansFromList");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinGroupBeansFromListSorted(java.lang.String sessionID, int start, int end, java.util.Comparator comparator, boolean ascendant, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinGroupBeansFromListSorted");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 5);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString("java.util.Comparator");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      streamWriter.writeObject(comparator);
      streamWriter.writeBoolean(ascendant);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinProjectionsByGeneNameFromProject(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinProjectionsByGeneNameFromProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinProjectionsByProteinACCFromProject(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinProjectionsByProteinACCFromProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinProjectionsByProteinNameFromProject(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinProjectionsByProteinNameFromProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinProjectionsFromProject(java.lang.String projectTag, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinProjectionsFromProject");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(projectTag);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinsByPeptide(java.lang.String sessionID, edu.scripps.yates.shared.model.interfaces.ContainsPeptides peptideProvider, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinsByPeptide");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.interfaces.ContainsPeptides");
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(peptideProvider);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinsFromProjects(java.lang.String sessionID, java.util.Set projectTags, java.lang.String uniprotVersion, boolean separateNonConclusiveProteins, java.lang.Integer defaultQueryIndex, boolean testMode, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinsFromProjects");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 6);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.util.Set");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("Z");
      streamWriter.writeString("java.lang.Integer/3438268394");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(projectTags);
      streamWriter.writeString(uniprotVersion);
      streamWriter.writeBoolean(separateNonConclusiveProteins);
      streamWriter.writeObject(defaultQueryIndex);
      streamWriter.writeBoolean(testMode);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getProteinsFromQuery(java.lang.String sessionID, java.lang.String queryText, java.util.Set projectTags, boolean separateNonConclusiveProteins, boolean lock, boolean testMode, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getProteinsFromQuery");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 6);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.util.Set");
      streamWriter.writeString("Z");
      streamWriter.writeString("Z");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeString(queryText);
      streamWriter.writeObject(projectTags);
      streamWriter.writeBoolean(separateNonConclusiveProteins);
      streamWriter.writeBoolean(lock);
      streamWriter.writeBoolean(testMode);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPsmBeansFromPsmProviderFromList(java.lang.String sessionID, edu.scripps.yates.shared.model.interfaces.ContainsPSMs psmProvider, int start, int end, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPsmBeansFromPsmProviderFromList");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 4);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.interfaces.ContainsPSMs");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(psmProvider);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getPsmBeansFromPsmProviderFromListSorted(java.lang.String sessionID, edu.scripps.yates.shared.model.interfaces.ContainsPSMs psmProvider, int start, int end, java.util.Comparator comparator, boolean ascending, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getPsmBeansFromPsmProviderFromListSorted");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 6);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.model.interfaces.ContainsPSMs");
      streamWriter.writeString("I");
      streamWriter.writeString("I");
      streamWriter.writeString("java.util.Comparator");
      streamWriter.writeString("Z");
      streamWriter.writeString(sessionID);
      streamWriter.writeObject(psmProvider);
      streamWriter.writeInt(start);
      streamWriter.writeInt(end);
      streamWriter.writeObject(comparator);
      streamWriter.writeBoolean(ascending);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getRatioDescriptorsFromProjects(java.util.Set projectTags, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getRatioDescriptorsFromProjects");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.util.Set");
      streamWriter.writeObject(projectTags);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getThresholdNamesFromProjects(java.util.Set projectTags, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getThresholdNamesFromProjects");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.util.Set");
      streamWriter.writeObject(projectTags);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getUniprotAnnotationsFromProjects(java.util.Set projectTags, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getUniprotAnnotationsFromProjects");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 1);
      streamWriter.writeString("java.util.Set");
      streamWriter.writeObject(projectTags);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getUniprotHeaderLines(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "getUniprotHeaderLines");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 0);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void groupProteins(java.lang.String sessionID, boolean separateNonConclusiveProteins, int pageSize, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "groupProteins");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("Z");
      streamWriter.writeString("I");
      streamWriter.writeString(sessionID);
      streamWriter.writeBoolean(separateNonConclusiveProteins);
      streamWriter.writeInt(pageSize);
      helper.finish(callback, ResponseReader.OBJECT);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void login(java.lang.String clientToken, java.lang.String userName, java.lang.String password, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "login");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(clientToken);
      streamWriter.writeString(userName);
      streamWriter.writeString(password);
      helper.finish(callback, ResponseReader.STRING);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void sendPSEAQuantQuery(java.lang.String email, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism organism, java.util.List replicates, edu.scripps.yates.shared.model.RatioDescriptorBean ratioDescriptor, long numberOfSamplings, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType quantType, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase annotationDatabase, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol cvTol, java.lang.Double cvTolFactor, edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias literatureBias, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper helper = new com.google.gwt.user.client.rpc.impl.RemoteServiceProxy.ServiceHelper("ProteinRetrievalService_Proxy", "sendPSEAQuantQuery");
    try {
      SerializationStreamWriter streamWriter = helper.start(REMOTE_SERVICE_INTERFACE_NAME, 10);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism/3199015948");
      streamWriter.writeString("java.util.List");
      streamWriter.writeString("edu.scripps.yates.shared.model.RatioDescriptorBean/1873648207");
      streamWriter.writeString("J");
      streamWriter.writeString("edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType/1784699068");
      streamWriter.writeString("edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase/1802311985");
      streamWriter.writeString("edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol/891003955");
      streamWriter.writeString("java.lang.Double/858496421");
      streamWriter.writeString("edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias/29143058");
      streamWriter.writeString(email);
      streamWriter.writeObject(organism);
      streamWriter.writeObject(replicates);
      streamWriter.writeObject(ratioDescriptor);
      streamWriter.writeLong(numberOfSamplings);
      streamWriter.writeObject(quantType);
      streamWriter.writeObject(annotationDatabase);
      streamWriter.writeObject(cvTol);
      streamWriter.writeObject(cvTolFactor);
      streamWriter.writeObject(literatureBias);
      helper.finish(callback, ResponseReader.OBJECT);
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
