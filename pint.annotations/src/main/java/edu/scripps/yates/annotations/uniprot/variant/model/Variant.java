package edu.scripps.yates.annotations.uniprot.variant.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Variant {
	private final String id;
	private final String seq;
	private final String description;
	private final boolean original;
	private final VariantType variantType;
	private List<UniprotPTM> ptms = new ArrayList<UniprotPTM>();

	public Variant(String id, String seq, String description, VariantType variantType, boolean original) {
		this.id = id;
		this.seq = seq;
		this.description = description;
		this.variantType = variantType;
		this.original = original;
	}

	public Variant(String id, String seq, String description, VariantType variantType) {
		this(id, seq, description, variantType, false);
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

	public VariantType getVariantType() {
		return variantType;
	}

	public List<UniprotPTM> getPtms() {
		return ptms;
	}

	public void addPTM(UniprotPTM ptm) {
		this.ptms.add(ptm);
		sortByPTMPosition();
	}

	private void sortByPTMPosition() {
		ptms = ptms.stream().sorted((e1, e2) -> Integer.compare(e1.getPositionInProtein(), e2.getPositionInProtein()))
				.collect(Collectors.toList());
	}
}
