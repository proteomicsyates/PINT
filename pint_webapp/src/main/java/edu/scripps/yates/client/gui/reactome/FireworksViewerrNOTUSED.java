package edu.scripps.yates.client.gui.reactome;

import org.reactome.web.fireworks.handlers.AnalysisResetHandler;
import org.reactome.web.fireworks.handlers.CanvasNotSupportedHandler;
import org.reactome.web.fireworks.handlers.ExpressionColumnChangedHandler;
import org.reactome.web.fireworks.handlers.FireworksLoadedHandler;
import org.reactome.web.fireworks.handlers.NodeHoverHandler;
import org.reactome.web.fireworks.handlers.NodeHoverResetHandler;
import org.reactome.web.fireworks.handlers.NodeOpenedHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedResetHandler;
import org.reactome.web.fireworks.handlers.ProfileChangedHandler;
import org.reactome.web.fireworks.model.Node;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;

public interface FireworksViewerrNOTUSED extends IsWidget, HasHandlers, RequiresResize {

	HandlerRegistration addAnalysisResetHandler(AnalysisResetHandler handler);

	HandlerRegistration addCanvasNotSupportedHandler(CanvasNotSupportedHandler handler);

	HandlerRegistration addFireworksLoaded(FireworksLoadedHandler handler);

	HandlerRegistration addExpressionColumnChangedHandler(ExpressionColumnChangedHandler handler);

	HandlerRegistration addNodeHoverHandler(NodeHoverHandler handler);

	HandlerRegistration addNodeHoverResetHandler(NodeHoverResetHandler handler);

	HandlerRegistration addNodeOpenedHandler(NodeOpenedHandler handler);

	HandlerRegistration addNodeSelectedHandler(NodeSelectedHandler handler);

	HandlerRegistration addNodeSelectedResetHandler(NodeSelectedResetHandler handler);

	HandlerRegistration addProfileChangedHandler(ProfileChangedHandler handler);

	Node getSelected();

	void highlightNode(String stableIdentifier);

	void highlightNode(Long dbIdentifier);

	void openPathway(String stableIdentifier);

	void openPathway(Long dbIdentifier);

	void resetAnalysis();

	void resetHighlight();

	void resetSelection();

	void selectNode(String stableIdentifier);

	void selectNode(Long dbIdentifier);

	void setAnalysisToken(String token, String resource);

	void showAll();

	void setVisible(boolean visible);
}
