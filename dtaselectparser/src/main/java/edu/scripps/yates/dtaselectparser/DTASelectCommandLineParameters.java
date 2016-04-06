package edu.scripps.yates.dtaselectparser;

import java.util.HashMap;
import java.util.Map;

/**
 * Parse command line parameters of DTASElect like -p 1 -y 2 --trypstat --fp
 * 0.01 --modstat --extra --pI -DM 5 --DB --dm -m 0 -S 3.5 --quiet
 * 
 * @author Salva
 * 
 */
public class DTASelectCommandLineParameters {

	private final Map<String, String> parameters = new HashMap<String, String>();

	public DTASelectCommandLineParameters(String commandLineParameters) {
		final String[] split = commandLineParameters.split(" ");

		String parameter = null;
		String value = null;
		for (String string : split) {
			if (string.startsWith("-")) {
				if (parameter != null) {
					parameters.put(parameter, null);
				}
				parameter = string;
				value = null;
			} else {
				value = string;
				if (parameter != null) {
					parameters.put(parameter, value);
					parameter = null;
				}
			}
		}
	}

	/**
	 * @return the parameters
	 */
	public Map<String, String> getParametersMap() {
		return parameters;
	}

	public static void main(String[] args) {
		String paramString = " -p 1 -y 2 --trypstat --fp 0.01 --modstat --extra --pI -DM 5 --DB --dm -m 0 -S 3.5 --quiet ";
		DTASelectCommandLineParameters d = new DTASelectCommandLineParameters(
				paramString);
		System.out.println(paramString);
		Map<String, String> parameters2 = d.getParametersMap();
		for (String param : parameters2.keySet()) {
			System.out.println(param + "\t" + parameters2.get(param));
		}

		String parameterValue = d.getParameterValue("t");
		System.out.println("PARAMETER t= '" + parameterValue + "'");

		paramString = " -p 1 -y 2 --trypstat --fp 0.01 --modstat --extra --pI -DM 5 --DB --dm -m 0 -S 3.5 --quiet -t 0";
		d = new DTASelectCommandLineParameters(paramString);
		System.out.println(paramString);
		parameters2 = d.getParametersMap();
		for (String param : parameters2.keySet()) {
			System.out.println(param + "\t" + parameters2.get(param));
		}

		parameterValue = d.getParameterValue("t");
		System.out.println("PARAMETER t= '" + parameterValue + "'");
	}

	/**
	 * Search for the value of that parameter, avoiding if necessary the "-" or
	 * "--" in front of the parameters
	 * 
	 * @param parameterName
	 * @return
	 */
	public String getParameterValue(String parameterName) {
		if (parameters.containsKey(parameterName)) {
			return parameters.get(parameterName);
		} else {
			for (String parameter : parameters.keySet()) {
				String parameterWithoutDash = parameter.replace("-", "");
				if (parameterWithoutDash.equals(parameterName))
					return parameters.get(parameter);
			}
		}
		return null;
	}
}
