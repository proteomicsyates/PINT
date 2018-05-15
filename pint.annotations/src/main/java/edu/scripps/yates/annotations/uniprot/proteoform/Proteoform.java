package edu.scripps.yates.annotations.uniprot.proteoform;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	public Proteoform(String originalACC, String originalSeq, String id, String seq, String name, String description,
			String gene, String taxonomy, ProteoformType proteoformType, boolean original) {
		this.originalACC = originalACC;
		this.originalSeq = originalSeq;
		this.id = id;
		this.seq = seq;
		this.name = name;
		this.description = description;
		this.gene = gene;
		this.taxonomy = taxonomy;
		this.proteoformType = proteoformType;
		this.original = original;
		if (description.equals(name)) {
			System.out.println("asdf");
		}
	}

	public Proteoform(String originalACC, String originalSeq, String id, String seq, String name, String description,
			String gene, String taxonomy, ProteoformType variantType) {
		this(originalACC, originalSeq, id, seq, name, description, gene, taxonomy, variantType, false);
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
		return "Variant [id=" + id + ", seq=" + seq + ", description=" + description + "]";
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
}
