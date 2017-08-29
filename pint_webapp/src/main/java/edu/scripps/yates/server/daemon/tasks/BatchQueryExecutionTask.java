package edu.scripps.yates.server.daemon.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import edu.scripps.yates.server.DataSet;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.set.hash.THashSet;

public class BatchQueryExecutionTask extends PintServerDaemonTask {
	private final File queriesFile;
	private final File outputFile;

	public BatchQueryExecutionTask(ServletContext servletContext) {
		super(servletContext);
		queriesFile = new File("/home/salvador/PInt/batch_query_list.txt");
		outputFile = new File("/home/salvador/PInt/queryBatchOutput.txt");
	}

	@Override
	public void run() {
		if (outputFile.exists() && outputFile.length() > 0) {
			log.info("Batch execution skipped. Results file already exists at: " + outputFile.getAbsolutePath());
			return;
		}
		Set<String> projects = new THashSet<String>();
		projects.add("DmDshybrids2014");
		Map<String, Pair<DataSet, String>> results = RemoteServicesTasks.batchQuery(queriesFile, projects, false);
		FileWriter out = null;
		try {
			out = new FileWriter(outputFile);
			out.write("Num protein groups" + "\t" + "Num proteins" + "\t" + "Num PSMs" + "\t" + "Num diff seqs" + "\t"
					+ "Num diff seqs (PTM sensible)" + "\t" + "URL to results");
			for (String queryString : results.keySet()) {
				final Pair<DataSet, String> pair = results.get(queryString);
				final DataSet dataSet = pair.getFirstelement();
				log.info("Writing dataset in the file:" + dataSet);
				final String fileName = pair.getSecondElement();
				StringBuilder sb = new StringBuilder();
				sb.append(queryString + "\n");
				sb.append(dataSet.getProteinGroups(false).size() + "\t" + dataSet.getProteins().size() + "\t"
						+ dataSet.getPsms().size() + "\t" + dataSet.getNumDifferentSequences(false) + "\t"
						+ dataSet.getNumDifferentSequences(true) + "\t"
						+ "http://sealion.scripps.edu/pint/download?fileType=idDataFile&filetodownload=" + fileName);
				out.write(sb.toString() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean justRunOnce() {
		return false;
	}
}
