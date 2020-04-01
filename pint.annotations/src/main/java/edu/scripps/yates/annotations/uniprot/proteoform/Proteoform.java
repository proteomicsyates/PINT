package edu.scripps.yates.annotations.uniprot.proteoform;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.scripps.yates.utilities.fasta.FastaParser;

public class Proteoform {
	private final String id;
	private final String seq;
	private final String description;
	private final boolean original;
	private final ProteoformType proteoformType;
	private List<UniprotPTM> ptms = new ArrayList<UniprotPTM>();
	private final String originalACC;
	private final String gene;
	private final String taxonomy;
	private final String originalSeq;
	private final String name;
	private final boolean isSwissprot;
	private final String taxID;

	public Proteoform(String originalACC, String originalSeq, String id, String seq, String name, String description,
			String gene, String taxonomy, String taxID, ProteoformType proteoformType, boolean original,
			boolean isSwissprot) {
		this.originalACC = originalACC;
		this.originalSeq = originalSeq;
		this.id = id;
		this.seq = seq;
		this.name = name;
		this.description = description;
		this.gene = gene;
		this.taxonomy = taxonomy;
		this.taxID = taxID;
		this.proteoformType = proteoformType;
		this.original = original;
		this.isSwissprot = isSwissprot;
//		if ("1".equals(FastaParser.getIsoformVersion(id))) {
//			System.out.println("asdf");
//		}

	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Proteoform) {
			final Proteoform p = (Proteoform) obj;
			if (p.getId().equals(getId())) {
				if (p.getProteoformType() == getProteoformType()) {
					return true;
				}
			}
			return false;
		}
		return super.equals(obj);
	}

	public Proteoform(String originalACC, String originalSeq, String id, String seq, String name, String description,
			String gene, String taxonomy, String taxID, ProteoformType variantType, boolean isSwissprot) {
		this(originalACC, originalSeq, id, seq, name, description, gene, taxonomy, taxID, variantType, false,
				isSwissprot);
	}

	public String getId() {
		return id;
	}

	public String getSeq() {
		return seq;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return getProteoformType() + " [id=" + id + ", seq=" + seq + ", description=" + description + "]";
	}

	public boolean isOriginal() {
		return original;
	}

	public ProteoformType getProteoformType() {
		return proteoformType;
	}

	public List<UniprotPTM> getPtms() {
		return ptms;
	}

	public void addPTM(UniprotPTM ptm) {
		ptms.add(ptm);
		sortByPTMPosition();
	}

	private void sortByPTMPosition() {
		ptms = ptms.stream().sorted((e1, e2) -> Integer.compare(e1.getPositionInProtein(), e2.getPositionInProtein()))
				.collect(Collectors.toList());
	}

	public String getOriginalACC() {
		return originalACC;
	}

	public String getGene() {
		return gene;
	}

	public String getTaxonomy() {
		return taxonomy;
	}

	public String getOriginalSeq() {
		return originalSeq;
	}

	public String getName() {
		return name;
	}

	public String getFastaHeader() {

		String sp = "sp";
		if (!isSwissprot) {
			sp = "tr";
		}
		String defline = ">" + sp + "|" + this.id + "|" + name + " " + description;
		if (FastaParser.isReverse(id)) {
			defline = ">" + id + " " + description;
		}

		if (gene != null) {
			defline += " GN=" + gene;
		}

		if (taxonomy != null) {
			defline += " OS=" + taxonomy;
		}

		if (taxID != null) {
			defline += " OX=" + taxID;
		}
		return defline;
	}

	public boolean isSwissprot() {
		return isSwissprot;
	}
}
