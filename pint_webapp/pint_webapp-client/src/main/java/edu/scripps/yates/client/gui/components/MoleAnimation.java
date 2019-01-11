package edu.scripps.yates.client.gui.components;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;

public class MoleAnimation extends Animation {
	private int endSize;
	private int startSize;
	private final Element borderElement;

	@Override
	protected void onComplete() {
		if (endSize == 0) {
			borderElement.getStyle().setDisplay(Display.NONE);
			return;
		}
		borderElement.getStyle().setHeight(endSize, Unit.PX);
	}

	@Override
	protected void onUpdate(double progress) {
		double delta = (endSize - startSize) * progress;
		double newSize = startSize + delta;
		borderElement.getStyle().setHeight(newSize, Unit.PX);
		// System.out.println(newSize);
	}

	public MoleAnimation(Element borderElement) {
		this.borderElement = borderElement;
	}

	public void animateMole(int startSize, int endSize, int duration) {

		borderElement.getStyle().setHeight(startSize, Unit.PX);
		borderElement.getStyle().setDisplay(Display.BLOCK);
		this.startSize = startSize;
		this.endSize = endSize;
		if (duration == 0) {
			onComplete();
			return;
		}
		run(duration);
	}

}
