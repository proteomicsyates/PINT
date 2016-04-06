package edu.scripps.yates.client.gui.components;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

public class CommandSuggestion extends MultiWordSuggestOracle {

	private static String whiteSpaceChars = ",[]";

	public CommandSuggestion() {
		super(whiteSpaceChars);
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		// TODO Auto-generated method stub
		System.out.println(request.getQuery());

		super.requestSuggestions(request, callback);
	}

}
