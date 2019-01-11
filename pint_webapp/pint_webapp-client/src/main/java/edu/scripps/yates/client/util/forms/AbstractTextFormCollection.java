package edu.scripps.yates.client.util.forms;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.shared.util.Pair;

@SuppressWarnings("rawtypes")
public abstract class AbstractTextFormCollection extends ArrayList<AbstractFormInformation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4143213889812127586L;

	/**
	 * Gets a list of pairs of {@link AbstractFormInformation}.<br>
	 * In each pair, the first element will be the base
	 * {@link AbstractFormInformation} and the second element will be the dependent
	 * {@link AbstractFormInformation}
	 * 
	 * @return
	 */
	public abstract List<Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>> getTextFormsLinks();

	protected PintContext context;

	public AbstractTextFormCollection(PintContext context) {
		this.context = context;
	}

	public Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation> getLink(
			AbstractTextBasedFormInformation baseTextForm, AbstractTextBasedFormInformation dependentTextForm) {
		final Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation> pair = new Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>(
				baseTextForm, dependentTextForm);
		return pair;
	}

}
