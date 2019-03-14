package edu.scripps.yates.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

import org.junit.Test;
import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

import edu.scripps.yates.mzidentmlparser.MzIdentMLIdentificationsParser;

public class MzIdentMLParserTest {
	@Test
	public void test2() {
		try {
			final File mzidFile = new File("C:\\Users\\salvador\\Desktop\\submission data\\DTASelect-filter.mzid");
			final MzIdentMLIdentificationsParser parser = new MzIdentMLIdentificationsParser(mzidFile);
			final MiapeMSIDocument miapeMSI = parser.parseMiapeMSIFromInputStream(new FileInputStream(mzidFile));
			System.out.println(miapeMSI.getIdentifiedPeptides().size() + " PSMs");
			final Set<InputParameter> inputParameters = miapeMSI.getInputParameters();
			System.out.println(inputParameters.size() + " params");
			for (final InputParameter inputParameter : inputParameters) {
				System.out.println("databases " + inputParameter.getDatabases().size());
				final Set<AdditionalParameter> additionalParameters = inputParameter.getAdditionalParameters();
				for (final AdditionalParameter param : additionalParameters) {
					System.out.println(param.getName() + "\t" + param.getValue());
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
