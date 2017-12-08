package edu.scripps.yates.client.gui.reactome;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.events.AnalysisResetEvent;
import org.reactome.web.diagram.events.DiagramLoadedEvent;
import org.reactome.web.diagram.events.DiagramObjectsFlagResetEvent;
import org.reactome.web.diagram.events.DiagramObjectsFlaggedEvent;
import org.reactome.web.diagram.events.FireworksOpenedEvent;
import org.reactome.web.diagram.events.GraphObjectHoveredEvent;
import org.reactome.web.diagram.events.GraphObjectSelectedEvent;
import org.reactome.web.diagram.events.InteractorHoveredEvent;
import org.reactome.web.diagram.handlers.AnalysisResetHandler;
import org.reactome.web.diagram.handlers.DiagramLoadedHandler;
import org.reactome.web.diagram.handlers.DiagramObjectsFlagResetHandler;
import org.reactome.web.diagram.handlers.DiagramObjectsFlaggedHandler;
import org.reactome.web.diagram.handlers.FireworksOpenedHandler;
import org.reactome.web.diagram.handlers.GraphObjectHoveredHandler;
import org.reactome.web.diagram.handlers.GraphObjectSelectedHandler;
import org.reactome.web.diagram.handlers.InteractorHoveredHandler;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.client.RESTFulClient;
import org.reactome.web.pwp.model.client.handlers.AncestorsCreatedHandler;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectCreatedHandler;
import org.reactome.web.pwp.model.util.Ancestors;
import org.reactome.web.pwp.model.util.Path;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;

import edu.scripps.yates.client.statusreporter.StatusReportersRegister;

public class DiagramLoader implements DatabaseObjectCreatedHandler, AncestorsCreatedHandler, DiagramLoadedHandler,
		AnalysisResetHandler, GraphObjectHoveredHandler, GraphObjectSelectedHandler, DiagramObjectsFlaggedHandler,
		DiagramObjectsFlagResetHandler, FireworksOpenedHandler, InteractorHoveredHandler {

	public interface SubpathwaySelectedHandler {
		void onSubPathwaySelected(String identifier);
	}

	private final Set<SubpathwaySelectedHandler> handlers = new HashSet<>();
	private final DiagramViewer diagram;

	private String loadedDiagram;
	private String selectedPathway;
	private String target;

	public DiagramLoader(DiagramViewer diagram) {
		this.diagram = diagram;
		this.diagram.addAnalysisResetHandler(this);
		this.diagram.addDatabaseObjectHoveredHandler(this);
		this.diagram.addDatabaseObjectSelectedHandler(this);
		this.diagram.addDiagramLoadedHandler(this);
		this.diagram.addDiagramObjectsFlaggedHandler(this);
		this.diagram.addDiagramObjectsFlagResetHandler(this);
		this.diagram.addFireworksOpenedHandler(this);
		this.diagram.addInteractorHoveredHandler(this);

	}

	public void load(String identifier) {
		if (!Objects.equals(identifier, selectedPathway)) {
			DatabaseObjectFactory.get(identifier, this);
		}
	}

	public void addFireworksOpenedHandler(FireworksOpenedHandler handler) {
		diagram.addFireworksOpenedHandler(handler);
	}

	public String getTarget() {
		return target;
	}

	public void addSubpathwaySelectedHandler(SubpathwaySelectedHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
		GWT.log("onDatabaseObjectLoaded (diagramLoader)");
		if (databaseObject instanceof Event) {
			target = databaseObject.getIdentifier();
			Event event = (Event) databaseObject;
			RESTFulClient.getAncestors(event, this);
			return;
		}
		target = null;
	}

	@Override
	public void onDatabaseObjectError(Throwable exception) {
		StatusReportersRegister.getInstance().notifyStatusReporters(exception);
	}

	@Override
	public void onAncestorsLoaded(Ancestors ancestors) {
		GWT.log("onAncestorsLoaded (diagramLoader)");
		if (ancestors != null) {
			Iterator<Path> it = ancestors.iterator();
			if (it.hasNext()) {
				Path path = it.next();
				String toLoad = path.getLastPathwayWithDiagram().getIdentifier();
				if (toLoad != null && !toLoad.equals(loadedDiagram)) {
					diagram.loadDiagram(toLoad);
				} else if (target != null && !target.equals(toLoad)) {
					onDiagramLoaded();
				}
				return;
			}
		}
		StatusReportersRegister.getInstance().notifyStatusReporters(
				"No ancestors found. Please check whether the provided identifier belongs to a Pathway or Reaction.");
	}

	@Override
	public void onAncestorsError(Throwable exception) {
		GWT.log("onAcenstorsError (diagramLoader)");
		StatusReportersRegister.getInstance().notifyStatusReporters(exception);
	}

	@Override
	public void onDiagramLoaded(DiagramLoadedEvent event) {
		GWT.log("onDiagramLoaded (diagramLoader)");
		loadedDiagram = event.getContext().getContent().getStableId();
		selectedPathway = loadedDiagram;
		onDiagramLoaded();
	}

	public void onDiagramLoaded() {
		if (!loadedDiagram.equals(target)) {
			diagram.selectItem(target);
			selectedPathway = target;
			for (SubpathwaySelectedHandler handler : handlers) {
				handler.onSubPathwaySelected(target);
			}
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					target = null;
				}
			});
		} else {
			target = null;
		}
	}

	@Override
	public void onGraphObjectSelected(GraphObjectSelectedEvent event) {
		GWT.log("onGraphObjectSelected:" + event.toDebugString());
	}

	@Override
	public void onGraphObjectHovered(GraphObjectHoveredEvent event) {
		GWT.log("onGraphObjectHovered:" + event.toDebugString());
	}

	@Override
	public void onAnalysisReset(AnalysisResetEvent event) {
		GWT.log("onAnalysisReset:" + event.toDebugString());
	}

	@Override
	public void onInteractorHovered(InteractorHoveredEvent event) {
		GWT.log("onInteractorHovered:" + event.toDebugString());
	}

	@Override
	public void onFireworksOpened(FireworksOpenedEvent event) {
		GWT.log("from DiagramLoader onFireworksOpened:" + event.toDebugString());
	}

	@Override
	public void onDiagramObjectsFlagReset(DiagramObjectsFlagResetEvent event) {
		GWT.log("onDiagramObjectsFlagReset:" + event.toDebugString());
	}

	@Override
	public void onDiagramObjectsFlagged(DiagramObjectsFlaggedEvent event) {
		GWT.log("onDiagramObjectsFlagged:" + event.toDebugString());
	}
}
