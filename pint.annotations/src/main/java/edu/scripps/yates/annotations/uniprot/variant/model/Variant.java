package edu.scripps.yates.annotations.uniprot.variant.model;

public class Variant {
	private final String id;
	private final String seq;
	private final String description;
	private final boolean original;
	private final VariantType variantType;

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

}
