package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAnnotationType;
import edu.scripps.yates.utilities.model.factories.ProteinAnnotationEx;
import edu.scripps.yates.utilities.pattern.Adapter;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;

public class ProteinAnnotationsAdapterByExcel implements Adapter<Set<ProteinAnnotation>> {
	private final int rowIndex;
	private final IdentificationExcelType excelCondition;
	private final ExcelFileReader excelFileReader;

	public ProteinAnnotationsAdapterByExcel(int rowIndex, IdentificationExcelType excelCondition,
			ExcelFileReader excelFileReader) {
		this.rowIndex = rowIndex;
		this.excelCondition = excelCondition;
		this.excelFileReader = excelFileReader;
	}

	@Override
	public Set<ProteinAnnotation> adapt() {
		Set<ProteinAnnotation> ret = new HashSet<ProteinAnnotation>();

		if (excelCondition.getProteinAnnotations() != null) {
			final List<ProteinAnnotationType> proteinAnnotationCfg = excelCondition.getProteinAnnotations()
					.getProteinAnnotation();
			if (proteinAnnotationCfg != null) {
				for (ProteinAnnotationType proteinAnnotationTypeCfg : proteinAnnotationCfg) {
					final ExcelColumn proteinAnnotationColumn = excelFileReader
							.getExcelColumnFromReference(proteinAnnotationTypeCfg.getColumnRef());
					if (proteinAnnotationColumn != null) {

						final List<Object> annotationValues = proteinAnnotationColumn.getValues();
						if (annotationValues.size() > rowIndex) {
							final Object annotationValue = annotationValues.get(rowIndex);
							if (annotationValue != null) {
								String annotationString = annotationValue.toString();
								if (proteinAnnotationTypeCfg.isBinary()) {
									if (annotationString.equals(proteinAnnotationTypeCfg.getYesValue())) {
										ProteinAnnotationEx annotation = new ProteinAnnotationEx(
												AnnotationType.manualAnnotation, proteinAnnotationColumn.getHeader());
										ret.add(annotation);
									} else {

									}
								} else {
									ProteinAnnotationEx annotation = new ProteinAnnotationEx(
											AnnotationType.manualAnnotation, annotationString);
									ret.add(annotation);
								}
							}
						}
					}
				}
			}
		}
		return ret;
	}

}
