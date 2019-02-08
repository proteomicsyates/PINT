package edu.scripps.yates.client.gui.templates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MyClientBundle extends ClientBundle {
	public static final MyClientBundle INSTANCE = GWT.create(MyClientBundle.class);

	// @Source("edu/scripps/yates/public/Pint.css")
	// public MyCss css();

	@Source("tsrilogo.jpg")
	public ImageResource tsrilogo();

	@Source("yateslogo.jpg")
	public ImageResource yateslablogo();

	@Source("yateslab_small.gif")
	public ImageResource yatesLabSmall();

	@Source("small_loader.gif")
	public ImageResource smallLoader();

	@Source("ajax-loader2.gif")
	public ImageResource horizontalLoader();

	@Source("ajax-red.gif")
	public ImageResource roundedLoader();

	@Source("greentick.png")
	public ImageResource greenTick();

	@Source("cross.png")
	public ImageResource redCross();

	@Source("upload-black-64.png")
	public ImageResource uploadBlack64();

	@Source("upload-grey-64.png")
	public ImageResource uploadGrey64();

	@Source("upload-gold-64.png")
	public ImageResource uploadGold64();

	@Source("upload-orange-64.png")
	public ImageResource uploadOrange64();

	@Source("upload-black-40.png")
	public ImageResource uploadBlack40();

	@Source("upload-grey-40.png")
	public ImageResource uploadGrey40();

	@Source("upload-gold-40.png")
	public ImageResource uploadGold40();

	@Source("upload-orange-40.png")
	public ImageResource uploadOrange40();

	@Source("floppy-black-64.png")
	public ImageResource floppyBlack64();

	@Source("floppy-grey-64.png")
	public ImageResource floppyGrey64();

	@Source("floppy-gold-64.png")
	public ImageResource floppyGold64();

	@Source("floppy-orange-64.png")
	public ImageResource floppyOrange64();

	@Source("floppy-black-24.png")
	public ImageResource floppyBlack24();

	@Source("floppy-grey-24.png")
	public ImageResource floppyGrey24();

	@Source("floppy-gold-24.png")
	public ImageResource floppyGold24();

	@Source("floppy-orange-24.png")
	public ImageResource floppyOrange24();

	@Source("experiment-icon-blank.png")
	public ImageResource experimentIconBlank();

	@Source("experiment-icon-full.png")
	public ImageResource experimentIconFull();

	@Source("experiment-icon-marked.png")
	public ImageResource experimentIconMarked();

	@Source("genenames_logo.png")
	public ImageResource hgncLogo();

	@Source("left.png")
	public ImageResource leftArrow();

	@Source("right.png")
	public ImageResource rigthArrow();

	@Source("left_small.png")
	public ImageResource smallLeftArrow();

	@Source("right_small.png")
	public ImageResource smallRigthArrow();

	// @Source("sequence_buffer.png")
	// public ImageResource sequenceBuffer();
	//
	// @Source("sequence_buffer_hover.png")
	// public ImageResource sequenceBufferHover();
	//
	// @Source("sequence_marker.png")
	// public ImageResource sequenceMarker();
	//
	// @Source("sequence_marker_hover.png")
	// public ImageResource sequenceMarkerHover();

	@Source("arrow_up.png")
	public ImageResource arrowUp();

	@Source("fireworks_small.png")
	public ImageResource fireworksIcon();

	@Source("PRIDEClusterLogo.png")
	public ImageResource prideClusterLogo();

	@Source("IntAct_logo.png")
	public ImageResource intActLogo();

	@Source("ComplexPortalLogo.png")
	public ImageResource complexPortalLogo();

	@Source("left-64.png")
	public ImageResource doubleLeftArrow();
}
