package edu.scripps.yates.server;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Criteria;
import org.hibernate.transform.Transformers;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRemoteRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.util.UniprotEntryUtil;
import edu.scripps.yates.client.ProteinRetrievalService;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.Command;
import edu.scripps.yates.proteindb.queries.semantic.Infix2QueryBinaryTree;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.QueryBinaryTree;
import edu.scripps.yates.proteindb.queries.semantic.QueryInterface;
import edu.scripps.yates.proteindb.queries.semantic.QueryResult;
import edu.scripps.yates.server.adapters.AccessionBeanAdapter;
import edu.scripps.yates.server.adapters.AlignmentResultAdapter;
import edu.scripps.yates.server.adapters.AmountBeanAdapter;
import edu.scripps.yates.server.adapters.ConditionBeanAdapter;
import edu.scripps.yates.server.adapters.LabelBeanAdapter;
import edu.scripps.yates.server.adapters.MSRunBeanAdapter;
import edu.scripps.yates.server.adapters.OrganismBeanAdapter;
import edu.scripps.yates.server.adapters.PSMBeanAdapter;
import edu.scripps.yates.server.adapters.PSMRatioBeanAdapter;
import edu.scripps.yates.server.adapters.PTMBeanAdapter;
import edu.scripps.yates.server.adapters.PTMSiteBeanAdapter;
import edu.scripps.yates.server.adapters.PeptideBeanAdapterFromPeptideSet;
import edu.scripps.yates.server.adapters.PeptideRatioBeanAdapter;
import edu.scripps.yates.server.adapters.ProjectBeanAdapter;
import edu.scripps.yates.server.adapters.ProteinAnnotationBeanAdapter;
import edu.scripps.yates.server.adapters.ProteinBeanAdapterFromProteinSet;
import edu.scripps.yates.server.adapters.ProteinRatioBeanAdapter;
import edu.scripps.yates.server.adapters.SampleBeanAdapter;
import edu.scripps.yates.server.adapters.ThresholdBeanAdapter;
import edu.scripps.yates.server.adapters.TissueBeanAdapter;
import edu.scripps.yates.server.cache.ServerCacheGeneNameProteinProjectionsByProjectTag;
import edu.scripps.yates.server.cache.ServerCacheOrganismBeansByProjectName;
import edu.scripps.yates.server.cache.ServerCachePSMBeansByPSMDBId;
import edu.scripps.yates.server.cache.ServerCacheProjectBeanByProjectTag;
import edu.scripps.yates.server.cache.ServerCacheProteinACCProteinProjectionsByProjectTag;
import edu.scripps.yates.server.cache.ServerCacheProteinBeansByProjectTag;
import edu.scripps.yates.server.cache.ServerCacheProteinBeansByProteinDBId;
import edu.scripps.yates.server.cache.ServerCacheProteinBeansByQueryString;
import edu.scripps.yates.server.cache.ServerCacheProteinFileDescriptorByProjectName;
import edu.scripps.yates.server.cache.ServerCacheProteinGroupFileDescriptorByProjectName;
import edu.scripps.yates.server.cache.ServerCacheProteinNameProteinProjectionsByProjectTag;
import edu.scripps.yates.server.configuration.PintConfigurationPropertiesIO;
import edu.scripps.yates.server.export.DataExporter;
import edu.scripps.yates.server.lock.LockerByTag;
import edu.scripps.yates.server.projectStats.ProjectStatsManager;
import edu.scripps.yates.server.pseaquant.PSEAQuantSender;
import edu.scripps.yates.server.pseaquant.PSEAQuantSender.RATIO_AVERAGING;
import edu.scripps.yates.server.tasks.GetDownloadLinkFromProteinGroupsFromQueryTask;
import edu.scripps.yates.server.tasks.GetDownloadLinkFromProteinGroupsInProjectTask;
import edu.scripps.yates.server.tasks.GetDownloadLinkFromProteinsInProjectTask;
import edu.scripps.yates.server.tasks.GetDownloadLinkFromProteinsfromQueryTask;
import edu.scripps.yates.server.tasks.GetProteinsFromProjectTask;
import edu.scripps.yates.server.tasks.GetProteinsFromQuery;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.tasks.ServerTaskRegister;
import edu.scripps.yates.server.tasks.Task;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.HttpUtils;
import edu.scripps.yates.server.util.ServerConstants;
import edu.scripps.yates.server.util.ServerUtil;
import edu.scripps.yates.server.util.ServletCommonInit;
import edu.scripps.yates.shared.columns.comparator.PSMComparator;
import edu.scripps.yates.shared.columns.comparator.ProteinComparator;
import edu.scripps.yates.shared.columns.comparator.ProteinGroupComparator;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.AccessionBean;
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
import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism;
import edu.scripps.yates.shared.util.AlignmentResult;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.FileDescriptor;
import edu.scripps.yates.shared.util.ProgressStatus;
import edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList;
import edu.scripps.yates.shared.util.sublists.PsmBeanSubList;
import edu.scripps.yates.shared.util.sublists.QueryResultSubLists;
import edu.scripps.yates.utilities.alignment.nwalign.NWAlign;
import edu.scripps.yates.utilities.alignment.nwalign.NWResult;
import edu.scripps.yates.utilities.email.EmailSender;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.UniprotLineHeader;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.map.hash.THashMap;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ProteinRetrievalServicesServlet extends RemoteServiceServlet implements ProteinRetrievalService {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ProteinRetrievalServicesServlet.class);
	private final static SimpleDateFormat emailDateFormatter = new SimpleDateFormat("EEE, MMM d,''yy 'at' HH:mm:ss z");

	// private final MySQLMultiProjectProteinProvider mySQLProteinProvider =
	// MySQLMultiProjectProteinProvider
	// .getInstance();

	// private final FakeProteinProvider mySQLProteinProvider = new
	// FakeProteinProvider(
	// 100);

	private static final Map<String, File> proteinResultFilesByQueryStringInOrder = new THashMap<String, File>();
	private static final Map<String, File> proteinGroupResultFilesByQueryStringInOrder = new THashMap<String, File>();

	private static final int MIN_PEPTIDE_ALIGNMENT_LENGTH = 6;

	private int times = 0;

	private ServletContext servletContext;

	// @Override
	// public String greetServer(String input) throws IllegalArgumentException {
	// // Verify that the input is valid.
	// if (!FieldVerifier.isValidName(input)) {
	// // If the input is not valid, throw an IllegalArgumentException back
	// // to
	// // the client.
	// throw new IllegalArgumentException(
	// "Name must be at least 4 characters long");
	// }
	//
	// String serverInfo = getServletContext().getServerInfo();
	// String userAgent = getThreadLocalRequest().getHeader("User-Agent");
	//
	// // Escape data from the client to avoid cross-site script
	// // vulnerabilities.
	// input = escapeHtml(input);
	// userAgent = escapeHtml(userAgent);
	//
	// return "Hello, " + input + "!<br><br>I am running " + serverInfo
	// + ".<br><br>It looks like you are using:<br>" + userAgent;
	// }

	/**
	 * Overrided in order to run the method implemented on ServletCommonInit
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		log.info("Init from ProteinRetrievalServicesServlet");
		ServletCommonInit.init(config.getServletContext());
	}

	private boolean getPSMCentric() {
		Boolean ret = ServerUtil.getPINTProperties(getServletContext()).getPsmCentric();
		if (ret == null) {
			ret = ServerConstants.psmCentricByDefault;
		}
		return ret;
	}
	// @Override
	// public ProteinBean[] getProteinsFromProject(String selectedProject,
	// int maxNumProteins) {
	// int numproteins = 0;
	// ProteinBean[] ret = new ProteinBean[maxNumProteins];
	// if (selectedProject.equals(projectName)) {
	// final Map<Accession, Protein> proteinMap = mySQLProteinProvider
	// .getProteinMap();
	// for (Protein protein : proteinMap.values()) {
	// // build the proteinBean
	// ProteinBean proteinBean = getProteinBeanFromProtein(protein);
	// ret[numproteins++] = proteinBean;
	// if (numproteins == maxNumProteins)
	// break;
	//
	// }
	// }
	// return ret;
	// }

	// @Override
	// public ProteinBean[] getProteinsFromProject(String selectedProject,
	// int maxNumProteins) {
	// int numproteins = 0;
	// ProteinBean[] ret = new ProteinBean[maxNumProteins];
	// if (selectedProject.equals(projectName)) {
	// final Map<Accession, Protein> proteinMap = mySQLProteinProvider
	// .getProteinMap();
	// for (Protein protein : proteinMap.values()) {
	// // build the proteinBean
	// ProteinBean proteinBean = getProteinBeanFromProtein(protein);
	// ret[numproteins++] = proteinBean;
	// if (numproteins == maxNumProteins)
	// break;
	//
	// }
	// }
	// return ret;
	// }

	@Override
	public Set<String> getProjectTags() throws PintException {
		logMethodCall(new Object() {
		}.getClass().getEnclosingMethod());
		try {
			final Set<String> projectTagSet = new HashSet<String>();

			final List<edu.scripps.yates.proteindb.persistence.mysql.Project> retrieveList = ContextualSessionHandler
					.retrieveList(edu.scripps.yates.proteindb.persistence.mysql.Project.class);
			for (final edu.scripps.yates.proteindb.persistence.mysql.Project project : retrieveList) {
				if (project.isHidden())
					continue;
				final ProjectBean projectBean = new ProjectBeanAdapter(project, false).adapt();
				ServerCacheProjectBeanByProjectTag.getInstance().addtoCache(projectBean, projectBean.getTag());
				projectTagSet.add(project.getTag());
			}

			return projectTagSet;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public QueryResultSubLists getProteinsFromProjects(String sessionID, Set<String> projectTags, String uniprotVersion,
			boolean separateNonConclusiveProteins, Integer defaultQueryIndex, boolean testMode) throws PintException {

		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		final String projectTagsString = getProjectTagString(projectTags);
		log.info("GET PROTEINS FROM PROJECT '" + projectTagsString + "' IN SESSION: " + sessionID);
		log.info(++times + " times getting proteins from project ");
		GetProteinsFromProjectTask task = null;
		try {
			LockerByTag.lock(projectTags, enclosingMethod);
			// create a Task
			task = new GetProteinsFromProjectTask(projectTagsString, uniprotVersion);

			// register task
			ServerTaskRegister.getInstance().registerTask(task);

			DataSetsManager.clearDataSet(sessionID);
			// set current thread as the one holding this dataset
			DataSetsManager.getDataSet(sessionID, projectTagsString, getPSMCentric())
					.setActiveDatasetThread(Thread.currentThread());

			if (ServerCacheProteinBeansByProjectTag.getInstance().contains(projectTagsString)
					&& defaultQueryIndex == null) {
				log.info("Getting proteinBeans from cache for project(s): '" + projectTagsString + "' in session '"
						+ sessionID + "'");
				final List<ProteinBean> proteinBeansInCache = ServerCacheProteinBeansByProjectTag.getInstance()
						.getFromCache(projectTagsString);
				if (proteinBeansInCache != null) {
					log.info(proteinBeansInCache.size() + " protein beans retrieved from cache");
					if (!proteinBeansInCache.isEmpty()) {
						// add to the dataset
						log.info("Adding them to the dataset in session '" + sessionID + "'");
						for (final ProteinBean proteinBean : proteinBeansInCache) {
							DataSetsManager.getDataSet(sessionID, projectTagsString, getPSMCentric())
									.addProtein(proteinBean);
						}
						log.info("Setting dataset ready in session '" + sessionID + "'");
						DataSetsManager.getDataSet(sessionID, projectTagsString, getPSMCentric()).setReady(true);
						log.info(DataSetsManager.getDataSet(sessionID, projectTagsString, getPSMCentric()));
						final QueryResultSubLists ret = getQueryResultSubListsFromDataSet(sessionID, projectTags,
								separateNonConclusiveProteins);
						return ret;
					}
				}
			}

			task.setTaskDescription("Loading proteins from  project(s) " + projectTagsString + "''...");

			final StringBuilder querySB = new StringBuilder();
			for (final String projectTag : projectTags) {
				try {
					final String query = getDefaultQueryAssocciatedByProject(projectTag, defaultQueryIndex);

					if (query != null && !"".equals(query)) {

						if (!"".equals(querySB.toString())) {
							querySB.insert(0, "(").append(") OR ");
						}
						querySB.append(query);
					} else {
						if (!"".equals(querySB.toString())) {
							querySB.insert(0, "(").append(") OR");
						}
						querySB.append("COND[,").append(projectTag).append("]");
					}
				} catch (final Exception e) {
					final Task runningTask = ServerTaskRegister.getInstance().getRunningTask(
							SharedTaskKeyGenerator.getKeyForGetProteinsFromProjectTask(projectTag, uniprotVersion));
					if (runningTask != null)
						runningTask.setAsFinished();
					e.printStackTrace();
					throw e;
				}
			}
			final QueryResultSubLists proteinsFromQuery = getProteinsFromQuery(sessionID, querySB.toString(),
					projectTags, separateNonConclusiveProteins, false, testMode);

			if (defaultQueryIndex == null && !DataSetsManager.getDataSet(sessionID, projectTagsString, getPSMCentric())
					.getProteins().isEmpty()) {
				// add to cache by project tags
				log.info("Adding protein beans to cache by project tag '" + projectTagsString + "' in session '"
						+ sessionID + "'");
				ServerCacheProteinBeansByProjectTag.getInstance().addtoCache(
						DataSetsManager.getDataSet(sessionID, projectTagsString, getPSMCentric()).getProteins(),
						projectTagsString);
			}

			return proteinsFromQuery;

		} catch (final Exception e) {
			log.error(e);
			e.printStackTrace();
			if (e instanceof PintException) {
				throw (PintException) e;
			}
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			LockerByTag.unlock(projectTags, enclosingMethod);
			if (task != null) {
				ServerTaskRegister.getInstance().endTask(task);
			}
		}

	}

	private String getProjectTagString(Set<String> projectTags) {
		final List<String> list = new ArrayList<String>();
		list.addAll(projectTags);
		Collections.sort(list);

		final StringBuilder sb = new StringBuilder();
		for (final String string : list) {
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
			sb.append(string);
		}
		return sb.toString();
	}

	/**
	 *
	 * @param sessionID
	 * @param projectTag
	 * @param numQuery
	 *            if null, the first query string is loaded (if available)
	 * @return
	 * @throws PintException
	 */
	private String getDefaultQueryAssocciatedByProject(String projectTag, Integer numQuery) throws PintException {
		try {
			final DefaultView defaultView = getDefaultViewByProject(projectTag);
			String defaultQueryString = null;
			if (numQuery != null && !defaultView.getProjectNamedQueries().isEmpty()
					&& defaultView.getProjectNamedQueries().size() > numQuery) {
				defaultQueryString = defaultView.getProjectNamedQueries().get(numQuery).getQuery();
			} else if (!defaultView.getProjectNamedQueries().isEmpty()) {
				defaultQueryString = defaultView.getProjectNamedQueries().get(0).getQuery();
			}
			if (defaultQueryString != null && !"".equals(defaultQueryString)) {
				// validate query
				try {
					final Infix2QueryBinaryTree tree = new Infix2QueryBinaryTree();
					// call to convertExpression to see whether it is malformed
					// or not. If yes, a MalformedQueryException is catched
					tree.convertExpresion(defaultQueryString);
					log.info("Default query string '" + defaultQueryString + "' detected and validated for project '"
							+ projectTag + "'");
					return defaultQueryString;
				} catch (final MalformedQueryException e) {
					e.printStackTrace();
					log.error("Query string for project '" + projectTag + "' was malformed: '" + defaultQueryString
							+ "'");
				}
			}

			return null;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public Set<String> getExperimentalConditionsFromProject(String projectTag) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			final Set<Condition> conditions = RemoteServicesTasks.getExperimentalConditionsFromProject(projectTag);
			final Set<String> names = new HashSet<String>();
			for (final Condition condition : conditions) {
				names.add(condition.getName());
			}
			return names;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public FileDescriptor getDownloadLinkForProteinsFromQuery(String sessionID, String queryText,
			Set<String> projectTags) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		final String projectTagCollectionKey = SharedDataUtils.getProjectTagCollectionKey(projectTags);

		// create a Task
		final GetDownloadLinkFromProteinsfromQueryTask task = new GetDownloadLinkFromProteinsfromQueryTask(
				projectTagCollectionKey);
		if (ServerTaskRegister.getInstance().isRunningTask(task)) {
			log.info("Task " + task.getKey() + " is already running. Discarding this request.");
			return null;
		}
		try {
			// register tag
			ServerTaskRegister.getInstance().registerTask(task);
			try {
				// lock project

				LockerByTag.lock(projectTags, enclosingMethod);

				// get the queryInOrder String
				final Infix2QueryBinaryTree tree = new Infix2QueryBinaryTree();
				QueryBinaryTree expressionTree;
				String queryInOrder = null;
				try {
					expressionTree = tree.convertExpresion(queryText);

					// look into server cache using the expression
					queryInOrder = expressionTree.printInOrder();
				} catch (final MalformedQueryException e1) {
					e1.printStackTrace();
					throw new IllegalArgumentException(e1.getMessage());
				}

				File file = null;
				if (proteinResultFilesByQueryStringInOrder.containsKey(queryInOrder + projectTagCollectionKey)) {
					file = proteinResultFilesByQueryStringInOrder.get(queryInOrder + projectTagCollectionKey);
				} else {
					final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
					if (!dataSet.isEmpty() && dataSet.isReady()) {
						log.info("Getting download link from query: " + queryText + " in projects "
								+ projectTagCollectionKey + " dataset '" + dataSet.getName() + "' in SessionID: "
								+ sessionID);

						final List<ProteinBean> proteinsFromQuery = dataSet.getProteins();
						file = DataExporter.exportProteins(proteinsFromQuery, queryText, getPSMCentric());

						proteinResultFilesByQueryStringInOrder.put(queryInOrder + projectTagCollectionKey, file);

					}
				}
				if (file != null && file.exists()) {
					// prepare the return
					final FileDescriptor ret = new FileDescriptor(FilenameUtils.getName(file.getAbsolutePath()),
							FileManager.getFileSizeString(file));
					return ret;
				}
				return null;
			} finally {
				LockerByTag.unlock(projectTags, enclosingMethod);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			ServerTaskRegister.getInstance().endTask(task);

		}
	}

	@Override
	public FileDescriptor getDownloadLinkForProteinGroupsFromQuery(String sessionID, String queryText,
			Set<String> projectTags, boolean separateNonConclusiveProteins) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		final String projectTagCollectionKey = SharedDataUtils.getProjectTagCollectionKey(projectTags);
		// create a Task
		final GetDownloadLinkFromProteinGroupsFromQueryTask task = new GetDownloadLinkFromProteinGroupsFromQueryTask(
				projectTagCollectionKey, separateNonConclusiveProteins);
		if (ServerTaskRegister.getInstance().isRunningTask(task)) {
			log.info("Task " + task.getKey() + " is already running. Discarding this request.");
			return null;
		}
		try {
			// register task
			ServerTaskRegister.getInstance().registerTask(task);
			try {
				// lock projects

				LockerByTag.lock(projectTags, enclosingMethod);

				// get the queryInOrder String
				final Infix2QueryBinaryTree tree = new Infix2QueryBinaryTree();
				QueryBinaryTree expressionTree;
				String queryInOrder = null;
				try {
					expressionTree = tree.convertExpresion(queryText);

					// look into server cache using the expression
					queryInOrder = expressionTree.printInOrder();
				} catch (final MalformedQueryException e1) {
					e1.printStackTrace();
					throw new IllegalArgumentException(e1.getMessage());
				}

				File file = null;
				if (proteinGroupResultFilesByQueryStringInOrder.containsKey(queryInOrder + projectTagCollectionKey)) {
					file = proteinGroupResultFilesByQueryStringInOrder.get(queryInOrder + projectTagCollectionKey);
				} else {
					final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
					if (!dataSet.isEmpty() && dataSet.isReady()) {
						log.info("Getting download link from query: " + queryText + " in projects "
								+ projectTagCollectionKey + " dataset '" + dataSet.getName() + " SessionID: "
								+ sessionID);

						final List<ProteinBean> proteinsFromQuery = dataSet.getProteins();
						file = DataExporter.exportProteinGroups(proteinsFromQuery, separateNonConclusiveProteins,
								queryText, getPSMCentric());

						proteinGroupResultFilesByQueryStringInOrder.put(queryInOrder + projectTagCollectionKey, file);

					}
				}
				if (file != null && file.exists()) {
					// prepare the return
					final FileDescriptor ret = new FileDescriptor(FilenameUtils.getName(file.getAbsolutePath()),
							FileManager.getFileSizeString(file));
					return ret;
				}
				return null;
			} finally {
				LockerByTag.unlock(projectTags, enclosingMethod);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {

			ServerTaskRegister.getInstance().endTask(task);
		}
	}

	@Override
	public FileDescriptor getDownloadLinkForProteinsInProject(List<String> projectTags) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		// check if download is allowed for these project
		for (final String projectTag : projectTags) {
			if (!PreparedQueries.isDownloadDataAllowed(projectTag))
				return null;
		}
		final String projectTagCollectionKey = SharedDataUtils.getProjectTagCollectionKey(projectTags);

		// create a Task
		final GetDownloadLinkFromProteinsInProjectTask task = new GetDownloadLinkFromProteinsInProjectTask(
				projectTagCollectionKey);
		if (ServerTaskRegister.getInstance().isRunningTask(task)) {
			log.info("Task " + task.getKey() + " is already running. Discarding this request.");
			return null;
		}
		try {
			// register task
			ServerTaskRegister.getInstance().registerTask(task);

			log.info("Getting download link for proteins in project: " + projectTagCollectionKey);
			if (ServerCacheProteinFileDescriptorByProjectName.getInstance().contains(projectTagCollectionKey)) {
				log.info("link found in cache");
				return ServerCacheProteinFileDescriptorByProjectName.getInstance()
						.getFromCache(projectTagCollectionKey);
			}

			File file = DataExporter.existsFileProteinsFromProjects(projectTags);
			if (file != null) {
				final FileDescriptor ret = new FileDescriptor(FilenameUtils.getName(file.getAbsolutePath()),
						FileManager.getFileSizeString(file));
				ServerCacheProteinFileDescriptorByProjectName.getInstance().addtoCache(ret, projectTagCollectionKey);
				log.info("File descriptor for proteins is already created: " + ret.getName() + " - " + ret.getSize());
				return ret;
			}
			try {
				// lock project
				LockerByTag.lock(projectTags, enclosingMethod);
				// create a new one
				final String omimAPIKey = ServerUtil.getPINTProperties(getServletContext()).getOmimKey();
				file = DataExporter.exportProteinsFromProjects(projectTags, omimAPIKey, getPSMCentric());
				log.info("file created in the server at: " + file.getAbsolutePath());

				log.info("creating file descriptor");
				// prepare the return
				final FileDescriptor ret = new FileDescriptor(FilenameUtils.getName(file.getAbsolutePath()),
						FileManager.getFileSizeString(file));
				ServerCacheProteinFileDescriptorByProjectName.getInstance().addtoCache(ret, projectTagCollectionKey);
				log.info("File descriptor created: " + ret.getName() + " - " + ret.getSize());
				return ret;
			} finally {
				LockerByTag.unlock(projectTags, enclosingMethod);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			ServerTaskRegister.getInstance().endTask(task);
		}
	}

	@Override
	public FileDescriptor getDownloadLinkForProteinGroupsInProject(List<String> projectTags,
			boolean separateNonConclusiveProteins) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		// check if download is allowed for these project
		for (final String projectTag : projectTags) {
			if (!PreparedQueries.isDownloadDataAllowed(projectTag))
				return null;
		}
		final String projectTagCollectionKey = SharedDataUtils.getProjectTagCollectionKey(projectTags);

		// create a Task
		final GetDownloadLinkFromProteinGroupsInProjectTask task = new GetDownloadLinkFromProteinGroupsInProjectTask(
				projectTagCollectionKey, separateNonConclusiveProteins);
		if (ServerTaskRegister.getInstance().isRunningTask(task)) {
			log.info("Task " + task.getKey() + " is already running. Discarding this request.");
			return null;
		}
		try {
			// register task
			ServerTaskRegister.getInstance().registerTask(task);

			log.info("Getting download link for proteins groups in project: " + projectTagCollectionKey);

			if (ServerCacheProteinGroupFileDescriptorByProjectName.getInstance().contains(projectTagCollectionKey)) {
				log.info("link found in cache");
				return ServerCacheProteinGroupFileDescriptorByProjectName.getInstance()
						.getFromCache(projectTagCollectionKey);
			}

			File file = DataExporter.existsFileProteinGroupsFromProjects(projectTags, separateNonConclusiveProteins);
			if (file != null) {
				final FileDescriptor ret = new FileDescriptor(FilenameUtils.getName(file.getAbsolutePath()),
						FileManager.getFileSizeString(file));
				ServerCacheProteinGroupFileDescriptorByProjectName.getInstance().addtoCache(ret,
						projectTagCollectionKey);
				log.info("File descriptor for protein groups is already created: " + ret.getName() + " - "
						+ ret.getSize());
				return ret;
			}
			try {
				// lock projects
				LockerByTag.lock(projectTags, enclosingMethod);
				// create a new one
				final String omimAPIKey = ServerUtil.getPINTProperties(getServletContext()).getOmimKey();
				file = DataExporter.exportProteinGroupsFromProjects(projectTags, separateNonConclusiveProteins,
						omimAPIKey, getPSMCentric());
				log.info("file created in the server at: " + file.getAbsolutePath());

				log.info("creating file descriptor");
				// prepare the return
				final FileDescriptor ret = new FileDescriptor(FilenameUtils.getName(file.getAbsolutePath()),
						FileManager.getFileSizeString(file));
				ServerCacheProteinGroupFileDescriptorByProjectName.getInstance().addtoCache(ret,
						projectTagCollectionKey);
				log.info("File descriptor created: " + ret.getName() + " - " + ret.getSize());
				return ret;
			} finally {
				LockerByTag.unlock(projectTags, enclosingMethod);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			ServerTaskRegister.getInstance().endTask(task);
		}
	}

	@Override
	public Set<String> getUniprotAnnotationsFromProjects(Set<String> projectTags) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			final Set<String> ret = new HashSet<String>();

			final String latestVersion = UniprotProteinRemoteRetriever.getCurrentUniprotRemoteVersion();
			if (latestVersion != null) {
				ret.add(latestVersion);
			}
			final Map<String, Date> uploadDatesByProjectTags = RemoteServicesTasks
					.getUploadDatesFromProjects(projectTags);
			final UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
					UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
					UniprotProteinRetrievalSettings.getInstance().isUseIndex(), true, true);
			upr.setCacheEnabled(false);
			final Set<String> uniprotversions = upr.getUniprotVersionsForProjects(uploadDatesByProjectTags);
			ret.addAll(uniprotversions);
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public List<String> getAnnotationTypes() throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			final List<String> ret = new ArrayList<String>();

			final AnnotationType[] annotationTypes = AnnotationType.values();

			for (final AnnotationType annotationType : annotationTypes) {
				ret.add(annotationType.getKey());
			}

			java.util.Collections.sort(ret, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					if (o1.startsWith("\""))
						o1 = o1.substring(1);
					if (o2.startsWith("\""))
						o2 = o2.substring(1);
					return o1.compareToIgnoreCase(o2);
				}

			});
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public Map<String, String> getUniprotHeaderLines() throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			final UniprotLineHeader[] values = UniprotLineHeader.values();
			final Map<String, String> ret = new HashMap<String, String>();
			for (final UniprotLineHeader uniprotLineHeader : values) {
				ret.put(uniprotLineHeader.name(), uniprotLineHeader.getDescription());
			}
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public Map<String, String> getCommands() throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			final Map<String, String> ret = new HashMap<String, String>();
			final Command[] values = Command.values();
			for (final Command command : values) {
				ret.put(command.name() + " (" + command.getAbbreviation() + ")", command.getFormat());
			}
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public Set<String> getThresholdNamesFromProjects(Set<String> projectTags) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			final Set<String> ret = new HashSet<String>();
			for (final String projectName : projectTags) {
				ret.addAll(PreparedQueries.getProteinThresholdNamesByProject(projectName));
			}
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public List<RatioDescriptorBean> getRatioDescriptorsFromProjects(Set<String> projectNames) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			return RemoteServicesTasks.getRatioDescriptorsFromProjects(projectNames);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public ProteinGroupBeanSubList groupProteins(String sessionID, boolean separateNonConclusiveProteins, int pageSize)
			throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			return DataSetsManager.getDataSet(sessionID, null, getPSMCentric())
					.getLightProteinGroupBeanSubList(separateNonConclusiveProteins, 0, pageSize);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}

	}

	@Override
	public QueryResultSubLists getProteinsFromQuery(String sessionID, String queryText, Set<String> projectTags,
			boolean separateNonConclusiveProteins, boolean lock, boolean testMode) throws PintException {
		GetProteinsFromQuery task = null;
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		try {

			logMethodCall(enclosingMethod);
			if (lock) {
				LockerByTag.lock(projectTags, enclosingMethod);
			}
			final QueryInterface expressionTree = new QueryInterface(projectTags, queryText, testMode, getPSMCentric());
			final String queryInOrder = expressionTree.printInOrder();
			// create task
			task = new GetProteinsFromQuery(projectTags, queryInOrder);
			// register task
			ServerTaskRegister.getInstance().registerTask(task);
			// clear map of current proteins for this sessionID
			DataSetsManager.clearDataSet(sessionID);

			// set current thread as the one holding this dataset
			DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric())
					.setActiveDatasetThread(Thread.currentThread());

			// look into cache
			// attach the project Tags to the queryInOrder, otherwise, if
			// someone execute one query in one project, and then another one in
			// other project, it would return the results of the first one.
			final String projectTagsString = getProjectTagString(projectTags);
			final String key = projectTagsString + ": " + queryInOrder;
			if (ServerCacheProteinBeansByQueryString.getInstance().contains(key)) {
				log.info("Getting proteinBeans from cache for query: " + key + " in session '" + sessionID + "'");
				final List<ProteinBean> ret = ServerCacheProteinBeansByQueryString.getInstance().getFromCache(key);
				if (!ret.isEmpty()) {
					// SEND TRACKING EMAIL. Testing it...
					sendTrackingEmail(getProjectTagString(projectTags) + "\t" + queryInOrder,
							"Triggered by query: " + queryText, "Getting dataset from server cache");
					// SEND TRACKING EMAIL
					// add to the dataset
					for (final ProteinBean proteinBean : ret) {
						DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric()).addProtein(proteinBean);
					}
					// set datasetReady
					DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric()).setReady(true);
					log.info(DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric()));
					return getQueryResultSubListsFromDataSet(sessionID, projectTags, separateNonConclusiveProteins);
				}
			}

			try {

				task.setTaskDescription("Performing query...");
				task.setMaxSteps(2);
				task.setCurrentStep(1);

				// clear static data by DB identifiers
				log.info("Clearing static beans objects by DB Identifiers");
				clearThreadLocalStaticMaps();

				// just clear this cache if the query is not about conditions
				if (!expressionTree.isProteinLevelQuery()) {
					ServerCacheProteinBeansByProteinDBId.getInstance().clearCache();
					ServerCachePSMBeansByPSMDBId.getInstance().clearCache();
				}
				// end clearing cache

				final QueryResult result = getQueryResultFromQuery(expressionTree, projectTags, testMode);

				final Map<String, QueriableProteinSet> proteins = result.getProteins();
				log.info(proteins.size() + " proteins comming from command  '" + queryText + "'");
				if (!proteins.isEmpty()) {

					// adapt experimental conditions first so that the
					// proteintocondition mapper is ready
					for (final String projectTag : projectTags) {
						final List<Condition> conditions = PreparedCriteria
								.getCriteriaForConditionsInProject(projectTag);
						for (final Condition condition : conditions) {
							new ConditionBeanAdapter(condition, true).adapt();
						}
					}

					log.info("Getting UniprotKB annotations in session '" + sessionID + "'");
					task.setTaskDescription("Retrieving UniprotKB annotations in session '" + sessionID + "'");
					task.incrementProgress(1);
					log.info(proteins.size() + " protein before annotate");
					// I do not save the annotated proteins because they will be
					// accessed from the queriableProteinSets
					RemoteServicesTasks.annotateProteinsWithUniprot(proteins.keySet(), null,
							getProjectTagString(projectTags));
					log.info(proteins.size() + " protein after annotate");

					log.info("Creating Protein beans in session '" + sessionID + "'");
					// RemoteServicesTasks.createProteinBeansFromQueriableProteins(sessionID,
					// proteins,
					// getHiddenPTMs(projectTags),
					// getProjectTagString(projectTags));
					RemoteServicesTasks.createProteinBeansFromQueriableProteins(sessionID, proteins,
							getHiddenPTMs(projectTags), getProjectTagString(projectTags),
							expressionTree.isProteinLevelQuery(), getPSMCentric());
					// proteins have to be before creating peptides, because
					// when peptides are created, the cache of the proteins is
					// used.
					// log.info("Creating Peptide beans in session '" +
					// sessionID + "'");
					// RemoteServicesTasks.createPeptideBeansFromPeptideMap(sessionID,
					// result.getPeptides(),
					// expressionTree.isProteinLevelQuery(), getPSMCentric(),
					// getHiddenPTMs(projectTags));
					log.info("Setting dataset ready in session '" + sessionID + "'");
					DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric()).setReady(true);

					log.info("Getting Uniprot annotations in session '" + sessionID + "'");
					RemoteServicesTasks.annotateProteinBeansWithUniprot(
							DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric()).getProteins(), null,
							getHiddenPTMs(projectTags));

					log.info("Getting OMIM annotations in session '" + sessionID + "'");
					task.setTaskDescription("Retrieving OMIM annotations...");
					task.incrementProgress(1);

					RemoteServicesTasks.annotateProteinsOMIM(
							DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric()).getProteins(),
							PintConfigurationPropertiesIO
									.readProperties(FileManager.getPINTPropertiesFile(getServletContext()))
									.getOmimKey());
				}
				// add to cache by query if not empty
				if (!DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric()).getProteins().isEmpty()) {
					ServerCacheProteinBeansByQueryString.getInstance().addtoCache(
							DataSetsManager.getDataSet(sessionID, queryInOrder, getPSMCentric()).getProteins(), key);
				}

				final QueryResultSubLists ret = getQueryResultSubListsFromDataSet(sessionID, projectTags,
						separateNonConclusiveProteins);

				return ret;

			} finally {
				// clear static data by DB identifiers
				clearThreadLocalStaticMaps();
				ServerCacheProteinBeansByProteinDBId.getInstance().clearCache();
				ServerCachePSMBeansByPSMDBId.getInstance().clearCache();
				// end clearing cache

			}
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException) {
				throw (PintException) e;
			}

			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			// release task
			if (task != null) {
				ServerTaskRegister.getInstance().endTask(task);
			}
			log.warn("Rolling back transaction for not making any change in DB");
			ContextualSessionHandler.rollbackTransaction();
			log.warn("Transaction rolled back");
			if (lock) {
				LockerByTag.unlock(projectTags, enclosingMethod);
			}

		}
	}

	private void clearThreadLocalStaticMaps() {
		log.info("Clearing static beans objects by DB Identifiers");

		AccessionBeanAdapter.clearStaticMap();
		AmountBeanAdapter.clearStaticMap();
		ConditionBeanAdapter.clearStaticMap();
		LabelBeanAdapter.clearStaticMap();
		MSRunBeanAdapter.clearStaticMap();
		OrganismBeanAdapter.clearStaticMap();
		PeptideRatioBeanAdapter.clearStaticMap();
		ProjectBeanAdapter.clearStaticMap();
		ProteinAnnotationBeanAdapter.clearStaticMap();
		ProteinRatioBeanAdapter.clearStaticMap();
		PSMBeanAdapter.clearStaticMap();
		PSMRatioBeanAdapter.clearStaticMap();
		PTMBeanAdapter.clearStaticMap();
		PTMSiteBeanAdapter.clearStaticMap();
		SampleBeanAdapter.clearStaticMap();
		ThresholdBeanAdapter.clearStaticMap();
		TissueBeanAdapter.clearStaticMap();
		PeptideBeanAdapterFromPeptideSet.clearStaticMap();
		ProteinBeanAdapterFromProteinSet.clearStaticMap();
		log.info("Static beans objects by DB Identifiers cleared");

	}

	public QueryResult getQueryResultFromQuery(QueryInterface expressionTree, Set<String> projectTags, boolean testMode)
			throws PintException {
		try {

			// look into server cache using the expression
			final String queryInOrder = expressionTree.printInOrder();
			// SEND TRACKING EMAIL.
			sendTrackingEmail(getProjectTagString(projectTags) + "\t" + queryInOrder,
					"Triggered by query: " + queryInOrder);
			// SEND TRACKING EMAIL

			final QueryResult result = expressionTree.getQueryResults();
			// QueryResult result = expressionTree.getQueryResultsParallel();
			return result;

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {

		}
	}

	private QueryResultSubLists getQueryResultSubListsFromDataSet(String sessionID, Set<String> projectTags,
			boolean separateNonConclusiveProteins) throws PintException {
		int psmsPageSize = 100;
		int proteinsPageSize = 50;
		int proteinGroupsPageSize = 50;
		int peptidesPageSize = 50;
		final String projectTagString = getProjectTagString(projectTags);
		final DefaultView defaultViewByProject = getDefaultViewByProject(projectTags.iterator().next());
		psmsPageSize = defaultViewByProject.getPsmPageSize();
		proteinsPageSize = defaultViewByProject.getProteinPageSize();
		proteinGroupsPageSize = defaultViewByProject.getProteinGroupPageSize();
		peptidesPageSize = defaultViewByProject.getPeptidePageSize();

		log.info("Checking if there is more than one protein in the same dataset with the same primary accession");
		final DataSet dataSet = DataSetsManager.getDataSet(sessionID, projectTagString, getPSMCentric());
		dataSet.fixPrimaryAccessionDuplicates();

		log.info("Getting query result sub lists from dataset:");
		log.info(dataSet);
		log.info("Sorting dataset according to default view of the project in session '" + sessionID + "'");

		final ProteinGroupComparator proteinGroupComparator = new ProteinGroupComparator(
				defaultViewByProject.getProteinGroupsSortedBy());
		final ProteinComparator proteinComparator = new ProteinComparator(defaultViewByProject.getProteinsSortedBy());
		final PSMComparator psmComparator = new PSMComparator(defaultViewByProject.getPsmsSortedBy());

		log.info("Sorting dataset according to default view of the project: protein groups by "
				+ defaultViewByProject.getProteinGroupsSortedBy().getName() + ", proteins sorted by "
				+ defaultViewByProject.getProteinsSortedBy().getName() + " and PSMs sorted by "
				+ defaultViewByProject.getPsmsSortedBy().getName());

		// sort by default views
		dataSet.sortProteinGroups(proteinGroupComparator, separateNonConclusiveProteins);
		dataSet.sortProteins(proteinComparator);
		dataSet.sortPsms(psmComparator);
		log.info("Data set sorted");

		// get sublists
		log.info("Getting sublists of the data");
		final ProteinBeanSubList proteinBeanSubList = dataSet.getLightProteinBeanSubList(0, proteinsPageSize);
		final PsmBeanSubList psmBeanSubList = dataSet.getLightPsmBeanSubList(0, psmsPageSize);
		final ProteinGroupBeanSubList proteinGroupBeanSubList = dataSet
				.getLightProteinGroupBeanSubList(separateNonConclusiveProteins, 0, proteinGroupsPageSize);
		final PeptideBeanSubList peptideSubList = dataSet.getLightPeptideBeanSubList(0, peptidesPageSize);
		final int numDifferentSequencesDistinguishingModifieds = dataSet.getNumDifferentSequences(true);
		final int numDifferentSequences = dataSet.getNumDifferentSequences(false);
		final QueryResultSubLists ret = new QueryResultSubLists(dataSet.getProteinScores(), dataSet.getPeptideScores(),
				dataSet.getPsmScores(), dataSet.getPtmScores(), dataSet.getScoreTypes());
		ret.setProteinGroupSubList(proteinGroupBeanSubList);
		ret.setProteinSubList(proteinBeanSubList);
		ret.setPsmSubList(psmBeanSubList);
		ret.setPeptideSubList(peptideSubList);
		ret.setNumDifferentSequences(numDifferentSequences);
		ret.setNumDifferentSequencesDistinguishingModifieds(numDifferentSequencesDistinguishingModifieds);
		log.info("Sublists data ready to be returned");
		return ret;
	}

	@Override
	public Set<ProjectBean> getProjectBeans() throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			return RemoteServicesTasks.getProjectBeans();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public ProjectBean getProjectBean(String projectTag) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			if (ServerCacheProjectBeanByProjectTag.getInstance().contains(projectTag)) {
				return ServerCacheProjectBeanByProjectTag.getInstance().getFromCache(projectTag);
			} else {
				final Set<ProjectBean> projectBeans = getProjectBeans();
				for (final ProjectBean projectBean : projectBeans) {
					if (projectBean.getTag().equals(projectTag)) {
						return projectBean;
					}
				}
			}
			throw new PintException("Project '" + projectTag + "' is not found in the system.",
					PINT_ERROR_TYPE.PROJECT_NOT_FOUND);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumExperiments() throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			return ProjectStatsManager.getInstance(enclosingMethod).getNumProjects();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumDifferentProteins() throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumDifferentProteins();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumDifferentPeptides() throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumDifferentPeptides();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumPSMs() throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumPSMs();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumConditions() throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumConditions();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public Set<OrganismBean> getOrganismsByProject(String projectTag) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			if (ServerCacheOrganismBeansByProjectName.getInstance().contains(projectTag)) {
				return ServerCacheOrganismBeansByProjectName.getInstance().getFromCache(projectTag);
			} else {
				final Set<Organism> organisms = PreparedQueries.getOrganismsByProject(projectTag);
				final Set<OrganismBean> ret = new HashSet<OrganismBean>();
				for (final Organism organism : organisms) {
					if (organism.getName().equals(ModelUtils.ORGANISM_CONTAMINANT.getName())) {
						continue;
					}
					ret.add(new OrganismBeanAdapter(organism).adapt());
				}
				return ret;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public Set<String> getHiddenPTMs(String sessionID, String projectTag) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		return RemoteServicesTasks.getHiddenPTMs(projectTag);
	}

	private Set<String> getHiddenPTMs(Collection<String> projectTags) throws PintException {
		return RemoteServicesTasks.getHiddenPTMs(projectTags);
	}

	@Override
	public DefaultView getDefaultViewByProject(String projectTag) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		return RemoteServicesTasks.getDefaultViewByProject(projectTag);
	}

	public void sendTrackingEmail(String projectTag, String... annotations) {
		try {
			if (SharedConstants.EMAIL_ENABLED) {

				log.info("Trying to send the tracking email");
				String remoteHost = "";
				final StringBuilder text = new StringBuilder();
				if (getThreadLocalRequest() != null) {
					remoteHost = HttpUtils.remoteAddr(getThreadLocalRequest());
					text.append("Someone from IP " + remoteHost + " has loaded the project '" + projectTag + "'\n\n");
					text.append("Remote address: " + getThreadLocalRequest().getRemoteAddr() + "\n");
					text.append("Remote host: '" + remoteHost + "'\n");
					text.append("Remote port: '" + getThreadLocalRequest().getRemotePort() + "'\n");
					text.append("Remote user: '" + getThreadLocalRequest().getRemoteUser() + "'\n");
					text.append("Timestamp: '" + emailDateFormatter.format(new Date()) + "'\n");
				}
				if (annotations != null && annotations.length > 0) {
					text.append("\n---\n");
					for (final String annotation : annotations) {
						text.append(annotation + "\n");
					}
					text.append("---\n");
				}
				final String from = "salvador@scripps.edu";
				final String to = "salvador@scripps.edu";
				final String subject = "PINT tracking email at " + DateFormat.getDateInstance().format(new Date());
				log.info("Trying to send the tracking email with text: " + text);
				final String error = EmailSender.sendEmail(subject, text.toString(), to, from);
				if (error != null) {
					log.warn(error);
				}
				log.info("After trying to send the tracking email");
			}
		} catch (final Exception e) {
			e.printStackTrace();
			log.warn("Something was wrong sending tracking email: " + e.getMessage());
		}

	}

	@Override
	public ProgressStatus getProgressStatus(String sessionID, String taskKey) {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		log.info("Request for progress on task: " + taskKey + " received");
		final Task runningTask = ServerTaskRegister.getInstance().getRunningTask(taskKey);
		if (runningTask != null) {
			log.info("The progress is: " + runningTask.getProgressStatus());
			return runningTask.getProgressStatus();
		}
		log.info(taskKey + " is not running. Returning null progress");
		return null;
	}

	@Override
	public ProteinBeanSubList getProteinBeansFromList(String sessionID, int start, int end) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			// log.info("Getting protein list from " + start + " to " + end + "
			// in session ID: " + sessionID);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				final ProteinBeanSubList proteinBeanSubList = dataSet.getLightProteinBeanSubList(start, end);
				return proteinBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public String login(String clientToken, String userName, String password) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		log.info("LOGIN!!!");
		final String clientIP = HttpUtils.remoteAddr(getThreadLocalRequest());
		// TODO validate login
		validateLogin(userName, password);
		final String id = UUID.randomUUID().toString() + clientIP;
		log.info("NEW SESSION ID:" + id);
		sendTrackingEmail(userName, "New session detected from IP: " + clientIP, "New session ID: " + id);

		// create db session
		return id;
	}

	private void validateLogin(String userName, String password) throws PintException {
		// TODO Auto-generated method stub
		if (userName.equals("guest") && password.equals("guest"))
			return;
		throw new PintException("Login failed for username: " + userName, PINT_ERROR_TYPE.LOGIN_FAILED);
	}

	@Override
	public ProteinBeanSubList getProteinBeansFromListSorted(String sessionID, int start, int end,
			Comparator<ProteinBean> comparator, boolean ascendant) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		if (comparator == null) {
			return getProteinBeansFromList(sessionID, start, end);
		}
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				log.info("Getting sorted protein list from " + start + " to " + end + " dataset '" + dataSet.getName()
						+ "' in session ID: " + sessionID);
				if (!ascendant) {
					comparator = new ReverseComparator(comparator);
				}
				try {
					dataSet.sortProteins(comparator);
				} catch (final Exception e) {
					e.printStackTrace();
					log.warn("Error while sorting proteins: " + e.getMessage() + ". Returning list anyway.");
				}
				final ProteinBeanSubList proteinBeanSubList = dataSet.getLightProteinBeanSubList(start, end);
				return proteinBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// return the unsorted list
			e.printStackTrace();
			log.info("Error while sorting Proteins. Returning unsorted list.");
			return getProteinBeansFromList(sessionID, start, end);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	private void logMethodCall(Method enclosingMethod) {
		log.info("Call to '" + enclosingMethod.getName() + "'");
	}

	@Override
	public ProteinGroupBeanSubList getProteinGroupBeansFromList(String sessionID, int start, int end)
			throws PintException {

		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				log.info("Getting protein group list from " + start + " to " + end + " dataset '" + dataSet.getName()
						+ "' in session ID: " + sessionID);
				final ProteinGroupBeanSubList proteinGroupBeanSubList = dataSet.getLightProteinGroupBeanSubList(null,
						start, end);
				return proteinGroupBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public PsmBeanSubList getPSMBeansFromList(String sessionID, int start, int end) throws PintException {

		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				final PsmBeanSubList proteinGroupBeanSubList = dataSet.getLightPsmBeanSubList(start, end);
				return proteinGroupBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public ProteinGroupBeanSubList getProteinGroupBeansFromListSorted(String sessionID, int start, int end,
			Comparator<ProteinGroupBean> comparator, boolean ascendant) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		if (comparator == null) {
			return getProteinGroupBeansFromList(sessionID, start, end);
		}

		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				if (!ascendant) {
					comparator = new ReverseComparator(comparator);
				}
				try {
					dataSet.sortProteinGroups(comparator, null);
				} catch (final Exception e) {
					e.printStackTrace();
					log.warn("Error while sorting proteinGroups: " + e.getMessage() + ". Returning list anyway.");
				}
				final ProteinGroupBeanSubList proteinGroupBeanSubList = dataSet.getLightProteinGroupBeanSubList(null,
						start, end);
				return proteinGroupBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// return the unsorted list
			e.printStackTrace();
			log.info("Error while sorting ProteinGroups. Returning unsorted list.");
			return getProteinGroupBeansFromList(sessionID, start, end);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public PsmBeanSubList getPSMBeansFromListSorted(String sessionID, int start, int end,
			Comparator<PSMBean> comparator, boolean ascendant) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		if (comparator == null) {
			return getPSMBeansFromList(sessionID, start, end);
		}
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				if (!ascendant) {
					comparator = new ReverseComparator(comparator);
				}
				try {
					dataSet.sortPsms(comparator);
				} catch (final Exception e) {
					e.printStackTrace();
					log.warn("Error while sorting PSMs: " + e.getMessage() + ". Returning list anyway.");
				}
				final PsmBeanSubList psmBeanSubList = dataSet.getLightPsmBeanSubList(start, end);
				return psmBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// return the unsorted list
			e.printStackTrace();
			log.info("Error while sorting PSMs. Returning unsorted list.");
			return getPSMBeansFromList(sessionID, start, end);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public PsmBeanSubList getPsmBeansFromPsmProviderFromListSorted(String sessionID, ContainsPSMs psmProvider,
			int start, int end, Comparator<PSMBean> comparator, boolean ascendant) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		if (comparator == null) {
			return getPsmBeansFromPsmProviderFromList(sessionID, psmProvider, start, end);
		}
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				log.info("Getting sorted psm list from " + start + " to " + end + " dataset '" + dataSet.getName()
						+ "' in session ID: " + sessionID + " for psmProvider");
				if (!ascendant) {
					comparator = new ReverseComparator(comparator);
				}

				final List<PSMBean> psms = dataSet.getPsmsFromPsmProvider(psmProvider);
				try {
					log.info("Sorting " + psms.size() + " PSMs");
					Collections.sort(psms, comparator);
					log.info(psms.size() + " PSMs sorted");
				} catch (final Exception e) {
					e.printStackTrace();
					log.warn("Error while sorting PSMs: " + e.getMessage() + ". Returning list anyway.");
				}
				if (end > psms.size()) {
					end = psms.size();
				}
				if (start > end)
					return null;
				log.info("Getting PSM sublist from " + start + " to " + end);
				final List<PSMBean> psmSubList = psms.subList(start, end);
				log.info("sublist ready of size " + psmSubList.size());
				log.info("Creating psmSubList object");
				final PsmBeanSubList psmBeanSubList = PsmBeanSubList.getLightPsmsBeanSubList(psmSubList, psms.size());
				log.info("psmSubList object created with " + psmBeanSubList.getTotalNumber() + " total number and "
						+ psmBeanSubList.getDataList().size() + " actual number");

				return psmBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// return the unsorted list
			e.printStackTrace();
			log.info("Error while sorting PSMs from PSMProvider. Returning unsorted list.");
			return getPsmBeansFromPsmProviderFromList(sessionID, psmProvider, start, end);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public PsmBeanSubList getPsmBeansFromPsmProviderFromList(String sessionID, ContainsPSMs psmProvider, int start,
			int end) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				log.info("Getting list from " + start + " to " + end + " dataset '" + dataSet.getName()
						+ "' in session ID: " + sessionID + " for psmProvider");

				final List<PSMBean> psms = dataSet.getPsmsFromPsmProvider(psmProvider);

				if (end > psms.size()) {
					end = psms.size();
				}
				if (start > end)
					return null;
				final List<PSMBean> psmSubList = psms.subList(start, end);
				final PsmBeanSubList psmBeanSubList = PsmBeanSubList.getLightPsmsBeanSubList(psmSubList, psms.size());
				return psmBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public void closeSession(String sessionID) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			ContextualSessionHandler.closeSession();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}

	}

	@Override
	public Set<String> getMsRunsFromProject(String projectTag) throws PintException {
		log.info("Getting MsRuns from project " + projectTag);

		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			return RemoteServicesTasks.getMsRunsFromProject(projectTag);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumMSRuns(String projectTag) throws PintException {
		log.info("Getting MsRuns from project " + projectTag);

		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			return ProjectStatsManager.getInstance(enclosingMethod).getNumMSRuns(projectTag);

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public PSEAQuantResult sendPSEAQuantQuery(String email, PSEAQuantSupportedOrganism organism,
			List<PSEAQuantReplicate> replicates, RatioDescriptorBean ratioDescriptor, long numberOfSamplings,
			PSEAQuantQuantType quantType, PSEAQuantAnnotationDatabase annotationDatabase, PSEAQuantCVTol cvTol,
			Double cvTolFactor, PSEAQuantLiteratureBias literatureBias) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		final UniprotProteinLocalRetriever upr = new UniprotProteinLocalRetriever(
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex(), true, true);
		final PSEAQuantSender pseaQuant = new PSEAQuantSender(email, organism, replicates, ratioDescriptor,
				numberOfSamplings, quantType, annotationDatabase, cvTol, cvTolFactor, literatureBias,
				RATIO_AVERAGING.AVERAGE, upr);
		final PSEAQuantResult response = pseaQuant.send();

		return response;
	}

	@Override
	public ProteinPeptideCluster getProteinsByPeptide(String sessionID, ContainsPeptides peptideProvider)
			throws PintException {

		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				if (peptideProvider instanceof ProteinGroupBean) {
					log.info("Getting peptide-protein table for protein group "
							+ ((ProteinGroupBean) peptideProvider).getPrimaryAccessionsString() + " in dataset '"
							+ dataSet.getName() + "'");
				} else if (peptideProvider instanceof ProteinBean) {
					log.info("Getting peptide-protein table for protein group "
							+ ((ProteinBean) peptideProvider).getPrimaryAccession().getAccession() + " in dataset '"
							+ dataSet.getName() + "'");
				}
				ProteinPeptideCluster ret = null;
				// get the non light version of the object
				if (peptideProvider instanceof ProteinBean) {
					final ProteinBean proteinBean = dataSet.getProteinBeansByUniqueIdentifier()
							.get(((ProteinBean) peptideProvider).getProteinBeanUniqueIdentifier());
					ret = new ProteinPeptideCluster(proteinBean);
				} else if (peptideProvider instanceof ProteinGroupBean) {
					final ProteinGroupBean proteinGroupLight = (ProteinGroupBean) peptideProvider;
					final ProteinGroupBean proteinGroup = new ProteinGroupBean();
					for (final ProteinBean proteinBeanLight : proteinGroupLight) {
						final ProteinBean proteinBean = dataSet.getProteinBeansByUniqueIdentifier()
								.get(proteinBeanLight.getProteinBeanUniqueIdentifier());
						proteinGroup.add(proteinBean);
					}
					ret = new ProteinPeptideCluster(proteinGroup);
				}
				final ProteinPeptideClusterAlignmentResults peptideAlignment = getPeptideAlignment(ret);
				ret.setAligmentResults(peptideAlignment);
				return ret;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
		throw new PintException("Unknown error in getProteinsByPeptide", PINT_ERROR_TYPE.INTERNAL_ERROR);
	}

	private ProteinPeptideClusterAlignmentResults getPeptideAlignment(ProteinPeptideCluster cluster)
			throws PintException {
		try {
			final Set<PeptideBean> peptides = cluster.getPeptides();
			final List<PeptideBean> peptideList = new ArrayList<PeptideBean>();
			peptideList.addAll(peptides);
			log.info("Getting alignments for cluster of " + peptides.size() + " peptides");

			final ProteinPeptideClusterAlignmentResults ret = new ProteinPeptideClusterAlignmentResults(cluster);
			for (int i = 0; i < peptideList.size(); i++) {
				for (int j = i + 1; j < peptideList.size(); j++) {
					final PeptideBean peptide1 = peptideList.get(i);
					final String peptide1Seq = peptide1.getSequence();
					final PeptideBean peptide2 = peptideList.get(j);
					final String peptide2Seq = peptide2.getSequence();
					// do not do the alignment if one peptide contains the other
					if (peptide1Seq.contains(peptide2Seq) || peptide2Seq.contains(peptide1Seq)) {
						continue;
					}
					// do not do the alignment if both peptides share the same
					// protein
					if (shareOneProtein(peptide1, peptide2)) {
						continue;
					}

					// do not do the alignment if the peptide belongs ONLY to
					// the same protein
					if (peptide1.getProteins().size() == 1 && peptide2.getProteins().size() == 1) {
						if (peptide1.getProteins().iterator().next().equals(peptide2.getProteins().iterator().next())) {
							continue;
						}
					}
					log.debug("Aligning " + peptide1Seq + " vs " + peptide2Seq);
					final NWResult result = NWAlign.needlemanWunsch(peptide1Seq, peptide2Seq);
					// only get alignments with lenth >= 6
					if (result.getAlignmentLength() < MIN_PEPTIDE_ALIGNMENT_LENGTH) {
						continue;
					}
					final AlignmentResult aligmentResultBean = new AlignmentResultAdapter(result, peptide1, peptide2)
							.adapt();
					// do not store if the aligment is an everlaping without
					// gaps
					if (aligmentResultBean.isAnOverlapping()) {
						System.out.println(aligmentResultBean.isAnOverlapping());
						continue;
					}

					log.debug(result.getAlignmentString());
					ret.addAlignmentResult(peptide1Seq, peptide2Seq, aligmentResultBean);
				}
			}
			log.info(ret.getResultsByPeptideSequence().size() + " alignments done from cluster of " + peptides.size()
					+ " peptides");
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	private boolean shareOneProtein(PeptideBean peptide1, PeptideBean peptide2) {
		final List<AccessionBean> primaryAccessions1 = peptide1.getPrimaryAccessions();
		final List<AccessionBean> primaryAccessions2 = peptide2.getPrimaryAccessions();
		for (final AccessionBean accessionBean : primaryAccessions1) {
			if (primaryAccessions2.contains(accessionBean)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public PeptideBeanSubList getPeptideBeansFromListSorted(String sessionID, int start, int end,
			Comparator<PeptideBean> comparator, boolean ascendant) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		if (comparator == null) {
			return getPeptideBeansFromList(sessionID, start, end);
		}
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				if (!ascendant) {
					comparator = new ReverseComparator(comparator);
				}
				try {
					dataSet.sortPeptides(comparator);
				} catch (final Exception e) {
					e.printStackTrace();
					log.warn("Error while sorting Peptides: " + e.getMessage() + ". Returning list anyway.");
				}
				final PeptideBeanSubList peptideBeanSubList = dataSet.getLightPeptideBeanSubList(start, end);
				return peptideBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// return the unsorted list
			e.printStackTrace();
			log.info("Error while sorting PSMs. Returning unsorted list.");
			return getPeptideBeansFromList(sessionID, start, end);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public PeptideBeanSubList getPeptideBeansFromList(String sessionID, int start, int end) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);

		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				final PeptideBeanSubList peptideBeanSubList = dataSet.getLightPeptideBeanSubList(start, end);
				return peptideBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public PeptideBeanSubList getPeptideBeansFromPeptideProviderFromListSorted(String sessionID,
			ContainsPeptides peptideProvider, int start, int end, Comparator<PeptideBean> comparator, boolean ascendant)
			throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		if (comparator == null) {
			return getPeptideBeansFromPeptideProviderFromList(sessionID, peptideProvider, start, end);
		}
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				log.info("Getting sorted peptide list from " + start + " to " + end + " dataset '" + dataSet.getName()
						+ "' in session ID: " + sessionID + " for peptideProvider");
				if (!ascendant) {
					comparator = new ReverseComparator(comparator);
				}

				final List<PeptideBean> peptides = dataSet.getPeptidesFromPeptideProvider(peptideProvider);
				try {
					log.info("Sorting " + peptides.size() + " Peptides");
					Collections.sort(peptides, comparator);
					log.info(peptides.size() + " Peptides sorted");
				} catch (final Exception e) {
					e.printStackTrace();
					log.warn("Error while sorting Peptides: " + e.getMessage() + ". Returning list anyway.");
				}
				if (end > peptides.size()) {
					end = peptides.size();
				}
				if (start > end)
					return null;
				log.info("Getting PSM sublist from " + start + " to " + end);
				final List<PeptideBean> peptideSubList = peptides.subList(start, end);
				log.info("sublist ready of size " + peptideSubList.size());
				log.info("Creating peptideSubList object");
				final PeptideBeanSubList peptideBeanSubList = PeptideBeanSubList
						.getLightPeptideBeanSubList(peptideSubList, peptides.size());
				log.info("peptideSubList object created with " + peptideBeanSubList.getTotalNumber()
						+ " total number and " + peptideBeanSubList.getDataList().size() + " actual number");

				return peptideBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			// return the unsorted list
			e.printStackTrace();
			log.info("Error while sorting Peptides from PeptideProvider. Returning unsorted list.");
			return getPeptideBeansFromPeptideProviderFromList(sessionID, peptideProvider, start, end);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public PeptideBeanSubList getPeptideBeansFromPeptideProviderFromList(String sessionID,
			ContainsPeptides peptideProvider, int start, int end) throws PintException {
		final Method enclosingMethod = new Object() {
		}.getClass().getEnclosingMethod();
		logMethodCall(enclosingMethod);
		try {
			LockerByTag.lock(sessionID, enclosingMethod);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null, getPSMCentric());
			if (!dataSet.isEmpty() && dataSet.isReady()) {
				log.info("Getting list from " + start + " to " + end + " dataset '" + dataSet.getName()
						+ "' in session ID: " + sessionID + " for peptideProvider");

				final List<PeptideBean> peptides = dataSet.getPeptidesFromPeptideProvider(peptideProvider);

				if (end > peptides.size()) {
					end = peptides.size();
				}
				if (start > end)
					return null;
				final List<PeptideBean> peptideSubList = peptides.subList(start, end);
				final PeptideBeanSubList peptideBeanSubList = PeptideBeanSubList
						.getLightPeptideBeanSubList(peptideSubList, peptides.size());
				return peptideBeanSubList;
			}
			return null;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			LockerByTag.unlock(sessionID, enclosingMethod);
		}
	}

	@Override
	public int getNumMSRuns() throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			return PreparedQueries.getNumMSRuns();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public Set<edu.scripps.yates.shared.model.AmountType> getPSMAmountTypesByCondition(String sessionID,
			String projectTag, String conditionName) throws PintException {

		try {

			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);
			return RemoteServicesTasks.getPSMAmountTypesByCondition(projectTag, conditionName);

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}

	}

	@Override
	public Set<edu.scripps.yates.shared.model.AmountType> getPeptideAmountTypesByCondition(String sessionID,
			String projectTag, String conditionName) throws PintException {

		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			return RemoteServicesTasks.getPeptideAmountTypesByCondition(projectTag, conditionName);

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}

	}

	@Override
	public Set<edu.scripps.yates.shared.model.AmountType> getProteinAmountTypesByCondition(String sessionID,
			String projectTag, String conditionName) throws PintException {

		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			return RemoteServicesTasks.getProteinAmountTypesByCondition(projectTag, conditionName);

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}

	}

	@Override
	public FileDescriptor getDownloadLinkForReactomeAnalysisResult(String sessionID) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			final File file = DataExporter.exportProteinsForReactome(sessionID, getServletContext(), getPSMCentric());
			if (file != null && file.exists()) {
				// prepare the return
				final FileDescriptor ret = new FileDescriptor(FilenameUtils.getName(file.getAbsolutePath()),
						FileManager.getFileSizeString(file));
				log.info("File for reactome created at: " + file.getAbsolutePath());
				log.info("File will be at http://sealion.scripps.edu/pint/download?" + SharedConstants.FILE_TO_DOWNLOAD
						+ "=" + FilenameUtils.getName(file.getAbsolutePath()) + "&" + SharedConstants.FILE_TYPE + "="
						+ SharedConstants.REACTOME_ANALYSIS_RESULT_FILE_TYPE);
				return ret;
			} else {
				throw new PintException(
						"DataSet is not ready of there was an error while creating the file to be accessed by reactome",
						PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public List<ProteinProjection> getProteinProjectionsFromProject(String projectTag) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			log.info("Retrieving protein projections for project '" + projectTag + "'");
			final List<String> accs = PreparedCriteria.getCriteriaForProteinPrimaryAccs(projectTag, null, null, null);
			final Map<String, Entry> annotatedProteinsWithUniprot = RemoteServicesTasks
					.annotateProteinsWithUniprot(accs, null, projectTag);
			final Criteria cr = PreparedCriteria.getCriteriaForProteinProjection(projectTag, null, null, null);
			// transform the results in ProteinProjections
			cr.setResultTransformer(Transformers.aliasToBean(ProteinProjection.class));
			final List<ProteinProjection> list = cr.list();

			log.info(list.size() + " protein projections");
			for (final ProteinProjection proteinProjection : list) {
				final Entry entry = annotatedProteinsWithUniprot.get(proteinProjection.getAcc());
				proteinProjection.setDescription(UniprotEntryUtil.getProteinDescription(entry));
				final List<Pair<String, String>> geneNames = UniprotEntryUtil.getGeneName(entry, true, true);
				if (!geneNames.isEmpty()) {
					proteinProjection.setGene(geneNames.get(0).getFirstelement());
				}
			}
			return list;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			log.info("Retrieving protein projections for project '" + projectTag + "' is finished");

		}
	}

	@Override
	public Map<String, Set<ProteinProjection>> getProteinProjectionsByProteinACCFromProject(String projectTag)
			throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			log.info("Retrieving protein projections by accession for project '" + projectTag + "'");
			if (ServerCacheProteinACCProteinProjectionsByProjectTag.getInstance().contains(projectTag)) {
				log.info("Protein projections for protein ACC are in server cache");
				return ServerCacheProteinACCProteinProjectionsByProjectTag.getInstance().getFromCache(projectTag);
			}
			final List<ProteinProjection> list = getProteinProjectionsFromProject(projectTag);
			// add to the map by acc
			final Map<String, Set<ProteinProjection>> ret = new HashMap<String, Set<ProteinProjection>>();
			for (final ProteinProjection proteinProjection : list) {
				if (ret.containsKey(proteinProjection.getAcc())) {
					ret.get(proteinProjection.getAcc()).add(proteinProjection);
				} else {
					final Set<ProteinProjection> set = new HashSet<ProteinProjection>();
					set.add(proteinProjection);
					ret.put(proteinProjection.getAcc(), set);
				}
			}
			ServerCacheProteinACCProteinProjectionsByProjectTag.getInstance().addtoCache(ret, projectTag);
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			log.info("Retrieving protein projections for project '" + projectTag + "' is finished");

		}
	}

	@Override
	public Map<String, Set<ProteinProjection>> getProteinProjectionsByGeneNameFromProject(String projectTag)
			throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			log.info("Retrieving protein projections by gene name for project '" + projectTag + "'");
			if (ServerCacheGeneNameProteinProjectionsByProjectTag.getInstance().contains(projectTag)) {
				log.info("Protein projections for protein name are in server cache");
				return ServerCacheGeneNameProteinProjectionsByProjectTag.getInstance().getFromCache(projectTag);
			}

			final List<ProteinProjection> list = getProteinProjectionsFromProject(projectTag);
			// add to the map by gene name
			final Map<String, Set<ProteinProjection>> ret = new HashMap<String, Set<ProteinProjection>>();
			for (final ProteinProjection proteinProjection : list) {
				if (ret.containsKey(proteinProjection.getGene())) {
					ret.get(proteinProjection.getGene()).add(proteinProjection);
				} else {
					final Set<ProteinProjection> set = new HashSet<ProteinProjection>();
					set.add(proteinProjection);
					ret.put(proteinProjection.getGene(), set);
				}
			}
			ServerCacheGeneNameProteinProjectionsByProjectTag.getInstance().addtoCache(ret, projectTag);
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			log.info("Retrieving protein projections for project '" + projectTag + "' is finished");

		}
	}

	@Override
	public Map<String, Set<ProteinProjection>> getProteinProjectionsByProteinNameFromProject(String projectTag)
			throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			log.info("Retrieving protein projections by protein name for project '" + projectTag + "'");
			if (ServerCacheProteinNameProteinProjectionsByProjectTag.getInstance().contains(projectTag)) {
				log.info("Protein projections for protein name are in server cache");
				return ServerCacheProteinNameProteinProjectionsByProjectTag.getInstance().getFromCache(projectTag);
			}
			final List<ProteinProjection> list = getProteinProjectionsFromProject(projectTag);
			log.info(list.size() + " protein projections by protein name");
			// override the protein names by using the FastaParser
			for (final ProteinProjection proteinProjection : list) {
				proteinProjection.setDescription(FastaParser.getDescription(proteinProjection.getDescription()));
			}
			// add to the map by gene name
			final Map<String, Set<ProteinProjection>> ret = new HashMap<String, Set<ProteinProjection>>();
			for (final ProteinProjection proteinProjection : list) {
				if (ret.containsKey(proteinProjection.getDescription())) {
					ret.get(proteinProjection.getDescription()).add(proteinProjection);
				} else {
					final Set<ProteinProjection> set = new HashSet<ProteinProjection>();
					set.add(proteinProjection);
					ret.put(proteinProjection.getDescription(), set);
				}
			}
			ServerCacheProteinNameProteinProjectionsByProjectTag.getInstance().addtoCache(ret, projectTag);
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		} finally {
			log.info("Retrieving protein projections for project '" + projectTag + "' is finished");

		}
	}

	@Override
	public int getNumDifferentProteins(String projectTag) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			return ProjectStatsManager.getInstance(method).getNumDifferentProteins(projectTag);

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumDifferentPeptides(String projectTag) throws PintException {
		try {
			final Method enclosingMethod = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(enclosingMethod);

			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			return ProjectStatsManager.getInstance(method).getNumDifferentPeptides(projectTag);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumPSMs(String projectTag) throws PintException {
		try {

			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumPSMs(projectTag);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumGenes() throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumGenes();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumGenes(String projectTag) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumGenes(projectTag);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumDifferentProteins(String projectTag, MSRunBean msRun) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumDifferentProteins(projectTag, msRun);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumDifferentPeptides(String projectTag, MSRunBean msRun) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumDifferentPeptides(projectTag, msRun);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumPSMs(String projectTag, MSRunBean msRun) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumPSMs(projectTag, msRun);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumGenes(String projectTag, MSRunBean msRun) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumGenes(projectTag, msRun);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumDifferentProteins(String projectTag, ExperimentalConditionBean condition) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumDifferentProteins(projectTag, condition);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumDifferentPeptides(String projectTag, ExperimentalConditionBean condition) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumDifferentPeptides(projectTag, condition);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumPSMs(String projectTag, ExperimentalConditionBean condition) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumPSMs(projectTag, condition);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumGenes(String projectTag, ExperimentalConditionBean condition) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumGenes(projectTag, condition);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumMSRuns(String projectTag, ExperimentalConditionBean condition) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumMSRuns(projectTag, condition);

		} catch (final Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		} finally {
		}
	}

	@Override
	public int getNumDifferentProteins(String projectTag, SampleBean sample) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumDifferentProteins(projectTag, sample);
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumDifferentPeptides(String projectTag, SampleBean sample) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumDifferentPeptides(projectTag, sample);

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumPSMs(String projectTag, SampleBean sample) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumPSMs(projectTag, sample);

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumGenes(String projectTag, SampleBean sample) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumGenes(projectTag, sample);

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public int getNumMSRuns(String projectTag, SampleBean sample) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumMSRuns(projectTag, sample);

		} catch (final Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		} finally {
		}
	}

	@Override
	public int getNumConditions(String projectTag) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumConditions(projectTag);

		} catch (final Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		} finally {
		}
	}

	@Override
	public int getNumConditions(String projectTag, MSRunBean msRun) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumConditions(projectTag, msRun);

		} catch (final Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		} finally {
		}
	}

	@Override
	public int getNumSamples(String projectTag, MSRunBean msRun) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			return ProjectStatsManager.getInstance(method).getNumSamples(projectTag, msRun);

		} catch (final Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		} finally {
		}
	}

	/**
	 * This method is used in case that the methods in this servlet are used
	 * from another servlet, so the servletContext is not initialized
	 * 
	 * @param servletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;

	}

	@Override
	public ServletContext getServletContext() {
		if (servletContext == null) {
			servletContext = super.getServletContext();
		}
		return servletContext;
	}

	@Override
	public void cancelQuery(String sessionID) throws PintException {
		try {
			final Method method = new Object() {
			}.getClass().getEnclosingMethod();
			logMethodCall(method);
			log.info("Cancelling request from session ID :" + sessionID);
			DataSetsManager.clearDataSet(sessionID);
		} catch (final Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		} finally {
		}
	}
}
