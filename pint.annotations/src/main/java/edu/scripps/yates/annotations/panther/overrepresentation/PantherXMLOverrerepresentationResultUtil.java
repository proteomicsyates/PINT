package edu.scripps.yates.annotations.panther.overrepresentation;

import java.util.List;
import java.util.stream.Collectors;

import edu.scripps.yates.annotations.panther.overrepresentation.xml.Overrepresentation;
import edu.scripps.yates.annotations.panther.overrepresentation.xml.Overrepresentation.Group;
import edu.scripps.yates.annotations.panther.overrepresentation.xml.Overrepresentation.Group.Result;

public class PantherXMLOverrerepresentationResultUtil {

	public static String getInputList(Overrepresentation o) {
		return o.getUploadLists().getInputList().getListName();
	}

	public static String getReference(Overrepresentation overrepresentation) {
		if (overrepresentation.getReference() != null) {
			return overrepresentation.getReference().getReferenceOrganism();
		}
		return null;
	}

	public static String getInputFileOrganism(Overrepresentation overrepresentation) {
		return overrepresentation.getUploadLists().getInputList().getOrganism();
	}

	public static int getNumTermsOverrepresented(Overrepresentation overrepresentation) {
		int num = 0;
		final List<Group> groups = getResults(overrepresentation);
		for (final Group group : groups) {
			for (final Result result : group.getResult()) {
				if (result.getInputList().getPlusMinus().equals("+")) {
					num++;
				}
			}
		}
		return num;
	}

	private static boolean isOverrepresented(Group group) {
		final Result result = group.getResult().stream().filter(g -> g.getTerm().getLevel() == 0).findFirst().get();
		if (result.getInputList().getPlusMinus().equals("+")) {
			return true;
		}
		return false;
	}

	private static List<Group> getResults(Overrepresentation overrepresentation) {
		return overrepresentation.getGroup();

	}

	public static int getNumTermsUnderrepresented(Overrepresentation overrepresentation) {
		int num = 0;
		final List<Group> groups = getResults(overrepresentation);
		for (final Group group : groups) {
			for (final Result result : group.getResult()) {
				if (!result.getInputList().getPlusMinus().equals("+")) {
					num++;
				}
			}
		}
		return num;
	}

	public static int getNumGroupTermsOverrepresented(Overrepresentation overrepresentation) {
		int num = 0;
		final List<Group> groups = getResults(overrepresentation);
		for (final Group group : groups) {
			if (isOverrepresented(group)) {
				num++;
			}
		}
		return num;
	}

	public static int getNumGroupTermsUnderrepresented(Overrepresentation overrepresentation) {
		int num = 0;
		final List<Group> groups = getResults(overrepresentation);
		for (final Group group : groups) {
			if (!isOverrepresented(group)) {
				num++;
			}
		}
		return num;
	}

	public static List<Result> getOverrepresentedResults(Group group) {
		return group.getResult().stream().filter(r -> r.getInputList().getPlusMinus().equals("+"))
				.collect(Collectors.toList());
	}

	public static List<Result> getUnderrepresentedResults(Group group) {
		return group.getResult().stream().filter(r -> r.getInputList().getPlusMinus().equals("-"))
				.collect(Collectors.toList());
	}
}
