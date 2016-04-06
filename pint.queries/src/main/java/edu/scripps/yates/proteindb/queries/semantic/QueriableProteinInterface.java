package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;

public interface QueriableProteinInterface {
	public Set<LinkBetweenProteinAndPSM> getLinks();

	public void removeLink(LinkBetweenProteinAndPSM link);

	public Set<Peptide> getPeptides();

	public Set<Psm> getPsms();

	public Set<ProteinAccession> getProteinAccessions();

	public String getPrimaryAccession();

	public ProteinAccession getPrimaryProteinAccession();

	public Set<Condition> getConditions();

	public Set<ProteinAmount> getProteinAmounts();

	public Set<ProteinScore> getProteinScores();

	public Integer getLength();

	public Double getMw();

	public Double getPi();

	public Set<MsRun> getMsRuns();

	public Set<ProteinRatioValue> getProteinRatioValues();

	public Set<ProteinRatioValue> getProteinRatiosBetweenTwoConditions(String condition1Name, String condition2Name,
			String ratioName);

	public Set<Gene> getGenes();

	public Set<ProteinThreshold> getProteinThresholds();

	public Set<ProteinAnnotation> getProteinAnnotations();

	public Organism getOrganism();

	public Set<Integer> getProteinDBIds();

	public Set<Protein> getProteins();

}
