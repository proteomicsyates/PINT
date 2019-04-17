package edu.scripps.yates;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.model.ProteinProjection;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;
import edu.scripps.yates.shared.tasks.Task;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.FileDescriptor;
import edu.scripps.yates.shared.util.ProgressStatus;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList;
import edu.scripps.yates.shared.util.sublists.PsmBeanSubList;
import edu.scripps.yates.shared.util.sublists.QueryResultSubLists;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("proteins")
public interface ProteinRetrievalService extends RemoteService {

	// String greetServer(String name) throws PintException;

	Set<String> getProjectTags() throws PintException;

	Set<ProjectBean> getProjectBeans() throws PintException;

	ProjectBean getProjectBean(String projectTag) throws PintException;

	List<ProjectBean> getProjectBeans(Set<String> projectTags) throws PintException;

	QueryResultSubLists getProteinsFromProjects(String sessionID, Set<String> projectTags, String uniprotVersion,
			boolean separateNonConclusiveProteins, Integer defaultQueryIndex, boolean testMode, Task task)
			throws PintException;

	QueryResultSubLists getProteinsFromQuery(String sessionID, String queryText, Set<String> projectTags,
			String uniprotVersion, boolean separateNonConclusiveProteins, boolean lock, boolean testMode,
			boolean ignoreReferences, boolean ignoreDBReferences, Task task) throws PintException;

	Set<String> getExperimentalConditionsFromProject(String projectTag) throws PintException;

	FileDescriptor getDownloadLinkForProteinsFromQuery(String sessionID, String queryText, Set<String> projectTags)
			throws PintException;

	FileDescriptor getDownloadLinkForProteinGroupsFromQuery(String sessionID, String queryText, Set<String> projectTags,
			boolean separateNonConclusiveProteins) throws PintException;

	Set<String> getUniprotAnnotationsFromProjects(Set<String> projectTags) throws PintException;

	List<String> getAnnotationTypes() throws PintException;

	Map<String, String> getUniprotHeaderLines() throws PintException;

	Map<String, String> getCommands() throws PintException;

	Set<String> getThresholdNamesFromProjects(Set<String> projectTags) throws PintException;

	List<RatioDescriptorBean> getRatioDescriptorsFromProjects(Set<String> projectTags) throws PintException;

	// List<ProteinGroupBean> groupProteins(
	// TInHashSet proteinBeanUniqueIdentifiers,
	// boolean separateNonConclusiveProteins) throws PintException;

	ProteinGroupBeanSubList groupProteins(String sessionID, boolean separateNonConclusiveProteins, int pageSize,
			Task task) throws PintException;

	int getNumExperiments() throws PintException;

	int getNumDifferentProteins() throws PintException;

	int getNumDifferentProteins(String projectTag) throws PintException;

	int getNumDifferentPeptides() throws PintException;

	int getNumDifferentPeptides(String projectTag) throws PintException;

	int getNumPSMs() throws PintException;

	int getNumPSMs(String projectTag) throws PintException;

	int getNumConditions() throws PintException;

	int getNumConditions(String projectTag) throws PintException;

	int getNumConditions(String projectTag, MSRunBean msRun) throws PintException;

	int getNumSamples(String projectTag, MSRunBean msRun) throws PintException;

	Set<OrganismBean> getOrganismsByProject(String projectTag) throws PintException;

	FileDescriptor getDownloadLinkForProteinsInProject(List<String> projectTags) throws PintException;

	FileDescriptor getDownloadLinkForProteinGroupsInProject(List<String> projectTags,
			boolean separateNonConclusiveProteins) throws PintException;

	Set<String> getHiddenPTMs(String sessionID, String projectTag) throws PintException;

	DefaultView getDefaultViewByProject(String projectTag) throws PintException;

	ProgressStatus getProgressStatus(String sessionID, Task task) throws PintException;

	ProteinBeanSubList getProteinBeansFromList(String sessionID, int start, int end) throws PintException;

	ProteinGroupBeanSubList getProteinGroupBeansFromList(String sessionID, int start, int end) throws PintException;

	PsmBeanSubList getPSMBeansFromList(String sessionID, int start, int end) throws PintException;

	ProteinBeanSubList getProteinBeansFromListSorted(String sessionID, int start, int end,
			Comparator<ProteinBean> comparator, boolean ascendant) throws PintException;

	ProteinGroupBeanSubList getProteinGroupBeansFromListSorted(String sessionID, int start, int end,
			Comparator<ProteinGroupBean> comparator, boolean ascendant) throws PintException;

