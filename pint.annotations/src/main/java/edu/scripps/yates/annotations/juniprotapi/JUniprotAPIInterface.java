package edu.scripps.yates.annotations.juniprotapi;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseCrossReference;
import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseType;
import uk.ac.ebi.kraken.interfaces.uniprot.Gene;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.QueryResult;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtComponent;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtQueryBuilder;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;
import uk.ac.ebi.uniprot.dataservice.query.Query;

public class JUniprotAPIInterface {
	private final static Logger log = Logger.getLogger(JUniprotAPIInterface.class);
	private static JUniprotAPIInterface instance;
	private final ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
	private UniProtService uniprotService;
	private static final int chunkSize = 200;

	public static JUniprotAPIInterface getInstance() {
		if (instance == null) {
			instance = new JUniprotAPIInterface();
		}
		return instance;
	}

	private JUniprotAPIInterface() {

	}

	private UniProtService getService() {
		if (uniprotService == null) {
			uniprotService = serviceFactoryInstance.getUniProtQueryService();
		}
		return uniprotService;
	}

	private UniProtService startService() {
		getService().start();
		return getService();
	}

	private void stopService() {
		getService().stop();
	}

	private Query getQueryForAccessions(Collection<String> accs) {
		if (accs instanceof Set) {
			Query query = UniProtQueryBuilder.accessions((Set<String>) accs);
			return query;
		} else {
			Set<String> set = new HashSet<String>();
			set.addAll(accs);
			Query query = UniProtQueryBuilder.accessions(set);
			return query;
		}

	}

	public Map<String, String> getProteinDescriptions(Set<String> accs) throws ServiceException {
		try {
			Map<String, String> ret = new HashMap<String, String>();
			ElementChunksIterator iterator = new ElementChunksIterator(chunkSize, accs);
			int num = 0;
			while (iterator.hasNext()) {
				List<String> accsToQuery = iterator.next();
				num += accsToQuery.size();
				log.info("Querying " + num + "/" + accs.size() + " protein names");
				Query query = getQueryForAccessions(accsToQuery);
				QueryResult<UniProtComponent<String>> components = startService().getProteinNames(query);

				while (components.hasNext()) {
					UniProtComponent<String> component = components.next();
					ret.put(component.getAccession().getValue(), component.getComponent().get(0));
				}
			}
			return ret;
		} finally {
			stopService();
		}
	}

	public Map<String, List<Gene>> getGenes(Set<String> accs) throws ServiceException {
		try {
			Map<String, List<Gene>> ret = new HashMap<String, List<Gene>>();
			ElementChunksIterator iterator = new ElementChunksIterator(chunkSize, accs);
			int num = 0;
			while (iterator.hasNext()) {
				List<String> accsToQuery = iterator.next();
				num += accsToQuery.size();
				log.info("Querying " + num + "/" + accs.size() + " genes");
				Query query = getQueryForAccessions(accsToQuery);
				QueryResult<UniProtComponent<Gene>> components = startService().getGenes(query);

				while (components.hasNext()) {
					UniProtComponent<Gene> component = components.next();
					ret.put(component.getAccession().getValue(), component.getComponent());
				}
			}
			return ret;
		} finally {
			stopService();
		}
	}

	public Map<String, String> getChromosomeNames(Set<String> accs) throws ServiceException {
		try {
			Map<String, String> ret = new HashMap<String, String>();
			ElementChunksIterator iterator = new ElementChunksIterator(chunkSize, accs);
			int num = 0;
			while (iterator.hasNext()) {
				List<String> accsToQuery = iterator.next();
				num += accsToQuery.size();
				log.info("Querying " + num + "/" + accs.size() + " genes");
				Query query = getQueryForAccessions(accsToQuery);
				QueryResult<UniProtComponent<DatabaseCrossReference>> xrefs = startService().getXrefs(query);

				while (xrefs.hasNext()) {
					UniProtComponent<DatabaseCrossReference> xref = xrefs.next();
					List<DatabaseCrossReference> component = xref.getComponent();
					for (DatabaseCrossReference databaseCrossReference : component) {
						if (databaseCrossReference.getDatabase() == DatabaseType.PROTEOMES) {
							if (databaseCrossReference.getDescription().getValue().contains("Chromosome")) {
								ret.put(xref.getAccession().getValue(),
										databaseCrossReference.getDescription().getValue());
							}
						}
					}
				}
			}
			return ret;
		} finally {
			stopService();
		}
	}
}
