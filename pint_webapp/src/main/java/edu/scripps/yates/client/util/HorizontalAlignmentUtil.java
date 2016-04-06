package edu.scripps.yates.client.util;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

import edu.scripps.yates.shared.util.HorizontalAlignmentSharedConstant;

public class HorizontalAlignmentUtil {

	public static HorizontalAlignmentConstant getHorizontalAlignmentConstant(
			HorizontalAlignmentSharedConstant aligment) {
		switch (aligment) {
		case ALIGN_CENTER:
			return HasHorizontalAlignment.ALIGN_CENTER;

		case ALIGN_LEFT:
			return HasHorizontalAlignment.ALIGN_LEFT;
		case ALIGN_RIGHT:
			return HasHorizontalAlignment.ALIGN_RIGHT;
		default:
			return HasHorizontalAlignment.ALIGN_LEFT;
		}
	}
}