	PsmBeanSubList getPSMBeansFromListSorted(String sessionID, int start, int end, Comparator<PSMBean> comparator,
			boolean ascendant) throws PintException;

	String login(String clientToken, String userName, String password) throws PintException;

	PsmBeanSubList getPsmBeansFromPsmProviderFromListSorted(String sessionID, ContainsPSMs psmProvider, int start,
			int end, Comparator<PSMBean> comparator, boolean ascending) throws PintException;

	PsmBeanSubList getPsmBeansFromPsmProviderFromList(String sessionID, ContainsPSMs psmProvider, int start, int end)
			throws PintException;

	void closeSession(String sessionID) throws PintException;

	Set<String> getMsRunsFromProject(String projectTag) throws PintException;

	PSEAQuantResult sendPSEAQuantQuery(String email, PSEAQuantSupportedOrganism organism,
			List<PSEAQuantReplicate> replicates, RatioDescriptorBean ratioDescriptor, long numberOfSamplings,
			PSEAQuantQuantType quantType, PSEAQuantAnnotationDatabase annotationDatabase, PSEAQuantCVTol cvTol,
			Double cvTolFactor, PSEAQuantLiteratureBias literatureBias) throws PintException;

	ProteinPeptideCluster getProteinsByPeptide(String sessionID, ContainsPeptides peptideProvider) throws PintException;

	PeptideBeanSubList getPeptideBeansFromListSorted(String sessionID, int start, int end,
			Comparator<PeptideBean> comparator, boolean ascendant) throws PintException;

	PeptideBeanSubList getPeptideBeansFromList(String sessionID, int start, int end) throws PintException;

	PeptideBeanSubList getPeptideBeansFromPeptideProviderFromListSorted(String sessionID,
			ContainsPeptides peptideProvider, int start, int end, Comparator<PeptideBean> comparator, boolean ascending)
			throws PintException;

	PeptideBeanSubList getPeptideBeansFromPeptideProviderFromList(String sessionID, ContainsPeptides peptideProvider,
			int start, int end) throws PintException;

	int getNumMSRuns() throws PintException;

	int getNumMSRuns(String projectTag) throws PintException;

	int getNumMSRuns(String projectTag, ExperimentalConditionBean condition) throws PintException;

	int getNumGenes() throws PintException;

	int getNumGenes(String projectTag) throws PintException;

	Set<AmountType> getPSMAmountTypesByCondition(String sessionID, String projectTag, String conditionName)
			throws PintException;

	Set<AmountType> getPeptideAmountTypesByCondition(String sessionID, String projectTag, String conditionName)
			throws PintException;

	Set<AmountType> getProteinAmountTypesByCondition(String sessionID, String projectTag, String conditionName)
			throws PintException;

	FileDescriptor getDownloadLinkForReactomeAnalysisResult(String sessionID) throws PintException;

	Map<String, Set<ProteinProjection>> getProteinProjectionsByProteinACCFromProject(String projectTag)
			throws PintException;

	Map<String, Set<ProteinProjection>> getProteinProjectionsByProteinNameFromProject(String projectTag)
			throws PintException;

	Map<String, Set<ProteinProjection>> getProteinProjectionsByGeneNameFromProject(String projectTag)
			throws PintException;

	int getNumDifferentProteins(String projectTag, MSRunBean msRun) throws PintException;

	int getNumDifferentPeptides(String projectTag, MSRunBean msRun) throws PintException;

	int getNumPSMs(String projectTag, MSRunBean msRun) throws PintException;

	int getNumGenes(String projectTag, MSRunBean msRun) throws PintException;

	int getNumDifferentProteins(String projectTag, ExperimentalConditionBean condition) throws PintException;

	int getNumDifferentPeptides(String projectTag, ExperimentalConditionBean condition) throws PintException;

	int getNumPSMs(String projectTag, ExperimentalConditionBean condition) throws PintException;

	int getNumGenes(String projectTag, ExperimentalConditionBean condition) throws PintException;

	int getNumDifferentProteins(String projectTag, SampleBean sample) throws PintException;

	int getNumDifferentPeptides(String projectTag, SampleBean sample) throws PintException;

	int getNumPSMs(String projectTag, SampleBean sample) throws PintException;

	int getNumGenes(String projectTag, SampleBean sample) throws PintException;

	int getNumMSRuns(String projectTag, SampleBean sample) throws PintException;

	void cancelQuery(String sessionID) throws PintException;

	boolean isTestServer() throws PintException;
}
