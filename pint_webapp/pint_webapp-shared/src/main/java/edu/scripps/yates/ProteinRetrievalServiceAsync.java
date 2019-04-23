package edu.scripps.yates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.shared.tasks.Task;

public interface ProteinRetrievalServiceAsync {

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProjectTags(AsyncCallback<java.util.Set<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProjectBeans(AsyncCallback<java.util.Set<edu.scripps.yates.shared.model.ProjectBean>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProjectBean(java.lang.String projectTag,
			AsyncCallback<edu.scripps.yates.shared.model.ProjectBean> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProjectBeans(java.util.Set<java.lang.String> projectTags,
			AsyncCallback<java.util.List<edu.scripps.yates.shared.model.ProjectBean>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinsFromProjects(java.lang.String sessionID, java.util.Set<java.lang.String> projectTags,
			java.lang.String uniprotVersion, boolean separateNonConclusiveProteins, java.lang.Integer defaultQueryIndex,
			boolean testMode, Task task,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.QueryResultSubLists> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinsFromQuery(java.lang.String sessionID, java.lang.String queryText,
			java.util.Set<java.lang.String> projectTags, String uniprotVersion, boolean separateNonConclusiveProteins,
			boolean lock, boolean testMode, boolean ignoreReferences, boolean ignoreDBReferences, Task task,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.QueryResultSubLists> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getExperimentalConditionsFromProject(java.lang.String projectTag,
			AsyncCallback<java.util.Set<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getDownloadLinkForProteinsFromQuery(java.lang.String sessionID, java.lang.String queryText,
			java.util.Set<java.lang.String> projectTags,
			AsyncCallback<edu.scripps.yates.shared.util.FileDescriptor> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getDownloadLinkForProteinGroupsFromQuery(java.lang.String sessionID, java.lang.String queryText,
			java.util.Set<java.lang.String> projectTags, boolean separateNonConclusiveProteins,
			AsyncCallback<edu.scripps.yates.shared.util.FileDescriptor> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getUniprotAnnotationsFromProjects(java.util.Set<java.lang.String> projectTags,
			AsyncCallback<java.util.Set<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getAnnotationTypes(AsyncCallback<java.util.List<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getUniprotHeaderLines(AsyncCallback<java.util.Map<java.lang.String, java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getCommands(AsyncCallback<java.util.Map<java.lang.String, java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getThresholdNamesFromProjects(java.util.Set<java.lang.String> projectTags,
			AsyncCallback<java.util.Set<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getRatioDescriptorsFromProjects(java.util.Set<java.lang.String> projectTags,
			AsyncCallback<java.util.List<edu.scripps.yates.shared.model.RatioDescriptorBean>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void groupProteins(java.lang.String sessionID, boolean separateNonConclusiveProteins, int pageSize, Task task,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumExperiments(AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentProteins(AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentProteins(java.lang.String projectTag, AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentPeptides(AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentPeptides(java.lang.String projectTag, AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumPSMs(AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumPSMs(java.lang.String projectTag, AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumConditions(AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumConditions(java.lang.String projectTag, AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumConditions(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumSamples(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getOrganismsByProject(java.lang.String projectTag,
			AsyncCallback<java.util.Set<edu.scripps.yates.shared.model.OrganismBean>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getDownloadLinkForProteinsInProject(java.util.Collection<java.lang.String> projectTags,
			AsyncCallback<edu.scripps.yates.shared.util.FileDescriptor> callback);

	void getDownloadLinkForInputFilesOfProject(String projectTag,
			AsyncCallback<edu.scripps.yates.shared.util.FileDescriptor> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getDownloadLinkForProteinGroupsInProject(java.util.Collection<java.lang.String> projectTags,
			boolean separateNonConclusiveProteins,
			AsyncCallback<edu.scripps.yates.shared.util.FileDescriptor> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getHiddenPTMs(java.lang.String sessionID, java.lang.String projectTag,
			AsyncCallback<java.util.Set<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getDefaultViewByProject(java.lang.String projectTag,
			AsyncCallback<edu.scripps.yates.shared.util.DefaultView> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProgressStatus(java.lang.String sessionID, Task task,
			AsyncCallback<edu.scripps.yates.shared.util.ProgressStatus> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinBeansFromList(java.lang.String sessionID, int start, int end,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.ProteinBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinGroupBeansFromList(java.lang.String sessionID, int start, int end,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPSMBeansFromList(java.lang.String sessionID, int start, int end,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.PsmBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinBeansFromListSorted(java.lang.String sessionID, int start, int end,
			java.util.Comparator<edu.scripps.yates.shared.model.ProteinBean> comparator, boolean ascendant,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.ProteinBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinGroupBeansFromListSorted(java.lang.String sessionID, int start, int end,
			java.util.Comparator<edu.scripps.yates.shared.model.ProteinGroupBean> comparator, boolean ascendant,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPSMBeansFromListSorted(java.lang.String sessionID, int start, int end,
			java.util.Comparator<edu.scripps.yates.shared.model.PSMBean> comparator, boolean ascendant,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.PsmBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void login(java.lang.String clientToken, java.lang.String userName, java.lang.String password,
			AsyncCallback<java.lang.String> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPsmBeansFromPsmProviderFromListSorted(java.lang.String sessionID,
			edu.scripps.yates.shared.model.interfaces.ContainsPSMs psmProvider, int start, int end,
			java.util.Comparator<edu.scripps.yates.shared.model.PSMBean> comparator, boolean ascending,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.PsmBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPsmBeansFromPsmProviderFromList(java.lang.String sessionID,
			edu.scripps.yates.shared.model.interfaces.ContainsPSMs psmProvider, int start, int end,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.PsmBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void closeSession(java.lang.String sessionID, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getMsRunsFromProject(java.lang.String projectTag, AsyncCallback<java.util.Set<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void sendPSEAQuantQuery(java.lang.String email,
			edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism organism,
			java.util.List<edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate> replicates,
			edu.scripps.yates.shared.model.RatioDescriptorBean ratioDescriptor, long numberOfSamplings,
			edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType quantType,
			edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase annotationDatabase,
			edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol cvTol, java.lang.Double cvTolFactor,
			edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias literatureBias,
			AsyncCallback<edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinsByPeptide(java.lang.String sessionID,
			edu.scripps.yates.shared.model.interfaces.ContainsPeptides peptideProvider,
			AsyncCallback<edu.scripps.yates.shared.model.ProteinPeptideCluster> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPeptideBeansFromListSorted(java.lang.String sessionID, int start, int end,
			java.util.Comparator<edu.scripps.yates.shared.model.PeptideBean> comparator, boolean ascendant,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.PeptideBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPeptideBeansFromList(java.lang.String sessionID, int start, int end,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.PeptideBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPeptideBeansFromPeptideProviderFromListSorted(java.lang.String sessionID,
			edu.scripps.yates.shared.model.interfaces.ContainsPeptides peptideProvider, int start, int end,
			java.util.Comparator<edu.scripps.yates.shared.model.PeptideBean> comparator, boolean ascending,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.PeptideBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPeptideBeansFromPeptideProviderFromList(java.lang.String sessionID,
			edu.scripps.yates.shared.model.interfaces.ContainsPeptides peptideProvider, int start, int end,
			AsyncCallback<edu.scripps.yates.shared.util.sublists.PeptideBeanSubList> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumMSRuns(AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumMSRuns(java.lang.String projectTag, AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumMSRuns(java.lang.String projectTag, edu.scripps.yates.shared.model.ExperimentalConditionBean condition,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumGenes(AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumGenes(java.lang.String projectTag, AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPSMAmountTypesByCondition(java.lang.String sessionID, java.lang.String projectTag,
			java.lang.String conditionName,
			AsyncCallback<java.util.Set<edu.scripps.yates.shared.model.AmountType>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getPeptideAmountTypesByCondition(java.lang.String sessionID, java.lang.String projectTag,
			java.lang.String conditionName,
			AsyncCallback<java.util.Set<edu.scripps.yates.shared.model.AmountType>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinAmountTypesByCondition(java.lang.String sessionID, java.lang.String projectTag,
			java.lang.String conditionName,
			AsyncCallback<java.util.Set<edu.scripps.yates.shared.model.AmountType>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getDownloadLinkForReactomeAnalysisResult(java.lang.String sessionID,
			AsyncCallback<edu.scripps.yates.shared.util.FileDescriptor> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinProjectionsByProteinACCFromProject(java.lang.String projectTag,
			AsyncCallback<java.util.Map<java.lang.String, java.util.Set<edu.scripps.yates.shared.model.ProteinProjection>>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinProjectionsByProteinNameFromProject(java.lang.String projectTag,
			AsyncCallback<java.util.Map<java.lang.String, java.util.Set<edu.scripps.yates.shared.model.ProteinProjection>>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getProteinProjectionsByGeneNameFromProject(java.lang.String projectTag,
			AsyncCallback<java.util.Map<java.lang.String, java.util.Set<edu.scripps.yates.shared.model.ProteinProjection>>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentProteins(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentPeptides(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumPSMs(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumGenes(java.lang.String projectTag, edu.scripps.yates.shared.model.MSRunBean msRun,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentProteins(java.lang.String projectTag,
			edu.scripps.yates.shared.model.ExperimentalConditionBean condition,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentPeptides(java.lang.String projectTag,
			edu.scripps.yates.shared.model.ExperimentalConditionBean condition,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumPSMs(java.lang.String projectTag, edu.scripps.yates.shared.model.ExperimentalConditionBean condition,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumGenes(java.lang.String projectTag, edu.scripps.yates.shared.model.ExperimentalConditionBean condition,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentProteins(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumDifferentPeptides(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumPSMs(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumGenes(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void getNumMSRuns(java.lang.String projectTag, edu.scripps.yates.shared.model.SampleBean sample,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ProteinRetrievalService
	 */
	void cancelQuery(java.lang.String sessionID, AsyncCallback<Void> callback);

	void isTestServer(AsyncCallback<Boolean> callback);

	/**
	 * Utility class to get the RPC Async interface from client-side code
	 */
	public static final class Util {
		private static ProteinRetrievalServiceAsync instance;

		public static final ProteinRetrievalServiceAsync getInstance() {
			if (instance == null) {
				instance = (ProteinRetrievalServiceAsync) GWT.create(ProteinRetrievalService.class);
			}
			return instance;
		}

		private Util() {
			// Utility class should not be instantiated
		}
	}

}
