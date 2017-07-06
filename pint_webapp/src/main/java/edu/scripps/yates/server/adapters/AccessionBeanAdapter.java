package edu.scripps.yates.server.adapters;

import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProteinAccessionAdapter;
import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AccessionType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import gnu.trove.map.hash.THashMap;

public class AccessionBeanAdapter implements Adapter<AccessionBean> {
	private final ProteinAccession proteinAccession;
	private final static Map<String, AccessionBean> map = new THashMap<String, AccessionBean>();
	private final static Logger log = Logger.getLogger(AccessionBeanAdapter.class);

	public AccessionBeanAdapter(ProteinAccession proteinAccession) {
		this.proteinAccession = proteinAccession;
	}

	@Override
	public AccessionBean adapt() {
		if (map.containsKey(proteinAccession.getAccession())) {

			AccessionBean ret = map.get(proteinAccession.getAccession());
			return ret;
		}
		AccessionBean ret = new AccessionBean();
		map.put(proteinAccession.getAccession(), ret);
		if (proteinAccession.getAlternativeNames() != null) {
			if (proteinAccession.getAlternativeNames().contains(ProteinAccessionAdapter.SEPARATOR)) {
				StringTokenizer tokenizer = new StringTokenizer(proteinAccession.getAlternativeNames(),
						ProteinAccessionAdapter.SEPARATOR);

				while (tokenizer.hasMoreElements()) {
					String string = tokenizer.nextToken();
					ret.addAlternativeName(string);
				}
			} else {

				ret.addAlternativeName(proteinAccession.getAlternativeNames());

			}
		}
		ret.setDescription(FastaParser.getDescription(proteinAccession.getDescription()));
		ret.setPrimaryAccession(proteinAccession.isIsPrimary());
		ret.setAccession(proteinAccession.getAccession());
		final AccessionType accType = AccessionType.fromValue(proteinAccession.getAccessionType());
		ret.setAccessionType(accType);
		return ret;
	}
}
